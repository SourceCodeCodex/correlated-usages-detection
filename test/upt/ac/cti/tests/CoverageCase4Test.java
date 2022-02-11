package upt.ac.cti.tests;

import org.junit.Test;

public class CoverageCase4Test extends CoverageTest {

  @Test
  public void test1() {
    assertExactlyOneFieldPairCoverageForClassIs("upt.ac.cti.sut.coverage.case4.test1.Test", 0);
  }

  @Test
  public void test2() {
    assertExactlyOneFieldPairCoverageForClassIs("upt.ac.cti.sut.coverage.case4.test2.Test", 4);
  }

  @Test
  public void test3() {
    assertExactlyOneFieldPairCoverageForClassIs("upt.ac.cti.sut.coverage.case4.test3.Test", 1);
  }

  @Test
  public void test4() {
    assertExactlyOneFieldPairCoverageForClassIs("upt.ac.cti.sut.coverage.case4.test4.Test", 1);
  }

}
