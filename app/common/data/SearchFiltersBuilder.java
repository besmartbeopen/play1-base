package common.data;

import common.Paginator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import org.assertj.core.util.Preconditions;
import lombok.val;
import play.mvc.Http.Request;
import play.mvc.Router;

/**
 * Costruisce la struttura utile ai filtri.
 *
 * @author marco
 *
 */
public class SearchFiltersBuilder {

  private final Request request;
  /**
   * filtri sui cui si mantiene l'ordine di inserimento
   */
  private final Map<String, String> filters = new LinkedHashMap<>();

  private SearchFiltersBuilder(Request request) {
    this.request = request;
  }

  /**
   * @param request
   * @return crea un nuovo builder partendo da una richiesta.
   */
  public static SearchFiltersBuilder request(Request request) {
    return new SearchFiltersBuilder(request);
  }

  /**
   * Aggiunge al builder il parametro <code>key</code> con la specifica
   * etichetta <code>label</code>.
   *
   * @param key
   * @param label
   * @return lo stesso builder
   */
  public SearchFiltersBuilder add(String key, String label) {
    Preconditions.checkNotNull(label);
    Preconditions.checkNotNull(key);

    val params = new HashMap<String, Object>(request.params.allSimple());
    params.remove(key);
    // salta i parametri da saltare
    Paginator.SKIP_PARAMS.forEach(params::remove);
    // azzera la paginazione
    params.remove(Paginator.PAGE_PARAM);

    // XXX: router importato staticamente == non testabile facilmente.
    val url = Router.reverse(request.action, params).url;
    filters.put(label, url);
    return this;
  }

  /**
   * @param key
   * @param value se true effettua l'aggiunta, altrimenti no.
   * @param label
   * @return lo stesso builder
   */
  public SearchFiltersBuilder addIf(String key, boolean value,
      Supplier<String> label) {
    if (value) {
      add(key, label.get());
    }
    return this;
  }

  /**
   * Aggiunge la chiave <code>key</code> al builder, solo se presente
   * <code>optional</code>
   *
   * @param key
   * @param optional
   * @param toLabel la funzione di trasformazione per l'etichetta
   * @return lo stesso builder
   */
  public <T> SearchFiltersBuilder addIfPresent(String key, Optional<T> optional,
      Function<T, String> toLabel) {

    if (optional.isPresent()) {
      return add(key, optional.map(toLabel).get());
    } else {
      return this;
    }
  }

  /**
   * @see #addIfPresent(String, Optional, Function)
   *
   * @param key
   * @param optional
   * @param toLabel
   * @return lo stesso builder
   */
  public <T> SearchFiltersBuilder addIfPresent(String key,
      com.google.common.base.Optional <T> value, Function<T, String> toLabel) {

    return addIfPresent(key, value.toJavaUtil(), toLabel);
  }

  /**
   * @return compone il filtro da utilizzare in una #{list}
   */
  public Set<Map.Entry<String, String>> build() {
    return filters.entrySet();
  }
}
