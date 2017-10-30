package common.data;

import java.lang.reflect.Type;

import javax.inject.Inject;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import common.dao.GeoDao;
import models.geo.Province;

/**
 * Province per codice.
 *
 * @author marco
 *
 */
public class ProvinceJsonAdapter implements JsonSerializer<Province>,
	JsonDeserializer<Province> {

	private final GeoDao geoDao;

	@Inject
	ProvinceJsonAdapter(GeoDao dao) {
		geoDao = dao;
	}

	@Override
	public JsonElement serialize(Province src, Type typeOfSrc,
			JsonSerializationContext context) {
		return new JsonPrimitive(src == null ? "" :	src.code);
	}

	@Override
	public Province deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		return json.getAsString().length() == 0 ? null :
			geoDao.provinceByCode(json.getAsString()).orNull();
	}
}
