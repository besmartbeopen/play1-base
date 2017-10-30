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
import common.dao.TaskDao;
import common.security.ISecurityCheck;
import controllers.Security;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import lombok.Getter;
import models.Notification;
import models.Operator;
import models.geo.Province;
import models.geo.Region;

/**
 * Injected nei template con $data.
 *
 * @author marco
 * @see TemplateDataInjector
 */
public class TemplateData {

  private final NotificationDao notificationDao;
  private final OperatorDao operatorDao;
  private final GeoDao geoDao;
  private final ISecurityCheck securityChecker;
  private final Supplier<Operator> currentUser;

  @Getter
  private final MemoizedResults<Notification> notifications;
  private final List<Province> allProvinces = allProvinces();
  @Getter(lazy = true)
  private final List<Region> allRegions = allRegions();

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
      OperatorDao operatorDao,
      Provider<Optional<Operator>> currentUser) {

    this.notificationDao = notificationDao;
    this.operatorDao = operatorDao;
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

  public ISecurityCheck getSecurityChecker() {
    return securityChecker;
  }
}
