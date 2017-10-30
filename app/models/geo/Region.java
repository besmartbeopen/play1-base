/**
 *
 */
package models.geo;

import com.google.common.collect.Sets;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import models.base.BaseModel;
import models.enums.GeographicalDistribution;
import models.interfaces.INamed;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * @author cristian
 *
 */
@Entity
public class Region extends BaseModel implements INamed {

  private static final long serialVersionUID = 5260314299299010283L;

  @Getter
  @Setter
  @NotNull
  @Column(unique = true, length = 64)
  @Size(max = 64)
  public String name;

  @OrderBy("name")
  @OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY, mappedBy = "region")
  @LazyCollection(LazyCollectionOption.EXTRA)
  public Set<Province> provinces = Sets.newHashSet();

  @Enumerated(EnumType.STRING)
  @NotNull
  public GeographicalDistribution geographicalDistribution;

  @Override
  public String toString() {
    return name;
  }
}
