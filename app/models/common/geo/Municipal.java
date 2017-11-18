/**
 *
 */
package models.common.geo;

import com.google.common.base.Optional;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import models.common.base.BaseModel;
import models.common.base.Label;
import models.common.interfaces.INamed;

import org.hibernate.envers.Audited;
import play.data.validation.Unique;

/**
 * @author cristian
 *
 */
@Entity @Audited
public class Municipal extends BaseModel implements INamed {

  private static final long serialVersionUID = -2582980802155786208L;

  @Getter
  @Setter
  @Label
  @NotNull
  public String name;

  @Unique
  @NotNull
  public int code;

  public boolean enabled = true;

  @NotNull
  @ManyToOne(optional=false)
  public Province province;

  @ManyToOne(optional=true)
  public Province provincePrevious;

  @Override
  public String toString() {
    return Optional.fromNullable(name).or(super.toString());
  }
}
