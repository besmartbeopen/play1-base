package common.binders;

import common.DateRange;
import java.lang.annotation.Annotation;
import java.util.Map;

import play.data.binding.Global;
import play.data.binding.TypeUnbinder;

/**
 * @author marco
 *
 */
@Global
public class DateRangeUnbinder implements TypeUnbinder<DateRange> {

	@Override
	public boolean unBind(Map<String, Object> result, Object src,
			Class<?> srcClazz, String name, Annotation[] annotations)
			throws Exception {

		if (src instanceof DateRange) {
			final DateRange range = (DateRange) src;
			final String value = DateRange.FMT.print(range.getStart())
					+ DateRange.DATE_SEPARATOR
					+ DateRange.FMT.print(range.getEnd());
			result.put(name, value);
			return true;
		} else {
			return false;
		}
	}
}
