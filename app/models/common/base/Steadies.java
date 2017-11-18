package models.common.base;

import java.math.BigDecimal;

/**
 * @author marco
 *
 */
public final class Steadies {

  private Steadies() {}

  public static final BigDecimal HUNDRED = new BigDecimal(100);

  public static final String PERCENTAGE_RANGE_MESSAGE = "Non può essere minore di 0 o maggiore di 100.";
}
