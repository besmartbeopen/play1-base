package models.common;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import com.google.common.hash.Hashing;
import common.Tools;
import common.binders.CapitalizeBinder;
import common.binders.StringTrimBinder;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import models.common.base.BaseModel;
import models.common.base.Label;
import models.common.enums.Role;
import models.common.interfaces.Iban;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDateTime;
import play.data.binding.As;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Blob;

/**
 * @author cristian
 *
 */
@Entity
@Audited
public class Operator extends BaseModel implements Comparable<Operator> {

  private static final long serialVersionUID = 1759893672932227356L;

  public final static int PASSWORD_MIN_SIZE = 6;

  @Unique(value="firstname,lastname", message="validation.duplicatedFullname")
  @As(binder=CapitalizeBinder.class)
  @Required
  @NotEmpty
  public String firstname;

  @As(binder=CapitalizeBinder.class)
  @Required
  @NotEmpty
  public String lastname;

  @As(binder=StringTrimBinder.class)
  @Required
  @NotEmpty
  @Unique
  public String email;

  @NotEmpty
  @Size(min = PASSWORD_MIN_SIZE)
  public String password;

  public boolean enabled = true;

  @NotAudited
  public LocalDateTime lastLoginDate;

  /*
   * IP
   */
  @NotAudited
  public String lastLoginAddress;

  @Enumerated(EnumType.STRING)
  @ElementCollection
  public Set<Role> roles = Sets.newHashSet();

  @Required
  @NotNull
  @ManyToOne
  public OperatorProfile profile;

  @NotAudited
  @OneToMany(mappedBy="operator", cascade=CascadeType.ALL, orphanRemoval=true)
  public Set<RecoveryRequest> recoveryRequests = Sets.newHashSet();

  public Blob photo;

  @Iban
  public String iban;

  /**
   * Task per cui si è candidati assegnatari.
   */
  @NotAudited
  @ManyToMany(mappedBy="candidateAssignees")
  public Set<Task> taskForCandidateAssignee = Sets.newHashSet();

  /**
   * Le notifiche ricevute da questo operatore, ordinate dal nuovo.
   */
  @NotAudited
  @OneToMany(mappedBy="destination")
  @OrderBy("createdAt desc")
  @LazyCollection(LazyCollectionOption.EXTRA)
  public Set<Notification> notifications = Sets.newHashSet();

  public Operator cryptPassword(String newPassword) {
    password = Hashing.sha512().hashString(newPassword, Charsets.UTF_8).toString();
    return this;
  }

  public boolean passwordMatch(String plain) {
    return Hashing.sha512().hashString(plain, Charsets.UTF_8).toString().equals(password);
  }

  @Label
  @Transient
  public String getFullname() {
    return Tools.WHITESPACE_JOINER.join(lastname, firstname);
  }

  @Override
  public String toString() {
    if (firstname == null && lastname == null) {
      return super.toString();
    } else {
      return getFullname();
    }
  }

  /**
   * Ordinamento basato sul fullname.
   *
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(Operator o) {
    return getFullname().compareTo(o.getFullname());
  }

  @Transient
  public Optional<LocalDateTime> getDisabledDate() {
    if (enabled) {
      return Optional.<LocalDateTime>absent();
    }
    return Optional.of(updatedAt);
  }
}
