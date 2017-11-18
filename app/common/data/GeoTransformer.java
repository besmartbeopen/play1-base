package common.data;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.simplify.TopologyPreservingSimplifier;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import models.common.interfaces.IGeoData;

import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import play.mvc.Http.Response;

/**
 * @author marco
 *
 */
@Singleton
@Slf4j
public class GeoTransformer {

  /**
   * GPS coordinate system
   */
  public static final int WGS84_SRID = 4326;
  /**
   * Standard Italiano
   */
  public static final int ETRF2000_UTM32 = 32632;
  private static final String EPSG = "EPSG:";
  private static double DISTANCE_APPROX = 0.00001;
  private static double DISTANCE_SIMPLIFY = 0.00001;

  @RequiredArgsConstructor(staticName="of")
  @EqualsAndHashCode
  @ToString
  @Getter
  public static class TwoSrid {
    private final int source;
    private final int destination;
  }

  private final ObjectMapper mapper;

  private final LoadingCache<TwoSrid, MathTransform> transformers = CacheBuilder
      .newBuilder()
      .maximumSize(10)
      .build(new CacheLoader<TwoSrid, MathTransform>() {

        @Override
        public MathTransform load(TwoSrid key) throws Exception {
          final CoordinateReferenceSystem targetCRS = CRS.decode(EPSG + key.getDestination());
          final CoordinateReferenceSystem sourceCRS = CRS.decode(EPSG + key.getSource());
          return CRS.findMathTransform(sourceCRS, targetCRS, true);
        }
      });
  public static final String GEO_JSON_CONTENT_TYPE = "application/vnd.geo+json";

  @Inject
  public GeoTransformer(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  /**
   * @param geom
   * @param dstSrid
   * @return geometry translated to <code>srid</code>
   */
  protected Geometry convert(Geometry geom, int srcSrid, int dstSrid) {
    Preconditions.checkNotNull(geom);
    Preconditions.checkArgument(srcSrid != 0);
    Preconditions.checkArgument(dstSrid != 0);

    // short-circuit
    if (geom.getSRID() == dstSrid) {
      return geom;
    }
    try {
      final Geometry result = JTS.transform(geom, transformers
          .getUnchecked(TwoSrid.of(srcSrid, dstSrid)));
      if (result.getSRID() != dstSrid) {
        result.setSRID(dstSrid);
      }
      return result;
    } catch (TransformException | IllegalArgumentException e) {
      log.error("geometry transformation error on {} src-srid={} dst-srid={}",
          geom.getGeometryType(), srcSrid, dstSrid);
      throw new IllegalStateException(e);
    }
  }

  /**
   * @param geom
   * @return geometry translated to ETRF2000 - UTM32
   */
  public Geometry toItalian(Geometry geom) {
    Preconditions.checkNotNull(geom);

    return convert(geom, geom.getSRID() == 0 ? WGS84_SRID : geom.getSRID(),
        ETRF2000_UTM32);
  }

  /**
   * @param geom
   * @return geometry translated to WGS84.
   */
  public Geometry toGeography(Geometry geom) {
    Preconditions.checkNotNull(geom);
    try {
      // i poligono sono passati al setaccio per evitare errori:
      return JTS.toGeographic(geom.getGeometryType().contains("Polygon")
          ? geom.buffer(0) : geom,
          CRS.decode(EPSG + geom.getSRID()));
    } catch (TransformException | FactoryException | IllegalStateException e) {
      log.warn("toGeographic failed on {}", geom);
      throw new RuntimeException(e);
    }
  }

  /**
   * Translate to WGS84 and convert to json string.
   *
   * @param geom
   * @return geojson format of geom translated to WGS84.
   * @throws JsonProcessingException
   */
  public String toJson(Geometry geom) throws JsonProcessingException {
    return mapper.writeValueAsString(toGeography(geom));
  }

  @RequiredArgsConstructor
  @Getter
  public static class Feature {

    private final String type = "Feature";
    private final Geometry geometry;
    private final Object properties;
  }

  @RequiredArgsConstructor
  @Getter
  public static class FeatureCollection {

    private final String type = "FeatureCollection";
    private final List<Feature> features = new ArrayList<>();
  }

  /**
   * Scrive il GeoJson corrispondente normalizzato in WGS84 su response.
   *
   * @param response
   * @param geoData
   * @throws IOException
   * @throws JsonMappingException
   * @throws JsonGenerationException
   */
  public void renderGeoJson(Response response, IGeoData geoData)
      throws JsonGenerationException, JsonMappingException, IOException {

    response.contentType = GEO_JSON_CONTENT_TYPE;
    mapper.writeValue(response.out, new Feature(toGeography(geoData
        .getGeometry()), geoData));
  }

  /**
   * Scrive il GeoJson corrispondente normalizzato in WGS84 su response.
   *
   * @param response
   * @param geoDatas
   * @throws IOException
   * @throws JsonMappingException
   * @throws JsonGenerationException
   */
  public void renderGeoJson(Response response, Stream<IGeoData> geoDatas)
      throws JsonGenerationException, JsonMappingException, IOException {

    response.contentType = GEO_JSON_CONTENT_TYPE;
    final FeatureCollection fc = new FeatureCollection();
    geoDatas.map(m -> new Feature(toGeography(m.getGeometry()), m))
      .forEach(fc.getFeatures()::add);
    mapper.writeValue(response.out, fc);
  }

  public Geometry fix(Geometry geometry) {
    Preconditions.checkState(!geometry.isValid());
    Geometry result = geometry.buffer(DISTANCE_APPROX);
    Verify.verify(result.isValid(), "geometry result must be valid");
    if (result instanceof Polygon) {
      final GeometryFactory factory = JTSFactoryFinder.getGeometryFactory();
      result = factory.createMultiPolygon(ImmutableList.of(result)
          .toArray(new Polygon[0]));
    }
    result.setSRID(geometry.getSRID());
    return result;
  }

  /**
   * @param geometry
   * @return union of geometry on the same srid.
   */
  public Geometry union(Collection<Geometry> geometries) {
    Preconditions.checkState(!geometries.isEmpty(), "at least a geometry is required");
    final GeometryFactory factory = JTSFactoryFinder.getGeometryFactory();
    // le geometrie vengono approssimate con un piccolo margine:
    Geometry geometry = factory.buildGeometry(geometries.stream()
        .map(geom -> TopologyPreservingSimplifier.simplify(geom.buffer(DISTANCE_APPROX),
            DISTANCE_SIMPLIFY))
        .collect(ImmutableList.toImmutableList())).union();
    if (geometry instanceof Polygon) {
      geometry = factory.createMultiPolygon(ImmutableList.of(geometry)
          .toArray(new Polygon[0]));
    }
    // get the srid from the first geometry
    geometry.setSRID(geometries.iterator().next().getSRID());
    return geometry;
  }

  /**
   * @param subtractor
   * @param subtractings
   * @return la differenza tra <code>subtractor</code> e i <code>subtractings</code>.
   */
  public Geometry subtract(Geometry subtractor, Stream<Geometry> subtractings) {
    // si procede facendo la differenza tra il subtractor senza un piccolo margine di
    // approssimazione e l'unione dei subtractings, con un piccolo margine aggiuntivo
    // di approssimazione:
    Geometry geometry = subtractor.buffer(-DISTANCE_APPROX).difference(union(subtractings
         .collect(ImmutableList.toImmutableList())).buffer(DISTANCE_APPROX));
    Verify.verify(geometry.isValid(), "geometry result must be valid");
    if (geometry instanceof Polygon) {
      final GeometryFactory factory = JTSFactoryFinder.getGeometryFactory();
      geometry = factory.createMultiPolygon(ImmutableList.of(geometry)
          .toArray(new Polygon[0]));
    }
    geometry.setSRID(subtractor.getSRID());
    return geometry;
  }
}
