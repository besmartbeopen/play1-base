package common.validators;

import org.joda.time.LocalDate;
import org.joda.time.base.AbstractPartial;

import com.google.common.base.Preconditions;

import lombok.val;
import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.AbstractAnnotationCheck;
import net.sf.oval.context.OValContext;
import net.sf.oval.exception.OValException;
import play.i18n.Messages;

/**
 * Verifica che il campo data sia abbastanza recente; in modo predefinito
 * controlla che la data sia al massimo dell'anno precedente.
 *
 * @author marco
 *
 */
public class FairlyRecentCheck extends AbstractAnnotationCheck<FairlyRecent> {

  private static final long serialVersionUID = -809410455978420123L;

  private int years;
  private String message;

  @Override
  public void configure(FairlyRecent fairlyRecent) {

    Preconditions.checkArgument(fairlyRecent.years() > 0);

    years = fairlyRecent.years();
    message = fairlyRecent.msg();
  }

  @Override
  public boolean isSatisfied(Object validatedObject, Object value,
      OValContext context, Validator validator) throws OValException {

    if (value == null) {
      // saltiamo il controllo in questo caso
      return true;
    } else {
      if (value instanceof AbstractPartial) {

        val partial = (AbstractPartial) value;
        val ref = LocalDate.now().withDayOfYear(1).minusYears(years);
        if (partial.isBefore(ref)) {
          setMessage(Messages.get(message));
          return false;
        }
        val max = LocalDate.now().withDayOfYear(1).plusYears(years + 1).minusDays(1);
        if (partial.isAfter(max)) {
          setMessage(Messages.get(message));
          return false;
        }
      } else {
        throw new IllegalStateException("instance not supported " + value.getClass());
      }
      return true;
    }
  }
}
