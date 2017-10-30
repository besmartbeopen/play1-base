package common.data;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import common.TemplateExtensions;
import common.injection.AutoRegister;
import java.io.IOException;

/**
 * Object mapper con autoregistrazione dei moduli jackson.
 *
 * @author marco
 *
 */
@AutoRegister
public class JacksonModule implements Module {

  /**
   * @return il factory per le geometrie con lo srid "giusto".
   */
  @Provides
  @Singleton
  public GeometryFactory geometryFactory() {
    return new GeometryFactory(new PrecisionModel(),
        GeoTransformer.WGS84_SRID);
  }

  @Provides
  @Singleton
  public ObjectMapper mapper(GeometryFactory geometryFactory) {
    final ObjectMapper mapper = new ObjectMapper();
    // registrazione esplicita per indicare lo srid di default WGS:
    mapper.registerModule(new JtsModule(geometryFactory));
    mapper.findAndRegisterModules();
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    // mapper.addMixIn(BaseModel.class, BaseModelMixin.class);
    return mapper;
  }

  @SuppressWarnings({"serial", "rawtypes"})
  @Provides
  @Singleton
  public CsvMapper csvMapper() {
    final CsvMapper mapper = new CsvMapper();
    mapper.findAndRegisterModules();
    mapper.configure(Feature.IGNORE_UNKNOWN, true);

    // conversione enum -> label
    final SimpleModule enumModule = new SimpleModule();
    enumModule.addSerializer(Enum.class, new StdSerializer<Enum>(Enum.class) {
      @Override
      public void serialize(Enum value, JsonGenerator jgen, SerializerProvider provider)
          throws IOException {
        jgen.writeString(TemplateExtensions.label(value));
      }
    });
    mapper.registerModule(enumModule);
    return mapper;
  }

  @Override
  public void configure(Binder binder) {
  }
}
