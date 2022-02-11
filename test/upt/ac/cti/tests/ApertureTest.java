package upt.ac.cti.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static upt.ac.cti.tests.FPDTestSuite.findClass;
import org.junit.Test;


public class ApertureTest {

  @Test
  public void test1() {
    assertExactlyOneFieldPairApertureForClassIs("upt.ac.cti.sut.aperture.test1.Test", 9);
  }

  @Test
  public void test2() {
    assertExactlyOneFieldPairApertureForClassIs("upt.ac.cti.sut.aperture.test2.Test", 9);
  }

  @Test
  public void test3() {
    assertExactlyOneFieldPairApertureForClassIs("upt.ac.cti.sut.aperture.test3.Test", 9);
  }

  @Test
  public void test4() {
    assertExactlyOneFieldPairApertureForClassIs("upt.ac.cti.sut.aperture.test4.Test", 9);
  }

  @Test
  public void test5() {
    assertExactlyOneFieldPairApertureForClassIs("upt.ac.cti.sut.aperture.test5.Test", 9);
  }

  @Test
  public void test6() {
    assertExactlyOneFieldPairApertureForClassIs("upt.ac.cti.sut.aperture.test6.Test", 4);
  }

  private void assertExactlyOneFieldPairApertureForClassIs(String className, int expected) {
    var mClass = findClass(className);
    assertNotNull(mClass);

    var fieldPairs = mClass.fieldPairGroup().getElements();
    assertEquals(1, fieldPairs.size());

    var firstPair = fieldPairs.get(0);
    var aperture = (int) firstPair.fieldPairAperture();

    assertEquals(expected, aperture);
  }

}
