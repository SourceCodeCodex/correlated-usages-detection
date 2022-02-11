package upt.ac.cti.tests;

import org.junit.Test;

public class CoverageCase2Test extends CoverageTest {

  @Test
  public void test1() {
    assertExactlyOneFieldPairCoverageForClassIs("upt.ac.cti.sut.coverage.case2.test1.Test", 1);
  }

  @Test
  public void test2() {
    assertExactlyOneFieldPairCoverageForClassIs("upt.ac.cti.sut.coverage.case2.test2.Test", 2);
  }

}
