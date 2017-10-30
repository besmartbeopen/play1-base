package common.binders;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Iterator;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import play.data.binding.Global;
import play.data.binding.TypeBinder;

import com.google.common.base.Splitter;

/**
 * @author marco
 *
 */
@Global
public class IntervalBinder implements TypeBinder<Interval> {

	public static final DateTimeFormatter DATE_FMT = DateTimeFormat
			.forPattern("dd/MM/yyyy HH:mm")
			.withZone(DateTimeZone.forID("Europe/Rome"));
	private static final Splitter SPLITTER = Splitter.on('-').limit(2)
			.omitEmptyStrings().trimResults();

	@Override
	public Object bind(String name, Annotation[] annotations, String value,
			@SuppressWarnings("rawtypes") Class actualClass, Type genericType) throws Exception {
		final Iterator<String> iter = SPLITTER.split(value).iterator();
		final DateTime start = DATE_FMT.parseDateTime(iter.next());
		final DateTime end = DATE_FMT.parseDateTime(iter.next());
		return new Interval(start, end);
	}
}