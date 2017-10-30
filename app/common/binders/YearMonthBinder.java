package common.binders;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;

import org.joda.time.YearMonth;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import play.data.binding.Global;
import play.data.binding.TypeBinder;
import play.data.binding.TypeUnbinder;

/**
 * @author marco
 *
 */
@Global
public class YearMonthBinder implements TypeBinder<YearMonth>,
	TypeUnbinder<YearMonth> {

	private final static DateTimeFormatter YM_FORMATTER =
			DateTimeFormat.forPattern("MM/yyyy");

	@Override
    public YearMonth bind(String name, Annotation[] annotations, String value,
    		@SuppressWarnings("rawtypes") Class actualClass, Type genericType) throws Exception {

        if (value == null || value.trim().length() == 0) {
            return null;
        }
        return YearMonth.parse(value, YM_FORMATTER);
    }

	@Override
	public boolean unBind(Map<String, Object> result, Object src,
			Class<?> srcClazz, String name, Annotation[] annotations)
			throws Exception {
		if (src instanceof YearMonth) {
			result.put(name, ((YearMonth) src).toString(YM_FORMATTER));
			return true;
		} else {
			return false;
		}
	}
}
