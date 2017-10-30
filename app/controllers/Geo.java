package controllers;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import common.ValueLabelItem;
import common.dao.GeoDao;
import java.util.List;
import javax.inject.Inject;
import lombok.val;
import models.geo.Province;
import play.mvc.Controller;
import play.mvc.With;

/**
 * Controller utile per comuni, province e regioni.
 *
 * @author marco
 *
 */
@With(Resecure.class)
public class Geo extends Controller {

  private static final long MAX_MUNICIPAL_RESULTS = 10L;

  @Inject
  static GeoDao geoDao;

  /**
   * Elenco province.
   */
  public static void provinces() {
    // usa il $data.allProvinces
    render();
  }

  /**
   * Elenco comuni per provincia (id).
   *
   * @param id
   */
  public static void municipalsByProvince(int id) {
    final Province province = Province.findById(id);
    notFoundIfNull(province);
    val municipals = geoDao.municipalsByProvince(province);
    render(municipals, province);
  }

  /**
   * Ricerca ajax/json dei comuni per caratteri iniziali.
   *
   * @param q
   */
  public static void municipalByName(String q) {
    if (Strings.isNullOrEmpty(q)) {
      notFound("required query string");
    }

    final List<ValueLabelItem> options = geoDao
        .municipals(q, Optional.absent())
        .list(MAX_MUNICIPAL_RESULTS).stream()
        .map(m -> new ValueLabelItem(m.id, m.name))
        .collect(ImmutableList.toImmutableList());
    renderJSON(options);
  }

  /**
   * Elenco dei comuni, filtrabile per nome, provincia...
   *
   * @param name
   * @param province
   * @param woMicroAreas
   */
  public static void municipals(String name, Optional<Province> province) {
    val municipals = geoDao.municipals(name, province).listResults();
    render(municipals, name, province);
  }
}
