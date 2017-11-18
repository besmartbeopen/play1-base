package common.validators;

import org.joda.time.LocalDate;

import models.common.interfaces.StartEndDate;
import play.data.validation.Check;


/**
 * Controlla che la data di inizio sia prima della data di fine
 *
 * @author cristian
 *
 */
public class StartDateBeforeEndDateCheck extends Check {

	@Override
	public boolean isSatisfied(Object validatedObject, Object v) {
		if (v == null) {
			return true;
		}
		if (!(v instanceof LocalDate)) {
			return false;
		}
		final LocalDate value = (LocalDate) v;  
		
		if (! (validatedObject instanceof StartEndDate)) {
			return false;
		}
		
		StartEndDate modelObject = (StartEndDate) validatedObject;		
		
		if (value.isAfter(modelObject.getEndDate()))  {
			setMessage("validation.startDate.afterEndDate");
			return false;
		}

		return true;
	}
}
