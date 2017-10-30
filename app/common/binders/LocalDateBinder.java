package common.binders;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import org.joda.time.LocalDate;

import play.data.binding.Global;
import play.data.binding.TypeBinder;
import play.data.binding.types.DateBinder;

@Global
public class LocalDateBinder implements TypeBinder<LocalDate> {
	 
	private static DateBinder dateBinder = new DateBinder();

	@Override
    public LocalDate bind(String name, Annotation[] annotations, String value, 
    		@SuppressWarnings("rawtypes") Class actualClass, Type genericType) throws Exception {
    	
        if (value == null || value.trim().length() == 0) {
            return null;
        }
        return new LocalDate(dateBinder.bind(name, annotations, value, 
        		actualClass, genericType));
    }
}
