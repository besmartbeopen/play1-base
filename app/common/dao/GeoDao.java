package common.dao;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQueryFactory;
import common.ModelQuery;
import common.ModelQuery.SimpleResults;
import java.util.List;
import javax.inject.Inject;
import lombok.val;
import models.common.geo.Municipal;
import models.common.geo.Province;
import models.common.geo.Region;
import models.common.geo.query.QMunicipal;
import models.common.geo.query.QProvince;
import models.common.geo.query.QRegion;

/**
 * DAO per l'accesso a comuni/province/regioni e rispettive geometri.
 *
 * @author marco
 *
 */
public class GeoDao {

  protected final JPQLQueryFactory queryFactory;

  @Inject
  GeoDao(JPQLQueryFactory queryFactory) {
    this.queryFactory = queryFactory;
  }

  /**
   * @param name
   * @param province
   * @return l'elenco dei comuni attivi filtrati in base ai parametri indicati.
   */
  public SimpleResults<Municipal> municipals(String name,
      Optional<Province> province) {
    final QMunicipal qm = QMunicipal.municipal;
    final BooleanBuilder condition = new BooleanBuilder();
    if (!Strings.nullToEmpty(name).trim().isEmpty()) {
      condition.and(qm.name.startsWithIgnoreCase(name));
    }
    if (province.isPresent()) {
      condition.and(qm.province.eq(province.get()));
    }
    return ModelQuery.wrap(queryFactory.from(qm)
        .where(condition)
        .orderBy(qm.name.asc()), qm);
  }

  /**
   * @param province
   * @return l'elenco dei comuni attivi nella provincia indicata.
   */
  public List<Municipal> municipalsByProvince(Province province) {
    Preconditions.checkNotNull(province);
    final QMunicipal qm = QMunicipal.municipal;
    return queryFactory.from(qm)
        .where(qm.province.eq(province), qm.enabled.isTrue())
        .orderBy(qm.name.asc())
        .select(qm).fetch();
  }

  /**
   * @return l'elenco delle regioni.
   */
  public List<Region> regions() {
    val region = QRegion.region;
    return queryFactory.selectFrom(region).orderBy(region.name.asc()).fetch();
  }

  /**
   * @param name
   * @return la regione corrispondente a <code>name</code>, se trovata.
   */
  public Optional<Region> regionByName(String name) {
    final QRegion region = QRegion.region;
    Region r = queryFactory.from(region).orderBy(region.name.asc())
        .where(region.name.equalsIgnoreCase(name)).select(region).fetchOne();
    return Optional.fromNullable(r);
  }

  /**
   * @return l'elenco di tutte le province.
   */
  public List<Province> provinces() {
    return queryFactory.selectFrom(QProvince.province)
        // TODO: filter active?
        .fetch();
  }

  /**
   * @param name
   * @return la provincia corrispondente a <code>name</code>, se trovata.
   */
  public Optional<Province> provinceByName(String name) {
    final QProvince province = QProvince.province;
    Province p = queryFactory.from(province)
        .where(province.name.equalsIgnoreCase(name))
        .select(province).fetchOne();
    return Optional.fromNullable(p);
  }

  public Optional<Province> provinceByCode(String name) {
    final QProvince province = QProvince.province;
    return Optional.fromNullable(queryFactory.from(province)
        .where(province.code.equalsIgnoreCase(name))
        .select(province).fetchOne());
  }

  public Optional<Municipal> allMunicipalByCode(int code) {
    final QMunicipal qm = QMunicipal.municipal;
    return Optional.fromNullable(queryFactory.selectFrom(qm)
        .where(qm.code.eq(code)).fetchOne());
  }

  public Optional<Municipal> municipalByCode(int code) {
    final QMunicipal qm = QMunicipal.municipal;
    return Optional.fromNullable(queryFactory.selectFrom(qm)
        .where(qm.code.eq(code), qm.enabled.isTrue()).fetchOne());
  }
}
