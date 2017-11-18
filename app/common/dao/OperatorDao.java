package common.dao;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.JPQLQueryFactory;
import common.DateRange;
import common.ModelQuery;
import common.ModelQuery.SimpleResults;
import common.Tools;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import lombok.val;
import models.common.Operator;
import models.common.RecoveryRequest;
import models.common.base.query.QRevision;
import models.common.enums.Role;
import models.common.query.QOperator;
import models.common.query.QRecoveryRequest;

import org.joda.time.LocalDateTime;

/**
 * @author marco
 *
 */
public class OperatorDao {

  private final JPQLQueryFactory queryFactory;

  @Inject
  OperatorDao(JPQLQueryFactory queryFactory) {
    this.queryFactory = queryFactory;
  }

  public static void addNameConditionIfRequired(BooleanBuilder predicate,
      Optional<String> name, QOperator operator) {
    if (name.isPresent()) {
      final BooleanBuilder nameCondition = new BooleanBuilder();
      for (String token : Tools.TOKEN_SPLITTER.split(name.get())) {
        nameCondition.andAnyOf(operator.firstname
          .startsWithIgnoreCase(token),
          operator.lastname.startsWithIgnoreCase(token));
      }
      predicate.andAnyOf(nameCondition,
        operator.lastname.startsWithIgnoreCase(name.get()),
        operator.firstname.startsWithIgnoreCase(name.get()));
    }
  }

  public Optional<Operator> byEmail(String email) {
    final QOperator operator = QOperator.operator;
    return Optional.fromNullable(queryFactory.from(operator)
        .where(operator.email.equalsIgnoreCase(email.trim())
            .and(operator.enabled.isTrue()))
            .select(operator).fetchOne());
  }

  public Optional<RecoveryRequest> byCode(String code) {
    final QRecoveryRequest rr = QRecoveryRequest.recoveryRequest;
    return Optional.fromNullable(queryFactory.from(rr)
        .where(rr.code.eq(code)).select(rr).fetchOne());
  }

  public long deleteRecoveryRequestOlderThan(LocalDateTime ldt) {
    final QRecoveryRequest rr = QRecoveryRequest.recoveryRequest;
    return queryFactory.delete(rr).where(rr.createdAt.before(ldt)).execute();
  }

  /**
   * @return tutti gli operatori, ordinati alfabeticamente.
   */
  public SimpleResults<Operator> all() {
    final QOperator operator = QOperator.operator;
    final JPQLQuery<Operator> query = queryFactory.from(operator)
        .where(operator.enabled.isTrue())
        .orderBy(operator.lastname.asc(),
            operator.firstname.asc()).select(operator);
    return ModelQuery.wrap(query);
  }

  public SimpleResults<Operator> list(Optional<String> name,
      Optional<Boolean> enabled) {
    final QOperator operator = QOperator.operator;

    JPQLQuery<Operator> query = queryFactory.from(operator)
        .select(operator);
    final BooleanBuilder predicate = new BooleanBuilder();

    addNameConditionIfRequired(predicate, name, operator);

    if (enabled.isPresent()) {
      predicate.and(operator.enabled.eq(enabled.get()));
    }

    predicate.and(operator.roles.any().eq(Role.super_user).not());

    query.where(predicate);

    return ModelQuery.wrap(query.orderBy(operator.lastname.asc(),
        operator.firstname.asc()));
  }

  public Set<Operator> administratorsNotSuperUser() {
    return byRole(Role.administrator).stream()
        .filter(o -> !o.roles.contains(Role.super_user))
        .collect(ImmutableSet.toImmutableSet());
  }
  /**
   * @param role
   * @return gli operatori attivi che hanno il ruolo indicato.
   */
  public List<Operator> byRole(Role role) {
    final QOperator op = QOperator.operator;
    return queryFactory.from(op)
      .where(op.roles.any().eq(role), op.enabled.isTrue())
      .orderBy(op.email.asc())
      .select(op).fetch();
  }

  /**
   * Se l'operatore ha un manager diretto correlato, restituisce quello;
   * altrimenti vengono restituiti gli amministratori non super-user.
   *
   * @param operator
   * @return l'elenco dei responsabili di questo operatore.
   */
  public Set<Operator> managerOf(Operator operator) {
    // XXX: da ripristinare.
    return administratorsNotSuperUser();
  }

//  /**
//   * Numero degli operatori attivi suddivisi per profilo.
//   *
//   */
//  public List<LabelNumber> statistics() {
//    val operator = QOperator.operator;
//    val profile = QOperatorProfile.operatorProfile;
//    return queryFactory.from(operator).join(operator.profile, profile)
//      .where(operator.enabled.isTrue())
//      .groupBy(profile)
//      .orderBy(profile.name.asc())
//      .select(Projections.constructor(LabelNumber.class, profile.name, operator.count()))
//      .fetch();
//  }

  /**
   * Notare che restituisce volutamente anche quelli che sono adesso disattivati,
   * ma hanno fatto comunque una operazione (storicizzata) nel periodo.
   *
   * @param dateRange
   * @return gli operatori che hanno fatto almeno un'operazione nel periodo indicato
   * da <code>start</code> e <code>to</code>.
   */
  public long withActivityOn(DateRange dateRange) {
    val startTimestamp = dateRange.getStart().toDateTimeAtStartOfDay().getMillis();
    val endTimestamp = dateRange.getEnd().plusDays(1).toDateTimeAtStartOfDay().getMillis();
    val operator = QOperator.operator;
    val revision = QRevision.revision;
    return queryFactory.selectFrom(operator)
        .where(JPAExpressions.selectFrom(revision)
            .where(revision.owner.eq(operator),
                revision.timestamp.goe(startTimestamp),
                revision.timestamp.lt(endTimestamp))
            .exists())
        .fetchCount();
  }
}
