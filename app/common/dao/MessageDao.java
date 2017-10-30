package common.dao;

import com.google.common.base.Optional;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.JPQLQueryFactory;
import common.DateRange;
import common.ModelQuery;
import common.ModelQuery.SimpleResults;
import java.util.Set;
import javax.inject.Inject;
import models.Message;
import models.Operator;
import models.query.QMessage;
import models.query.QMessageDetail;

/**
 * @author cristian
 *
 */
public class MessageDao {

  private final JPQLQueryFactory queryFactory;

  @Inject
  MessageDao(JPQLQueryFactory queryFactory) {
    this.queryFactory = queryFactory;
  }


  public SimpleResults<Message> list(Optional<Operator> source,
      Optional<Operator> destination, Optional<String> subject,
      Set<String> tags, Optional<Boolean> read, Optional<Boolean> starred,
      Optional<DateRange> dateRange) {
    final QMessage message = QMessage.message;
    final QMessageDetail messageDetail = QMessageDetail.messageDetail;

    final JPQLQuery<Message> query = queryFactory.from(message)
        .select(message).join(message.details, messageDetail)
        .orderBy(message.createdAt.desc());
    BooleanBuilder conditions = new BooleanBuilder();

    if(subject.isPresent()) {
      conditions.and(message.subject.contains(subject.get()));
    }

    if (source.isPresent() && destination.isPresent()) {
      conditions.and(message.source.eq(source.get())
        .or(messageDetail.operator.eq(destination.get())));
    } else {
      if (source.isPresent()) {
        conditions.and(message.source.eq(source.get()));
      }
      if (destination.isPresent()) {
        conditions.and(messageDetail.operator.eq(destination.get()));
      }
    }


    if (tags != null && !tags.isEmpty()) {
      conditions.and(messageDetail.tags.any().in(tags));
    }

    if (dateRange.isPresent()) {
      conditions.and(message.createdAt.goe(
        dateRange.get().getStart().toDateTimeAtStartOfDay().toLocalDateTime()));
      conditions.and(message.createdAt.lt(
        dateRange.get().getEnd().toDateTimeAtStartOfDay().plusDays(1).toLocalDateTime()));
    }
    if (read.isPresent()) {
      conditions.and(messageDetail.read.eq(read.get()));
    }
    if (starred.isPresent()) {
      conditions.and(messageDetail.starred.eq(starred.get()));
    }
    if (conditions.hasValue()) {
      query.where(conditions);
    }

    return ModelQuery.wrap(query.distinct());
  }

}
