package models.common.query;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;

import models.common.Operator;


/**
 * QOperator is a Querydsl query type for Operator
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QOperator extends EntityPathBase<Operator> {

    private static final long serialVersionUID = 2107976648L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOperator operator = new QOperator("operator");

    public final models.common.base.query.QBaseModel _super = new models.common.base.query.QBaseModel(this);

    //inherited
    public final DateTimePath<org.joda.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final BooleanPath enabled = createBoolean("enabled");

    //inherited
    public final SimplePath<Object> entityId = _super.entityId;

    public final StringPath firstname = createString("firstname");

    public final StringPath iban = createString("iban");

    //inherited
    public final NumberPath<Integer> id = _super.id;

    public final StringPath lastLoginAddress = createString("lastLoginAddress");

    public final DateTimePath<org.joda.time.LocalDateTime> lastLoginDate = createDateTime("lastLoginDate", org.joda.time.LocalDateTime.class);

    public final StringPath lastname = createString("lastname");

    public final SetPath<models.common.Notification, QNotification> notifications = this.<models.common.Notification, QNotification>createSet("notifications", models.common.Notification.class, QNotification.class, PathInits.DIRECT2);

    public final StringPath password = createString("password");

    //inherited
    public final BooleanPath persistent = _super.persistent;

    public final SimplePath<play.db.jpa.Blob> photo = createSimple("photo", play.db.jpa.Blob.class);

    public final QOperatorProfile profile;

    public final SetPath<models.common.RecoveryRequest, QRecoveryRequest> recoveryRequests = this.<models.common.RecoveryRequest, QRecoveryRequest>createSet("recoveryRequests", models.common.RecoveryRequest.class, QRecoveryRequest.class, PathInits.DIRECT2);

    public final SetPath<models.common.enums.Role, EnumPath<models.common.enums.Role>> roles = this.<models.common.enums.Role, EnumPath<models.common.enums.Role>>createSet("roles", models.common.enums.Role.class, EnumPath.class, PathInits.DIRECT2);

    public final SetPath<models.common.Task, QTask> taskForCandidateAssignee = this.<models.common.Task, QTask>createSet("taskForCandidateAssignee", models.common.Task.class, QTask.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<org.joda.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QOperator(String variable) {
        this(Operator.class, forVariable(variable), INITS);
    }

    public QOperator(Path<? extends Operator> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOperator(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOperator(PathMetadata metadata, PathInits inits) {
        this(Operator.class, metadata, inits);
    }

    public QOperator(Class<? extends Operator> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.profile = inits.isInitialized("profile") ? new QOperatorProfile(forProperty("profile")) : null;
    }

}

