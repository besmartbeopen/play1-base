package common.binders;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import play.data.binding.Global;
import play.data.binding.TypeBinder;

/**
 * @author marco
 *
 */
@Global
public class LocalTimeBinder implements TypeBinder<LocalTime> {

	private static final DateTimeFormatter TIME_FORMAT = DateTimeFormat
			.forPattern("HH:mm");

	@Override
    public LocalTime bind(String name, Annotation[] annotations, String value,
    		@SuppressWarnings("rawtypes") Class actualClass, Type genericType) throws Exception {

        if (value == null || value.trim().length() == 0) {
            return null;
        }
        return TIME_FORMAT.parseLocalTime(value);
    }
}
