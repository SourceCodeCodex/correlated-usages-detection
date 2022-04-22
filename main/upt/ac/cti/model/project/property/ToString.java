package upt.ac.cti.model.project.property;

import org.eclipse.jdt.core.IJavaProject;
import familypolymorphismdetection.metamodel.entity.MProject;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;

@PropertyComputer
public class ToString implements IPropertyComputer<String, MProject> {

  @Override
  public String compute(MProject mProject) {
    var project = (IJavaProject) mProject.getUnderlyingObject();
    return project.getElementName();
  }

}
