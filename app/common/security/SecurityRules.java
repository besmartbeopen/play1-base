package common.security;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import lombok.extern.slf4j.Slf4j;

import models.Operator;

import org.drools.KnowledgeBase;
import org.drools.command.Command;
import org.drools.command.CommandFactory;
import org.drools.event.rule.AfterActivationFiredEvent;
import org.drools.event.rule.DefaultAgendaEventListener;
import org.drools.runtime.StatelessKnowledgeSession;

import play.mvc.results.Forbidden;

import java.util.List;

/**
 * @author marco
 *
 */
@Singleton @Slf4j
public class SecurityRules implements ISecurityCheck {

  private final static String CURRENT_OPERATOR_IDENTIFIER = "currentOperator";

  private final Provider<Optional<Operator>> currentOperator;
  private final Provider<String> currentAction;
  private final Provider<KnowledgeBase> knowledge;
  private final Provider<CachedValues<PermissionCheckKey, Boolean>> checkCache;

  @Inject
  SecurityRules(Provider<Optional<Operator>> operator,
      @Named(SecurityModule.REQUEST_ACTION) Provider<String> action,
      Provider<KnowledgeBase> knowledgeBase,
      Provider<CachedValues<PermissionCheckKey, Boolean>> requestCache) {
    currentOperator = operator;
    currentAction = action;
    knowledge = knowledgeBase;
    checkCache = requestCache;
  }

  /* (non-Javadoc)
   * @see security.ISecurityCheck#checkIfPermitted(java.lang.Object)
   */
  @Override
  public void checkIfPermitted(Object instance) {
    if (!check(instance)) {
      // TODO: aggiungere messaggio più chiaro con permesso e operatore.
      throw new Forbidden("Access forbidden");
    }
  }

  /* (non-Javadoc)
   * @see security.ISecurityCheck#checkIfPermitted()
   */
  @Override
  public void checkIfPermitted() {
    if (!checkAction()) {
      // TODO: aggiungere messaggio più chiaro con permesso e operatore.
      throw new Forbidden("Access forbidden");
    }
  }

  /* (non-Javadoc)
   * @see security.ISecurityCheck#check(java.lang.Object)
   */
  @Override
  public boolean check(Object instance) {
    return check(currentAction.get(), instance);
  }

  /* (non-Javadoc)
   * @see security.ISecurityCheck#check(java.lang.String, java.lang.Object)
   */
  @Override
  public boolean check(String action, Object instance) {
    final PermissionCheckKey key = PermissionCheckKey.of(instance, action);
    return isPermitted(key);
  }

  /* (non-Javadoc)
   * @see security.ISecurityCheck#checkAction()
   */
  @Override
  public boolean checkAction() {
    return checkAction(currentAction.get());
  }

  /* (non-Javadoc)
   * @see security.ISecurityCheck#checkAction(java.lang.String)
   */
  @Override
  public boolean checkAction(String action) {
    final PermissionCheckKey key = PermissionCheckKey.of(null, action);
    return isPermitted(key);
  }

  private boolean isPermitted(final PermissionCheckKey key) {
    return checkCache.get().computeIfAbsent(key, this::doCheck);
  }

  private boolean doCheck(final PermissionCheckKey key) {
    final Optional<Operator> operator = currentOperator.get();
    if (!operator.isPresent()) {
      return false;
    }

    final StatelessKnowledgeSession session = knowledge.get()
        .newStatelessKnowledgeSession();
    session.addEventListener(new AgendaLogger());

    session.setGlobal(CURRENT_OPERATOR_IDENTIFIER, operator.get());

    final PermissionCheck check = new PermissionCheck(key.getTarget(),
        key.getAction());
    final List<Command<?>> commands = Lists.newArrayList();
    commands.add(CommandFactory.newInsert(check));
    commands.add(CommandFactory.newInsert(check.getTarget()));
    commands.add(CommandFactory.newInsert(operator.get()));
    commands.add(CommandFactory.newInsertElements(operator.get().roles));
    session.execute(CommandFactory.newBatchExecution(commands));

    log.debug("{}", check);
    return check.isGranted();
  }

   private static class AgendaLogger extends DefaultAgendaEventListener {
     @Override
     public void afterActivationFired(AfterActivationFiredEvent event) {
       log.debug("RULE {} {}", event.getActivation().getRule().getName(),
           event.getActivation().getFactHandles());
     }
   }
}
