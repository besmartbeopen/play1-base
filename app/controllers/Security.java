package controllers;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.base.Supplier;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import common.dao.OperatorDao;
import common.events.data.LoggedInEvent;
import common.injection.StaticInject;
import common.security.SecurityRules;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import models.common.Operator;


/**
 * L'autenticazione è gestita con questa classe.
 *
 * @author marco
 *
 */
/**
 * @author marco
 *
 */
@Slf4j
@StaticInject
public class Security extends Secure.Security {

  public static final String CURRENT_USER = "current_user";
  private static final String WHITOUT_SPACES = "^\\S+$";

  @Inject
  static SecurityRules rules;
  @Inject
  static OperatorDao operatorDao;
  @Inject
  static EventBus bus;

  /**
   * @return l'operatore corrente, se presente, altrimenti "absent".
   */
  public static Optional<Operator> getCurrentUser() {
    final Optional<String> username = Optional.fromNullable(connected())
        .or(SecurityTokens.jwtUsername());
    if (username.isPresent()) {
      if (request.args.containsKey(CURRENT_USER)) {
        return Optional.of((Operator) request.args.get(CURRENT_USER));
      } else {
        final Optional<Operator> op = operatorDao.byEmail(username.get());
        if (op.isPresent()) {
          request.args.put(CURRENT_USER, op.get());
        }
        return op;
      }
    } else {
      return Optional.absent();
    }
  }

  public static Supplier<Operator> getOperatorSupplier() {
    return new Supplier<Operator>() {
      @Override
      public Operator get() {
        return getCurrentUser().get();
      }
    };
  }

  public static Optional<Operator> getRealOperator() {
    Optional<String> realId = Resecure.realOperator();
    if (realId.isPresent()) {
      return operatorDao.byEmail(realId.get());
    } else {
      return Optional.absent();
    }
  }

  static boolean authenticate(String username, String password) {
    if (!username.matches(WHITOUT_SPACES)) {
      flash.put("warning", "notify.whitespaces.username");
      return false;
    }
    if (!password.matches(WHITOUT_SPACES)) {
      flash.put("warning", "notify.whitespaces.password");
      return false;
    }
    final Optional<Operator> user = operatorDao.byEmail(username);
    log.info("authenticate {}", user.transform(Operator::toString).or("<unknown>"));
    return user.isPresent() && user.get().passwordMatch(password)
        // se nel profilo è indicato un indirizzo deve essere uguale al baseurl
        && (Strings.isNullOrEmpty(user.get().profile.loginUrl) ||
          Objects.equals(request.getBase(), user.get().profile.loginUrl));
  }

  /**
   * @param profile la stringa indicante la action da verificare
   * @return true se profile è una action permessa per l'operatore corrente,
   * false altrimenti.
   */
  static boolean check(String profile) {
    final Optional<Operator> user = getCurrentUser();
    if (user.isPresent()) {
      return rules.checkAction(profile);
    } else {
      return false;
    }
  }

  static void onAuthenticated() {
    bus.post(LoggedInEvent.of(getCurrentUser().get(), request.remoteAddress));
  }
}
