package models.common.query;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;

import models.common.Task;


/**
 * QTask is a Querydsl query type for Task
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QTask extends EntityPathBase<Task> {

    private static final long serialVersionUID = 1339583145L;

    public static final QTask task = new QTask("task");

    public final models.common.base.query.QBaseModel _super = new models.common.base.query.QBaseModel(this);

    public final SetPath<models.common.Operator, QOperator> candidateAssignees = this.<models.common.Operator, QOperator>createSet("candidateAssignees", models.common.Operator.class, QOperator.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<org.joda.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    //inherited
    public final SimplePath<Object> entityId = _super.entityId;

    //inherited
    public final NumberPath<Integer> id = _super.id;

    //inherited
    public final BooleanPath persistent = _super.persistent;

    public final DatePath<org.joda.time.LocalDate> startDate = createDate("startDate", org.joda.time.LocalDate.class);

    public final NumberPath<Integer> targetId = createNumber("targetId", Integer.class);

    public final EnumPath<models.common.enums.TaskTargetType> targetType = createEnum("targetType", models.common.enums.TaskTargetType.class);

    //inherited
    public final DateTimePath<org.joda.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QTask(String variable) {
        super(Task.class, forVariable(variable));
    }

    public QTask(Path<? extends Task> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTask(PathMetadata metadata) {
        super(Task.class, metadata);
    }

}

