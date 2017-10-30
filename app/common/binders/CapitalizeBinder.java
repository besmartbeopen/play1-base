package common.binders;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

import play.data.binding.TypeBinder;


/**
 * @author marco
 *
 */
public class CapitalizeBinder implements TypeBinder<String> {
	
	@Override
	public Object bind(String name, Annotation[] annotations, String value,
			@SuppressWarnings("rawtypes") Class actualClass, Type genericType) throws Exception {
		
		return WordUtils.capitalizeFully(StringUtils.trim(value));
	}
}
