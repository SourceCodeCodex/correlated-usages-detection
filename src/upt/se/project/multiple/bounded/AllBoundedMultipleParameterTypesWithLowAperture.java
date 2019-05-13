package upt.se.project.multiple.bounded;

import io.vavr.collection.List;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MClass;
import thesis.metamodel.entity.MProject;
import upt.se.utils.helpers.GroupBuilder;

@RelationBuilder
public class AllBoundedMultipleParameterTypesWithLowAperture
    implements IRelationBuilder<MClass, MProject> {

  @Override
  public Group<MClass> buildGroup(MProject project) {
    return GroupBuilder.wrap(List.ofAll(project.allMultipleParameterTypes().getElements())
        .filter(mClass -> List.ofAll(mClass.allTypeParameterPairs().getElements())
            .map(pair -> pair.aperture())
            .filter(aperture -> aperture <= 50d)
            .isEmpty()));
  }
}
