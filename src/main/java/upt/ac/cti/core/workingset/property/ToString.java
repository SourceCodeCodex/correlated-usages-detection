package upt.ac.cti.core.workingset.property;

import org.eclipse.ui.IWorkingSet;
import familypolymorphismdetection.metamodel.entity.MWorkingSet;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;

@PropertyComputer
public final class ToString implements IPropertyComputer<String, MWorkingSet> {

  @Override
  public String compute(MWorkingSet mWorkingSet) {
    var ws = (IWorkingSet) mWorkingSet.getUnderlyingObject();
    return ws.getName();
  }

}
