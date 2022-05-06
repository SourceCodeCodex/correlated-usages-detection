package upt.ac.cti.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static upt.ac.cti.tests.FPDTestSuite.findClass;
import org.junit.Test;

public class CoverageTest {

  @Test
  public void case1Test1() {
    assertExactlyOneFieldPairCoverageForClassIs("upt.ac.cti.sut.coverage.case1.test1.Test", 1);
  }

  @Test
  public void case1Test2() {
    assertExactlyOneFieldPairCoverageForClassIs("upt.ac.cti.sut.coverage.case1.test2.Test", 2);
  }

  @Test
  public void case2Test1() {
    assertExactlyOneFieldPairCoverageForClassIs("upt.ac.cti.sut.coverage.case2.test1.Test", 4);
  }

  @Test
  public void case2Test2() {
    assertExactlyOneFieldPairCoverageForClassIs("upt.ac.cti.sut.coverage.case2.test2.Test", 2);
  }

  @Test
  public void case3Test1() {
    assertExactlyOneFieldPairCoverageForClassIs("upt.ac.cti.sut.coverage.case3.test1.Test", 1);
  }

  @Test
  public void case3Test2() {
    assertExactlyOneFieldPairCoverageForClassIs("upt.ac.cti.sut.coverage.case3.test2.Test", 1);
  }

  @Test
  public void case3Test3() {
    assertExactlyOneFieldPairCoverageForClassIs("upt.ac.cti.sut.coverage.case3.test3.Test", 2);
  }

  @Test
  public void case3CodeParserExcpetion() {
    assertExactlyOneFieldPairCoverageForClassIs(
        "upt.ac.cti.sut.coverage.case3.exception.CodeParserExceptionTest", 0);
  }

  @Test
  public void case4Test1() {
    assertExactlyOneFieldPairCoverageForClassIs("upt.ac.cti.sut.coverage.case4.test1.Test", 0);
  }

  @Test
  public void case4Test2() {
    assertExactlyOneFieldPairCoverageForClassIs("upt.ac.cti.sut.coverage.case4.test2.Test", 2);
  }

  @Test
  public void case4Test3() {
    assertExactlyOneFieldPairCoverageForClassIs("upt.ac.cti.sut.coverage.case4.test3.Test", 1);
  }

  @Test
  public void case4Test4() {
    assertExactlyOneFieldPairCoverageForClassIs("upt.ac.cti.sut.coverage.case4.test4.Test", 1);
  }


  private void assertExactlyOneFieldPairCoverageForClassIs(String className, int expected) {
    var mClass = findClass(className);
    assertNotNull(mClass);

    var fieldPairs = mClass.susceptibleFieldPairs().getElements();
    assertEquals(1, fieldPairs.size());

    var firstPair = fieldPairs.get(0);
    var coverage = (int) firstPair.coverage();

    assertEquals(expected, coverage);
  }

}
