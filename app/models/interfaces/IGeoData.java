package models.interfaces;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vividsolutions.jts.geom.Geometry;

/**
 * @author marco
 *
 */
public interface IGeoData {

  int getId();
  String getName();
  @JsonIgnore
  Geometry getGeometry();
}
