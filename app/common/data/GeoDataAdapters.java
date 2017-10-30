package common.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.vividsolutions.jts.geom.Geometry;
import java.awt.Color;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import models.interfaces.IGeoData;
import org.geotools.brewer.color.ColorBrewer;

/**
 * @author marco
 *
 */
public class GeoDataAdapters {

  /**
   * Permette di ottenere una mappa di colori in funzione di un elenco di
   * elementi T discriminanti e la funzione che determina la corrispondenza.
   *
   * @author marco
   *
   * @param <T>
   */
  @Slf4j
  public static class MapColorAdapter<T> {

    private final Function<IGeoData, T> selector;
    private final Map<T, String> colorByInstance;
    private static final float FIXED_SATURATION = 0.7f;
    private static final float FIXED_VALUE = 0.6f;

    /**
     * TODO: aggiungere la selezionabilità della palette?
     *
     * @param colorClasses
     * @param selector
     */
    public MapColorAdapter(Collection<T> colorClasses, Function<IGeoData, T> selector) {
      this.selector = selector;
      final Color[] colors;
      val palette = ColorBrewer.instance().getPalette("Spectral").getColors();
      if (palette.length < colorClasses.size()) {
        log.info("palette ({}) does not have enough colors ({}), fallback to random.",
            palette.length, colorClasses.size());
        val rand = new Random();
        val colorSet = new HashSet<Color>(colorClasses.size());
        while (colorSet.size() < colorClasses.size()) {
          // si sceglie di prendere colori diversi solo per tonalità (HUE):
          colorSet.add(Color.getHSBColor(rand.nextFloat(), FIXED_SATURATION, FIXED_VALUE));
        }
        colors = colorSet.toArray(new Color[] {});
      } else {
        colors = palette;
      }
      val instances = ImmutableList.copyOf(colorClasses);
      colorByInstance = IntStream.range(0, instances.size())
          .boxed()
          .collect(ImmutableMap.<Integer, T, String>toImmutableMap(i -> instances.get(i),
            i -> "#" + Integer.toHexString(colors[i].getRGB() & 0xffffff)));
    }

    public IGeoData adapt(IGeoData m) {
      return new IGeoData() {

        @Override
        public int getId() {
          return m.getId();
        }

        @Override
        public String getName() {
          return m.getName();
        }

        @Override
        public Geometry getGeometry() {
          return m.getGeometry();
        }

        /**
         * WARNING: è usata via jackson -> geojson.
         *
         * @return il colore
         */
        @SuppressWarnings("unused")
        public String getFillColor() {
          return colorByInstance.get(selector.apply(m));
        }
      };
    }

    /**
     * @param data
     * @return una adapter per i colori, semplificato che associa un colore diverso a ciascun elemento
     * di data.
     */
    public static MapColorAdapter<IGeoData> simple(Collection<IGeoData> data) {
      return new MapColorAdapter<>(data, Function.identity());
    }
  }
}
