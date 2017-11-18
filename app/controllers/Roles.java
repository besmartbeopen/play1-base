package controllers;
import com.google.common.base.Preconditions;
import common.Web;
import common.dao.OperatorDao;
import common.jpa.JpaReferenceBinder;
import java.util.List;
import javax.inject.Inject;

import models.common.Operator;
import models.common.enums.Role;
import play.data.binding.As;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.mvc.Controller;
import play.mvc.With;


@With(Resecure.class)
public class Roles extends Controller {

	@Inject
	static OperatorDao operatorDao;

	public static void list() {
		final Object roles = Role.values();
		render(roles);
	}

	public static void show(Role role) {
		Preconditions.checkNotNull(role);
		final List<Operator> operators = operatorDao.byRole(role);
		final List<Operator> allOperators = operatorDao.all().list();
		render(role, operators, allOperators);
	}

	public static void save(@Required Role role,
			@Required @As(binder=JpaReferenceBinder.class) List<Operator> operators) {

		if (Validation.hasErrors()) {
			flash.error(Web.msgHasErrors());
			final List<Operator> allOperators = operatorDao.all().list();
			render("@show", role, operators, allOperators);
		} else {
			List<Operator> prevs = operatorDao.byRole(role);
			for (Operator operator : operators) {
				// aggiunge i mancanti:
				if (!prevs.contains(operator)) {
					operator.roles.add(role);
					operator.save();
				}
			}
			for (Operator prev : prevs) {
				// rimuove i non più selezionati:
				if (!operators.contains(prev)) {
					prev.roles.remove(role);
					prev.save();
				}
			}
			flash.success(Web.msgSaved(Role.class));
			list();
		}
	}
}
