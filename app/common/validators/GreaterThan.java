package common.validators;

import net.sf.oval.configuration.annotation.Constraint;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(checkWith=GreaterThanCheck.class)
public @interface GreaterThan {
	String value();
	boolean equalAlso() default false;
	String msg() default "validation.greaterThan";
}
