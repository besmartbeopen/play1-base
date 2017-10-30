package common.events;

import com.google.inject.Binder;
import com.google.inject.Module;
import common.injection.AutoRegister;


/**
 * @author marco
 *
 */
@AutoRegister
public class EventsModule implements Module {

  @Override
  public void configure(Binder binder) {
    // binder.bind(***Listener.class).asEagerSingleton();
  }
}
