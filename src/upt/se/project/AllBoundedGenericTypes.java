package upt.se.project;

import static upt.se.utils.helpers.Equals.parentExtendsObject;
import io.vavr.collection.List;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MClass;
import thesis.metamodel.entity.MProject;
import upt.se.utils.helpers.GroupBuilder;

@RelationBuilder
public class AllBoundedGenericTypes implements IRelationBuilder<MClass, MProject> {

  @Override
  public Group<MClass> buildGroup(MProject project) {
    return GroupBuilder.wrap(List.ofAll(project.genericTypes().getElements())
        .filter(type -> List.ofAll(type.allTypeParameters().getElements())
            .filter(parameter -> parentExtendsObject(parameter))
            .size() == 0));
  }
}
