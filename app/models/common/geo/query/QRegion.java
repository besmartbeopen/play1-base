package models.common.geo.query;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;

import models.common.geo.Region;


/**
 * QRegion is a Querydsl query type for Region
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QRegion extends EntityPathBase<Region> {

    private static final long serialVersionUID = 405760597L;

    public static final QRegion region = new QRegion("region");

    public final models.common.base.query.QBaseModel _super = new models.common.base.query.QBaseModel(this);

    //inherited
    public final DateTimePath<org.joda.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final SimplePath<Object> entityId = _super.entityId;

    public final EnumPath<models.common.enums.GeographicalDistribution> geographicalDistribution = createEnum("geographicalDistribution", models.common.enums.GeographicalDistribution.class);

    //inherited
    public final NumberPath<Integer> id = _super.id;

    public final StringPath name = createString("name");

    //inherited
    public final BooleanPath persistent = _super.persistent;

    public final SetPath<models.common.geo.Province, QProvince> provinces = this.<models.common.geo.Province, QProvince>createSet("provinces", models.common.geo.Province.class, QProvince.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<org.joda.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QRegion(String variable) {
        super(Region.class, forVariable(variable));
    }

    public QRegion(Path<? extends Region> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRegion(PathMetadata metadata) {
        super(Region.class, metadata);
    }

}

