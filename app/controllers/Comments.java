package controllers;

import common.security.SecurityRules;
import javax.inject.Inject;
import models.Comment;
import models.interfaces.Commentable;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.data.validation.Validation;
import play.mvc.Controller;
import play.mvc.With;

/**
 * @author marco
 *
 */
@With(Resecure.class)
public class Comments extends Controller {

  @Inject
  static SecurityRules rules;

  /**
   * @param id
   *
   * Effettua il redirect all'oggetto correlato.
   *
   */
  public static void show(int id) {
    final Comment comment = Comment.findById(id);
    notFoundIfNull(comment);
    rules.checkIfPermitted(comment);

    final Comment first = comment.firstComment();
    // TODO: redirect to referenced item?
    throw new IllegalStateException("type not supported: " + comment);
  }

  public static void save(@Required @Valid Comment comment) {
    // controllo di consistenza
    final Commentable value = comment.getParent();

    if (comment.isPersistent()) {
      // se esiste già si controlla il commento stesso.
      rules.checkIfPermitted(comment);
    } else {
      // se è nuovo si controlla su ciò che si commenta.
      rules.checkIfPermitted(value);
    }

    if (!comment.isPersistent()) {
      // sui nuovi commenti si imposta l'autore, anche prima di verificare.
      comment.owner = Security.getCurrentUser().get();
    }
    if (Validation.hasErrors()) {
      response.status = 400;
      render("/Comments/comments.html", value, comment);
    } else {
      comment.save();
      render("/Comments/comments.html", value);
    }
  }

  // DELETE only
  public static void delete(int id) {
    final Comment comment = Comment.findById(id);
    notFoundIfNull(comment);
    rules.checkIfPermitted(comment);

    final Commentable value = comment.getParent();
    // si cancella soltanto se è vuoto, altrimenti lo si disabilita.
    if (comment.comments.isEmpty()) {
      comment.delete();
    } else {
      comment.active = false;
      comment.save();
    }
    render("/Comments/comments.html", value);
  }
}
