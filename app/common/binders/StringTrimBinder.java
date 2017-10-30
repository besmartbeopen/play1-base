package common.binders;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import com.google.common.base.Strings;

import play.data.binding.TypeBinder;


/**
 * @author marco
 *
 */
public class StringTrimBinder implements TypeBinder<String> {
	
	@Override
	public Object bind(String name, Annotation[] annotations, String value,
			@SuppressWarnings("rawtypes") Class actualClass, Type genericType) throws Exception {
		
		if (value == null) {
			return null;
		}
		return Strings.emptyToNull(value.trim());
	}
}
