package models.base.query;

import static com.querydsl.core.types.PathMetadataFactory.*;
import models.base.BaseAddress;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBaseAddress is a Querydsl query type for BaseAddress
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QBaseAddress extends EntityPathBase<BaseAddress> {

    private static final long serialVersionUID = -905240790L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBaseAddress baseAddress = new QBaseAddress("baseAddress");

    public final QBaseModel _super = new QBaseModel(this);

    public final StringPath cap = createString("cap");

    //inherited
    public final DateTimePath<org.joda.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final SimplePath<Object> entityId = _super.entityId;

    public final StringPath fax = createString("fax");

    //inherited
    public final NumberPath<Integer> id = _super.id;

    public final models.geo.query.QMunicipal municipal;

    //inherited
    public final BooleanPath persistent = _super.persistent;

    public final StringPath phone = createString("phone");

    public final StringPath street = createString("street");

    //inherited
    public final DateTimePath<org.joda.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QBaseAddress(String variable) {
        this(BaseAddress.class, forVariable(variable), INITS);
    }

    public QBaseAddress(Path<? extends BaseAddress> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBaseAddress(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBaseAddress(PathMetadata metadata, PathInits inits) {
        this(BaseAddress.class, metadata, inits);
    }

    public QBaseAddress(Class<? extends BaseAddress> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.municipal = inits.isInitialized("municipal") ? new models.geo.query.QMunicipal(forProperty("municipal"), inits.get("municipal")) : null;
    }

}

