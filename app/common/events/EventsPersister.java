package common.events;

import com.google.common.eventbus.Subscribe;
import common.events.bus.EventSubscriber;
import common.events.data.LoggedInEvent;
import org.joda.time.LocalDateTime;

/**
 * Un listener per eventi generici.
 *
 * @author marco
 *
 */
@EventSubscriber
public class EventsPersister {

  @Subscribe
  public void loggedIn(LoggedInEvent event) {
    event.value.lastLoginAddress = event.fromAddress;
    event.value.lastLoginDate = new LocalDateTime();
    event.value.save();
  }
}
