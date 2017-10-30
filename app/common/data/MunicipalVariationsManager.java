package common.data;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import common.dao.GeoDao;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import javax.inject.Inject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import models.geo.Municipal;
import models.geo.Province;

/**
 * @author marco
 */
@Slf4j
public class MunicipalVariationsManager {

  public enum VariationType {
    /** Viene descritta la costituzione comune */
    CS,
    /** Viene descritta l'estinzione di un comune */
    ES,
    /** Viene descritto il cambio denominazione Comune */
    CD,
    /** Viene descritta l'acquisizione di territorio */
    AQ,
    /** Viene descritta la cessione di territorio */
    CE,
    /**
     * Viene descritto il cambio di appartenenza alla unità amministrativa
     * gerarchicamente superiore (tipicamente, viene associato ad un cambio
     * di provincia e o regione).
     */
    AP
  }

  private final static int MUNICIPAL_FIELD = 1000;
  private final static int MIN_YEAR = 2013;

  /**
   * Rivisto e corrette in funzione delle variazioni amministrative che ogni
   * anno produce l'ISTAT.
   *
   * @author marco
   *
   */
  @Data
  public static class MunicipalVariation {
    private int year;
    private VariationType type;
    private int regionCode;
    private int municipalCode;
    private String name;
    private int otherMunicipalCode;
    private String otherName;
    private String reference;
    private String description;
    private String fromDate;

    public int getProvinceId() {
      return getMunicipalCode() / MUNICIPAL_FIELD;
    }
  }

  private final GeoDao geoDao;
  private final CsvMapper mapper;

  @Inject
  MunicipalVariationsManager(GeoDao geoDao, CsvMapper mapper) {
    this.geoDao = geoDao;
    this.mapper = mapper;
  }

  protected static class DoVariations {

    private final LoadingCache<Integer, Optional<Municipal>> municipalByCode;
    private final LoadingCache<Integer, Province> provinceByCode;

    DoVariations(GeoDao geoDao) {

      municipalByCode = CacheBuilder
          .newBuilder()
          .build(new CacheLoader<Integer, Optional<Municipal>>() {

            @Override
            public Optional<Municipal> load(Integer code) throws Exception {
              Preconditions.checkNotNull(code);
              return geoDao.allMunicipalByCode(code).toJavaUtil();
            }
          });

      provinceByCode = CacheBuilder
          .newBuilder()
          .build(new CacheLoader<Integer, Province>() {

            @Override
            public Province load(Integer code) throws Exception {
              Preconditions.checkNotNull(code);
              return Province.findById(code);
            }
          });
    }

    /**
     * @return il comune associato al code.
     */
    private Optional<Municipal> municipalBy(Integer code) {
      Preconditions.checkNotNull(code);

      return municipalByCode.getUnchecked(code);
    }

    /**
     * Aggiunge il comune indicato.
     *
     * @param municipal
     */
    private void addMunicipal(Municipal municipal) {
      Preconditions.checkNotNull(municipal);

      municipalByCode.put(municipal.code, Optional.of(municipal));
    }

    private Province provinceBy(Integer code) {
      Preconditions.checkNotNull(code);

      return provinceByCode.getUnchecked(code);
    }

    public void process(MunicipalVariation variation) {
      final Optional<Municipal> om = municipalBy(variation.getMunicipalCode());
      switch (variation.getType()) {
        case AP:
          // cambio di appartenenza:
          if (om.isPresent()) {
            // se c'è il nuovo comune non c'è da fare niente su questo:
            log.debug("municipal {} was already changed to {}.",
                om.get(), om.get().province);
          } else {
            // altrimenti occorre prendere il vecchio comune e cambiarne il codice:
            final Municipal municipal = municipalBy(variation
                .getOtherMunicipalCode()).get();
            municipal.code = variation.getMunicipalCode();
            municipal.province = provinceBy(variation.getProvinceId());
            municipal.save();
            log.info("municipal {} changed to {}",
                municipal, municipal.province);
          }
          break;
        case AQ:
          // acquisizione territorio:
          // - municipalCode è il codice istat del nuovo comune che acquisisce;
          // - otherMunicipalCode è il codice istat del comune acquisito.
          log.debug("skipping territory acquisition {}", variation);
          break;
        case CD:
          // cambio nome comune
          if (om.isPresent()) {
            final Municipal municipal = om.get();
            if (municipal.name.equals(variation.getOtherName())) {
              log.info("name already changed in {}", municipal);
            } else {
              municipal.name = variation.getOtherName();
              municipal.save();
              log.info("municipal name changed: {}.", municipal);
            }
          } else {
            log.info("municipal not found, skipping name change for {}.", variation);
          }
          break;
        case CE:
          // cessione territorio
          om.ifPresent(municipal -> {
            municipal.enabled = false;
            municipal.save();
            log.info("municipal {} was been given: disabled.", municipal);
          });
          break;
        case CS:
          // nuovo comune
          if (om.isPresent()) {
            final Municipal m = om.get();
            if (m.enabled == false) {
              m.enabled = true;
              m.save();
              log.info("re-enabled new municipal {}", m);
            } else {
              log.info("new municipal was already created, skipping {}.", variation);
            }
          } else {
            final Municipal municipal = new Municipal();
            municipal.code = variation.getMunicipalCode();
            municipal.name = variation.getName();
            municipal.province = provinceBy(variation.getProvinceId());
            municipal.save();
            // notifica alla cache.
            addMunicipal(municipal);

            log.info("new municipal {} saved", municipal);
          }
          break;
        case ES:
          // cancellazione comune
          if (om.isPresent()) {
            final Municipal municipal = om.get();
            if (municipal.enabled != false) {
              municipal.enabled = false;
              municipal.save();
              log.info("disabling municipal {}", municipal);
            } else {
              log.info("skipping municipal {}, because was already disabled.",
                  municipal);
            }
          } else {
            log.info("municipal not found, skipping deletion {}.", variation);
          }
          break;
        default:
          throw new IllegalStateException("unknown type: " + variation.getType());
      }
    }
  }

  public void process(File file) throws IOException {
    final DoVariations variations = new DoVariations(geoDao);
    CsvSchema schema = mapper.schemaFor(MunicipalVariation.class)
        .withHeader();
    mapper.readerFor(MunicipalVariation.class).with(schema)
      .<MunicipalVariation> readValues(file).forEachRemaining(v -> {
        // solo le modifiche da MIN_YEAR:
        if (v.getYear() >= MIN_YEAR) {
          variations.process(v);
        }
      });
  }
}
