package common.events;

import com.google.common.base.Verify;
import com.google.common.eventbus.EventBus;
import common.events.bus.EventBusKey;
import common.injection.StaticInject;
import models.base.BaseModel;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Utiler per propagare le istanze create/modificate o cancellate in jpa
 * via eventbus.
 *
 * @author marco
 *
 */
@StaticInject
public final class EntityEvents {

  @Inject
  static EventBus eventBus;

  @Inject @Named(EventBusKey.JPA_PRE_SAVE)
  static EventBus eventBusPreSave;

  /**
   * Propaga un evento consistente nel `model` stesso, da chiamare
   * successivamente agli eventi C-U-D. Da notare che in caso sia stato
   * cancellato l'oggetto sarà non più persistent.
   *
   * @param model
   */
  public static void postFor(BaseModel model) {
    Verify.verifyNotNull(eventBus).post(model);
  }

  /**
   * Propaga un evento consistente nel `model stesso, da chiamare prima della
   * persist/update. Da notare che il model è non persistent nel caso sia un
   * evento di creazione.
   *
   * @param model
   */
  public static void preSave(BaseModel model) {
    Verify.verifyNotNull(eventBusPreSave).post(model);
  }

  public static void preRemove(BaseModel model) {
    // XXX: not implemented yet
  }
}
