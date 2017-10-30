package common;

import java.util.Iterator;

import org.joda.time.LocalDate;

import com.google.common.collect.AbstractIterator;

/**
 * Iteratore su un intervallo di date.
 *
 * @author marco
 *
 */
public class LocalDatesIterable implements Iterable<LocalDate> {

	static class LocalDatesIterator extends AbstractIterator<LocalDate> {

		private LocalDate date;
		private final LocalDate end;

		LocalDatesIterator(LocalDate start, LocalDate end) {
			this.date = start;
			this.end = end;
		}

		@Override
		protected LocalDate computeNext() {
			if (date.compareTo(end) > 0) {
				return endOfData();
			} else {
				final LocalDate result = date;
				date = date.plusDays(1);
				return result;
			}
		}
	}

	private final LocalDate start;
	private final LocalDate end;

	public LocalDatesIterable(LocalDate start, LocalDate end) {
		this.start = start;
		this.end = end;
	}

	@Override
	public Iterator<LocalDate> iterator() {
		return new LocalDatesIterator(start, end);
	}
}