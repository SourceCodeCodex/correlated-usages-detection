package upt.se.project;

import io.vavr.collection.List;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MClass;
import thesis.metamodel.entity.MProject;
import upt.se.utils.helpers.GroupBuilder;

@RelationBuilder
public class GenericTypesWithBoundedParameterPairs implements IRelationBuilder<MClass, MProject> {

	  @Override
	  public Group<MClass> buildGroup(MProject project) {
	    return GroupBuilder.wrap(List.ofAll(project.genericTypes().getElements())
	        .filter(type -> List.ofAll(type.typeParameters().getElements())
	            .filter(parameter -> parameter.isBounded())
	            .size() > 1));
	  }
	  
}
