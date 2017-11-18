package models.common.base;

import java.util.Collection;

public interface INode<T> {

  Collection<T> getChildren();
}
