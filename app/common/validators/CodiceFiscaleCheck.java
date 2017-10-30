package common.validators;

import play.data.validation.Check;


/**
 * Controllo per il codice fiscale Italiano.
 *
 * @author marco
 *
 */
public class CodiceFiscaleCheck extends Check {

	private final static int[] DISPONIBILI = {
		1, 0, 5, 7, 9, 13, 15, 17, 19, 21, 2, 4, 18, 20,
		11, 3, 6, 8, 12, 14, 16, 10, 22, 25, 24, 23 };

	@Override
	public boolean isSatisfied(Object validatedObject, Object v) {
		if (v == null) {
			return true;
		}
		if (!(v instanceof String)) {
			return false;
		}
		final String value = ((String) v).toUpperCase();

		if (value.isEmpty()) {
			return true;
		}

		if (value.length() != 16)  {
			setMessage("validation.codiceFiscale.length");
			return false;
		}

		for (int i = 0; i < 16; i++){

			char c = value.charAt(i);
			if (!(c >= '0' && c <= '9' || c >= 'A' && c <= 'Z' )) {
				setMessage("validation.codiceFiscale.char");
				return false;
			}
		}
		int sum = 0;
		for (int i = 1; i <= 13; i += 2) {

			char c = value.charAt(i);
			if (c >= '0' && c <= '9') {
				sum += c - '0';
			} else {
				sum += c - 'A';
			}
		}
		for (int i = 0; i <= 14; i += 2) {

			int c = value.charAt(i);
			if (c >= '0' && c <= '9') {
				c = c - '0' + 'A';
			}
			sum += DISPONIBILI[c - 'A'];
		}
		if (sum % 26 + 'A' != value.charAt(15)) {
			setMessage("validation.codiceFiscale.check");
			return false;
		}
		return true;
	}
}
