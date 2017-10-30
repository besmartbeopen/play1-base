package models.geo.query;

import static com.querydsl.core.types.PathMetadataFactory.*;
import models.geo.Municipal;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMunicipal is a Querydsl query type for Municipal
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMunicipal extends EntityPathBase<Municipal> {

    private static final long serialVersionUID = -2145022991L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMunicipal municipal = new QMunicipal("municipal");

    public final models.base.query.QBaseModel _super = new models.base.query.QBaseModel(this);

    public final NumberPath<Integer> code = createNumber("code", Integer.class);

    //inherited
    public final DateTimePath<org.joda.time.LocalDateTime> createdAt = _super.createdAt;

    public final BooleanPath enabled = createBoolean("enabled");

    //inherited
    public final SimplePath<Object> entityId = _super.entityId;

    //inherited
    public final NumberPath<Integer> id = _super.id;

    public final StringPath name = createString("name");

    //inherited
    public final BooleanPath persistent = _super.persistent;

    public final QProvince province;

    public final QProvince provincePrevious;

    //inherited
    public final DateTimePath<org.joda.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QMunicipal(String variable) {
        this(Municipal.class, forVariable(variable), INITS);
    }

    public QMunicipal(Path<? extends Municipal> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMunicipal(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMunicipal(PathMetadata metadata, PathInits inits) {
        this(Municipal.class, metadata, inits);
    }

    public QMunicipal(Class<? extends Municipal> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.province = inits.isInitialized("province") ? new QProvince(forProperty("province"), inits.get("province")) : null;
        this.provincePrevious = inits.isInitialized("provincePrevious") ? new QProvince(forProperty("provincePrevious"), inits.get("provincePrevious")) : null;
    }

}

