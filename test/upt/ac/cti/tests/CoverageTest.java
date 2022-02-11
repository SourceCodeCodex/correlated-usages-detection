package upt.ac.cti.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static upt.ac.cti.tests.FPDTestSuite.findClass;

public abstract class CoverageTest {

  protected void assertExactlyOneFieldPairCoverageForClassIs(String className, int expected) {
    var mClass = findClass(className);
    assertNotNull(mClass);

    var fieldPairs = mClass.fieldPairGroup().getElements();
    assertEquals(1, fieldPairs.size());

    var firstPair = fieldPairs.get(0);
    var coverage = (int) firstPair.fieldPairApertureCoverage();

    assertEquals(expected, coverage);
  }

}
