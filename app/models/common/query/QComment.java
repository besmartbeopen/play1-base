package models.common.query;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;

import models.common.Comment;


/**
 * QComment is a Querydsl query type for Comment
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QComment extends EntityPathBase<Comment> {

    private static final long serialVersionUID = 1173092027L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QComment comment1 = new QComment("comment1");

    public final models.common.base.query.QBaseModel _super = new models.common.base.query.QBaseModel(this);

    public final BooleanPath active = createBoolean("active");

    public final StringPath comment = createString("comment");

    public final SetPath<Comment, QComment> comments = this.<Comment, QComment>createSet("comments", Comment.class, QComment.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<org.joda.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final SimplePath<Object> entityId = _super.entityId;

    //inherited
    public final NumberPath<Integer> id = _super.id;

    public final QOperator owner;

    //inherited
    public final BooleanPath persistent = _super.persistent;

    public final QComment relatedToComment;

    public final NumberPath<Integer> targetId = createNumber("targetId", Integer.class);

    public final EnumPath<models.common.enums.CommentTargetType> targetType = createEnum("targetType", models.common.enums.CommentTargetType.class);

    //inherited
    public final DateTimePath<org.joda.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final NumberPath<Integer> version = _super.version;

    public QComment(String variable) {
        this(Comment.class, forVariable(variable), INITS);
    }

    public QComment(Path<? extends Comment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QComment(PathMetadata metadata, PathInits inits) {
        this(Comment.class, metadata, inits);
    }

    public QComment(Class<? extends Comment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.owner = inits.isInitialized("owner") ? new QOperator(forProperty("owner"), inits.get("owner")) : null;
        this.relatedToComment = inits.isInitialized("relatedToComment") ? new QComment(forProperty("relatedToComment"), inits.get("relatedToComment")) : null;
    }

}

