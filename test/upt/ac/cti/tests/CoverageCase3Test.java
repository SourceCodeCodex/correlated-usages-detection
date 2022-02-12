package upt.ac.cti.tests;

import org.junit.Test;

public class CoverageCase3Test extends CoverageTest {

  @Test
  public void test1() {
    assertExactlyOneFieldPairCoverageForClassIs("upt.ac.cti.sut.coverage.case3.test1.Test", 1);
  }

  @Test
  public void test2() {
    assertExactlyOneFieldPairCoverageForClassIs("upt.ac.cti.sut.coverage.case3.test2.Test", 1);
  }

  @Test
  public void test3() {
    assertExactlyOneFieldPairCoverageForClassIs("upt.ac.cti.sut.coverage.case3.test3.Test", 2);
  }

}
