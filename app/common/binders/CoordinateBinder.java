package common.binders;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Iterator;

import javax.inject.Inject;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import common.injection.StaticInject;
import play.data.binding.Global;
import play.data.binding.TypeBinder;

/**
 * @author marco
 *
 */
@Global
@StaticInject
public class CoordinateBinder implements TypeBinder<Point> {

  private final static Splitter SPLITTER = Splitter.on(',').limit(2);

  @Inject
  static GeometryFactory geometryFactory;

  @Override
  public Object bind(String name, Annotation[] anns, String value,
      @SuppressWarnings("rawtypes") Class actualClass, Type genericType) {

    if (!Strings.isNullOrEmpty(value)) {
      final Iterator<String> iter = SPLITTER.split(value).iterator();
      final double lat = Double.parseDouble(iter.next());
      final double lon = Double.parseDouble(iter.next());
      final Coordinate coord = new Coordinate(lon, lat);
      return geometryFactory.createPoint(coord);
    } else {
      return null;
    }
  }
}


