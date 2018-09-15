package common.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Provider;
import lombok.extern.slf4j.Slf4j;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

/**
 * Avvia il calcolo delle metriche registrando tutte quelle configurate.
 *
 * @author marco
 *
 */
@OnApplicationStart
@Slf4j
public class StartMetrics extends Job <Void> {

  /**
   * Questa classe è utile subito
   * @author marco
   *
   */
  public static class Starter {

    private final MeterRegistry registry;
    // per posticipare l'accesso appena dopo l'avvio:
    private final Provider<Set<MeterBinder>> meterBindersProvider;

    @Inject
    Starter(MeterRegistry registry,
        Provider<Set<MeterBinder>> meterBindersProvider) {
      this.registry = registry;
      this.meterBindersProvider = meterBindersProvider;
    }

    public void run() {
      registry.config().commonTags("application", "play1-base");
      meterBindersProvider.get().forEach(mb -> mb.bindTo(registry));
      log.debug("meter-registry setup completed for application  \"{}\"", "play1-base");
    }
  }

  @Inject
  static Starter starter;

  @Override
  public void doJob() {
    starter.run();
  }
}
