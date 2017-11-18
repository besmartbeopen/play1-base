package models.common;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import models.common.base.BaseModel;
import models.common.enums.NotificationSubject;
import play.data.validation.Required;

/**
 * @author marco
 *
 */
@Entity
public class Notification extends BaseModel {

	private static final long serialVersionUID = -7368104051600322496L;

	@ManyToOne(optional=false) @NotNull
	public Operator destination;

	@Required @NotNull
	public String message;

	@Required @NotNull
	@Enumerated(EnumType.STRING)
	public NotificationSubject subject;

	// id dell'oggetto correlato indicato dal target.
	public Integer subjectId;

	@NotNull
	public boolean read = false;

	@Transient
	public boolean isRedirect() {
		return subject.isRedirect();
	}

	@Transient
	public String getUrl() {
		return subject.toUrl(subjectId);
	}

	public interface NotificationBuilderTypeCreate {
		Notification create();
	}

	public interface NotificationBuilderType {
		NotificationBuilderTypeCreate subject(NotificationSubject s);
		NotificationBuilderTypeCreate subject(NotificationSubject s, Integer id);
	}

	public interface NotificationBuilderMessage {
		NotificationBuilderType message(String text);
	}

	public interface NotificationBuilderOperator {
		NotificationBuilderMessage destination(Operator operator);
	}

	public static class NotificationBuilder implements
		NotificationBuilderTypeCreate, NotificationBuilderType,
		NotificationBuilderMessage, NotificationBuilderOperator {

		private Operator destination;
		private String message;
		private NotificationSubject subject;
		private Integer subjectId;

		@Override
		public NotificationBuilderMessage destination(Operator operator) {
			destination = operator;
			return this;
		}
		@Override
		public NotificationBuilderType message(String text) {
			message = text;
			return this;
		}
		@Override
		public NotificationBuilderTypeCreate subject(NotificationSubject t) {
			subject = t;
			return this;
		}
		@Override
		public NotificationBuilderTypeCreate subject(NotificationSubject t, Integer tid) {
			subject = t;
			subjectId = tid;
			return this;
		}
		@Override
		public Notification create() {
			final Notification notification = new Notification();
			notification.destination = destination;
			notification.message = message;
			notification.subject = subject;
			notification.subjectId = subjectId;
			return notification.save();
		}

	}

	/**
	 * @param destination
	 * @param message
	 * @param reference
	 * @return a new notification, saved.
	 */
	public static NotificationBuilderOperator builder() {
		return new NotificationBuilder();
	}
}
