package common.dao;

import com.google.common.base.Function;
import com.google.common.base.Verify;
import com.google.common.collect.FluentIterable;
import com.querydsl.jpa.JPQLQueryFactory;
import common.jpa.HistoryViews;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import models.base.BaseModel;
import models.base.Revision;
import models.base.query.QRevision;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.joda.time.LocalDateTime;

@Slf4j
public class HistoricalDao {

  @Inject
  private Provider<AuditReader> auditReader;
  @Inject
  private JPQLQueryFactory queryFactory;
  @Inject
  private Provider<EntityManager> emp;

  @RequiredArgsConstructor
  public static class HistoryData {
    @Getter
    private final int revision;
    @Getter
    private final RevisionType revisionType;
    private final long timestamp;
    @Getter
    private final String owner;

    public LocalDateTime getRevisionDate() {
      return new LocalDateTime(timestamp);
    }

    public static Function<Object,HistoryData> toHistory() {
      return new Function<Object, HistoryData>() {
        @Override
        public HistoryData apply(Object obj) {
          final Object[] oo = (Object[]) obj;
          return new HistoryData((int) oo[0],	(RevisionType) oo[1],
              (long) oo[2], Objects.toString(oo[3]));
        }
      };
    }
  }

  public Revision getRevision(int id) {
    return Verify.verifyNotNull(queryFactory.from(QRevision.revision)
        .where(QRevision.revision.id.eq(id))
        .select(QRevision.revision).fetchOne());
  }

  public <T extends BaseModel> T valueAtRevision(Class<T> cls, int id,
      int revisionId) {

    final T current = Verify.verifyNotNull(emp.get().find(cls, id));
    final T history = cls.cast(auditReader.get().createQuery()
          .forEntitiesAtRevision(cls, revisionId)
          .add(AuditEntity.id().eq(current.id))
          .getSingleResult());
    final LocalDateTime date = getRevision(revisionId)
        .getRevisionLocalDateTime();
    return HistoryViews.historicalViewOf(cls, current, history, date);
  }

  @SuppressWarnings("unchecked")
  public List<HistoryData> lastRevisionsOf(Class<? extends BaseModel> cls, int id) {
    return FluentIterable.from(auditReader.get().createQuery()
        .forRevisionsOfEntity(cls, false, true)
        .add(AuditEntity.id().eq(id))
        .addOrder(AuditEntity.revisionNumber().desc())
        .addProjection(AuditEntity.revisionNumber())
        .addProjection(AuditEntity.revisionType())
        .addProjection(AuditEntity.revisionProperty("timestamp"))
        .addProjection(AuditEntity.revisionProperty("owner"))
        .setMaxResults(100)
        .getResultList()).transform(HistoryData.toHistory()).toList();
  }

  /**
   * @param cls
   * @param id
   * @return la versione precedente del istanza individuata da cls e id.
   */
  public <T extends BaseModel> T previousRevisionOf(Class<T> cls, int id) {
    final Integer currentRevision = (Integer) auditReader.get().createQuery()
        .forRevisionsOfEntity(cls, false, true)
      .add(AuditEntity.id().eq(id))
      .addProjection(AuditEntity.revisionNumber().max())
      .getSingleResult();
    final Integer previousRevision = (Integer) auditReader.get().createQuery()
        .forRevisionsOfEntity(cls, false, true)
          .addProjection(AuditEntity.revisionNumber().max())
        .add(AuditEntity.id().eq(id))
        .add(AuditEntity.revisionNumber().lt(currentRevision))
        .getSingleResult();
    log.debug("current-revision {} of ({}:{}), previous-revision: {}",
        currentRevision, cls, id, previousRevision);
    return valueAtRevision(cls, id, previousRevision);
  }
}
