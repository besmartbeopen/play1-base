package common;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.net.MediaType;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.resizers.configurations.Antialiasing;
import play.Play;
import play.db.jpa.Blob;

/**
 * @author marco
 *
 */
@Singleton
public class PhotoManager {
	
	public static final MediaType THUMBNAIL_TYPE = MediaType.JPEG;
	private static final int THUMBNAIL_DEFAULT_SIZE = 80;
	
	private final File thumbnails;
	
	@Inject
	PhotoManager() {
		thumbnails = Play.getFile("thumbnails");
		if (!thumbnails.exists()) {
			thumbnails.mkdirs();
		}
	}
	
	private File thumbnailFileFor(File file, Optional<Integer> size) {
		return new File(thumbnails, Joiner.on('.').skipNulls()
				.join(file.getName(), size.orNull(), THUMBNAIL_TYPE.subtype()));
	}

	public File assertThumbnail(Blob input, Optional<Integer> size) throws IOException {
		final File thumbnail = thumbnailFileFor(input.getFile(), size);
		// se non c'è la thumbnail o è più vecchia dell'immagine originale.
		if (!thumbnail.exists() || 
				thumbnail.lastModified() < input.getFile().lastModified()) {
			// si crea la thumbnail
			Thumbnails.of(input.getFile())
				.antialiasing(Antialiasing.ON)
				.size(size.or(THUMBNAIL_DEFAULT_SIZE), size.or(THUMBNAIL_DEFAULT_SIZE))
				.allowOverwrite(true)
				.outputFormat(THUMBNAIL_TYPE.subtype())
				.toFile(thumbnail);
		}
		return thumbnail;
	}
}
