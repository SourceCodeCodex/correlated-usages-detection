package upt.ac.cti.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static upt.ac.cti.tests.FPDTestSuite.findClass;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.Job;
import org.junit.Test;
import familypolymorphismdetection.metamodel.entity.MFieldPair;
import familypolymorphismdetection.metamodel.entity.MParameterPair;
import upt.ac.cti.config.Config;
import upt.ac.cti.util.report.ReportUtil;

public class FPDTests {

  @Test
  public void susceptribleClassesTest() {
    assertEquals(18, FPDTestSuite.countSusceptibleClasses());
  }

  @Test
  public void exportTests() throws IOException {
    ReportUtil.USE_TEST_PATH = true;

    var url = Platform.getBundle("FamilyPolymorphismDetection").getEntry("/");

    url = FileLocator.resolve(url);
    var path = url.getPath() + "target/" + ReportUtil.TEST_DIR_NAME + "/";

    var ws = FPDTestSuite.getWorkingSet();
    ws.cFI_ExportReports();
    ws.cNS_ExportReports();
    ws.sFI_ExportReports();
    ws.sNS_ExportReports();

    var jobMan = Job.getJobManager();

    List.of(jobMan.find(Config.JOB_FAMILY)).stream().forEach(t -> {
      try {
        t.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });

    var d = new File(path);
    assertEquals(4, d.listFiles().length);
    List.of(d.listFiles()).stream().forEach(File::delete);
    d.delete();
    ReportUtil.USE_TEST_PATH = false;
  }

  @Test
  public void downcastTest() {
    assertSFIApertureCoverage("upt.ac.cti.DowncastTest", 1. / 6.);
    assertCFIApertureCoverage("upt.ac.cti.DowncastTest", 1. / 6.);
    assertSNSApertureCoverage("upt.ac.cti.DowncastTest", 1. / 3.);
    assertCNSApertureCoverage("upt.ac.cti.DowncastTest", 1. / 3.);
  }

  @Test
  public void inconclusiveMethodInvocationInitTest() {
    assertSFIApertureCoverage("upt.ac.cti.InconclusiveMethodInvocationInitTest", 0.2);
    assertCFIApertureCoverage("upt.ac.cti.InconclusiveMethodInvocationInitTest", 0.2);
    assertSNSApertureCoverage("upt.ac.cti.InconclusiveMethodInvocationInitTest",
        2. / 15.);
    assertCNSApertureCoverage("upt.ac.cti.InconclusiveMethodInvocationInitTest", 0.36);
  }

  @Test
  public void listTest() {
    assertSFIApertureCoverage("upt.ac.cti.ListTest", 0.08);
    assertCFIApertureCoverage("upt.ac.cti.ListTest", 0.08);
    assertSNSApertureCoverage("upt.ac.cti.ListTest", 0.16);
    assertCNSApertureCoverage("upt.ac.cti.ListTest", 0.36);
  }

  @Test
  public void listTest2() {
    assertSFIApertureCoverage("upt.ac.cti.ListTest2", 0.2);
    assertCFIApertureCoverage("upt.ac.cti.ListTest2", 0.2);
    assertSNSApertureCoverage("upt.ac.cti.ListTest2", 0.16);
    assertCNSApertureCoverage("upt.ac.cti.ListTest2", 0.36);
  }

  @Test
  public void accessObjectTest() {
    assertSFIApertureCoverage("upt.ac.cti.AccessObjectTest", 0.12);
    assertCFIApertureCoverage("upt.ac.cti.AccessObjectTest", 0.12);
    assertSNSApertureCoverage("upt.ac.cti.AccessObjectTest", 0.16);
    assertCNSApertureCoverage("upt.ac.cti.AccessObjectTest", 0.36);
  }

  @Test
  public void thisApertureCoverageTest() {
    assertSFIApertureCoverage("upt.ac.cti.ThisApertureCoverageTest", 1. / 3.);
    assertCFIApertureCoverage("upt.ac.cti.ThisApertureCoverageTest", 1. / 3.);
    assertSNSApertureCoverage("upt.ac.cti.ThisApertureCoverageTest", 0.16);
    assertCNSApertureCoverage("upt.ac.cti.ThisApertureCoverageTest", 0.36);
  }

  @Test
  public void complexDerivationTest() {
    assertSFIApertureCoverage("upt.ac.cti.ComplexDerivationTest", 0.04);
    assertCFIApertureCoverage("upt.ac.cti.ComplexDerivationTest", 0.04);
    assertSNSApertureCoverage("upt.ac.cti.ComplexDerivationTest", 0.16);
    assertCNSApertureCoverage("upt.ac.cti.ComplexDerivationTest", 0.36);
  }

  @Test
  public void inheritanceTest() {
    assertSFIApertureCoverage("upt.ac.cti.InheritanceTest", 0.08);
    assertSFIApertureCoverage("upt.ac.cti.InheritanceTest1", 0.04);

    assertCFIApertureCoverage("upt.ac.cti.InheritanceTest", 0.08);
    assertCFIApertureCoverage("upt.ac.cti.InheritanceTest1", 0.04);

    assertSNSApertureCoverage("upt.ac.cti.InheritanceTest", 0.16);
    assertSNSApertureCoverage("upt.ac.cti.InheritanceTest1", 0.16);

    assertCNSApertureCoverage("upt.ac.cti.InheritanceTest", 0.36);
    assertCNSApertureCoverage("upt.ac.cti.InheritanceTest1", 0.36);
  }

  @Test
  public void inheritanceTest2() {
    assertSFIApertureCoverage("upt.ac.cti.InheritanceTest2", 0.12);
    assertSFIApertureCoverage("upt.ac.cti.InheritanceTest21", 0.08);

    assertCFIApertureCoverage("upt.ac.cti.InheritanceTest2", 0.12);
    assertCFIApertureCoverage("upt.ac.cti.InheritanceTest21", 0.08);

    assertSNSApertureCoverage("upt.ac.cti.InheritanceTest2", 0.16);
    assertSNSApertureCoverage("upt.ac.cti.InheritanceTest21", 0.16);

    assertCNSApertureCoverage("upt.ac.cti.InheritanceTest2", 0.36);
    assertCNSApertureCoverage("upt.ac.cti.InheritanceTest21", 0.36);
  }

  @Test
  public void constructorParametersTest() {
    assertSFIApertureCoverage("upt.ac.cti.ConstructorParametersTest", 1. / 3.);
    assertCFIApertureCoverage("upt.ac.cti.ConstructorParametersTest", 1. / 3.);
    assertSNSApertureCoverage("upt.ac.cti.ConstructorParametersTest", 1. / 3.);
    assertCNSApertureCoverage("upt.ac.cti.ConstructorParametersTest", 1. / 3.);
  }

  @Test
  public void partialDerivationTest() {
    assertSFIApertureCoverage("upt.ac.cti.PartialDerivationTest", 0.2);
    assertCFIApertureCoverage("upt.ac.cti.PartialDerivationTest", 0.2);
    assertSNSApertureCoverage("upt.ac.cti.PartialDerivationTest", 0.16);
    assertCNSApertureCoverage("upt.ac.cti.PartialDerivationTest", 0.36);
  }

  @Test
  public void partialDerivationTest2() {
    assertSFIApertureCoverage("upt.ac.cti.APartialDerivationTest2", 0.04);
    assertSFIApertureCoverage("upt.ac.cti.PartialDerivationTest2", 0.12);

    assertCFIApertureCoverage("upt.ac.cti.APartialDerivationTest2", 0.04);
    assertCFIApertureCoverage("upt.ac.cti.PartialDerivationTest2", 0.12);

    assertSNSApertureCoverage("upt.ac.cti.APartialDerivationTest2", 0.16);
    assertSNSApertureCoverage("upt.ac.cti.PartialDerivationTest2", 0.16);

    assertCNSApertureCoverage("upt.ac.cti.APartialDerivationTest2", 0.36);
    assertCNSApertureCoverage("upt.ac.cti.PartialDerivationTest2", 0.36);
  }

  @Test
  public void simpleDerivationTest() {
    assertSFIApertureCoverage("upt.ac.cti.SimpleDerivationTest", 0.16);
    assertCFIApertureCoverage("upt.ac.cti.SimpleDerivationTest", 0.16);
    assertSNSApertureCoverage("upt.ac.cti.SimpleDerivationTest", 0.16);
    assertCNSApertureCoverage("upt.ac.cti.SimpleDerivationTest", 0.36);
  }

  @Test
  public void exceedDepthTest() {
    assertSFIApertureCoverage("upt.ac.cti.ExceedDepthTest", 0.00);
    assertCFIApertureCoverage("upt.ac.cti.ExceedDepthTest", 0.08);
    assertSNSApertureCoverage("upt.ac.cti.ExceedDepthTest", 0.16);
    assertCNSApertureCoverage("upt.ac.cti.ExceedDepthTest", 0.36);
  }

  private void assertSFIApertureCoverage(String className, Double expected) {
    var mClass = findClass(className);
    assertNotNull(mClass);
    assertEquals(expected, mClass.sFI_ApertureCoverage());
  }


  private void assertCFIApertureCoverage(String className, Double expected) {
    var mClass = findClass(className);
    assertNotNull(mClass);
    assertEquals(expected, mClass.cFI_ApertureCoverage());
  }

  private void assertSNSApertureCoverage(String className, Double expected) {
    var mClass = findClass(className);
    assertNotNull(mClass);
    assertEquals(expected, mClass.sNS_ApertureCoverage());
  }

  private void assertCNSApertureCoverage(String className, Double expected) {
    var mClass = findClass(className);
    assertNotNull(mClass);
    assertEquals(expected, mClass.cNS_ApertureCoverage());
  }

  @Test
  public void apairingValidationTest() {

    var mClass = findClass("upt.ac.cti.PairingValidationTest");
    assertNotNull(mClass);

    var fieldAperturesExpected = List.of(3, 5, 5, 15, 15, 25);
    var fieldPairs = mClass.susceptibleFieldPairs().getElements();
    assertEquals(6, fieldPairs.size());

    var fieldApertures =
        new ArrayList<>(fieldPairs.stream()
            .map(MFieldPair::aperture)
            .toList());

    fieldApertures.sort(Comparator.naturalOrder());
    assertTrue(fieldAperturesExpected.equals(fieldApertures));

    var sfiFieldCoverageSum = fieldPairs.stream()
        .map(MFieldPair::sFI_CoveredTypes)
        .collect(Collectors.summingInt(g -> g.getElements().size()));

    assertEquals(0, sfiFieldCoverageSum);



    var parameterAperturesExpected = List.of(5, 5, 25);
    var parameterPairs = mClass.susceptibleParameterPairs().getElements();
    assertEquals(3, parameterPairs.size());

    var parameterApertures =
        new ArrayList<>(parameterPairs.stream()
            .map(MParameterPair::aperture)
            .toList());

    parameterApertures.sort(Comparator.naturalOrder());
    assertTrue(parameterAperturesExpected.equals(parameterApertures));

    var sfiParameterCoverageSum = parameterPairs.stream()
        .map(MParameterPair::sFI_CoveredTypes)
        .collect(Collectors.summingInt(g -> g.getElements().size()));

    assertEquals(0, sfiParameterCoverageSum);



  }
}
