package common.dto;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

/**
 * @author marco
 *
 */
public class IntervalQuantity {
	
	public final Interval key;
	public final long value;
	
	public IntervalQuantity(Date date, Long value) {
		this.key = new Interval(new DateTime(date), Days.ONE);
		this.value = value;
	}

	public IntervalQuantity(LocalDate date, Long value) {
		this.key = new Interval(date.toDateTimeAtStartOfDay(), Days.ONE);
		this.value = value;
	}
}
