package controllers;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import controllers.Resecure.NoCheck;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.impl.crypto.MacProvider;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import play.Play;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.Header;
import play.mvc.Scope.Session;
import play.mvc.Util;
import play.mvc.With;

/**
 * Integrazione essenziale con JWT per la generazione di token e la
 * successiva rilettura/verifica.
 *
 * @author marco
 *
 */
@With(Resecure.class)
@Slf4j
public class SecurityTokens extends Controller {

  private static String key() {
    String key = Play.configuration.getProperty("jwt.key");
    if (Strings.isNullOrEmpty(key)) {
      key = TextCodec.BASE64.encode(MacProvider.generateKey().getEncoded());
      log.warn("the new jwt.key = \"{}\" must be saved into application.conf", key);
      Play.configuration.setProperty("jwt.key", key);
    }
    return key;
  }

  /**
   * Risponde con un nuovo token attivo per 1ora.
   */
  public static void token() {
    String username = Security.getCurrentUser().get().email;
    String token = Jwts.builder().setSubject(username)
        .setExpiration(DateTime.now().plusHours(1).toDate())
        .signWith(SignatureAlgorithm.HS512, key()).compact();
    renderText(token);
  }

  @NoCheck
  public static void check(String token) {
    try {
      Object body = Jwts.parser().setSigningKey(key()).parse(token).getBody();
      String user = ((Claims)body).getSubject();
      renderText("success " + user);
    } catch (SignatureException e) {
      renderText("fail");
    }
  }

  @Util
  public static Optional<String> jwtUsername() {
    Header authorization = Http.Request.current.get().headers.get("authorization");
    if (authorization != null && authorization.value().startsWith("Bearer ")) {
      try {
        String token = authorization.value().substring("Bearer ".length());
        Object body = Jwts.parser().setSigningKey(key()).parse(token).getBody();
        String username = ((Claims)body).getSubject();

        // WARNING: inserimento in sessione per farlo digerire alla play Security
        Session.current().put("username", username);
        return Optional.of(username);
      } catch (SignatureException e) {
        log.warn("signature error", e);
      }
    }
    return Optional.absent();
  }
}
