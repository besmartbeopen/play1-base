package models.query;

import static com.querydsl.core.types.PathMetadataFactory.*;
import models.OperatorProfile;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOperatorProfile is a Querydsl query type for OperatorProfile
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QOperatorProfile extends EntityPathBase<OperatorProfile> {

    private static final long serialVersionUID = 435594433L;

    public static final QOperatorProfile operatorProfile = new QOperatorProfile("operatorProfile");

    public final models.base.query.QBaseModel _super = new models.base.query.QBaseModel(this);

    public final BooleanPath active = createBoolean("active");

    //inherited
    public final DateTimePath<org.joda.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    //inherited
    public final SimplePath<Object> entityId = _super.entityId;

    //inherited
    public final NumberPath<Integer> id = _super.id;

    public final StringPath loginUrl = createString("loginUrl");

    public final StringPath name = createString("name");

    public final SetPath<models.Operator, QOperator> operators = this.<models.Operator, QOperator>createSet("operators", models.Operator.class, QOperator.class, PathInits.DIRECT2);

    //inherited
    public final BooleanPath persistent = _super.persistent;

    public final SetPath<models.enums.Role, EnumPath<models.enums.Role>> roles = this.<models.enums.Role, EnumPath<models.enums.Role>>createSet("roles", models.enums.Role.class, EnumPath.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<org.joda.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QOperatorProfile(String variable) {
        super(OperatorProfile.class, forVariable(variable));
    }

    public QOperatorProfile(Path<? extends OperatorProfile> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOperatorProfile(PathMetadata metadata) {
        super(OperatorProfile.class, metadata);
    }

}

