package models.common;

import com.google.common.base.Optional;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import models.common.base.BaseModel;
import models.common.enums.Role;

import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotEmpty;
import play.data.validation.Required;
import play.data.validation.URL;
import play.data.validation.Unique;

@Entity
@Audited
public class OperatorProfile extends BaseModel {

  private static final long serialVersionUID = -6411820205190216518L;

  @Required
  @NotNull
  @NotEmpty
  @Unique
  public String name;

  public String description;

  public boolean active = true;

  @URL
  public String loginUrl;

  @Enumerated(EnumType.STRING)
  @ElementCollection
  public Set<Role> roles = new HashSet<>(0);

  @OneToMany(mappedBy="profile")
  public Set<Operator> operators = new HashSet<>(0);

  @Override
  public String toString() {
    return Optional.fromNullable(name).or(super.toString());
  }
}
