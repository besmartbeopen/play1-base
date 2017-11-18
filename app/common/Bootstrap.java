package common;

import com.querydsl.jpa.impl.JPAQueryFactory;
import common.injection.StaticInject;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import models.common.Operator;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

/**
 * @author marco
 *
 */
@OnApplicationStart
@StaticInject
@Slf4j
public class Bootstrap extends Job<Void> {

  @Inject
  static JPAQueryFactory queryFactory;

  @Override
  public void doJob() {

    if (Operator.count() == 0L) {
      Fixtures.loadModels("defaults.yml");
      log.info("Importati gli opreatori predefiniti");
    }
  }
}
