package common;

import java.lang.reflect.Type;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.joda.time.LocalDateTime;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * @author marco
 *
 */
@Singleton
public class LocalDateTimeSerializer implements JsonSerializer<LocalDateTime> {

	@Inject
	LocalDateTimeSerializer() {}

	@Override
	public JsonElement serialize(LocalDateTime src, Type typeOfSrc,
			JsonSerializationContext context) {
		return new JsonPrimitive(src.toString());
	}

}