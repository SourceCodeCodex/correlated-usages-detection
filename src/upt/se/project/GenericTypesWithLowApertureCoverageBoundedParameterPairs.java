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
	        .filter(mClass -> List.ofAll(mClass.typeParameterPairs().getElements())
		        .filter(pair -> pair.isBounded())
	            .map(pair -> pair.apertureCoverage())
	            .filter(aperture -> aperture <= 50d)
	            .isEmpty()));
	}
	
}
