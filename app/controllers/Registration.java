package controllers;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import common.Cleaner;
import common.TemplateExtensions;
import common.dao.OperatorDao;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import models.common.Operator;
import models.common.RecoveryRequest;
import notifiers.Notifier;
import play.data.validation.Email;
import play.data.validation.Equals;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.i18n.Messages;
import play.mvc.Controller;


/**
 * @author marco
 *
 */
@Slf4j
public class Registration extends Controller {

  @Inject
  static OperatorDao operatorDao;

  public static void lostPassword(String email) {
    render(email);
    // POST -> passwordResetRequest
  }

  public static void passwordResetRequest(@Required @Email String email) {
    if (Validation.hasErrors()) {
      Validation.keep();
      // params.flash();
      lostPassword(email);
    } else {
      Optional<Operator> operator = operatorDao.byEmail(email);
      if (operator.isPresent()) {
        final RecoveryRequest recoveryRequest = RecoveryRequest
            .createFrom(operator.get(), request.remoteAddress);
        Notifier.passwordResetRequest(recoveryRequest);
        log.info("password reset email sent to {}: {})", email, recoveryRequest);
      } else {
        log.warn("user unknown {}, response hidden", email);
      }
      passwordResetRequested();
    }
  }

  public static void passwordResetRequested() {
    final String recoveryRequestExpire = TemplateExtensions
        .format(Cleaner.recoveryRequestExpire);
    render(recoveryRequestExpire);
  }

  public static void verify(String code) {

    if (Strings.isNullOrEmpty(code)) {
      flash.error(Messages.get("registration.codeRequired"));
      lostPassword(null);
    }

    Optional<RecoveryRequest> recoveryRequest = operatorDao.byCode(code);
    if (!recoveryRequest.isPresent()) {
      flash.error(Messages.get("registration.codeExpiredOrInvalid", code));
      lostPassword(null);
    }
    if (recoveryRequest.get().operator.password == null) {
      flash.success(Messages.get("registration.verifyRequired", recoveryRequest.get().operator));
    } else {
      flash.success(Messages.get("registration.verify", recoveryRequest.get().operator));
    }
    render(code);
  }

  public static void changePassword(@Required String code,
      @Required @MinSize(Operator.PASSWORD_MIN_SIZE) String password,
      @Required @Equals("password") String confirmPassword) {

    if (Validation.hasErrors()) {
      Validation.keep();
      params.flash();
      verify(code);
    } else {
      Optional<RecoveryRequest> recoveryRequest = operatorDao.byCode(code);
      if (!recoveryRequest.isPresent()) {
        notFound();
      }
      log.info("password changed for recovery request {}, from {}",
          recoveryRequest.get(), request.remoteAddress);
      final Operator operator = recoveryRequest.get().operator;
      operator.cryptPassword(password).save();
      recoveryRequest.get().delete();
      verified();
    }
  }

  public static void verified() {
    render();
  }
}
