package models.common.base.query;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import models.common.base.BaseModel;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QBaseModel is a Querydsl query type for BaseModel
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QBaseModel extends EntityPathBase<BaseModel> {

    private static final long serialVersionUID = 721081311L;

    public static final QBaseModel baseModel = new QBaseModel("baseModel");

    public final play.db.jpa.query.QGenericModel _super = new play.db.jpa.query.QGenericModel(this);

    public final DateTimePath<org.joda.time.LocalDateTime> createdAt = createDateTime("createdAt", org.joda.time.LocalDateTime.class);

    //inherited
    public final SimplePath<Object> entityId = _super.entityId;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    //inherited
    public final BooleanPath persistent = _super.persistent;

    public final DateTimePath<org.joda.time.LocalDateTime> updatedAt = createDateTime("updatedAt", org.joda.time.LocalDateTime.class);

    public final NumberPath<Integer> version = createNumber("version", Integer.class);

    public QBaseModel(String variable) {
        super(BaseModel.class, forVariable(variable));
    }

    public QBaseModel(Path<? extends BaseModel> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBaseModel(PathMetadata metadata) {
        super(BaseModel.class, metadata);
    }

}

