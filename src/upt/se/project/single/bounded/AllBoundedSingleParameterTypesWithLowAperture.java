package upt.se.project.single.bounded;

import io.vavr.collection.List;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MClass;
import thesis.metamodel.entity.MProject;
import upt.se.utils.helpers.GroupBuilder;

@RelationBuilder
public class AllBoundedSingleParameterTypesWithLowAperture
    implements IRelationBuilder<MClass, MProject> {

  @Override
  public Group<MClass> buildGroup(MProject project) {
    return GroupBuilder.wrap(List.ofAll(project.allBoundedSingleParameterTypes().getElements())
        .filter(mClass -> List.ofAll(mClass.allTypeParameterPairs().getElements())
            .map(pair -> pair.aperture())
            .filter(aperture -> aperture <= 50d)
            .isEmpty()));
  }
}
