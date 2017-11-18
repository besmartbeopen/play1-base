package common;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.ReadablePeriod;
import org.joda.time.YearMonth;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import com.google.api.client.util.DateTime;
import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import common.binders.IntervalBinder;
import common.dto.IdText;
import models.common.base.Steadies;
import play.db.jpa.GenericModel;
import play.i18n.Messages;
import play.libs.Crypto;
import play.templates.JavaExtensions;

/**
 * @author marco
 */
public class TemplateExtensions extends JavaExtensions {

  private static final Joiner COMMAJ = Joiner.on(", ").skipNulls();

  private static final PeriodFormatter PERIOD_FORMATTER = new PeriodFormatterBuilder()
      .appendMonths()
      .appendSuffix(" mese", " mesi")
      .appendSeparator(", ")
      .appendDays()
      .appendSuffix(" giorno", " giorni")
      .appendSeparator(", ")
      .appendHours()
      .appendSuffix(" ora", " ore")
      .appendSeparator(", ")
      .appendMinutes()
      .appendSuffix(" minuto", " minuti")
      .appendSeparator(", ")
      .printZeroRarelyLast()
      .appendSeconds()
      .appendSuffix(" secondo", " secondi")
      .toFormatter();

  private static final DateTimeFormatter DT_FORMATTER = DateTimeFormat
      .forPattern("dd/MM/yyyy HH:mm:ss");
  private static final DateTimeFormatter DATE_FULL_FORMATTER = DateTimeFormat
      .forPattern("EEEE d MMMM yyyy");
  private static final DateTimeFormatter DATETIME_FULL_FORMATTER = DateTimeFormat
      .forPattern("EEEE d MMMM yyyy HH:mm:ss");
  private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormat
      .forPattern("HH:mm");

  public static String format(ReadablePeriod period) {
    return PERIOD_FORMATTER.print(period);
  }

  public static String format(LocalDate date) {
    return format(date.toDate());
  }

  public static String format(LocalTime time) {
    return time.toString(TIME_FORMATTER);
  }

  public static String format(LocalDateTime dt) {
    return DT_FORMATTER.print(dt);
  }

  public static String format(Interval i) {
    return IntervalBinder.DATE_FMT.print(i.getStart()) + " - " +
        IntervalBinder.DATE_FMT.print(i.getEnd());
  }

  public static String format(DateTime dateTime) {
    return format(new LocalDateTime(dateTime.getValue()));
  }

  /**
   * @param date
   * @return la data in formato esteso.
   */
  public static String formatFull(LocalDate date) {
    return DATE_FULL_FORMATTER.print(date);
  }

  /**
   * @param dt
   * @return la data e l'ora in formato esteso.
   */
  public static String formatFull(LocalDateTime dt) {
    return DATETIME_FULL_FORMATTER.print(dt);
  }

  public static String formatFull(DateTime dateTime) {
    return formatFull(new LocalDateTime(dateTime.getValue()));
  }

  public static String format(Collection<?> collection, String separator) {
    return Joiner.on(separator).join(collection);
  }

  public static String formatForCode(Number number, String format) {
    return new DecimalFormat(format, DecimalFormatSymbols.getInstance(Locale.ENGLISH)).format(number);
  }

  public static String percentage(BigDecimal value) {
    return new DecimalFormat("##.### %").format(value);
  }

  public static String percentageEdit(BigDecimal value) {
    return new DecimalFormat("##.###").format(value.multiply(Steadies.HUNDRED));
  }

  public static String currency(BigDecimal value) {
  return new DecimalFormat("#.## €").format(value);
  }

  public static String currencyEdit(BigDecimal value) {
    return new DecimalFormat("#.##").format(value);
  }

  public static <T extends GenericModel> String joinOnField(final Iterable<T> models, final String fieldName) {

    return COMMAJ.join(Iterables.transform(models, new Function<T, String>() {

      @Override
      public String apply(T model) {
        return getField(model, fieldName);
      }
    }));
  }

  public static String i18nJoin(final Iterable<Enum<?>> fields) {
    return COMMAJ.join(Iterables.transform(fields, new Function<Enum<?>, String>() {

      @Override
      public String apply(Enum<?> field) {
        return Messages.get(field.toString());
      }
    }));
  }

  /**
   * @return la traduzione dei valori di un enum è composta da NomeSempliceEnum.valore
   */
  public static String label(Enum<?> item) {
    return Messages.get(item.getClass().getSimpleName() + "." + item.name());
  }

  public static Object label(String label) {
    return label(label, new Object[]{});
  }

  public static Object label(String label, Object... args) {
//		if (Session.current().contains(CustomMessages.I18N_KEY)) {
//			final String url = Router.getFullUrl("CustomMessages.customize");
//			final String locale = Lang.get();
//			return raw("<span class=\"i18n\" data-url=\"" + url
//					+ "\" data-locale=\"" + locale +"\" data-key=\"" + label
//					+ "\" data-value=\"" + VitisdbPlugin.getKey(locale, label)
//					+ "\">" + Messages.get(label, args) + "</span>");
//		} else {
    return raw(Messages.get(label, args));
//		}
  }

  private static final Splitter COMMA_SPLITTER = Splitter.on(',').omitEmptyStrings().trimResults();

  public static Iterable<String> commaSplit(String value) {
    return COMMA_SPLITTER.split(value);
  }

  /**
   * @return la stringa cryptata con aes e chiave play predefinita.
   */
  public static String encrypt(String value) {
    return Crypto.encryptAES(value);
  }

  public static List<IdText> toIdText(Collection<Enum<?>> c) {
    return FluentIterable.from(c).transform(IdText.fromEnum()).toList();
  }

  public static String toJson(Object obj) {
    return new Gson().toJson(obj);
  }

  public static String escapeAttribute(String str) {
    return str.replace("\"", "&quot;");
  }

  public static String[] toStringItems(Iterable<Object> iterable) {
    return Iterables.toArray(Iterables.transform(iterable,
        Functions.toStringFunction()), String.class);
  }

  public static String value(LocalDate date) {
    return date.toString("dd/MM/yyyy");
  }

  public static String value(YearMonth ym) {
    return ym.toString("MM/yyyy");
  }

  public static String value(LocalTime time) {
    return time.toString("HH:mm");
  }

  public static String value(Interval interval) {
    return interval.getStart().toString("dd/MM/YYYY") + " - " +
        interval.getEnd().toString("dd/MM/yyyy");
  }

  private static String getField(GenericModel model, String fieldName) {
    try {
      final Object obj = model.getClass().getField(fieldName).get(model);
      return obj != null ? obj.toString() : null;
    } catch (IllegalAccessException | NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }

  public static String summaryLimitedTo(Collection<?> collection, int limit) {

    if (collection.isEmpty()) {
      return Messages.get("summaryEmpty");
    } else if (collection.size() <= limit) {
      return COMMAJ.join(collection);
    } else {
      return Messages.get("summaryLimited",
          COMMAJ.join(FluentIterable.from(collection).limit(limit)),
          collection.size() - limit);
    }
  }

  public static String orEmpty(Optional<?> item) {
    return item.map(Objects::toString).orElse("");
  }

  public static String orEmpty(com.google.common.base.Optional<?> item) {
    return item.transform(Objects::toString).or("");
  }

  public static String stripped(String item) {
    return item.replace(" ", "");
  }
}
