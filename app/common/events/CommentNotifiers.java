package common.events;

import com.google.common.base.Predicates;
import com.google.common.collect.Sets;
import com.google.common.eventbus.Subscribe;
import common.events.bus.EventSubscriber;
import lombok.extern.slf4j.Slf4j;
import models.common.Comment;
import models.common.Notification;
import models.common.Operator;
import models.common.enums.NotificationSubject;
import play.i18n.Messages;

import javax.inject.Inject;

@Slf4j
@EventSubscriber
public class CommentNotifiers {

  @Inject
  CommentNotifiers() { }

  @Subscribe
  public void updateComment(Comment comment) {
    // solo nel caso sia creato/modificato (non cancellato)
    if (comment.isPersistent()) {
      // occorre notificare gli interessati, ad esclusione dell'autore
      // del commento.
      for (Operator operator : Sets.filter(comment.getInvolveds(),
          Predicates.not(Predicates.equalTo(comment.owner)))) {
        final String msg = Messages.get(comment.isUpdated() ?
            "comment.updated" : "comment.created",
            comment.owner,comment.root().getLabel());
        Notification.builder().destination(operator)
          .message(msg)
          .subject(NotificationSubject.COMMENT, comment.id)
          .create()
          .save();
        log.debug("notifica commento per {}", operator);
      }
    }
  }
}
