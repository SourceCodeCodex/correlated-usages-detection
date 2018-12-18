package classes;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MClass;
import thesis.metamodel.entity.MMethod;
import thesis.metamodel.factory.Factory;

@RelationBuilder
public class MethodGroup implements IRelationBuilder<MMethod, MClass> {

	@Override
	public Group<MMethod> buildGroup(MClass arg0) {
		Group<MMethod> res = new Group<>();
		try {
			IMethod[] all = arg0.getUnderlyingObject().getMethods();
			for (IMethod aJDTMethod : all) {
				res.add(Factory.getInstance().createMMethod(aJDTMethod));
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return res;
	}

}
