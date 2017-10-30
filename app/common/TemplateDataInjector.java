package common;

import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Scope.RenderArgs;

import com.google.inject.Inject;
import com.google.inject.Provider;
import common.injection.StaticInject;

/**
 * inject nei renderargs del template una variable $data che corrisponde
 * ad una istanza (nuova per la richiesta) di TemplateData.
 * 
 * @author marco
 *
 */
@StaticInject
public class TemplateDataInjector extends Controller {
	
	@Inject
	static Provider<TemplateData> templateData;
	
	@Before(unless={"login", "authenticate", "logout"})
	static void injectTemplateData() {
		RenderArgs.current().put("$data", templateData.get());
	}
}
