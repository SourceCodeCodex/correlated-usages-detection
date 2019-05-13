package upt.se.project.single.unbounded;

import io.vavr.collection.List;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import thesis.metamodel.entity.MClass;
import thesis.metamodel.entity.MProject;
import upt.se.utils.helpers.GroupBuilder;

public class AllSingleParameterTypesWithLowAperture
    implements IRelationBuilder<MClass, MProject> {

  @Override
  public Group<MClass> buildGroup(MProject project) {
    return GroupBuilder.wrap(List.ofAll(project.allSingleParameterTypes().getElements())
        .filter(mClass -> List.ofAll(mClass.allTypeParameterPairs().getElements())
            .map(pair -> pair.aperture())
            .filter(aperture -> aperture <= 50d)
            .isEmpty()));
  }
}
