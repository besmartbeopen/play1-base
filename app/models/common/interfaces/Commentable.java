package models.common.interfaces;

import java.util.Set;

import com.google.common.base.Function;

import models.common.Comment;
import models.common.Operator;

/**
 * @author marco
 *
 */
public interface Commentable {

	String getLabel();
	Set<Comment> getComments();
	Set<Operator> getInvolveds();

	public enum CommentableInvolved implements Function<Commentable, Set<Operator>> {
		INSTANCE;

		@Override
		public Set<Operator> apply(Commentable commentable) {
			return commentable.getInvolveds();
		}
	};
}
