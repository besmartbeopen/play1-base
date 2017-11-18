package controllers;

import common.ModelQuery;
import common.Web;
import common.security.SecurityRules;
import models.common.Operator;

import javax.inject.Inject;

import play.data.validation.Equals;
import play.data.validation.Max;
import play.data.validation.Min;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.With;

/**
 * @author marco
 *
 */
@With(Resecure.class)
public class Personal extends Controller {

  @Inject
  static SecurityRules rules;

  public static void index() {
    final long perPage = ModelQuery.personalPageSize();
    render(perPage);
  }

  // POST
  public static void updateSettings(@Required @Min(10) @Max(100) long perPage) {
    if (Validation.hasErrors()) {
      flash.error(Web.msgHasErrors());
      index();
    }
    ModelQuery.storeSessionPageSize(perPage);
    flash.success("Impostazioni salvate.");
    index();
  }

  public static void editPassword() {
    render();
  }

  public static void updatePassword(@Required
      @MinSize(Operator.PASSWORD_MIN_SIZE) String password,
      @Required @Equals("password") String confirmPassword) {

    if (Validation.hasErrors()) {
      flash.error(Web.MSG_HAS_ERRORS);
      render("@editPassword", password, confirmPassword);
    } else {
      flash.success(Messages.get(Web.MSG_MODIFIED, "password"));
      Security.getCurrentUser().get().cryptPassword(password).save();
      editPassword();
    }
  }
}
