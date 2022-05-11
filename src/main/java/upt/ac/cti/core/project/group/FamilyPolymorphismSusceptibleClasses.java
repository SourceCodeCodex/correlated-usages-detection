package upt.ac.cti.core.project.group;

import org.eclipse.core.runtime.jobs.Job;
import familypolymorphismdetection.metamodel.entity.MClass;
import familypolymorphismdetection.metamodel.entity.MProject;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;

@RelationBuilder
public final class FamilyPolymorphismSusceptibleClasses
    implements IRelationBuilder<MClass, MProject> {



  @Override
  public Group<MClass> buildGroup(MProject mProject) {

    var group = new Group<MClass>();

    var job = new FamilyPolymorphismSusceptibleClassesJob(mProject);

    job.setPriority(Job.LONG);
    job.setSystem(false);
    job.setUser(true);
    job.schedule();

    try {
      job.join();
      group.addAll(job.mClasses());
    } catch (InterruptedException e1) {
      e1.printStackTrace();
    }

    return group;
  }



}
