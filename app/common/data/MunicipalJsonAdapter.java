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
import models.geo.Municipal;

/**
 * Comuni per codice-istat.
 *
 * @author marco
 *
 */
public class MunicipalJsonAdapter implements JsonDeserializer<Municipal>,
    JsonSerializer<Municipal> {

  private final GeoDao geoDao;

  @Inject
  MunicipalJsonAdapter(GeoDao geoDao) {
    this.geoDao = geoDao;
  }

  @Override
  public JsonElement serialize(Municipal src, Type typeOfSrc,
      JsonSerializationContext context) {
    return new JsonPrimitive(src == null ? 0 :	src.code);
  }

  @Override
  public Municipal deserialize(JsonElement json, Type typeOfT,
      JsonDeserializationContext context) throws JsonParseException {

    final int code = json.getAsInt();
    return code == 0 ? null :
      geoDao.allMunicipalByCode(code).orNull();
  }
}
