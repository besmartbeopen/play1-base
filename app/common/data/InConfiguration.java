package common.data;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Set;

import javax.validation.constraints.Min;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.inject.Inject;
import com.google.inject.ProvidedBy;
import com.google.inject.Provider;
import common.binders.CurrencyBinder;
import lombok.extern.slf4j.Slf4j;
import play.Play;
import play.cache.Cache;
import play.data.binding.As;
import play.data.validation.Email;
import play.data.validation.Required;

/**
 * Configurazione locale.
 *
 * @author marco
 *
 */
@ProvidedBy(InConfiguration.InConfigurationManager.class)
public class InConfiguration implements Serializable {

  private static final long serialVersionUID = 4295379641843586790L;

  @Required
  @Email
  public String generalFrom;

  @Required @As(", *")
  public Set<String> salesOrderTo;

  @Required @Min(0) @As(binder=CurrencyBinder.class)
  public BigDecimal costPerKm;

  @Required @Min(0) @As(binder=CurrencyBinder.class)
  public BigDecimal refundPerKm;

  public boolean geoMaster;

  /**
   * Tempo massimo per i rilasci di saggi (in mesi).
   * <br>
   * <b>valore predefinito: 1</b>
   */
  @Required
  @Min(0)
  public int maximumNumberOfMonthsForSampleLeft = 1;

  /**
   * Numero di mesi precedenti all'attuale di cui calcolare 
   * le commissioni.
   */
  @Required
  @Min(0)
  public int numberOfMonthsForMonthFeeAdvance = 0;
  
  /**
   * Se abilitato automatizza l'esportazione dei csv dei clienti/indirizzi.
   */
  public boolean nightlyExporterEnabled;

  /**
   * Gestione della configurazione.
   *
   * @author marco
   *
   */
  @Slf4j
  public static class InConfigurationManager implements Provider<InConfiguration> {
    private static final String PATH = "conf/inphase.json";
    static final InConfiguration SINGLETON = new InConfiguration();

    private final GsonBuilder builder;

    @Inject
    InConfigurationManager(GsonBuilder builder) {
      this.builder = builder;

      builder.registerTypeAdapter(InConfiguration.class,
          new	InstanceCreator<InConfiguration>() {

        @Override
        public InConfiguration createInstance(Type arg0) {
          return InConfigurationManager.SINGLETON;
        }
      });
    }

    InConfiguration read() {
      final File file = Play.getFile(PATH);
      try {
        final String json = Files.asCharSource(file, Charsets.UTF_8).read();
        return builder.create().fromJson(json, InConfiguration.class);
      } catch (IOException e) {
        log.error("error reading \"{}\" configuration file: {}", file, e);
        // empty configuration
        return SINGLETON;
      }
    }

    public void save(InConfiguration configuration) throws IOException {
      final File file = Play.getFile(PATH);
      Files.asCharSink(file, Charsets.UTF_8)
        .write(builder.create().toJson(configuration));
      Cache.delete(PATH);
    }

    @Override
    public InConfiguration get() {
      InConfiguration conf = Cache.get(PATH, InConfiguration.class);
      if (conf == null) {
        conf = read();
        Cache.set(PATH, conf);
      }
      return conf;
    }
  }
}
