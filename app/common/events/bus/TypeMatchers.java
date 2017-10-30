package common.events.bus;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;

import java.lang.annotation.Annotation;

/**
 * A factory of {@link Matcher}s that can be used in
 * {@link AbstractModule}.bindListener(matcher,listener)
 * @author ooktay
 * @author marco.andreini
 */
public class TypeMatchers {

  /**
   * Matcher matches all classes that are annotated with cls.
   * @param cls
   * @return Matcher
   */
  public static Matcher<TypeLiteral<?>> annotatedWith(Class<? extends Annotation> cls) {
    return new AbstractMatcher<TypeLiteral<?>>() {

      @Override
      public boolean matches(TypeLiteral<?> t) {
        return t.getRawType().getAnnotation(cls) != null;
      }
    };
  }

  /**
   * Matcher matches all classes that extends, implements or is the same as cls.
   * @param cls
   * @return Matcher
   */
  public static Matcher<TypeLiteral<?>> subclassesOf(Class<?> cls) {
    return new AbstractMatcher<TypeLiteral<?>>() {

      @Override
      public boolean matches(TypeLiteral<?> t) {
        return cls.isAssignableFrom( t.getRawType() );
      }
    };
  }
}
