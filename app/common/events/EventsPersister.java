package common.events;

import org.joda.time.LocalDateTime;

import com.google.common.eventbus.Subscribe;
import common.events.bus.EventSubscriber;
import common.events.data.LoggedInEvent;
import common.events.data.MessageCreated;
import lombok.extern.slf4j.Slf4j;
import models.MessageDetail;
import models.Notification;
import models.enums.NotificationSubject;
import play.i18n.Messages;

/**
 * Un listener per eventi generici.
 *
 * @author marco
 *
 */
@Slf4j
@EventSubscriber
public class EventsPersister {

  @Subscribe
  public void loggedIn(LoggedInEvent event) {
    event.value.lastLoginAddress = event.fromAddress;
    event.value.lastLoginDate = new LocalDateTime();
    event.value.save();
  }

  /**
   * Notifica ai destinatari che è stato creato un messaggio
   * @param event
   */
  @Subscribe
  public void messageCreated(MessageCreated event) {
    log.debug("starting notifications for {}", event);
    //I dettagli potrebbero non essere attached alla entity corrente
    event.value.refresh();
    for (MessageDetail messageDetail : event.value.details) {
      log.debug("new notification for {} -> {}", messageDetail.operator, event);
      Notification.builder().destination(messageDetail.operator)
        .message(Messages.get("notify.messageCreated", event.value.source.getFullname()))
        .subject(NotificationSubject.MESSAGE, event.value.id)
        .create();
    }
  }
}
