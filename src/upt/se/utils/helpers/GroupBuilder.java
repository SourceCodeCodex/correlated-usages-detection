package upt.se.utils.helpers;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ITypeBinding;
import io.vavr.collection.List;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.XEntity;
import thesis.metamodel.entity.MArgument;
import thesis.metamodel.entity.MArgumentPair;
import thesis.metamodel.entity.MParameter;
import thesis.metamodel.entity.MParameterPair;
import upt.se.utils.ArgumentPair;
import upt.se.utils.ParameterPair;

public class GroupBuilder {

  public static <T extends XEntity> Group<T> wrap(List<T> list) {
    Group<T> group = new Group<>();
    group.addAll(list.asJava());

    return group;
  }

  public static List<ITypeBinding> unwrapParameters(Group<MParameter> group) {
    return List.ofAll(group.getElements()).map(MParameter::getUnderlyingObject);
  }

  public static List<IType> unwrapArguments(Group<MArgument> group) {
    return List.ofAll(group.getElements()).map(MArgument::getUnderlyingObject);
  }
  
  public static List<ParameterPair> unwrapParameterPairs(Group<MParameterPair> group) {
    return List.ofAll(group.getElements()).map(MParameterPair::getUnderlyingObject);
  }
  
  public static List<ArgumentPair> unwrapArgumentsPairs(Group<MArgumentPair> group) {
    return List.ofAll(group.getElements()).map(MArgumentPair::getUnderlyingObject);
  }
}
