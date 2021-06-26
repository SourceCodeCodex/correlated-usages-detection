package upt.se.classes;

import org.eclipse.jdt.core.JavaModelException;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import thesis.metamodel.entity.MClass;

@PropertyComputer
public class ToString implements IPropertyComputer<String, MClass> {

	  @Override
	  public String compute(MClass entity) {
		  try {
			  return entity.getUnderlyingObject().getFullyQualifiedParameterizedName();
		  } catch (JavaModelException e) {
			  return entity.getUnderlyingObject().getFullyQualifiedName();
		  }
	  }
	  
}
