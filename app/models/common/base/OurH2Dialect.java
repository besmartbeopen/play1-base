package models.common.base;

import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

import java.sql.Types;

/**
 * Configurazioni specifiche h2.
 * 
 * @author marco
 *
 */
public class OurH2Dialect extends org.hibernate.dialect.H2Dialect {

  public OurH2Dialect() {
    super();
    registerColumnType(Types.OTHER, "varchar");
    registerHibernateType(Types.OTHER, "string");
    registerFunction("add_months",
				new SQLFunctionTemplate(StandardBasicTypes.DATE,
						"dateadd('month', ?2, ?1)"));
  }
}
