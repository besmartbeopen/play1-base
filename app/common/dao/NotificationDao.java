package common.dao;

import com.google.inject.Inject;
import com.querydsl.jpa.JPQLQueryFactory;
import common.ModelQuery;
import common.ModelQuery.SimpleResults;
import models.Notification;
import models.Operator;
import models.query.QNotification;

/**
 * @author marco
 *
 */
public class NotificationDao {

  private final JPQLQueryFactory queryFactory;

  @Inject
  NotificationDao(JPQLQueryFactory queryFactory) {
    this.queryFactory = queryFactory;
  }

  public SimpleResults<Notification> listUnreadFor(Operator operator) {
    final QNotification qn = QNotification.notification;
    return ModelQuery.wrap(queryFactory.from(qn)
        .where(qn.destination.eq(operator), qn.read.isFalse())
        .orderBy(qn.createdAt.desc()), qn);
  }

  public SimpleResults<Notification> listFor(Operator operator,
      boolean archived) {
    final QNotification qn = QNotification.notification;
    return ModelQuery.wrap(queryFactory.from(qn)
        .where(qn.destination.eq(operator), qn.read.eq(archived))
        .orderBy(qn.createdAt.desc()), qn);
  }
}
