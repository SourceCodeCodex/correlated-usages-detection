package upt.ac.cti.tests;

import org.junit.Test;

public class CoverageCase1Test extends CoverageTest {

  @Test
  public void test1() {
    assertExactlyOneFieldPairCoverageForClassIs("upt.ac.cti.sut.coverage.case1.test1.Test", 1);
  }

  @Test
  public void test2() {
    assertExactlyOneFieldPairCoverageForClassIs("upt.ac.cti.sut.coverage.case1.test2.Test", 4);
  }
}
