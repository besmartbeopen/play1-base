package common.binders;

import common.DateRange;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.data.binding.Global;
import play.data.binding.TypeBinder;
import play.data.binding.types.DateBinder;

/**
 * @author cristian
 *
 */
@Global
public class DateRangeBinder implements TypeBinder<DateRange> {
	 	
	private final Logger LOG = LoggerFactory.getLogger(DateRangeBinder.class);
	
	private static DateBinder dateBinder = new DateBinder();

	@Override
    public DateRange bind(String name, Annotation[] annotations, String value, 
    		@SuppressWarnings("rawtypes") Class actualClass, Type genericType) throws Exception {
		
        if (value == null || value.trim().length() == 0) {
            return null;
        }
        
        String[] dateParts = value.toString().split(DateRange.DATE_SEPARATOR);
        
        if (dateParts.length != 2) {
        	LOG.info("unable to bind {} to a DateRange Object", value);
        	return null;
        }

        return new DateRange(
        		new LocalDate(
        				dateBinder.bind(
        						name, annotations, dateParts[0].trim(), 
        						actualClass, genericType)),
        		new LocalDate(
        			dateBinder.bind(
        					name, annotations, dateParts[1].trim(), 
        					actualClass, genericType)
        		));
    }
}
