package common;

import common.injection.StaticInject;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.joda.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.vfs.VirtualFile;

/**
 * Mantiene la versione corrente dell'applicazione:
 *  - in produzione corrisponde al contenuto del file VERSION se c'è,
 *    altrimenti corrisponde alla data di avvio (prod).
 *  - in sviluppo corrisponde alla data di avvio (dev).
 *
 * @author marco
 *
 */
@Singleton
@Slf4j
public class VersionData {

	private String value;

	@Inject
	VersionData() {
		load();
	}

	/**
	 * Ricarica la data da file o ricompone da boot.
	 */
	void load() {
		if (Play.mode.isProd()) {
			try {
				value = VirtualFile.open("VERSION").contentAsString().trim();
			} catch (Exception e) {
				value = "prod/" + LocalDateTime.now().toString();
				log.warn("{}, using current datetime for versioning",
						e.getMessage());
			}
		} else {
			value = "dev/" + LocalDateTime.now().toString();
		}
	}

	@Override
	public String toString() {
		return value;
	}

	/**
	 * Rigenerazione automatica della versione corrente dell'applicativo.
	 */
	@OnApplicationStart @StaticInject
	public static class StartVersionData extends Job<Void> {

		@Inject
		static VersionData versionData;

		@Override
		public void doJob() {
			// in teoria occorre soltanto in dev mode.
			versionData.load();
			log.info("starting version {}", versionData);
		}
	}
}
