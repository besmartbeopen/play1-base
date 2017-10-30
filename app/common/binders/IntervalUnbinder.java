package common.binders;

import common.TemplateExtensions;
import java.lang.annotation.Annotation;
import java.util.Map;

import org.joda.time.Interval;

import play.data.binding.Global;
import play.data.binding.TypeUnbinder;

/**
 * @author marco
 *
 */
@Global
public class IntervalUnbinder implements TypeUnbinder<Interval>{

	@Override
	public boolean unBind(Map<String, Object> result, Object src,
			Class<?> srcClazz, String name, Annotation[] annotations)
			throws Exception {
		if (src instanceof Interval) {
			final Interval interval = (Interval) src;
			result.put(name, TemplateExtensions.value(interval));
			return true;
		} else {
			return false;
		}
	}
}
