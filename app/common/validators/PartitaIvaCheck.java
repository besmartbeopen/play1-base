package common.validators;

import com.google.common.base.CharMatcher;

import models.common.interfaces.PartitaIva;
import play.i18n.Messages;
import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.AbstractAnnotationCheck;
import net.sf.oval.context.OValContext;
import net.sf.oval.exception.OValException;

/**
 * @author marco
 *
 */
public class PartitaIvaCheck extends AbstractAnnotationCheck <PartitaIva> {

	private static final long serialVersionUID = -6702007927232598681L;

	@Override
	public boolean isSatisfied(Object validatedObject, Object value,
			OValContext context, Validator validator) throws OValException {
		if (value == null) {
			return true;
		}
		if (!(value instanceof String)) {
			return false;
		}
		final String piva = (String) value;
		if (piva.isEmpty()) {
			// non deve essere obbligatorio qui.
			return true;
		}
		if (piva.length() != 11) {
			setMessage(Messages.get("validation.piva.length"));
			return false;
		}
   	    if (!CharMatcher.digit().matchesAllOf(piva)) {
			setMessage(Messages.get("validation.piva.number"));
			return false;
   	    }
   	    int x = 0; // pesi dispari
   	    int y = 0; // pesi pari
		for (int i = 0; i < piva.length(); i++) {
			final int c = piva.charAt(i) - '0';
			if (i % 2 == 0) {
				x += c;
			} else {
				if (c * 2 > 9) {
					y += c * 2 - 9;
				} else {
					y += c * 2;
				}
			}
		}
		if ((x + y) % 10 == 0) {
			return true;
		}
		setMessage(Messages.get("validation.piva.check"));
		return false;
	}
}
