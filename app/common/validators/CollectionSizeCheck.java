package common.validators;

import java.util.Collection;

import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.AbstractAnnotationCheck;
import net.sf.oval.context.OValContext;
import net.sf.oval.exception.OValException;
import play.data.validation.Validation;
import play.data.validation.Validation.ValidationResult;
import play.i18n.Messages;

/**
 * Controlla che la collezione abbia un numero di elementi compresi tra min e
 * max.
 *
 * @author marco
 *
 */
public class CollectionSizeCheck extends AbstractAnnotationCheck<CollectionSize> {

	private static final long serialVersionUID = 5235611539956033360L;

	public final static String MIN_MSG = "validation.collectionSize.min";
	public final static String MAX_MSG = "validation.collectionSize.max";

	private Integer min;
	private Integer max;

	@Override
	public void configure(CollectionSize collectionSize) {
		min = collectionSize.min();
		max = collectionSize.max();
	}

	@Override
	public boolean isSatisfied(Object validatedObject, Object value,
			OValContext context, Validator validator) throws OValException {
		if (value == null) {
			return true;
		} else {
			final Collection<?> coll = (Collection<?>) value;
			if (min != null && coll.size() < min) {
				setMessage(Messages.get(MIN_MSG));
				return false;
			} else if (max != null && coll.size() > max) {
				setMessage(Messages.get(MAX_MSG));
				return false;
			}
			return true;
		}
	}

    static ValidationResult applyCheck(CollectionSizeCheck check, String key,
    		Object o) {

    	final ValidationResult result = new ValidationResult();
    	if (!check.isSatisfied(o, o, null, null)) {
    		Validation.addError(key, check.getMessage());
    		result.ok = false;
    	} else {
    		result.ok = true;
    	}
    	return result;
    }

	public static ValidationResult minSize(String key, Object obj, int min) {
		final CollectionSizeCheck check = new CollectionSizeCheck();
		check.min = min;
		return applyCheck(check, key, obj);
	}

	public static ValidationResult maxSize(String key, Object obj, int max) {
		final CollectionSizeCheck check = new CollectionSizeCheck();
		check.max = max;
		return applyCheck(check, key, obj);
	}
}
