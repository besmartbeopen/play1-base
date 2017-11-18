package models.common.base;

import org.hibernate.envers.RevisionListener;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.ProvisionException;
import com.google.inject.name.Named;
import common.injection.StaticInject;
import common.security.SecurityModule;
import lombok.extern.slf4j.Slf4j;
import models.common.Operator;

/**
 * @author marco
 *
 */
@StaticInject
@Slf4j
public class ExtendedRevisionListener implements RevisionListener {

  @Inject
  static Provider<Optional<Operator>> operator;
  @Inject @Named(SecurityModule.REQUEST_REMOTE_ADDRESS)
  static Provider<Optional<String>> ipaddress;

  @Override
  public void newRevision(Object revisionEntity) {
    final Revision revision = (Revision) revisionEntity;
    try {
      revision.ipaddress = ipaddress.get().orNull();
      revision.owner = operator.get().orNull();
    } catch (ProvisionException e) {
      if (!(e.getCause() instanceof NullPointerException)) {
        log.warn("error retrieving operator or ipaddress for revision {}", revision, e);
      } // else nothing, è normale.
    }
  }
}
