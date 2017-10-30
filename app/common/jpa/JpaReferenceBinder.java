package common.jpa;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;

import models.base.BaseModel;
import play.data.binding.TypeBinder;
import play.db.jpa.JPA;

/**
 * @author marco
 *
 */
public class JpaReferenceBinder implements TypeBinder<Collection<? extends BaseModel>> {
	
	@SuppressWarnings("unchecked")
	@Override
	public Object bind(String name, Annotation[] annotations, String value,
			@SuppressWarnings("rawtypes") Class actualClass, Type genericType) throws Exception {
		final Integer id = Integer.parseInt(value);
		return JPA.em().getReference(actualClass, id);
	}
}
