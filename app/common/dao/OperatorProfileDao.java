package common.dao;

import com.querydsl.jpa.JPQLQueryFactory;
import common.ModelQuery;
import common.ModelQuery.SimpleResults;
import javax.inject.Inject;
import models.OperatorProfile;
import models.query.QOperatorProfile;

/**
 * @author marco
 *
 */
public class OperatorProfileDao {

  private final JPQLQueryFactory queryFactory;

  @Inject
  OperatorProfileDao(JPQLQueryFactory queryFactory) {
    this.queryFactory = queryFactory;
  }

  public SimpleResults<OperatorProfile> list() {

    final QOperatorProfile qop = QOperatorProfile.operatorProfile;
    return ModelQuery.wrap(queryFactory.selectFrom(qop).orderBy(qop.name.asc())
        .where(qop.active.isTrue()));
  }
}
