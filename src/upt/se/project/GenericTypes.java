package upt.se.project;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MClass;
import thesis.metamodel.entity.MProject;
import thesis.metamodel.factory.Factory;

@RelationBuilder
public class GenericTypes implements IRelationBuilder<MClass, MProject> {

	  @Override
	  public Group<MClass> buildGroup(MProject project) {
		  Group<MClass> result = new Group<MClass>();
		  try {
			for (IPackageFragmentRoot packageFragmentRoot : project.getUnderlyingObject().getAllPackageFragmentRoots()) {
				for (IJavaElement javaElement  : packageFragmentRoot.getChildren()) {
					  if (javaElement.getElementType() == IJavaElement.PACKAGE_FRAGMENT) {
						  IPackageFragment packageFragment = (IPackageFragment) javaElement;
						  for (ICompilationUnit cu : packageFragment.getCompilationUnits()) {
							  if (cu.isStructureKnown()) {
								  for (IType aType : cu.getAllTypes()) {
									  if (aType.getTypeParameters().length != 0) {
										  result.add(Factory.getInstance().createMClass(aType));
									  }
								  }
							  }
						  }
					  }					
				}
			}
		  } catch (JavaModelException e) {
			  e.printStackTrace();
		  }
		  return result;
	  }
}
