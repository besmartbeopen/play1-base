package common;

import java.lang.reflect.Type;

import javax.inject.Named;
import javax.inject.Provider;
import javax.persistence.EntityManager;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.joda.time.LocalDateTime;
import org.joda.time.format.ISODateTimeFormat;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import common.injection.AutoRegister;
import common.issues.IssueManager;
import play.Play;
import play.db.jpa.JPA;

/**
 * @author marco
 *
 */
@AutoRegister
public class HelpersModule implements Module {

	@Provides
	public EntityManager getEntityManager() {
		return JPA.em();
	}

	@Provides @Singleton
	public JPAQueryFactory getQueryFactory(Provider<EntityManager> emp) {
		return new JPAQueryFactory(emp);
	}

	@Provides @Named("issues.url")
	public String getIssueUrl() {
		return Play.configuration.getProperty("issues.url");
	}

	@Provides @Named("issues.token")
	public String getIssueToken() {
		return Play.configuration.getProperty("issues.token");
	}

	@Provides @Named("issues.projectId")
	public int getIssueProjectId() {
		return Integer.parseInt(Play.configuration.getProperty("issues.projectId"));
	}

	@Provides
	public AuditReader getAuditReader(EntityManager em) {
		return AuditReaderFactory.get(em);
	}

	static class LocalDateTimeConverter implements JsonDeserializer<LocalDateTime> {

		@Override
		public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext context)
				throws JsonParseException {
			return ISODateTimeFormat.dateTimeParser().parseLocalDateTime(json.getAsString());
		}
	}

	@Singleton @Provides
	public Gson getGson(LocalDateTimeConverter dateTimeConverter) {
		return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy
				.LOWER_CASE_WITH_UNDERSCORES)
				.registerTypeAdapter(LocalDateTime.class, dateTimeConverter)
				.create();
	}

	@Override
	public void configure(Binder binder) {
		binder.bind(PhotoManager.class);
		binder.bind(IssueManager.class);
		binder.bind(JPQLQueryFactory.class).to(JPAQueryFactory.class);
	}
}
