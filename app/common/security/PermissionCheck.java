package common.security;

import com.google.common.base.MoreObjects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * seam like permission check.
 *
 * @author marco
 *
 */
@Getter @RequiredArgsConstructor
public class PermissionCheck {

  private final Object target;
  private final String action;
  private boolean granted = false;

  public void grant() {
    this.granted = true;
  }

  public void revoke() {
    this.granted = false;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).omitNullValues()
        .add("action", getAction())
        .add("target", getTarget())
        .addValue(granted ? "GRANTED" : "DENIED").toString();
  }
}
