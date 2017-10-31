package common.dao;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSet;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQueryFactory;
import common.ModelQuery;
import common.ModelQuery.SimpleResults;
import java.util.Set;
import javax.inject.Inject;
import models.Operator;
import models.Task;
import models.enums.TaskTargetType;
import models.query.QTask;
import org.joda.time.LocalDate;

/**
 * @author marco
 * @author cristian
 *
 */
public class TaskDao {

  private final JPQLQueryFactory queryFactory;

  @Inject
  TaskDao(JPQLQueryFactory queryFactory) {
    this.queryFactory = queryFactory;
  }

  public Supplier<SimpleResults<Task>> tasksSupplierFor(
      final Supplier<Operator> operator,
      final TaskTargetType ...type) {
    Preconditions.checkNotNull(operator);
    Preconditions.checkNotNull(type);

    return () -> tasksFor(operator.get(), ImmutableSet.copyOf(type));
  }

  /**
   * @param operator
   * @param types
   * @return l'elenco dei task pendenti per l'operator, eventualmente filtrati
   * per tipi di task.
   */
  public SimpleResults<Task> tasksFor(Operator operator,
      Set<TaskTargetType> types) {
    return allTasks(Optional.of(operator), Optional.of(true), types);
  }

  /**
   * @param operator
   * @return l'elenco dei task pendenti per l'operatore.
   */
  public SimpleResults<Task> tasksFor(Operator operator) {
    return allTasks(Optional.of(operator), Optional.of(true),
        ImmutableSet.of());
  }

  public SimpleResults<Task> allTasks(Optional<Operator> operator,
      Optional<Boolean> alreadyStarted, Set<TaskTargetType> types) {
      Preconditions.checkNotNull(types);

    final QTask task = QTask.task;
    final BooleanBuilder conditions = new BooleanBuilder();
    if (operator.isPresent()) {
      conditions.and(task.candidateAssignees.contains(operator.get()));
    }
    if (alreadyStarted.isPresent()) {
      if (alreadyStarted.get()) {
        conditions.and(task.startDate.loe(LocalDate.now())
          .or(task.startDate.isNull()));
      } else {
        conditions.and(task.startDate.gt(LocalDate.now()));
      }
    }
    if (!types.isEmpty()) {
      conditions.and(task.targetType.in(types));
    }
    return ModelQuery.wrap(queryFactory.from(task)
        .where(conditions)
        .orderBy(task.createdAt.asc()).select(task));

  }
}
