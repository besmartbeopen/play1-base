package common.binders;

import java.lang.annotation.Annotation;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.data.binding.Global;
import play.data.binding.TypeUnbinder;

import com.google.common.base.Optional;

import models.common.base.BaseModel;

/**
 * @author marco
 *
 */
@Global
public class OptionalUnbinder implements TypeUnbinder<Optional<?>>{

	private static final Logger LOG = LoggerFactory.getLogger(OptionalUnbinder.class);

	@Override
	public boolean unBind(Map<String, Object> result, Object src,
			Class<?> srcClazz, String name, Annotation[] annotations)
			throws Exception {
		if (src instanceof Optional) {
			final Optional<?> optional = (Optional<?>)src;
			if (optional.isPresent()) {
				final Object instance = optional.get();
				if (instance instanceof BaseModel) {
					result.put(name, ((BaseModel)instance).id);
				} else if (instance instanceof Integer) {
                  result.put(name, ((Integer)instance));
                } else if (instance instanceof String) {
                  result.put(name, ((String)instance));
                } else if (instance instanceof Boolean) {
				  result.put(name, ((Boolean)instance));
				} else {
					// XXX: supporterei anche gli enum... se solo li
					// supportasse il play.
					LOG.warn("unsupported optional<type> \"{}\"", src);
				}
			} // else nothing
			return true;
		} else {
			return false;
		}
	}
}
