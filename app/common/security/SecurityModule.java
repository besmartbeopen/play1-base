package common.security;

import com.google.common.base.Optional;
import com.google.common.base.Verify;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import common.injection.AutoRegister;
import controllers.Security;
import models.Operator;

import org.drools.KnowledgeBase;

import play.mvc.Http;

/**
 * Unione di injection con il play.
 *
 * @author marco
 *
 */
@AutoRegister
public class SecurityModule implements Module {

  private static final String SECURITY_CHECKS = "security.checks";
  public final static String REQUEST_ACTION = "request.action";
  public final static String REQUEST_REMOTE_ADDRESS = "request.remoteAddress";

  @Provides @Named(REQUEST_ACTION)
  public String currentAction() {
    return Http.Request.current().action;
  }

  @Provides @Named(REQUEST_REMOTE_ADDRESS)
  public Optional<String> currentIpAddress() {
    if (Http.Request.current() == null) {
      return Optional.absent();
    }
    return Optional.of(Http.Request.current()).transform(r -> r.remoteAddress);
  }

  @Provides
  public KnowledgeBase knowledgeBase() {
    return SecureRulesPlugin.knowledgeBase;
  }

  @Provides
  public Optional<Operator> currentOperatorGuava() {
    return Security.getCurrentUser();
  }

  @Provides
  public java.util.Optional<Operator> currentOperator() {
    return java.util.Optional.ofNullable(Security.getCurrentUser().orNull());
  }

  /**
   * @return the cached permission checker if exists, otherwise a new instance.
   */
  @SuppressWarnings("unchecked")
  @Provides
  public CachedValues<PermissionCheckKey, Boolean> currentPermissionChecker() {
    Verify.verifyNotNull(Http.Request.current(),
        "Cannot create a PermissionCheck cache without a request.");
    return (CachedValues<PermissionCheckKey, Boolean>) Http.Request.current()
        .args.computeIfAbsent(SECURITY_CHECKS,
            s -> new CachedValues<PermissionCheckKey, Boolean>());
  }

  @Override
  public void configure(Binder binder) {
    binder.bind(SecurityRules.class);
  }
}
