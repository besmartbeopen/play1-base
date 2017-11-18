package models.common.enums;

/**
 * Persona giuridica o fisica.
 *
 * @author marco
 *
 */
public enum PersonType {
  PHYSICAL_PERSON,
  LEGAL_ENTITY;

  public boolean isPhysical() {
    return this == PHYSICAL_PERSON;
  }

  public boolean isLegal() {
    return this == LEGAL_ENTITY;
  }
}
