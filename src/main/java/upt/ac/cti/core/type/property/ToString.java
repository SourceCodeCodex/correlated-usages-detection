package upt.ac.cti.core.type.property;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import familypolymorphismdetection.metamodel.entity.MClass;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;

@PropertyComputer
public final class ToString implements IPropertyComputer<String, MClass> {

  @Override
  public String compute(MClass mClass) {
    var iType = (IType) mClass.getUnderlyingObject();
    try {
      return iType.getFullyQualifiedParameterizedName();
    } catch (JavaModelException e) {
      return iType.getFullyQualifiedName();
    }
  }

}
