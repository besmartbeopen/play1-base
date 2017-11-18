package models.common;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import models.common.base.BaseModel;
import models.common.enums.CommentTargetType;
import models.common.interfaces.Commentable;
import play.data.validation.Required;

/**
 * @author marco
 *
 */
@Entity
public class Comment extends BaseModel implements Commentable {

  private static final long serialVersionUID = -6011507996193307620L;

  @Required
  @NotNull
  @Column(nullable=false, columnDefinition="text")
  public String comment;

  @ManyToOne(optional=true)
  public Operator owner;

  @ManyToOne(optional=true)
  public Comment relatedToComment;

  @Enumerated(EnumType.STRING)
  @NotNull
  public CommentTargetType targetType;

  public Integer targetId;

  @OneToMany(mappedBy="relatedToComment")
  @OrderBy("updatedAt")
  public Set<Comment> comments = Sets.newHashSet();

  @NotNull
  public boolean active = true;

  @Override
  public Set<Comment> getComments() {
    return comments;
  }

  public Comment firstComment() {
    if (relatedToComment == null) {
      return this;
    } else {
      return relatedToComment.firstComment();
    }
  }

  public Commentable root() {
    return firstComment().getParent();
  }

  @Override
  @Transient
  public String getLabel() {
    return comment;
  }

  @Transient
  public Commentable getParent() {
    // TODO: un oggetto staticamente accessibile? CommentResolver.resolve(reference);
    return null;
  }

  @Transient
  @Override
  public Set<Operator> getInvolveds() {
    return ImmutableSet.<Operator>builder().add(owner)
        .addAll(getParent().getInvolveds()).build();
  }
}
