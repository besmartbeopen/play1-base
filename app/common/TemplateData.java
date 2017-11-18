package common;

import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.inject.Provider;
import common.ModelQuery.SimpleResults;
import common.dao.GeoDao;
import common.dao.MemoizedResults;
import common.dao.NotificationDao;
import common.dao.OperatorDao;
import common.dao.OperatorProfileDao;
import common.dao.TaskDao;
import common.security.ISecurityCheck;
import controllers.Security;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import lombok.Getter;
import models.common.Notification;
import models.common.Operator;
import models.common.OperatorProfile;
import models.common.geo.Province;
import models.common.geo.Region;

/**
 * Injected nei template con $data.
 *
 * @author marco
 * @see TemplateDataInjector
 */
public class TemplateData {

  private final NotificationDao notificationDao;
  private final OperatorDao operatorDao;
  private final OperatorProfileDao operatorProfileDao;
  private final GeoDao geoDao;
  private final ISecurityCheck securityChecker;
  private final Supplier<Operator> currentUser;

  @Getter
  private final MemoizedResults<Notification> notifications;
  @Getter(lazy=true)
  private final List<Province> allProvinces = allProvinces();
  @Getter(lazy=true)
  private final List<Region> allRegions = allRegions();
  @Getter(lazy=true)
  private final List<OperatorProfile> allProfiles = allProfiles();

  public class UnreadNotifications {

    private final Supplier<Long> count;
    private final Supplier<Collection<Notification>> list;

    UnreadNotifications() {
      final Supplier<SimpleResults<Notification>> results = Suppliers
          .memoize(new Supplier<SimpleResults<Notification>>() {

            @Override
            public SimpleResults<Notification> get() {
              return notificationDao.listUnreadFor(Security
                  .getCurrentUser().get());
            }
          });

      count = Suppliers.memoize(new Supplier<Long>() {

        @Override
        public Long get() {
          return results.get().count();
        }
      });
      list = Suppliers.memoize(new Supplier<Collection<Notification>>() {

        @Override
        public Collection<Notification> get() {
          return results.get().list(10L);
        }
      });
    }

    public long count() {
      return count.get();
    }

    public Collection<Notification> list() {
      return list.get();
    }
  }

  @Inject
  TemplateData(final NotificationDao notificationDao, final TaskDao taskDao,
      GeoDao geoDao, ISecurityCheck securityChecker,
      OperatorDao operatorDao, OperatorProfileDao operatorProfileDao,
      Provider<Optional<Operator>> currentUser) {

    this.notificationDao = notificationDao;
    this.operatorDao = operatorDao;
    this.operatorProfileDao = operatorProfileDao;
    this.geoDao = geoDao;
    this.securityChecker = securityChecker;

    notifications = MemoizedResults
        .memoize(new Supplier<SimpleResults<Notification>>() {

          @Override
          public SimpleResults<Notification> get() {
            return notificationDao.listUnreadFor(Security
                .getCurrentUser().get());
          }
        });

    this.currentUser = () -> currentUser.get().get();
  }

  List<Province> allProvinces() {
    return geoDao.provinces();
  }

  List<Region> allRegions() {
    return geoDao.regions();
  }

  List<OperatorProfile> allProfiles() {
    return operatorProfileDao.list().list();
  }

  public ISecurityCheck getSecurityChecker() {
    return securityChecker;
  }
}
