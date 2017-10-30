package common.exporters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.joda.time.LocalDateTime;

import lombok.RequiredArgsConstructor;

/**
 * @author marco
 *
 */
@Singleton
public class ExportedFileManager {

  @RequiredArgsConstructor
  public static class PathInfo {
    private final Path path;

    public boolean isAvailable() {
      return path.toFile().exists();
    }

    public long getSize() throws IOException {
      return Files.size(path);
    }

    public LocalDateTime getUpdatedAt() {
      return new LocalDateTime(path.toFile().lastModified());
    }

    public File toFile() {
      return path.toFile();
    }
  }

  static final String EXPORTED_BASEDIR = "exported.basedir";

  private final Path base;

  @Inject
  ExportedFileManager(@Named(EXPORTED_BASEDIR) Path base) {
    this.base = base;
  }

  public PathInfo getDoctors() {
    return new PathInfo(base.resolve("doctors.csv"));
  }

  public PathInfo getDoctorShipmentAddresses() {
    return new PathInfo(base.resolve("doctorShipmentAddresses.csv"));
  }
  
  public PathInfo getDoctorAddresses() {
    return new PathInfo(base.resolve("doctorAddresses.csv"));
  }
}
