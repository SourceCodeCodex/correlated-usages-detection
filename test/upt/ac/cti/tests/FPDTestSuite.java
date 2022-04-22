package upt.ac.cti.tests;

import org.eclipse.jdt.core.IType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import familypolymorphismdetection.metamodel.entity.MClass;
import familypolymorphismdetection.metamodel.factory.Factory;
import ro.lrg.xcore.metametamodel.Group;
import upt.ac.cti.util.TestUtil;

@RunWith(Suite.class)
@SuiteClasses({ApertureTest.class, CoverageCase1Test.class, CoverageCase2Test.class,
    CoverageCase3Test.class, CoverageCase4Test.class})
public class FPDTestSuite {

  private static final String PROJECT_NAME = "SUT";

  private static Group<MClass> nonGenericClasses;

  static MClass findClass(String name) {
    for (MClass mClass : nonGenericClasses.getElements()) {
      if (((IType) mClass.getUnderlyingObject()).getFullyQualifiedName().equals(name)) {
        return mClass;
      }
    }
    return null;
  }

  @BeforeClass
  public static void setUp() {
    TestUtil.importProject(PROJECT_NAME, PROJECT_NAME + ".zip");
    var mProject = Factory.getInstance()
        .createMProject(TestUtil.getProject(PROJECT_NAME).get());
    nonGenericClasses = mProject.unparametrizedClasses();
  }

  @AfterClass
  public static void tearDown() {
    TestUtil.deleteProject(PROJECT_NAME);
  }
}
