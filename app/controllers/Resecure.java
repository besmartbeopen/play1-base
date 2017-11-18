package controllers;

import com.google.common.base.Optional;
import com.google.common.eventbus.EventBus;
import common.TemplateDataInjector;
import common.VersionData;
import common.security.SecurityRules;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import models.common.Operator;
import play.Play;
import play.exceptions.UnexpectedException;
import play.i18n.Messages;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.CookieDataCodec;
import play.mvc.Finally;
import play.mvc.With;

/**
 * @author marco
 *
 */
@With(TemplateDataInjector.class)
@Slf4j
public class Resecure extends Controller {

	private final static String USERNAME = "username";
	// come le chiavi utilizzate in play
	private final static String KEY = "___";
	private final static String SUDO = KEY + "sudo";
	private final static String SUDO_DATA = SUDO + "_data";

	@Inject
	static SecurityRules rules;
	@Inject
	static VersionData versionData;
	@Inject
	static EventBus bus;

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.METHOD})
	public @interface NoCheck {
	}

	/**
	 * La versione è disponibile in tutti i template (resecure).
	 */
	@Before
	public static void setVersionData() {
		renderArgs.put("appVersionData", versionData);
	}

	/**
	 * Invia gli X-PJAX-URL che sono poi utilizzati dalla jquery.pjax.js sulle
	 * redirect, nascoste nei browser dalle xhr.
	 */
	@Finally
	public static void sendPjaxUrl() {
		if (request.headers.containsKey("x-pjax")) {
			response.setHeader("X-PJAX-URL", request.url);
			response.setHeader("X-PJAX-Version", versionData.toString());
			// disponibile via template
			renderArgs.put("via_pjax", true);
		}
	}

	@Before(unless={"login", "authenticate", "logout"})
    static void checkAccess() throws Throwable {
		if (getActionAnnotation(NoCheck.class) != null ||
				getControllerInheritedAnnotation(NoCheck.class) != null) {
			return;
		} else {
			if (Security.getCurrentUser().isPresent()) {
				renderArgs.put("currentOperator", Security.getCurrentUser().get());
			}
			Secure.checkAccess();
			rules.checkIfPermitted();
        }
	}

	public static boolean check(String action, Object instance) {
		return rules.check(action, instance);
	}

	/**
	 * @return la login dell'operatore reale se ha fatto "sudo".
	 */
	public static Optional<String> realOperator() {
		return Optional.fromNullable(session.get(SUDO));
	}

	/**
	 * Switch verso un altro operatore, salvando l'autenticazione di questo.
	 */
	public static void switchOperatorTo(int id) {
		final Operator operator = Operator.findById(id);
		notFoundIfNull(operator);
		rules.checkIfPermitted(operator);

		if (session.contains(SUDO)) {
			flash.error("sudo.alreadySwitched");
		} else {
		    final Operator previous = Security.getCurrentUser().get();
            try {
              final String sudo = CookieDataCodec.encode(session.all());
              // rimuove tutte le chiavi (presumibilmente) non play
              session.all().entrySet().removeIf(entry -> !entry.getKey().startsWith(KEY));
              // salva il precedente
              session.put(SUDO, previous.email);
              session.put(SUDO_DATA,  sudo);
            } catch (UnsupportedEncodingException e) {
              log.error("unsupported encoding", e);
              throw new UnexpectedException("corrupted session", e);
            }
            // recupera
            session.put(USERNAME, operator.email);
            log.info("switch from {} to {}", previous.getFullname(), 
                operator.getFullname());
            flash.success(Messages.get("sudo.switched", operator.getFullname()));
		}
		// redirect alla radice
		redirect(Play.ctxPath + "/");
	}

	/**
	 * Switch verso un altro operatore.
	 */
	@NoCheck
	public static void restoreOperator() {
		if (session.contains(SUDO)) {
		  final String sudo = session.get(SUDO_DATA);
		  session.clear();
		  try {
		    CookieDataCodec.decode(session.all(), sudo);
          } catch (UnsupportedEncodingException e) {
            log.error("unsupported encoding", e);
            throw new UnexpectedException("corrupted session", e);
          }
		  log.info("restore to {}", Security.getCurrentUser().get().getFullname()); 
		  flash.success("sudo.restored");
		}
		// redirect alla radice
		redirect(Play.ctxPath + "/");
	}
}
