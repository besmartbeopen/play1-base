package controllers;

import com.google.gdata.util.common.base.Preconditions;
import common.Web;
import common.dao.OperatorProfileDao;
import common.security.SecurityRules;
import javax.inject.Inject;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import models.common.OperatorProfile;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.data.validation.Validation;
import play.mvc.Controller;
import play.mvc.With;

/**
 * @author marco
 *
 */
@With(Resecure.class)
@Slf4j
public class OperatorProfiles extends Controller {

  @Inject
  static SecurityRules rules;
  @Inject
  static OperatorProfileDao operatorProfileDao;

  public static void index() {

    flash.keep();
    list();
  }

  public static void list() {

    val profiles = operatorProfileDao.list().listResults();
    render(profiles);
  }

  public static void show(int id) {

    final OperatorProfile profile = OperatorProfile.findById(id);
    notFoundIfNull(profile);
    rules.checkIfPermitted(profile);
    render(profile);
  }

  public static void blank(OperatorProfile profile) {
    render("@edit", profile);
  }

  public static void edit(int id) {

    final OperatorProfile profile = OperatorProfile.findById(id);
    notFoundIfNull(profile);
    rules.checkIfPermitted(profile);

    render(profile);
  }

  public static void save(@Required @Valid OperatorProfile profile) {

    if (Validation.hasErrors()) {
      flash.error(Web.msgHasErrors());
      render("@edit", profile);
    } else {
      profile.save();
      flash.success(Web.msgSaved(OperatorProfile.class));
      index();
    }
  }

  // DELETE
  public static void delete(int id) {

    final OperatorProfile profile = OperatorProfile.findById(id);
    notFoundIfNull(profile);
    rules.checkIfPermitted(profile);

    if (profile.operators.isEmpty() || profile.operators.stream().allMatch(o -> !o.enabled)) {
      profile.delete();
      flash.success(Web.msgDeleted(OperatorProfile.class));
      index();
    } else {
      flash.error("Impossibile eliminare questo profilo, ci sono ancora degli operatori attivi correlati.");
      show(id);
    }
  }

  /**
   * POST only
   *
   * @param profile il profilo da cui prelevare i ruoli
   * @param synchronize se true cancella anche i ruoli dagli operatori
   */
  public static void applyRoles(@Required OperatorProfile profile,
      boolean synchronize) {
    Preconditions.checkState("post".equalsIgnoreCase(request.method));
    Preconditions.checkState(profile.isPersistent(), "existing profile is required");
    if (Validation.hasErrors()) {
      flash.error(Web.msgHasErrors());
    } else {
      if (profile.operators.isEmpty()) {
        flash.error("Nessun operatore collegato.");
      } else {
        profile.operators.forEach(operator -> {
          // solo per gli operatori abilitati
          if (operator.enabled) {
            if (operator.roles.addAll(profile.roles) || (synchronize &&
                operator.roles.removeIf(r -> !profile.roles.contains(r)))) {
              log.info("synchronize roles to profile for {} ({})", operator,
                  synchronize ? "add/remove" : "add only");
              operator.save();
            } // else nothing to do
          }
        });
      }
      flash.success("I ruoli sono stati applicati con successo a %d operatori.",
          profile.operators.size());
    }
    show(profile.id);
  }
}
