package common.events.bus;


import com.google.inject.BindingAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author marco
 *
 */
@BindingAnnotation
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventSubscriber {
  /**
   * @return il bus a cui si vuole far registrare l'oggetto.
   */
  String value() default EventBusKey.DEFAULT;
}
