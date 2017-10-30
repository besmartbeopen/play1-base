package common.data;

import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormat;

import lombok.extern.slf4j.Slf4j;

/**
 * Utilità per effettuare log dei tempi di elaborazione.
 *
 * @author marco
 *
 */
@Slf4j
public class TimeElapse {

	private LocalDateTime start;

	public TimeElapse() {
		start = LocalDateTime.now();
	}

	public void logAndReset(String name) {
		final LocalDateTime now = LocalDateTime.now();
		log.info("{}: {}", name, PeriodFormat.getDefault()
				.print(new Period(start, now)));
		start = now;
	}
}
