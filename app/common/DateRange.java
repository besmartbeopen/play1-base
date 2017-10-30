package common;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DatePath;
import com.querydsl.core.types.dsl.DateTimePath;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import models.interfaces.StartEndDate;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.YearMonth;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Intervallo di date con date di inizio e fine
 * @author cristian
 *
 */
@RequiredArgsConstructor
@Getter
public class DateRange implements StartEndDate {

  public final static String DATE_SEPARATOR = "-";
  public final static DateTimeFormatter FMT = DateTimeFormat.forPattern("dd/MM/yyyy");

  private final LocalDate start;
  private final LocalDate end;

  /**
   * Costruisce un daterange dal <code>ym</code>
   * @param ym
   */
  public DateRange(YearMonth ym) {
    start = ym.toLocalDate(1);
    end = start.dayOfMonth().withMaximumValue();
  }

  /**
   * @param date
   * @return il predicato di contenimento della data.
   */
  public BooleanExpression contains(DatePath<LocalDate> date) {
    return date.between(start, end);
  }

  /**
   * @param dateTime
   * @return il predicato di contenimento per il dateTime.
   */
  public BooleanExpression contains(DateTimePath<LocalDateTime> dateTime) {
    return dateTime.goe(start.toDateTimeAtStartOfDay().toLocalDateTime())
        .and(dateTime.lt(end.plusDays(1).toDateTimeAtStartOfDay().toLocalDateTime()));
  }

  @Override
  public LocalDate getStartDate() {
    return getStart();
  }

  @Override
  public LocalDate getEndDate() {
    return getEnd();
  }

  @Override
  public String toString() {
    return start.toString(FMT) + " " + DATE_SEPARATOR + " " + end.toString(FMT);
  }

  public Days toDays() {
    return Days.daysBetween(start, end.plusDays(1));
  }
}
