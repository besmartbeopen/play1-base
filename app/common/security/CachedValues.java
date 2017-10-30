package common.security;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Simple java>=8 grow only cache implementation.
 *
 * @author marco
 *
 */
public class CachedValues<K, R> {

  private final Map<K, R> values = new HashMap<>();

  public R computeIfAbsent(K key, Function<? super K, R> f) {
    return values.computeIfAbsent(key, f);
  }
}
