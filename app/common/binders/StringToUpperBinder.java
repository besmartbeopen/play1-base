package common.binders;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import play.data.binding.TypeBinder;


/**
 * @author marco
 *
 */
public class StringToUpperBinder implements TypeBinder<String> {

  @Override
  public Object bind(String name, Annotation[] annotations, String value,
      @SuppressWarnings("rawtypes") Class actualClass, Type genericType) throws Exception {

    if (value == null) {
      return null;
    }
    return value.toUpperCase();
  }
}
