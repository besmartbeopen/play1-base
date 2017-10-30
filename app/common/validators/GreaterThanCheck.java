package common.validators;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import lombok.extern.slf4j.Slf4j;
import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.AbstractAnnotationCheck;
import net.sf.oval.context.FieldContext;
import net.sf.oval.context.MethodParameterContext;
import net.sf.oval.context.OValContext;
import net.sf.oval.exception.OValException;
import play.data.binding.Binder;
import play.data.binding.RootParamNode;
import play.i18n.Messages;
import play.mvc.Scope;
import play.utils.Java;

import com.google.common.collect.ImmutableList;

/**
 * Verifica che il valore (Comparable) sia strettamente successivo al valore
 * del campo indicato nella @GreaterThan.
 *
 * @author marco
 *
 */
@Slf4j
public class GreaterThanCheck extends AbstractAnnotationCheck<GreaterThan> {

  private static final long serialVersionUID = 5235611539956033360L;

  public String field;
  private boolean equalAlso;
  private String message;

  @Override
  public void configure(GreaterThan isGreaterThan) {
    field = isGreaterThan.value();
    equalAlso = isGreaterThan.equalAlso();
    message = isGreaterThan.msg();
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public boolean isSatisfied(Object validatedObject, Object value,
      OValContext context, Validator validator) throws OValException {

    if (value == null) {
      return true;
    } else {
      try {
        final int result;
        if (context instanceof MethodParameterContext) {
          MethodParameterContext ctx = (MethodParameterContext) context;
          Method method = ctx.getMethod();
          int index = ImmutableList.copyOf(Java.parameterNames(method))
              .indexOf(field);

          RootParamNode rootParamNode = Scope.Params.current()
              .getRootParamNode();
          Class<?> clazz = method.getParameterTypes()[index];
          Type type = method.getGenericParameterTypes()[index];

          Object otherValue = Binder.bind(rootParamNode, field, clazz,
              type, method.getParameterAnnotations()[index]);
          if (otherValue == null) {
            return true;
          }
          result = ((Comparable) value).compareTo(otherValue);
        } else if (context instanceof FieldContext) {
          FieldContext ctx = (FieldContext) context;
          Field otherField = ctx.getField().getDeclaringClass()
              .getDeclaredField(field);
          Object otherValue = otherField.get(validatedObject);
          // nel caso non ci sia il valore è ok.
          if (otherValue == null) {
            return true;
          }
          result = ((Comparable) value).compareTo(otherValue);
        } else {
          throw new IllegalStateException("unknown context ");
        }
        if (result > 0 || (equalAlso && result == 0)) {
          return true;
        }
      } catch (Exception e) {
        // nothing, ... return false
        log.warn("greaterthan error: {}", e.getMessage());
      }
      setMessage(Messages.get(message));
      return false;
    }
  }
}
