package controllers;

import common.VersionData;
import common.Web;
import common.data.InConfiguration;
import common.data.MunicipalVariationsManager;
import common.data.InConfiguration.InConfigurationManager;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import javax.inject.Inject;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import play.Play;
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
@Slf4j
public class Setup extends Controller {

  @Inject
  static MunicipalVariationsManager municipalVariationManager;
  @Inject
  static InConfigurationManager configurationManager;
  @Inject
  static VersionData versionData;

  public static void index() {
    final InConfiguration configuration = configurationManager.get();
    render(configuration);
  }

  public static void info() {
    val playVersion = Play.version;
    val appVersion = versionData.toString();
    val javaVersion = System.getProperty("java.version");
    val startTime = new LocalDateTime(ManagementFactory.getRuntimeMXBean().getStartTime());
    render(playVersion, appVersion, javaVersion, startTime);
  }

  public static void configurationUpdate(@Valid InConfiguration configuration) {
    if (Validation.hasErrors()) {
      flash.error(Web.msgHasErrors());
      val errors = validation.errorsMap();
      render("@index", configuration, errors);
    } else {
      try {
        configurationManager.save(configuration);
        flash.success(Web.msgSaved(InConfiguration.class));
        index();
      } catch (IOException e) {
        log.error("configuration file save error: {}", e);
        flash.error(e.getLocalizedMessage());
        render("@index", configuration);
      }
    }
  }

  public static void importMunicipalVariations(@Required File file) {
    if (Validation.hasErrors()) {
      flash.error("Occorre il file CSV corretto.");
      index();
    }
    try {
      municipalVariationManager.process(file);
      flash.success("Importazione delle variazioni sui comuni riuscita.");
    } catch (IOException e) {
      log.error("municipal variations import error", e);
      flash.error("Errore di importazione.");
    }
    index();
  }
}
