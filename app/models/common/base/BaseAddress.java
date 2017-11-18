package models.common.base;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;

import play.data.binding.As;
import play.data.validation.Match;
import play.data.validation.Phone;
import play.data.validation.Required;

import com.google.common.base.Strings;
import common.Tools;
import common.binders.CapitalizeBinder;
import models.common.geo.Municipal;

/**
 * @author marco
 *
 */
@Audited
@MappedSuperclass
public abstract class BaseAddress extends BaseModel {

	private static final long serialVersionUID = 2398528502804269994L;

	@As(binder=CapitalizeBinder.class)
	public String street;
	@Phone
	public String phone;
	@Phone
	public String fax;
	@Match(value="[0-9]{4,5}", message="validation.cap")
	public String cap;

	@Required
	@NotNull
	@ManyToOne
	public Municipal municipal;

	public String getLabel() {
		return Tools.COMMA_JOINER.join(Strings.emptyToNull(street),
				municipal);
	}
}
