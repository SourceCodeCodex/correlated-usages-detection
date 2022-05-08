package upt.ac.cti.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static upt.ac.cti.tests.FPDTestSuite.findClass;
import org.junit.Test;

public class ApertureCoverageTest {

  @Test
  public void test1() {
    assertApertureCoverageForClassIs("upt.ac.cti.sut.aperturecoverage.test1.Test", 1.0 / 9.0);
  }

  @Test
  public void test2() {
    assertApertureCoverageForClassIs("upt.ac.cti.sut.aperturecoverage.test2.ATest", 1.0 / 9.0);
    assertApertureCoverageForClassIs("upt.ac.cti.sut.aperturecoverage.test2.Test", 2.0 / 9.0);
  }

  @Test
  public void test3() {
    assertApertureCoverageForClassIs("upt.ac.cti.sut.aperturecoverage.test3.ATest", 4.0 / 9.0);
    assertApertureCoverageForClassIs("upt.ac.cti.sut.aperturecoverage.test3.Test", 4.0 / 9.0);
  }

  @Test
  public void test4() {
    assertApertureCoverageForClassIs("upt.ac.cti.sut.aperturecoverage.test4.Test", 4.0 / 9.0);
  }

  @Test
  public void test5() {
    assertApertureCoverageForClassIs("upt.ac.cti.sut.aperturecoverage.test5.ATest", 1.0 / 9.0);
    assertApertureCoverageForClassIs("upt.ac.cti.sut.aperturecoverage.test5.Test", 1.0 / 9.0);
  }


  private void assertApertureCoverageForClassIs(String className, Double expected) {
    var mClass = findClass(className);
    assertNotNull(mClass);

    assertEquals(expected, mClass.apertureCoverage());
  }

}
