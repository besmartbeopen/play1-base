package controllers;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.querydsl.core.QueryResults;
import common.DateRange;
import common.Tools;
import common.Web;
import common.dao.MessageDao;
import common.dao.OperatorDao;
import common.events.data.MessageCreated;
import common.jpa.JpaReferenceBinder;
import common.security.SecurityRules;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import models.Message;
import models.MessageDetail;
import models.Operator;
import play.data.binding.As;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.data.validation.Validation;
import play.db.jpa.Blob;
import play.mvc.Controller;
import play.mvc.With;

/**
 * @author cristian
 *
 */
@With(Resecure.class)
@Slf4j
public class Messages extends Controller {

  @Inject
  static MessageDao messageDao;

  @Inject
  static OperatorDao operatorDao;

  @Inject
  static SecurityRules rules;

  @Inject
  static EventBus eventbus;

  public static void index() {
    flash.keep();
    list(Optional.<String>absent(),
        Sets.<String>newHashSet(), Optional.<Boolean>absent(),
        Optional.<Boolean>absent(), Optional.<DateRange>absent());
  }

  public static void list(
      Optional<String> subject,
      Set<String> tags,
      Optional<Boolean> read,
      Optional<Boolean> starred,
      Optional<DateRange> dateRange) {
    final QueryResults<Message> msgs =
        messageDao.list(
            Optional.<Operator>absent(),
            Optional.<Operator>absent(), subject,
            tags, read, starred,
            dateRange).listResults();
    render(msgs, subject, tags, dateRange);
  }

  public static void show(int id) {
    final Message message = Message.findById(id);
    notFoundIfNull(message);
    rules.checkIfPermitted(message);
    render(message);
  }

  public static void download(int id, final String reference) throws IOException {
    final Message message = Message.findById(id);
    notFoundIfNull(message);
    final Blob blob = FluentIterable.from(message.attachments)
        .firstMatch(new Predicate<Blob>() {

          @Override
          public boolean apply(Blob input) {
            return reference.equals(input.getUUID());
          }
        }).get();
    Tools.renderFile(blob.getFile(), blob.type());
  }

  public static void edit(int id) {
    final Message message = Message.findById(id);
    notFoundIfNull(message);
    render(message);
  }

  public static void update(@Required @Valid Message message) {
    Preconditions.checkState(message.isPersistent());

    if (params.get("_delete") != null) {
      rules.checkIfPermitted(message);

      message.delete();
      flash.success(play.i18n.Messages.get(Web.MSG_DELETED, "message"));
      index();
    }

    if (Validation.hasErrors()) {
      flash.error(Web.msgHasErrors());
      log.debug("Errori durante il salvataggio del messaggio {}: {}", message, validation.errorsMap());
      render("@edit", message);
    } else {
      message.save();
      flash.success(Web.msgModified(Message.class));
      index();
    }
  }

  public static void blank(Message message) {
    if (message == null)  {
      message = new Message();
    }
    render(message);
  }

  public static void create(@Required @Valid Message message,
      @As(binder=JpaReferenceBinder.class) List<Operator> recipients) {
    Preconditions.checkState(!message.isPersistent());

    if (Validation.hasErrors()) {
      flash.error(Web.msgHasErrors());
      log.info("Errori durante la creazione del messaggio {}: {}", message, validation.errorsMap());
      render("@blank", message, recipients);
    } else {
      if (recipients == null) {
        recipients = operatorDao.all().list();
      }
      message.source = Security.getCurrentUser().get();
      message.save();
      for (Operator operator : recipients) {
        (new MessageDetail(message, operator, Sets.newHashSet(message.tags))).save();
      }

      flash.success(Web.msgCreated(Message.class));
      eventbus.post(MessageCreated.instanceFor(message));

      index();
    }
  }
}
