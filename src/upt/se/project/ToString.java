package upt.se.project;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import thesis.metamodel.entity.MProject;

@PropertyComputer
public class ToString implements IPropertyComputer<String, MProject> {

  @Override
  public String compute(MProject entity) {
    return entity.getUnderlyingObject().getProject().getName();
  }

}
