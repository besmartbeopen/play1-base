package models.query;

import static com.querydsl.core.types.PathMetadataFactory.*;
import models.Operator;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOperator is a Querydsl query type for Operator
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QOperator extends EntityPathBase<Operator> {

    private static final long serialVersionUID = 2107976648L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOperator operator = new QOperator("operator");

    public final models.base.query.QBaseModel _super = new models.base.query.QBaseModel(this);

    public final NumberPath<java.math.BigDecimal> costPerKm = createNumber("costPerKm", java.math.BigDecimal.class);

    //inherited
    public final DateTimePath<org.joda.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final BooleanPath employee = createBoolean("employee");

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

    public final SetPath<Operator, QOperator> managedOperators = this.<Operator, QOperator>createSet("managedOperators", Operator.class, QOperator.class, PathInits.DIRECT2);

    public final QOperator manager;

    public final SetPath<models.MessageDetail, QMessageDetail> messageDetails = this.<models.MessageDetail, QMessageDetail>createSet("messageDetails", models.MessageDetail.class, QMessageDetail.class, PathInits.DIRECT2);

    public final SetPath<models.Notification, QNotification> notifications = this.<models.Notification, QNotification>createSet("notifications", models.Notification.class, QNotification.class, PathInits.DIRECT2);

    public final StringPath password = createString("password");

    //inherited
    public final BooleanPath persistent = _super.persistent;

    public final SimplePath<play.db.jpa.Blob> photo = createSimple("photo", play.db.jpa.Blob.class);

    public final QOperatorProfile profile;

    public final SetPath<models.RecoveryRequest, QRecoveryRequest> recoveryRequests = this.<models.RecoveryRequest, QRecoveryRequest>createSet("recoveryRequests", models.RecoveryRequest.class, QRecoveryRequest.class, PathInits.DIRECT2);

    public final NumberPath<java.math.BigDecimal> refundPerKm = createNumber("refundPerKm", java.math.BigDecimal.class);

    public final SetPath<models.enums.Role, EnumPath<models.enums.Role>> roles = this.<models.enums.Role, EnumPath<models.enums.Role>>createSet("roles", models.enums.Role.class, EnumPath.class, PathInits.DIRECT2);

    public final SetPath<models.Message, QMessage> sentMessages = this.<models.Message, QMessage>createSet("sentMessages", models.Message.class, QMessage.class, PathInits.DIRECT2);

    public final SetPath<models.Task, QTask> taskForCandidateAssignee = this.<models.Task, QTask>createSet("taskForCandidateAssignee", models.Task.class, QTask.class, PathInits.DIRECT2);

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
        this.manager = inits.isInitialized("manager") ? new QOperator(forProperty("manager"), inits.get("manager")) : null;
        this.profile = inits.isInitialized("profile") ? new QOperatorProfile(forProperty("profile")) : null;
    }

}

