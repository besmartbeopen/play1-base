package common.dto;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.persistence.Id;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import common.TemplateExtensions;
import lombok.EqualsAndHashCode;
import models.base.BaseModel;
import models.base.Label;

/**
 * @author marco
 *
 */
@EqualsAndHashCode(of="id")
public class IdText {

	public final Object id;
	public final String text;

	public IdText(Object id, String text) {
		this.id = id;
		this.text = text;
	}
	
	@Override
	public String toString() {
	  return text;
	}

	public static <T extends Enum<?>> Function<T, IdText> fromEnum() {
		return new Function<T, IdText>() {

			@Override
			public IdText apply(T input) {
				return new IdText(input.name(), TemplateExtensions.label(input));
			}
		};
	}

	public static <T extends BaseModel> Function<T, IdText> fromModel() {
		return new Function<T, IdText> () {

			@Override
			public IdText apply(T input) {
				Integer id = null;
				String text = null;
				try {
					for (Field field : input.getClass().getFields()) {
						if (field.isAnnotationPresent(Id.class)) {
							id = (Integer) field.get(input);
						}
						if (field.isAnnotationPresent(Label.class)) {
							text = field.get(input).toString();
						}
					}
					for (Method method : input.getClass().getMethods()) {
						if (method.isAnnotationPresent(Label.class)) {
							text = method.invoke(input).toString();
						}
					}
					Preconditions.checkNotNull(id);
					Preconditions.checkNotNull(text);
					return new IdText(id, text);
				} catch (IllegalAccessException | IllegalArgumentException
				    | InvocationTargetException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

}
