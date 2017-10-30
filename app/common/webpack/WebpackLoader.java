package common.webpack;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import org.assertj.core.util.Preconditions;
import org.jsoup.Jsoup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.collect.ImmutableMap;
import common.injection.StaticInject;
import lombok.Builder;
import lombok.Data;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import play.Play;
import play.cache.Cache;

/**
 * @author marco
 *
 */
@StaticInject
@Slf4j
public final class WebpackLoader {

  private WebpackLoader() {}

  @Inject
  static ObjectMapper objectMapper;

  private final static String JS_KEY = "template-js-data";
  private final static String INDEX_HTML = "public/dist/index.html";
  private final static String TITLE_END = "</title>";
  private final static String PUBLIC_DIST = "public/dist/";
  private final static String MANIFEST_JSON = PUBLIC_DIST + "manifest.json";
  private final static String PRE_SCRIPT = "<script src=\"/" + PUBLIC_DIST;
  private final static String POST_SCRIPT = "\"></script>";
  private final static String PRE_STYLE = "<link rel=\"stylesheet\" type=\"text/css\" href=\"/" + PUBLIC_DIST;
  private final static String POST_STYLE = "\">";
  private final static String MEDIA_PRINT = "\" media=\"print";
  private final static MapType MAP_TYPE = TypeFactory.defaultInstance()
          .constructMapType(TreeMap.class, String.class, String.class);

  @Data
  @Builder
  static class JsData implements Serializable {
    private static final long serialVersionUID = -2852781910534527975L;
    private final String head;
    private final String body;
    private final long lastModified;
  }

  public static String head() {
    return getJavascript().getHead();
  }

  public static String body() {
    return getJavascript().getBody();
  }

  static JsData getJavascript() {
    JsData data = Cache.get(JS_KEY, JsData.class);
    if (data == null || (Play.mode.isDev() &&
        Play.getFile(INDEX_HTML).lastModified() > data.getLastModified())) {
      try {
        val indexHtml = Play.getFile(INDEX_HTML);
        val dom = Jsoup.parse(indexHtml, "UTF-8");
        val head = dom.getElementsByTag("head").html();
        data = JsData.builder()
            .head(head.substring(head.indexOf(TITLE_END) + TITLE_END.length()))
            .body(dom.getElementsByTag("body").html())
            .lastModified(indexHtml.lastModified())
            .build();
        Cache.set(JS_KEY, data);
      } catch (IOException e) {
        log.error(INDEX_HTML  + " not found: was webpack built?");
        return JsData.builder().body("").head("").lastModified(0L).build();
      }
    }
    return data;
  }

  /**
   * @param id
   * @return
   * @throws IOException
   */
  public static String load(String id) {
    Preconditions.checkNotNull(id);

    boolean devserverEnabled = WebpackPlugin.isDevserverEnabled();

    getJavascript();

    if (!devserverEnabled) {
      @SuppressWarnings("unchecked")
      Map<String, String> map = Cache.get(MANIFEST_JSON, Map.class);
      if (map == null) {
        try {
          map = objectMapper.readValue(Play.getFile(MANIFEST_JSON), MAP_TYPE);
        } catch (IOException e) {
          map = ImmutableMap.of();
        }
        if (Play.mode.isProd()) {
          Cache.set(MANIFEST_JSON, map);
        }
      }
      // se non c'è il valore associato alla chiave, si utilizza la chiave:
      id = map.getOrDefault(id, id);
    }
    if (id.endsWith(".js")) {
      return PRE_SCRIPT + id + POST_SCRIPT;
    } else {
      if (devserverEnabled) {
        // utilizzato il load dei css via webpack:
        return "";
      }
      return PRE_STYLE + id + (id.endsWith("print.css")
          ? MEDIA_PRINT : "") + POST_STYLE;
    }
  }

  public static void reset() {
    Cache.delete(JS_KEY);
  }
}
