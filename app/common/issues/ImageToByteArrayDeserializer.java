package common.issues;

import com.google.common.base.Preconditions;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Base64;

/**
 * @author marco
 *
 */
public class ImageToByteArrayDeserializer implements JsonDeserializer<byte[]> {

  private final static String IMAGE_MAGIK = "data:image/png;base64,";

  @Override
  public byte[] deserialize(JsonElement json, Type typeOfT,
      JsonDeserializationContext context) throws JsonParseException {
    final String value = json.getAsString();
    Preconditions.checkState(value.startsWith(IMAGE_MAGIK));
    return Base64.getDecoder().decode(value.substring(IMAGE_MAGIK.length()));
  }
}
