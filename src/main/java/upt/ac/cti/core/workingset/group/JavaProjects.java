package upt.ac.cti.core.workingset.group;

import java.util.stream.Stream;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.IWorkingSet;
import familypolymorphismdetection.metamodel.entity.MProject;
import familypolymorphismdetection.metamodel.entity.MWorkingSet;
import familypolymorphismdetection.metamodel.factory.Factory;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;

@RelationBuilder
public final class JavaProjects implements IRelationBuilder<MProject, MWorkingSet> {

  @Override
  public Group<MProject> buildGroup(MWorkingSet mWorkingSet) {

    var group = new Group<MProject>();

    var ws = (IWorkingSet) mWorkingSet.getUnderlyingObject();

    var mProjects = Stream.of(ws.getElements())
        .map(a -> a.getAdapter(IResource.class))
        .filter(r -> r != null)
        .map(IResource::getProject)
        .filter(p -> {
          try {
            return p.isOpen() && p.hasNature(JavaCore.NATURE_ID);
          } catch (CoreException e) {
            e.printStackTrace();
            return false;
          }
        })
        .map(p -> Factory.getInstance().createMProject(JavaCore.create(p)))
        .toList();

    group.addAll(mProjects);

    return group;
  }



}
