package common;

import common.dao.OperatorDao;
import common.injection.StaticInject;
import javax.inject.Inject;

import org.joda.time.Days;
import org.joda.time.LocalDateTime;
import org.joda.time.ReadablePeriod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.jobs.Every;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

/**
 * @author marco
 *
 */
@OnApplicationStart
@Every("24h")
@StaticInject
public class Cleaner extends Job<Void> {

	private static final Logger LOG = LoggerFactory.getLogger(Cleaner.class);
	
	public static ReadablePeriod recoveryRequestExpire = Days.FOUR;
	
	@Inject
	static OperatorDao operatorDao;

	@Override
	public void doJob() {
		/*
		 * Removes all recoveryrequest older than Cleaner.recoveryRequestExpire.
		 */
		final LocalDateTime date = new LocalDateTime().minus(recoveryRequestExpire);
		long l = operatorDao.deleteRecoveryRequestOlderThan(date);
		LOG.info("removed expired {} recovery request", l);
	}
}
