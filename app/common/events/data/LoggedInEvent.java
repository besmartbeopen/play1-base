package common.events.data;

import models.common.Operator;

/**
 * @author marco
 *
 */
public class LoggedInEvent {
	
	public final String fromAddress;
	public final Operator value;
	
	LoggedInEvent(Operator value, String fromAddress) {
		this.value = value;
		this.fromAddress = fromAddress;
	}
	
	public static LoggedInEvent of(Operator operator, String fromAddress) {
		return new LoggedInEvent(operator, fromAddress);
	}
}
