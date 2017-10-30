package models;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Sets;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import models.base.BaseModel;
import models.enums.TaskType;
import models.interfaces.IFlowable;
import org.joda.time.LocalDate;
import play.data.validation.Required;

/**
 * Mantenere allineata alla enum TaskType.
 *
 * @author cristian
 * @author marco
 *
 */
@Entity
public class Task extends BaseModel {

  private static final long serialVersionUID = 6352260720474200968L;

  public String description;

  @ManyToMany
  public Set<Operator> candidateAssignees = Sets.newHashSet();

  @Required
  public TaskType type;

  @OneToOne(optional=true, cascade=CascadeType.PERSIST)
  public Taskable target;

  /**
   * può essere null
   */
  public LocalDate startDate;

  public Task relatedTo(IFlowable flowable) {
    flowable.setTask(this);
    return this;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).addValue(this.description)
        .toString();
  }
}
