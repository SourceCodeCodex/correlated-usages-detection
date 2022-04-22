package upt.ac.cti.model.method.property;

import org.eclipse.jdt.core.IMethod;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;

@PropertyComputer
public final class ToString implements IPropertyComputer<String, MMethod> {

  @Override
  public String compute(MMethod mClass) {
    var iMethod = (IMethod) mClass.getUnderlyingObject();
    return iMethod.getElementName();
  }

}
