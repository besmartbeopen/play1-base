package common.notifiers;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSMediaRule;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS3;

/**
 * @author marco
 *
 */
@Slf4j
public class CssInliner {

	private static final String UID = "data-uid";

	/**
	 * Un iterabile per le cssrule.
	 *
	 * @author marco
	 *
	 */
	@RequiredArgsConstructor
	public static class CSSIterable implements Iterable<CSSRule> {

		@RequiredArgsConstructor
		public static class CSSIterator extends AbstractIterator<CSSRule> {

			private final CSSStyleSheet css;
			private int i = 0;

			@Override
			protected CSSRule computeNext() {
				final CSSRule rule = css.getCssRules().item(i ++);
				if (rule == null) {
					endOfData();
				}
				return rule;
			}
		}

		private final CSSStyleSheet css;

		@Override
		public Iterator<CSSRule> iterator() {
			return new CSSIterator(css);
		}
	}

	public static String inlineCss(String originalHtml) throws IOException {
		final CSSOMParser parser = new CSSOMParser(new SACParserCSS3());

		final Document document = Jsoup.parse(originalHtml);

		// tutte le regole di stile
		List<CSSStyleRule> allStyleRules = Lists.newArrayList();
		// regole media e in generale che non possono essere inlined
		List<CSSRule> allMediaRules = Lists.newArrayList();
		for (Element link : document.getElementsByTag("link")) {
			if (link.hasAttr("rel") &&
					"stylesheet".equalsIgnoreCase(link.attr("rel"))) {
				final String href = link.attr("href");
				log.info("parsing {}", href);
				final CSSStyleSheet stylesheet = parser
						.parseStyleSheet(new InputSource(href), null, href);

				final CSSIterable cssIterable = new CSSIterable(stylesheet);
				// style
				FluentIterable.from(cssIterable).filter(CSSStyleRule.class)
					.copyInto(allStyleRules);
				// media
				FluentIterable.from(cssIterable).filter(CSSMediaRule.class)
					.copyInto(allMediaRules);
			}
		}

		// css selector -> property-name -> property-value
		final Map<String, Map<String, String>> cssMapping = Maps.newHashMap();
		int nextUid = 0; // utilizzata per valorizzare gli UID
		// itera su tutte le regole di stile:
		for (CSSStyleRule rule : allStyleRules) {
			final String selector = rule.getSelectorText();
			if (selector.contains(":")) {
				// evita le pseudo classi non utilizzabili inline
				allMediaRules.add(rule);
			} else if (selector.equals("body")) {
				// dal premailer
				allMediaRules.add(rule);
			} else {
				// colleziona gli stili per ogni elemento dentro al body.
				for (Element element : document.getElementsByTag("body")
						.select(selector)) {
					if (!element.hasAttr(UID)) {
						element.attr(UID, Integer.toString(nextUid ++));
					}
					final String key = element.attr(UID);
					if (!cssMapping.containsKey(key)) {
						cssMapping.put(key,	Maps.<String, String>newHashMap());
					}
					final CSSStyleDeclaration style = rule.getStyle();
					for (int i = 0; i < style.getLength(); i++ ) {
						final String name = style.item(i);
						final String value = style.getPropertyValue(name);
						cssMapping.get(key).put(name, value);
					}
				}
			}
		}
		// rimozione degli stili esterni
		document.getElementsByTag("link").remove();

		// applica gli stili direttamente sugli elementi e rimuove la class:
		for (val elementEntry : cssMapping.entrySet()) {
			final Element element = document.getElementsByAttributeValue(UID,
					elementEntry.getKey()).first();
			final StringBuilder styleBuilder = new StringBuilder();
			for (val styleEntry : elementEntry.getValue().entrySet()) {
				styleBuilder.append(styleEntry.getKey()).append(":")
					.append(styleEntry.getValue()).append(";");
			}
			// il precedente stile
			if (element.hasAttr("style")) {
				styleBuilder.append(element.attr("style"));
			}
			element.attr("style", styleBuilder.toString());
		}
		// rimuove tutte le .class
		for (val element : document.getElementsByAttribute("class")) {
			element.removeAttr("class");
		}
		// rimuove tutti gli UID
		for (val element : document.getElementsByAttribute(UID)) {
			element.removeAttr(UID);
		}
		// aggiunge lo stile nello html
		final Element style = document.createElement("style");
		style.attr("type", "text/css");
		for (CSSRule rule : allMediaRules) {
			style.append(rule.toString());
			style.appendText(" ");
		}
		document.select("head").first().appendChild(style);
		return document.html();
	}
}
