package common.events.bus;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

/**
 * @author marco
 *
 */
public final class EventBusKey {

  public final static String DEFAULT = "default";
  // public final static String JPA_POST_SAVE = "jpa-post-save";
  public final static String JPA_PRE_SAVE = "jpa-pre-save";

  EventBusKey() {}

  public static Set<String> values() {
    return ImmutableSet.of(DEFAULT, /* JPA_POST_SAVE, */ JPA_PRE_SAVE);
  }
}
