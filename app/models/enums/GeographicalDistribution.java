package models.enums;

/**
 * @author cristian
 *
 */
public enum GeographicalDistribution {
  NORD_OVEST,
  NORD_EST,
  CENTRO,
  SUD,
  ISOLE;

  public String getName() {
    return super.name().replace("_", "-");
  }
}