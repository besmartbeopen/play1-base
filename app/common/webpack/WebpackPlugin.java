package common.webpack;

import java.io.IOException;
import java.net.ServerSocket;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import play.Play;
import play.PlayPlugin;

/**
 * @author marco
 *
 */
@Slf4j
public class WebpackPlugin extends PlayPlugin {

  /**
   * Property utilizzata come lock per i sottoprocessi webpack
   */
  private final static String WEBPACK = "webpack.devserver";
  private final static int WEBPACK_PORT = 8080;

  public static boolean isWebpackStarted() {
    return System.getProperty(WEBPACK) != null;
  }

  public static void webpackStarted() {
    System.setProperty(WEBPACK, WEBPACK);
  }

  public static void webpackStopped() {
    System.clearProperty(WEBPACK);
  }

  /**
   * @return true quando non è in precompilazione ed è attivato in
   * configurazione webpack.devserver
   */
  public static boolean isDevserverEnabled() {
    return System.getProperty("precompile") == null &&
        "true".equals(Play.configuration.getProperty(WEBPACK, "false"));
  }

  @Override
  public void onApplicationStart() {
    WebpackLoader.reset();
  }

  @Override
  public void onLoad() {
    /*
     * - se il webpack-dev-server è abilitato;
     * - e non è ancora stato avviato.
     */
    if (isDevserverEnabled() && !isWebpackStarted()) {
      try (ServerSocket ss = new ServerSocket(WEBPACK_PORT)) {
        ss.setReuseAddress(true);
        ss.close();
        log.info("Starting webpack dev server on port {}...", WEBPACK_PORT);
        // si avvia il devserver via gradle/yarn
        val builder = new ProcessBuilder()
          .directory(Play.applicationPath);
        // disable gradle output
        builder.environment().put("TERM", "dumb");
        builder
          .command("./gradlew", "-q", "--no-daemon", "webpack")
          .inheritIO()
          .start();
        // si imposta che è avviato il webpack
        webpackStarted();
      } catch (IOException e) {
        log.error("Cannot start webpack on port {}: {}", WEBPACK_PORT, e.getMessage());
      }
    }
  }
}
