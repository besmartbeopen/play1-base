package common.binders;


import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import play.data.binding.Global;
import play.data.binding.TypeBinder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * @author daniele
 * @since 29/11/16
 */
@Global
public class LocalDateTimeBinder implements TypeBinder<LocalDateTime> {

  private static final DateTimeFormatter TIME_FORMAT = DateTimeFormat
      .forPattern("yyyy-MM-dd'T'HH:mm:ss");

  @Override
  public LocalDateTime bind(String name, Annotation[] annotations, String value,
      @SuppressWarnings("rawtypes") Class actualClass, Type genericType) throws Exception {

    if (value == null || value.trim().length() == 0) {
      return null;
    }
    return TIME_FORMAT.parseLocalDateTime(value);
  }
}
