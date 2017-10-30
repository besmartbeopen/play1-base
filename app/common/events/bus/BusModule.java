package common.events.bus;

import com.google.common.base.Verify;
import com.google.common.eventbus.EventBus;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import common.injection.AutoRegister;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Modulo per l'auto-registrazione su eventbus specifici degli oggetti annotati
 * con @EventSubscriber e creati col Guice.
 *
 * @author marco
 *
 */
@AutoRegister
@Slf4j
public class BusModule implements Module {

  /**
   * Mappa degli EventBus per nome.
   */
  private final Map<String, EventBus> busMap = EventBusKey.values().stream()
      .collect(Collectors.toMap(Function.identity(),
          k -> new EventBus(k)));

  @Override
  public void configure(Binder binder) {
    busMap.forEach((k, v) -> {
      if (k.equals(EventBusKey.DEFAULT)) {
        // per il default non occorre alcuna annotazione.
        binder.bind(EventBus.class).toInstance(v);
      } else {
        // le altre sono annotate con la propria chiave.
        binder.bind(EventBus.class).annotatedWith(Names.named(k)).toInstance(v);
      }
    });

    binder.bindListener(TypeMatchers.annotatedWith(EventSubscriber.class),
        new TypeListener() {

      @Override
      public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
        final EventSubscriber annotation = type.getRawType()
            .getAnnotation(EventSubscriber.class);
        Verify.verifyNotNull(annotation);
        Verify.verify(busMap.containsKey(annotation.value()),
            "eventBus not valid: " + annotation.value());

        final EventBus bus = busMap.get(annotation.value());
        encounter.register(new InjectionListener<I>() {
          @Override
          public void afterInjection(I injectee) {
            log.info("auto-register eventsubscriber {} on {}.",
                injectee.getClass().getName(), bus);
            bus.register(injectee);
          }
        });
      }
    });
  }
}
