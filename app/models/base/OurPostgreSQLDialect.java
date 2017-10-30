package models.base;

import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.spatial.dialect.postgis.PostgisPG94Dialect;
import org.hibernate.type.StandardBasicTypes;

public class OurPostgreSQLDialect extends PostgisPG94Dialect {

  private static final long serialVersionUID = -7748731292688669652L;

  /**
   * Valori restituiti dalla dayofweek del postgresql.
   */
  public static final int MONDAY = 1;
  public static final int TUESDAY = 2;
  public static final int WEDNESDAY = 3;
  public static final int THURSDAY = 4;
  public static final int FRIDAY = 5;
  public static final int SATURDAY = 6;
  public static final int SUNDAY = 0;


  public OurPostgreSQLDialect() {
    super();
    registerFunction("dayofyear", new SQLFunctionTemplate(StandardBasicTypes.INTEGER, "extract(doy from ?1)"));
    registerFunction("dayofweek", new SQLFunctionTemplate(StandardBasicTypes.INTEGER, "extract(dow from ?1)"));
    registerFunction("dayofmonth", new SQLFunctionTemplate(StandardBasicTypes.INTEGER, "extract(dom from ?1)"));
    registerFunction("week", new SQLFunctionTemplate(StandardBasicTypes.INTEGER, "extract(week from ?1)"));
    registerFunction("date", new SQLFunctionTemplate(StandardBasicTypes.TIMESTAMP, "?1::date"));
    registerFunction("day_serie", new SQLFunctionTemplate(StandardBasicTypes.TIMESTAMP,
        "generate_series(?1, ?2, '1 day')"));
    registerFunction("month_serie", new SQLFunctionTemplate(StandardBasicTypes.TIMESTAMP,
        "generate_series(?1, ?2, '1 month')"));
    registerFunction("add_hours",
        new SQLFunctionTemplate(StandardBasicTypes.TIMESTAMP,
            "(?1 + ((?2::text) || ' hour')::interval)"));
    registerFunction("add_days",
        new SQLFunctionTemplate(StandardBasicTypes.TIMESTAMP,
            "(?1 + ((?2::text) || ' day')::interval)"));
    registerFunction("add_months",
        new SQLFunctionTemplate(StandardBasicTypes.DATE,
            "(?1 + ((?2::text) || ' month')::interval)"));
    registerFunction("diff_months",
        new SQLFunctionTemplate(StandardBasicTypes.INTEGER,
            "(date_part('year', age(?1, ?2))*12+date_part('month', age(?1, ?2)))"));
  }
}
