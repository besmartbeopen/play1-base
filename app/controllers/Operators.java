package controllers;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;
import com.querydsl.core.QueryResults;
import common.PhotoManager;
import common.Tools;
import common.ValueLabelItem;
import common.Web;
import common.dao.OperatorDao;
import common.jpa.JpaReferenceBinder;
import common.security.SecurityRules;
import common.validators.CollectionSize;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import models.Operator;
import models.enums.Role;
import play.data.binding.As;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.data.validation.Validation;
import play.db.jpa.Blob;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.With;


/**
 * @author marco
 *
 */
@With({Resecure.class})
@Slf4j
public class Operators extends Controller {

  @Inject
  static PhotoManager photoManager;

  @Inject
  static SecurityRules rules;

  @Inject
  static OperatorDao operatorDao;

  /**
   * La foto intera.
   *
   * @param id
   * @param nocache qualsiasi valore, meglio se random o timestamp
   * @throws IOException
   */
  public static void showPhoto(int id, int nocache) throws IOException {
    final Operator operator = Operator.findById(id);
    notFoundIfNull(operator);
    if (!operator.photo.exists()) {
      notFound();
    }
    Tools.renderFile(operator.photo.getFile(), operator.photo.type());
  }

  /**
   * La foto ridimensionata.
   *
   * @param id
   * @param size
   * @param nocache qualsiasi valore, meglio se random o timestamp
   * @throws IOException
   */
  public static void showThumbnail(int id, Integer size, int nocache) throws IOException {
    final Operator operator = Operator.findById(id);
    notFoundIfNull(operator);
    if (!operator.photo.exists()) {
      notFound();
    }
    final File file = photoManager.assertThumbnail(operator.photo,
        Optional.fromNullable(size));
    Tools.renderFile(file, PhotoManager.THUMBNAIL_TYPE.toString());
  }

  public static void uploadPhoto(int id, Blob photo) {
    final Operator operator = Operator.findById(id);
    notFoundIfNull(operator);
    rules.checkIfPermitted(operator);

    if (photo == null || !photo.exists() || photo.length() == 0) {
      error("required file is empty " + MoreObjects.toStringHelper(Blob.class)
      .addValue(photo).toString());
    } else {
      if (operator.photo.exists()) {
        log.debug("remove old photo {} from {}", operator.photo.getUUID(), operator);
        operator.photo.getFile().delete();
      }
      operator.photo = photo;
      operator.save();

      if (!request.headers.get("accept").value().contains("application/json")) {
        // FIX for IE
        response.setContentTypeIfNotSet("text/plain");
      }
      renderJSON(ImmutableMap.of("id", id));
    }
  }

  public static void deletePhoto(int id) {
    final Operator operator = Operator.findById(id);
    notFoundIfNull(operator);
    rules.checkIfPermitted(operator);

    if (operator.photo != null) {
      operator.photo = null;
      operator.save();
    }
    renderJSON(ImmutableMap.of("result", "ok"));
  }

  public static void index() {
    flash.keep();
    list(Optional.absent(), Optional.of(Boolean.TRUE));
  }

  public static void list(Optional<String> name,
      Optional<Boolean> enabled) {

    if (Validation.hasErrors()) {
      flash.error(Web.MSG_HAS_ERRORS);
      render(name, enabled);
    }

    final QueryResults<Operator> results =
        operatorDao.list(name, enabled)
        .listResults();
    render(results, name, enabled);
  }

  public static void bulkAction(@Required Role role, boolean toRemove,
    @Required @CollectionSize(min=1)
    @As(binder=JpaReferenceBinder.class) Set<Operator> operators) {
    if (Validation.hasErrors()) {
      flash.error(Web.msgHasErrors());
    } else {
      operators.forEach(operator -> {
        final boolean modified;
        if (toRemove) {
          log.debug("Remove {} role from operator \"{}\"", role, operator);
          modified = operator.roles.remove(role);
        } else {
          log.debug("Add {} role to operator \"{}\"", role, operator);
          modified = operator.roles.add(role);
        }
        if (modified) {
          operator.save();
        }
      });
      flash.success("Ruolo %s su %d operatori.",
          toRemove ? "rimosso" : "aggiunto", operators.size());
    }
    index();
  }

  public static void blank(Operator operator) {
    render(operator);
  }

  public static void edit(int id) {
    final Operator operator = Operator.findById(id);
    notFoundIfNull(operator);
    render(operator);
  }

  public static void save(@Valid @Required Operator operator) {
    if (Validation.hasErrors()) {
      flash.error(Messages.get(Web.MSG_HAS_ERRORS));
      render("@blank", operator);
    } else {
      if (operator.password == null) {
        // La password è generata casualmente poi l'operatore
        // se la cambierà con l'apposita funzione...
        operator.cryptPassword(UUID.randomUUID().toString());
      }
      operator.save();
      flash.success(Messages.get(Web.MSG_SAVED, operator));
      index();
    }
  }

  public static void show(int id) {
    Operator operator = Operator.findById(id);
    notFoundIfNull(operator);
    render(operator);
  }

  public static void search(String q) {
    if (Strings.isNullOrEmpty(q)) {
      notFound("required query string");
    }
    final List<Operator> items =
        operatorDao.list(Optional.of(q), Optional.of(true)).list(10);

    final List<ValueLabelItem> options = FluentIterable.from(items)
        .transform(input -> new ValueLabelItem(input.id, input.getFullname()))
        .toList();
    renderJSON(options);
  }
}
