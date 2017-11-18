package controllers;

import com.querydsl.core.QueryResults;
import common.dao.NotificationDao;
import common.dao.TaskDao;
import common.jpa.JpaReferenceBinder;
import common.security.SecurityRules;
import models.common.Notification;
import models.common.Operator;
import models.common.Task;

import java.util.List;
import javax.inject.Inject;

import play.data.binding.As;
import play.data.validation.Required;
import play.mvc.Controller;
import play.mvc.With;

/**
 * @author marco
 *
 */
@With(Resecure.class)
public class Notifications extends Controller {

  @Inject
  static NotificationDao notificationDao;
  @Inject
  static SecurityRules rules;
  @Inject
  static TaskDao taskDao;

  public static void tasks() {
    QueryResults<Task> tasks = taskDao
        .tasksFor(Security.getCurrentUser().get()).listResults();
    render(tasks);
  }

  public static void archiveds() {
    final QueryResults<Notification> notifications = notificationDao
        .listFor(Security.getCurrentUser().get(), true)
        .listResults();
    render(notifications);
  }

  public static void list() {
    final QueryResults<Notification> notifications = notificationDao
        .listFor(Security.getCurrentUser().get(), false)
        .listResults();
    render("@archiveds", notifications);
  }

    /**
     * ajax only
     */
    public static void notifications() {
      final QueryResults<Notification> notifications = notificationDao
          .listUnreadFor(Security.getCurrentUser().get()).listResults();
      render(notifications);
    }

    public static void readAndRedirect(int id) {
      final Notification notification = Notification.findById(id);
      notFoundIfNull(notification);
      rules.checkIfPermitted(notification);

      notification.read = true;
      notification.save();
      redirect(notification.getUrl());
    }

    /**
     * Questa chiamata si assume che sia sempre POST+ajax.
     *
     * @param id
     */
    public static void read(int id) {
      final Notification notification = Notification.findById(id);
      notFoundIfNull(notification);
      rules.checkIfPermitted(notification);

      notification.read = true;
      notification.save();
      render("/Notifications/notification.html", notification);
    }

    /**
     * Elenco delle notifiche per l'operatore indicato da `id`.
     *
     * Nota: si può passare a questo elenco dalla pagina degli operatori.
     *
     * @param id
     */
    public static void listFor(int id) {
      final Operator operator = Operator.findById(id);
      notFoundIfNull(operator);
      final QueryResults<Notification> notifications = notificationDao
          .listFor(operator, false).listResults();
      render(operator, notifications);
    }

    public static void readFor(@Required Operator operator,
        @As(binder=JpaReferenceBinder.class) List<Notification> notifications,
        boolean all) {
      notFoundIfNull(operator);
      if (all) {
        notifications = notificationDao.listFor(operator, false).list();
      }
      if (notifications != null && !notifications.isEmpty()) {
        notifications.forEach(notification -> {
          notification.read = true;
          notification.save();
        });
        flash.success("Le %d notifiche sono state marcate come \"lette\".",
            notifications.size());
      } else {
        flash.error("Occorre almeno una notifica...");
      }
      listFor(operator.id);
    }
}
