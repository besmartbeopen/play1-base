package common.security;

import lombok.Data;

/**
 * Target and action.
 *
 * @author marco
 *
 */
@Data(staticConstructor="of")
public class PermissionCheckKey {
  private final Object target;
  private final String action;
}