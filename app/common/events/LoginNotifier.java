package common.events;

import com.google.common.eventbus.Subscribe;
import common.events.bus.EventSubscriber;
import common.events.data.LoggedInEvent;
import play.i18n.Messages;
import play.mvc.Scope.Flash;

/**
 * Notifica della nota "disclaimer" nel caso ci sia nel profilo.
 *
 * @author marco
 *
 */
@EventSubscriber
public class LoginNotifier {

  @Subscribe
  public void loggedIn(LoggedInEvent event) {
    Flash.current().success(Messages.get("secure.loggedIn", event.value));
  }
}
