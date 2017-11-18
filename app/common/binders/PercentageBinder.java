package common.binders;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Locale;

import models.common.base.Steadies;
import play.data.binding.TypeBinder;

/**
 * @author marco
 *
 */
public class PercentageBinder implements TypeBinder<BigDecimal>{

  @Override
  public Object bind(String name, Annotation[] annotations, String value,
      @SuppressWarnings("rawtypes") Class actualClass, Type genericType) throws Exception {
    final DecimalFormat format = (DecimalFormat) DecimalFormat
        .getInstance(Locale.ITALY);
    format.setParseBigDecimal(true);
    final BigDecimal v = (BigDecimal) format.parse(value);
    return v.divide(Steadies.HUNDRED);
  }
}
