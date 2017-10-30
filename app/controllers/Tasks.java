package controllers;

import com.google.common.base.Optional;
import com.querydsl.core.QueryResults;
import common.Web;
import common.dao.TaskDao;
import common.security.SecurityRules;
import java.util.Set;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import models.Operator;
import models.Task;
import models.enums.TaskType;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.With;

/**
 * @author cristian
 *
 */
@Slf4j
@With(Resecure.class)
public class Tasks extends Controller {

  @Inject
  static SecurityRules rules;
  @Inject
  static TaskDao taskDao;

  public static void list(Optional<Operator> operator,
      Optional<Boolean> alreadyStarted, Optional<TaskType> taskType) {
    Set<TaskType> types = taskType.asSet();
    QueryResults<Task> tasks = taskDao
        .allTasks(operator, alreadyStarted, types).listResults();
    render(tasks, operator, alreadyStarted, taskType);
  }

  public static void task(int id) {
    final Task task = Task.findById(id);
    notFoundIfNull(task);
    rules.checkIfPermitted(task);

    throw new IllegalStateException("not implemented yet... " + task.type);
  }

    /**
     * Questa chiamata si assume che sia sempre DELETE+ajax.
     *
     * @param id
     */
    public static void delete(int id) {
      final Task task = Task.findById(id);
      notFoundIfNull(task);
      rules.checkIfPermitted(task);
       task.delete();
       log.info("Task deleted. id = {}, description = {}",
           task.id, task.description);
    flash.success(Messages.get(Web.MSG_DELETED, task));
      list(Optional.<Operator>absent(), Optional.<Boolean>absent(), Optional.<TaskType>absent());
    }
}
