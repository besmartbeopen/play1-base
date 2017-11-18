package common.validators;

import org.iban4j.IbanFormatException;
import org.iban4j.IbanUtil;
import org.iban4j.InvalidCheckDigitException;
import org.iban4j.UnsupportedCountryException;

import models.common.interfaces.Iban;
import play.i18n.Messages;

import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.AbstractAnnotationCheck;
import net.sf.oval.context.OValContext;
import net.sf.oval.exception.OValException;

/**
 * @author cristian
 *
 */
public class IbanCheck extends AbstractAnnotationCheck <Iban> {

  private static final long serialVersionUID = -4201061557289608695L;

  @Override
  public boolean isSatisfied(Object validatedObject, Object value,
      OValContext context, Validator validator) throws OValException {
    if (value == null) {
      return true;
    }
    if (!(value instanceof String)) {
      return false;
    }
    final String iban = (String) value;
    if (iban.isEmpty()) {
      // non deve essere obbligatorio qui.
      return true;
    }
    try {
      IbanUtil.validate(iban);
    } catch (IbanFormatException ife) {
      if (iban.toLowerCase().startsWith("it") && iban.length() != 27) {
        setMessage(Messages.get("validation.iban.italy.length"));
      } else {
        setMessage(Messages.get("validation.iban.format"));  
      }
      return false;
    } catch (UnsupportedCountryException uce) {
      setMessage(Messages.get("validation.iban.country"));
      return false;       		  
    } catch (InvalidCheckDigitException icde) {
      setMessage(Messages.get("validation.iban.checkDigit"));
      return false;       
    }

    return true;
  }
}
