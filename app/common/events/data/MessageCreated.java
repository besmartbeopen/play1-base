package common.events.data;

import models.Message;

import com.google.common.base.MoreObjects;


/**
 * @author cristian
 *
 */
public class MessageCreated {

	public final Message value;
	
	MessageCreated(Message value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).addValue(value).toString();
	}
	
	public static MessageCreated instanceFor(Message value) {
		return new MessageCreated(value);
	}
}
