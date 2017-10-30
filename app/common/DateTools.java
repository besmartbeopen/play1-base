package common;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.DatePath;
import com.querydsl.core.types.dsl.DateTimePath;
import common.data.InConfiguration.InConfigurationManager;
import common.injection.StaticInject;
import de.jollyday.Holiday;
import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;
import de.jollyday.ManagerParameters;
import lombok.extern.slf4j.Slf4j;

import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import java.util.Calendar;
import java.util.Set;

@Slf4j
@StaticInject
public class DateTools {

  @Inject
  static InConfigurationManager configurationManager;

  private static final HolidayManager hm =
      HolidayManager.getInstance(ManagerParameters.create(HolidayCalendar.ITALY));

  /**
   * @return un Predicate query dsl con la condizioni costruita affinché siano filtrate dalla
   *     query solo le date che si sovrappongono ai due intervalli passati.
   */
  public static Predicate overlapsCondition(DateTimePath<LocalDateTime> startAt,
      DateTimePath<LocalDateTime> endTo, LocalDateTime start,
      LocalDateTime end) {
    final BooleanBuilder condition = new BooleanBuilder();

    condition.or(startAt.between(start, end));
    condition.or(endTo.between(start, end));
    condition.or(startAt.loe(start).and(endTo.goe(end)));
    return condition;
  }

  /**
   * @param startAt relativo al campo dell'entity che contiene la data di partenza.
   * @param endTo relativo al campo dell'entity che contiene la data di fine
   * @param start data di inizio dell'intervallo in cui cercare
   * @param end data di fine dell'intervallo in cui cercare
   *
   * @return la condizione querydsl per restituire gli oggetti che hanno la data di inizio
   *     e fine comprese nell'intervallo passato
   */
  public static Predicate overlapsCondition(
      DateTimePath<LocalDateTime> startAt, DateTimePath<LocalDateTime> endTo,
      LocalDate start, LocalDate end) {

    BooleanBuilder dateConditions = new BooleanBuilder();

    LocalDateTime startWithTime = start.toLocalDateTime(LocalTime.MIDNIGHT);
    LocalDateTime endwithTime = end.toLocalDateTime(LocalTime.MIDNIGHT).plusDays(1).minusMillis(1);
    dateConditions.or(startAt.between(startWithTime, endwithTime));
    dateConditions.or(endTo.between(startWithTime,endwithTime));
    dateConditions.or(startAt.loe(startWithTime).and(endTo.goe(endwithTime)));


    return dateConditions;
  }

  /**
   * @param startAt relativo al campo dell'entity che contiene la data di partenza.
   * @param endTo relativo al campo dell'entity che contiene la data di fine
   * @param start data di inizio dell'intervallo in cui cercare
   * @param end data di fine dell'intervallo in cui cercare
   *
   * @return la condizione querydsl per restituire gli oggetti che hanno la data di inizio
   *     e fine comprese nell'intervallo passato
   */
  public static Predicate overlapsCondition(
      DatePath<LocalDate> startAt, DatePath<LocalDate> endTo, LocalDate start, LocalDate end) {

    BooleanBuilder dateConditions = new BooleanBuilder();

    dateConditions.or(startAt.between(start,end));
    dateConditions.or(endTo.between(start,end));
    dateConditions.or(startAt.loe(start).and(endTo.goe(end)));

    return dateConditions;
  }

  /**
   * @param date la data da verificare.
   * @return True se la data è festiva, False altrimenti
   */
  public static boolean isHoliday(LocalDate date) {
    return hm.isHoliday(date) || date.getDayOfWeek() > 5;
  }

  /**
   * @see #isHoliday(LocalDate date).
   *
   * @param date la data da verificare
   * @return True se la data è festiva, False altrimenti
   */
  public static boolean isHoliday(LocalDateTime date) {
    return isHoliday(date.toLocalDate());
  }

  /**
   * @return il Predicate di validazione se un LocalDate è festivo o meno.
   */
  public static com.google.common.base.Predicate<LocalDate> isHoliday() {
    return new com.google.common.base.Predicate<LocalDate>() {

      @Override
      public boolean apply(LocalDate input) {
        return isHoliday(input);
      }

    };
  }

  public static Set<Holiday> getHolidays(LocalDate start, LocalDate stop) {
    return hm.getHolidays(new Interval(start.toDateTime(LocalTime.MIDNIGHT),
        stop.toDateTime(LocalTime.MIDNIGHT)));
  }

  /**
   * @param date la data da verificare.
   * @return il primo giorno lavorativo a partire dalla data passata
   */
  public static LocalDate getCurrentBusinessDate(LocalDate date) {
    LocalDate businessDate = date;
    while (isHoliday(businessDate)) {
      businessDate.plusDays(1);
    }
    return businessDate;
  }

  /**
   * @param date la data da verificare.
   * @return il primo giorno lavorativo a partire dalla data passata
   */
  public static LocalDateTime getCurrentBusinessDate(LocalDateTime date) {
    LocalDateTime businessDate = date;
    while (isHoliday(businessDate)) {
      businessDate = businessDate.plusDays(1);
    }
    return businessDate;
  }

  /**
   * @param startAt inizio del periodo da controllare.
   * @param endTo fine del periodo da controllare.
   * @return l'ultimo giorno lavorativo prima del primo giorno festivo
   *     nel periodo indicato
   */
  public static Optional<LocalDateTime> getLastWorkingDateBeforeFirstHoliday(
      LocalDateTime startAt, LocalDateTime endTo) {

    LocalDateTime date = getCurrentBusinessDate(startAt);

    if (date.toLocalDate().isAfter(endTo.toLocalDate())) {
      return Optional.<LocalDateTime>absent();
    }

    while (date.toLocalDate().isBefore(endTo.toLocalDate())) {
      if (isHoliday(date.plusDays(1))) {
        return Optional.of(date);
      }
      date = date.plusDays(1);
    }
    return Optional.of(date);
  }

  /**
   * @param startAt la data di inizio dell'intervallo di cui calcolare i giorni.
   * @param endTo la data di fine dell'intervallo di cui calcolare i giorni.
   * @return l'insieme (ordinato) dei giorni compresi tra i due localDateTime
   */
  public static Set<LocalDate> getDays(LocalDateTime startAt, LocalDateTime endTo) {
    LocalDate dateStart = startAt.toLocalDate();
    Set<LocalDate> days = Sets.newHashSet();
    // day by day:
    while (dateStart.isBefore(endTo.toLocalDate())) {
      days.add(dateStart);
      dateStart = dateStart.plusDays(1);
    }

    return days;
  }

  /**
   * @param startAt la data di inizio dell'intervallo di cui calcolare i giorni.
   * @param endTo la data di fine dell'intervallo di cui calcolare i giorni.
   * @return l'insieme (ordinato) dei giorni compresi tra i due localDate
   */
  public static Set<LocalDate> getDays(LocalDate startAt, LocalDate endTo) {
    LocalDate dateStart = new LocalDate(startAt);
    Set<LocalDate> days = Sets.newTreeSet();
    // day by day:
    while (dateStart.isBefore(endTo)) {
      days.add(dateStart);
      dateStart = dateStart.plusDays(1);
    }
    return days;
  }

  /**
   *
   * @param start la data di inizio dell'intervallo di cui calcolare i giorni lavorativi.
   * @param end la data di fine dell'intervallo di cui calcolare i giorni lavorativi.
   * @return il numero dei giorni lavorativi compreso tra start e end
   *     (incluso) considerando anche le festività nazionali.
   */
  public static int workingDays(LocalDate start, LocalDate end) {
    Preconditions.checkArgument(Preconditions.checkNotNull(start)
        .compareTo(Preconditions.checkNotNull(end)) <= 0);

    int days = weekdays(start, end);
    log.debug("month days meno i sabato e domenica = {}", days);

    // esclusione delle festività nei giorni lavorativi
    for (Holiday holiday : hm.getHolidays(new Interval(start
        .toDateTimeAtStartOfDay(), end.toDateTimeAtStartOfDay()))) {
      if (holiday.getDate().getDayOfWeek() <= DateTimeConstants.FRIDAY) {
        days -= 1;
      }
    }
    return days;
  }

  /**
   * @see http://stackoverflow.com/questions/4600034/calculate-number-of-weekdays-between-two-dates-in-java
   *
   * @param start date di inizio periodo
   * @param end data di fine periodo
   *
   * @return il numero di giorni feriali (numero di giorni escluso i sabato e domenica)
   */
  public static int weekdays(LocalDate start, LocalDate end) {
    Preconditions.checkArgument(Preconditions.checkNotNull(start)
        .compareTo(Preconditions.checkNotNull(end)) <= 0);

    int w1 = start.getDayOfWeek() == DateTimeConstants.SUNDAY ? 1 : start.getDayOfWeek() + 1;
    int w2 = end.getDayOfWeek() == DateTimeConstants.SUNDAY ? 1 : end.getDayOfWeek() + 1;

    // Take back the dates to the previous Sunday
    LocalDate d1 = start.plusDays(-w1 + 1);
    LocalDate d2 = end.plusDays(-w2 + 1);

    int days = Days.daysBetween(d1, d2.plusDays(1)).getDays();
    int daysWithoutWeekDays = days - (days * 2 / 7);

    // Adjust days to add on (w2) and days to subtract (w1) so that Saturday
    // and Sunday are not included
    if (w1 == Calendar.SUNDAY) {
      w1 = Calendar.MONDAY;
    }

    if (w2 == Calendar.SATURDAY) {
      w2 = Calendar.FRIDAY;
    }

    int weekdays = daysWithoutWeekDays - w1 + w2;

    return weekdays;
  }

  /**
   * @param date
   * @return true se la data fornita è precedente al primo del mese precedente,
   * false altrimenti.
   */
  public static boolean isBeforePreviousMonth(LocalDate date) {

    return date.isBefore(LocalDate.now().withDayOfMonth(1).minusMonths(1));
  }

  /**
   * @param date
   * @return true se la data fornita è precedente al primo del mese indicato
   * da months, false altrimenti.
   */
  public static boolean isAllowedDateForSampleLeft(LocalDate date) {

    return date.isBefore(LocalDate.now().withDayOfMonth(1)
        .minusMonths(configurationManager.get()
            .maximumNumberOfMonthsForSampleLeft));
  }
}
