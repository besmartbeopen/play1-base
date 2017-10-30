package common.exporters;

import java.io.File;
import java.nio.file.Path;

import javax.inject.Named;
import javax.inject.Singleton;

import com.google.common.base.Verify;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import common.injection.AutoRegister;
import play.Play;

/**
 * Utilità per la configurazione dell'esportazione.
 *
 * @author marco
 *
 */
@AutoRegister
public class ExportedFileModule extends AbstractModule {

  @Provides
  @Singleton
  @Named(ExportedFileManager.EXPORTED_BASEDIR)
  public Path getExportedDirectory() {
    final Path base = Play.applicationPath.toPath().resolve("data/exporteds");
    final File fb = base.toFile();
    if (!fb.exists()) {
      Verify.verify(fb.mkdirs(), "could not create exporteds directory");
    }
    return base;
  }

  @Override
  protected void configure() {
  }
}
