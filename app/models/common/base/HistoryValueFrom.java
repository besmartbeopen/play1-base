package models.common.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HistoryValueFrom {

	public enum HistoryFrom {
		REVISION, CURRENT;
	}

	HistoryFrom value();
}
