package upt.ac.cti.core.workingset.group;

import org.eclipse.core.runtime.jobs.Job;
import familypolymorphismdetection.metamodel.entity.MClass;
import familypolymorphismdetection.metamodel.entity.MWorkingSet;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;

@RelationBuilder
public class WSFamilyPolymorphismSusceptibleClasses
    implements IRelationBuilder<MClass, MWorkingSet> {

  @Override
  public Group<MClass> buildGroup(MWorkingSet mWorkingSet) {

    var group = new Group<MClass>();

    var job = new WSFamilyPolymorphismSusceptibleClassesJob(mWorkingSet);

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
