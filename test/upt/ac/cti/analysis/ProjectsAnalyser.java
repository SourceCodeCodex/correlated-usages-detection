package upt.ac.cti.analysis;

import org.junit.Test;
import familypolymorphismdetection.metamodel.factory.Factory;
import upt.ac.cti.util.TestUtil;

public class ProjectsAnalyser {

  private static void analyseProject(String PROJECT_NAME) {

    TestUtil.importProject(PROJECT_NAME, PROJECT_NAME + ".zip");
    var mProject = Factory.getInstance()
        .createMProject(TestUtil.getProject(PROJECT_NAME).get());
    mProject.familyPolymorphismSusceptibleClasses();

    TestUtil.deleteProject(PROJECT_NAME);
  }

  @Test
  public void project1() {
    analyseProject("P1");
  }

}
