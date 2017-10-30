package common.validators;

import net.sf.oval.configuration.annotation.Constraint;
import java.lang.annotation.*;

/**
 * @author marco
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(checkWith=FairlyRecentCheck.class)
public @interface FairlyRecent {
  int years() default 1;
  String msg() default "validation.fairlyRecent";
}
