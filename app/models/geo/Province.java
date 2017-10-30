/**
 *
 */
package models.geo;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import models.base.BaseModel;
import models.interfaces.INamed;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import play.data.validation.Unique;

/**
 * @author cristian
 *
 */
@Entity
@Audited
public class Province extends BaseModel implements INamed {

  private static final long serialVersionUID = -8811525870378574036L;

  @Getter
  @Setter
  @Unique
  @NotNull
  @Column(nullable = false)
  public String name;

  @Unique
  @NotNull
  @Column(nullable = false)
  public String code;

  @NotAudited
  @ManyToOne(optional=false)
  public Region region;

  @OneToMany(mappedBy="province")
  @OrderBy("name")
  @LazyCollection(LazyCollectionOption.EXTRA)
  public Set<Municipal> municipals = Sets.newHashSet();

  @Override
  public String toString() {
    return Optional.fromNullable(name).or(super.toString());
  }
}
