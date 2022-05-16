package upt.ac.cti.tests;

import java.util.List;
import org.eclipse.jdt.core.IType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import familypolymorphismdetection.metamodel.entity.MClass;
import familypolymorphismdetection.metamodel.entity.MProject;
import familypolymorphismdetection.metamodel.entity.MWorkingSet;
import familypolymorphismdetection.metamodel.factory.Factory;
import upt.ac.cti.config.Config;
import upt.ac.cti.ui.Startup;
import upt.ac.cti.util.TestUtil;

@RunWith(Suite.class)
@SuiteClasses({
    FPDTests.class})
public class FPDTestSuite {

  private static final String PROJECT_NAME = "SUT";

  private static List<MClass> familyPolymorphismSusceptibleClasses;

  private static MProject mProject;

  private static MWorkingSet mWorkingSet;

  static MClass findClass(String name) {
    for (MClass mClass : familyPolymorphismSusceptibleClasses) {
      if (((IType) mClass.getUnderlyingObject()).getFullyQualifiedName().equals(name)) {
        return mClass;
      }
    }
    return null;
  }

  static MProject getProject() {
    return mProject;
  }

  static MWorkingSet getWorkingSet() {
    return mWorkingSet;
  }

  static int countSusceptibleClasses() {
    return familyPolymorphismSusceptibleClasses.size();
  }

  @BeforeClass
  public static void setUp() {
    Startup.init();
    Config.MAX_DEPTH_THRESHOLD = 100;
    Config.MAX_DEPTH_DIFF = 100;
    Config.TOKENS_MAX_DIFF = 3;
    Config.TOKENS_THRESHOLD = 0.5;

    TestUtil.importProject(PROJECT_NAME, PROJECT_NAME + ".zip");
    var p = TestUtil.getProjectAndWorkingSet(PROJECT_NAME).get();
    var javaProject = p.getValue0();
    var workingSet = p.getValue1();
    mWorkingSet = Factory.getInstance().createMWorkingSet(workingSet);
    mProject = Factory.getInstance().createMProject(javaProject);
    familyPolymorphismSusceptibleClasses =
        mProject.familyPolymorphismSusceptibleClasses().getElements();
  }

  @AfterClass
  public static void tearDown() {
    TestUtil.deleteProject(PROJECT_NAME);
  }

}
