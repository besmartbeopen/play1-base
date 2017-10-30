package common;

import java.util.Iterator;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;
import com.querydsl.core.QueryModifiers;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.JPQLQuery;

import models.base.BaseModel;
import play.db.jpa.JPA;
import play.mvc.Scope;

/**
 * @author marco
 *
 */
public class ModelQuery {

  /**
   * Nome dell'argomento da fornire via request per indicare il numero di
   * elementi per pagina.
   *
   */
  private static final String PAGE_SIZE_PARAM = "limit";
  /**
   * Numero predefinito di elementi per pagina.
   */
  private static final long DEFAULT_PAGE_SIZE = 20L;

  private ModelQuery() {}

  public static Long personalPageSize() {
    if (!Scope.Session.current().contains(PAGE_SIZE_PARAM)) {
      return DEFAULT_PAGE_SIZE;
    } else {
      return Long.parseLong(Scope.Session.current().get(PAGE_SIZE_PARAM));
    }
  }

  public static void storeSessionPageSize(long value) {
    Preconditions.checkState(value >= 1);
    Scope.Session.current().put(PAGE_SIZE_PARAM, Long.toString(value));
  }

  public static <T> JPQLQuery<T> paginatedQuery(JPQLQuery<T> query) {
    final Integer page = Optional.fromNullable(Scope.Params.current()
        .get(Paginator.PAGE_PARAM, Integer.class)).or(1);
    final long limit = Optional.fromNullable(Scope.Params.current()
        .get(PAGE_SIZE_PARAM, Long.class)).or(personalPageSize());
    query.restrict(new QueryModifiers(limit, (page - 1L) * limit));
    return query;
  }

  /**
   * @author marco
   *
   * @param <T>
   */
  public static class SimpleResults<T> {
    private final JPQLQuery<T> query;

    SimpleResults(JPQLQuery<T> query) {
      this.query = query;
    }

    public long count() {
      return query.fetchCount();
    }

    public List<T> list() {
      return query.fetch();
    }

    /**
     * Create an iterable for simpleresult, with pageSize.
     *
     * @param pageSize
     * @return an iterable with pageSize objects on each iteration.
     */
    public Iterable<List<T>> paged(final long pageSize) {
      return new Iterable<List<T>>() {

        @Override
            public Iterator<List<T>> iterator() {
          return new AbstractIterator<List<T>>() {

            long page = 1L;

            @Override
                protected List<T> computeNext() {
              final List<T> items = query
                  .restrict(new QueryModifiers(pageSize,
                    (page - 1L) * pageSize)).fetch();
              if (items.isEmpty()) {
                return endOfData();
              } else {
                page ++;
                return items;
              }
                }
          };
        }
      };
    }

    public QueryResults<T> listResults() {
      return paginatedQuery(query).fetchResults();
    }

    public List<T> list(long limits) {
      return query.restrict(QueryModifiers.limit(limits)).fetch();
    }

    public List<T> list(long limit, long offset) {
      return query.restrict(new QueryModifiers(limit, offset)).fetch();
    }
  }


  /**
   * @param query
   * @return a simple query related to query
   */
  public static <T> SimpleResults<T> wrap(JPQLQuery<T> query) {
    return new SimpleResults<>(query);
  }

    /**
   * @param query
   * @param expression
   * @return a simplequery object, wrap list or listResults
   */
  public static <T> SimpleResults<T> wrap(JPQLQuery<?> query,
      Expression<T> expression) {
    return new SimpleResults<T>(query.select(expression));
  }

  public static boolean isNotEmpty(BaseModel model) {
    return model != null && model.isPersistent();
  }

  /**
   * @return la funzione di trasformazione da modello a proprio id.
   */
  public static <T extends BaseModel> Function<T, Integer> jpaId() {
    return new Function<T, Integer>() {

      @Override
      public Integer apply(T input) {
        return input.id;
      }
    };
  }

  /**
   * @param model
   * @return la funzione per ottenere un oggetto via em.find().
   */
  public static <T extends BaseModel> Function<Integer,T> jpaFind(final Class<T> model) {
    return new Function<Integer, T>() {

      @Override
      public T apply(Integer id) {
        return JPA.em().find(model, id);
      }
    };
  }
}
