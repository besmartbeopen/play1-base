package models.query;

import static com.querydsl.core.types.PathMetadataFactory.*;
import models.Taskable;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTaskable is a Querydsl query type for Taskable
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QTaskable extends EntityPathBase<Taskable> {

    private static final long serialVersionUID = -2096200829L;

    public static final QTaskable taskable = new QTaskable("taskable");

    public final models.base.query.QBaseModel _super = new models.base.query.QBaseModel(this);

    //inherited
    public final DateTimePath<org.joda.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final SimplePath<Object> entityId = _super.entityId;

    //inherited
    public final NumberPath<Integer> id = _super.id;

    //inherited
    public final BooleanPath persistent = _super.persistent;

    //inherited
    public final DateTimePath<org.joda.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QTaskable(String variable) {
        super(Taskable.class, forVariable(variable));
    }

    public QTaskable(Path<? extends Taskable> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTaskable(PathMetadata metadata) {
        super(Taskable.class, metadata);
    }

}

