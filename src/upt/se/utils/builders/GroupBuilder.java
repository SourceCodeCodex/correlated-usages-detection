package upt.se.utils.builders;

import static upt.se.utils.builders.ListBuilder.toList;
import org.eclipse.jdt.core.dom.ITypeBinding;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.XEntity;
import thesis.metamodel.entity.MArgumentType;

public class GroupBuilder {

  public static <T extends XEntity> Group<T> wrap(java.util.List<T> list) {
    Group<T> group = new Group<>();
    group.addAll(list);

    return group;
  }

  public static java.util.List<ITypeBinding> unwrap(Group<MArgumentType> group) {
    return toList(group.getElements()).map(MArgumentType::getUnderlyingObject).asJava();
  }
  
}
