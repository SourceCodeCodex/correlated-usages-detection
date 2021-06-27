package upt.se.project;

import io.vavr.collection.List;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MClass;
import thesis.metamodel.entity.MProject;
import upt.se.utils.helpers.GroupBuilder;

@RelationBuilder
public class GenericTypesWithLowApertureCoverageBoundedParameterPairs implements IRelationBuilder<MClass, MProject> {

	@Override
	public Group<MClass> buildGroup(MProject project) {
		return GroupBuilder.wrap(List.ofAll(project.genericTypesWithBoundedParameterPairs().getElements())
	        .filter(mClass -> mClass.apertureCoverage() <= 0.5));
	}
	
}
