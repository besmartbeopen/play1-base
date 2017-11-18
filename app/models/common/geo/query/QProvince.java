package models.common.geo.query;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;

import models.common.geo.Province;


/**
 * QProvince is a Querydsl query type for Province
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QProvince extends EntityPathBase<Province> {

    private static final long serialVersionUID = -1203234223L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProvince province = new QProvince("province");

    public final models.common.base.query.QBaseModel _super = new models.common.base.query.QBaseModel(this);

    public final StringPath code = createString("code");

    //inherited
    public final DateTimePath<org.joda.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final SimplePath<Object> entityId = _super.entityId;

    //inherited
    public final NumberPath<Integer> id = _super.id;

    public final SetPath<models.common.geo.Municipal, QMunicipal> municipals = this.<models.common.geo.Municipal, QMunicipal>createSet("municipals", models.common.geo.Municipal.class, QMunicipal.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    //inherited
    public final BooleanPath persistent = _super.persistent;

    public final QRegion region;

    //inherited
    public final DateTimePath<org.joda.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QProvince(String variable) {
        this(Province.class, forVariable(variable), INITS);
    }

    public QProvince(Path<? extends Province> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProvince(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProvince(PathMetadata metadata, PathInits inits) {
        this(Province.class, metadata, inits);
    }

    public QProvince(Class<? extends Province> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.region = inits.isInitialized("region") ? new QRegion(forProperty("region")) : null;
    }

}

