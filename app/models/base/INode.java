package models.base;

import java.util.Collection;

public interface INode<T> {

  Collection<T> getChildren();
}
