package common;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import lombok.val;
import models.common.base.BaseModel;

import org.jboss.netty.handler.codec.http.HttpResponseStatus;

import play.data.validation.Validation;
import play.i18n.Messages;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.mvc.Scope.Flash;
import play.mvc.results.RenderBinary;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.FluentIterable;
import com.google.common.io.Files;
import common.dto.IdText;

/**
 * @author marco
 *
 */
public final class Tools {
  private Tools() {}

  public static final String CALENDAR_FMT = "yyyy-MM-dd";
  public static final Joiner WHITESPACE_JOINER = Joiner.on(' ').skipNulls();
  public static final Joiner COMMA_JOINER = Joiner.on(", ").skipNulls();

  public static final Splitter TOKEN_SPLITTER = Splitter.on(' ')
      .trimResults().omitEmptyStrings();

  /**
   * @param models
   * @return la lista degli idtext estratti dalla collezione di models.
   */
  public static List<IdText> toOptions(Collection<? extends BaseModel> models) {
    return FluentIterable
        .from(models)
        .transform(IdText.fromModel())
        .toList();
  }

  public static void renderFile(File file, String type) throws IOException {
    final Response response = Response.current();
    response.setContentTypeIfNotSet(type);

    final long last = file.lastModified();
    final String etag = "\"" + last + "-" + file.hashCode() + "\"";

    if (!Request.current().isModified(etag, last)) {
      response.setHeader("Cache-Control", "must-revalidate");
      response.status = HttpResponseStatus.NOT_MODIFIED.getCode();
    } else {
      response.cacheFor(etag, "10s", last);
      response.setHeader("Cache-Control", "max-age: 0; must-revalidate");
      throw new RenderBinary(Files.asByteSource(file).openBufferedStream(), null, true);
    }
  }

  public static void flashValidationErrors(Validation validation) {
    final val flash = Flash.current();
    final StringBuilder builder = new StringBuilder();
    boolean first = true;
    for (val entry : validation.errorsMap().entrySet()) {
      if (first) {
        first = false;
      } else {
        builder.append("; ");
      }
      builder.append(Messages.get(entry.getKey()));
      builder.append(":");
      builder.append(entry.getValue());
    }
    flash.error(Messages.get("validation.hasErrors", builder.toString()));
  }

  /**
   * Rende unico il valore fornito in value.
   *
   * @param value
   * @param found
   * @return il nome unico, verificandolo col predicato found e inserendo
   * opportunamente una numerazione finale (-1, -2, ...).
   */
  public static String makeUnique(String value, Predicate<String> found) {
    int i = 1;
    if (value.matches(".+-\\d+$")) {
      i = Integer.parseInt(value.substring(value.lastIndexOf('-') + 1)) + 1;
    }
    while (found.test(value)) {
      final String name = value.replaceAll("-\\d+$", "-" + i);
      if (name.equals(value)) {
        value += "-" + i;
      } else {
        value = name;
      }
      i ++;
    }
    return value;
  }
}
