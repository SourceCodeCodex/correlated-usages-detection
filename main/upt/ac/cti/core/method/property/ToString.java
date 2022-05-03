package upt.ac.cti.core.method.property;

import org.eclipse.jdt.core.IMethod;
import familypolymorphismdetection.metamodel.entity.MMethod;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;

@PropertyComputer
public final class ToString implements IPropertyComputer<String, MMethod> {

  @Override
  public String compute(MMethod mMethod) {
    var iMethod = (IMethod) mMethod.getUnderlyingObject();
    return iMethod.getElementName();
  }

}
