package common.validators;

import net.sf.oval.configuration.annotation.Constraint;
import java.lang.annotation.*;

/**
 * @author marco
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(checkWith=CollectionSizeCheck.class)
public @interface CollectionSize {
	int min() default 0;
	int max() default Integer.MAX_VALUE;
}
