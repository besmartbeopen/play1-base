package models.query;

import static com.querydsl.core.types.PathMetadataFactory.*;
import models.MessageDetail;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMessageDetail is a Querydsl query type for MessageDetail
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMessageDetail extends EntityPathBase<MessageDetail> {

    private static final long serialVersionUID = 1042478740L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMessageDetail messageDetail = new QMessageDetail("messageDetail");

    public final models.base.query.QBaseModel _super = new models.base.query.QBaseModel(this);

    //inherited
    public final DateTimePath<org.joda.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final SimplePath<Object> entityId = _super.entityId;

    //inherited
    public final NumberPath<Integer> id = _super.id;

    public final QMessage message;

    public final QOperator operator;

    //inherited
    public final BooleanPath persistent = _super.persistent;

    public final BooleanPath read = createBoolean("read");

    public final BooleanPath starred = createBoolean("starred");

    public final SetPath<String, StringPath> tags = this.<String, StringPath>createSet("tags", String.class, StringPath.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<org.joda.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QMessageDetail(String variable) {
        this(MessageDetail.class, forVariable(variable), INITS);
    }

    public QMessageDetail(Path<? extends MessageDetail> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMessageDetail(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMessageDetail(PathMetadata metadata, PathInits inits) {
        this(MessageDetail.class, metadata, inits);
    }

    public QMessageDetail(Class<? extends MessageDetail> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.message = inits.isInitialized("message") ? new QMessage(forProperty("message"), inits.get("message")) : null;
        this.operator = inits.isInitialized("operator") ? new QOperator(forProperty("operator"), inits.get("operator")) : null;
    }

}

