package models.query;

import static com.querydsl.core.types.PathMetadataFactory.*;
import models.RecoveryRequest;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecoveryRequest is a Querydsl query type for RecoveryRequest
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QRecoveryRequest extends EntityPathBase<RecoveryRequest> {

    private static final long serialVersionUID = 1292973462L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecoveryRequest recoveryRequest = new QRecoveryRequest("recoveryRequest");

    public final models.base.query.QBaseModel _super = new models.base.query.QBaseModel(this);

    public final StringPath code = createString("code");

    //inherited
    public final DateTimePath<org.joda.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final SimplePath<Object> entityId = _super.entityId;

    //inherited
    public final NumberPath<Integer> id = _super.id;

    public final StringPath ipaddress = createString("ipaddress");

    public final QOperator operator;

    //inherited
    public final BooleanPath persistent = _super.persistent;

    //inherited
    public final DateTimePath<org.joda.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QRecoveryRequest(String variable) {
        this(RecoveryRequest.class, forVariable(variable), INITS);
    }

    public QRecoveryRequest(Path<? extends RecoveryRequest> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecoveryRequest(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecoveryRequest(PathMetadata metadata, PathInits inits) {
        this(RecoveryRequest.class, metadata, inits);
    }

    public QRecoveryRequest(Class<? extends RecoveryRequest> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.operator = inits.isInitialized("operator") ? new QOperator(forProperty("operator"), inits.get("operator")) : null;
    }

}

