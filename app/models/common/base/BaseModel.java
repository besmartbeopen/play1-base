package models.common.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import common.events.EntityEvents;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.persistence.Version;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.envers.NotAudited;
import org.joda.time.LocalDateTime;
import play.db.jpa.GenericModel;
import play.db.jpa.JPABase;

@MappedSuperclass
@TypeDefs({
  @TypeDef(name = "yearMonth", defaultForType = org.joda.time.YearMonth.class,
      typeClass = org.jadira.usertype.dateandtime.joda.PersistentYearMonthAsString.class)
  }
)
public abstract class BaseModel extends GenericModel {

  private static final long serialVersionUID = 6291731038424201700L;

  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  public Integer id;

  @JsonIgnore
  @NotAudited
  @Version
  public Integer version;

  @NotAudited
  public LocalDateTime createdAt;

  @NotAudited
  public LocalDateTime updatedAt;

  @Transient
  public boolean isUpdated() {
    return !createdAt.equals(updatedAt);
  }

  @Transient
  public boolean isNew() {
    return version == null || version == 0;
  }

  @Override
  public Object _key() {
    return id;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("id", id).toString();
  }

  @PrePersist @PreUpdate
  public void updateBaseModel() {
    updatedAt = new LocalDateTime();
    if (createdAt == null) {
      createdAt = updatedAt;
    }
  }

  @Override
  public <T extends JPABase> T save() {
    EntityEvents.preSave(this);
    T t = super.save();
    EntityEvents.postFor(this);
    return t;
  }

  @Override
  public <T extends JPABase> T delete() {
    EntityEvents.preRemove(this);
    T t = super.delete();
    EntityEvents.postFor(this);
    return t;
  }
}
