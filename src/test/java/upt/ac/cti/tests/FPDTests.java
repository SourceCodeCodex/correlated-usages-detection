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
    assertEquals(17, FPDTestSuite.countSusceptibleClasses());
  }

  @Test
  public void exportTests() throws IOException {
    ReportUtil.USE_TEST_PATH = true;

    var url = Platform.getBundle("FamilyPolymorphismDetection").getEntry("/");

    url = FileLocator.resolve(url);
    var path = url.getPath() + "target/" + ReportUtil.TEST_DIR_NAME + "/";

    var ws = FPDTestSuite.getWorkingSet();
    ws.exportInOneReportFlowInsensitive();
    ws.exportInOneReportNameSimilarity();
    ws.exportReportsFlowInsensitive();
    ws.exportReportsNameSimilarity();

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
    assertFlowInsensitiveApertureCoverage("upt.ac.cti.DowncastTest", 1. / 6.);
    assertNameSimilarityApertureCoverage("upt.ac.cti.DowncastTest", 1. / 3.);
  }

  @Test
  public void inconclusiveMethodInvocationInitTest() {
    assertFlowInsensitiveApertureCoverage("upt.ac.cti.InconclusiveMethodInvocationInitTest", 0.2);
    assertNameSimilarityApertureCoverage("upt.ac.cti.InconclusiveMethodInvocationInitTest",
        2. / 15.);
  }

  @Test
  public void listTest() {
    assertFlowInsensitiveApertureCoverage("upt.ac.cti.ListTest", 0.08);
    assertNameSimilarityApertureCoverage("upt.ac.cti.ListTest", 0.16);
  }

  @Test
  public void listTest2() {
    assertFlowInsensitiveApertureCoverage("upt.ac.cti.ListTest2", 0.2);
    assertNameSimilarityApertureCoverage("upt.ac.cti.ListTest2", 0.16);
  }

  @Test
  public void accessObjectTest() {
    assertFlowInsensitiveApertureCoverage("upt.ac.cti.AccessObjectTest", 0.12);
    assertNameSimilarityApertureCoverage("upt.ac.cti.AccessObjectTest", 0.16);
  }

  @Test
  public void thisApertureCoverageTest() {
    assertFlowInsensitiveApertureCoverage("upt.ac.cti.ThisApertureCoverageTest", 1. / 3.);
    assertNameSimilarityApertureCoverage("upt.ac.cti.ThisApertureCoverageTest", 0.16);
  }

  @Test
  public void simpleDerivationTest() {
    assertFlowInsensitiveApertureCoverage("upt.ac.cti.SimpleDerivationTest", 0.16);
    assertNameSimilarityApertureCoverage("upt.ac.cti.SimpleDerivationTest", 0.16);
  }

  @Test
  public void complexDerivationTest() {
    assertFlowInsensitiveApertureCoverage("upt.ac.cti.ComplexDerivationTest", 0.04);
    assertNameSimilarityApertureCoverage("upt.ac.cti.ComplexDerivationTest", 0.16);
  }

  @Test
  public void inheritanceTest() {
    assertFlowInsensitiveApertureCoverage("upt.ac.cti.InheritanceTest", 0.08);
    assertFlowInsensitiveApertureCoverage("upt.ac.cti.InheritanceTest1", 0.04);

    assertNameSimilarityApertureCoverage("upt.ac.cti.InheritanceTest", 0.16);
    assertNameSimilarityApertureCoverage("upt.ac.cti.InheritanceTest1", 0.16);
  }

  @Test
  public void inheritanceTest2() {
    assertFlowInsensitiveApertureCoverage("upt.ac.cti.InheritanceTest2", 0.12);
    assertFlowInsensitiveApertureCoverage("upt.ac.cti.InheritanceTest21", 0.08);

    assertNameSimilarityApertureCoverage("upt.ac.cti.InheritanceTest2", 0.16);
    assertNameSimilarityApertureCoverage("upt.ac.cti.InheritanceTest21", 0.16);
  }

  @Test
  public void constructorParametersTest() {
    assertFlowInsensitiveApertureCoverage("upt.ac.cti.ConstructorParametersTest", 1. / 3.);
    assertNameSimilarityApertureCoverage("upt.ac.cti.ConstructorParametersTest", 1. / 3.);
  }

  @Test
  public void partialDerivationTest() {
    assertFlowInsensitiveApertureCoverage("upt.ac.cti.PartialDerivationTest", 0.2);
    assertNameSimilarityApertureCoverage("upt.ac.cti.PartialDerivationTest", 0.16);
  }

  @Test
  public void partialDerivationTest2() {
    assertFlowInsensitiveApertureCoverage("upt.ac.cti.APartialDerivationTest2", 0.04);
    assertFlowInsensitiveApertureCoverage("upt.ac.cti.PartialDerivationTest2", 0.12);

    assertNameSimilarityApertureCoverage("upt.ac.cti.APartialDerivationTest2", 0.16);
    assertNameSimilarityApertureCoverage("upt.ac.cti.PartialDerivationTest2", 0.16);
  }

  private void assertFlowInsensitiveApertureCoverage(String className, Double expected) {
    var mClass = findClass(className);
    assertNotNull(mClass);
    assertEquals(expected, mClass.apertureCoverageFlowInsensitive());
  }

  private void assertNameSimilarityApertureCoverage(String className, Double expected) {
    var mClass = findClass(className);
    assertNotNull(mClass);
    assertEquals(expected, mClass.apertureCoverageNameSimilarity());
  }

  @Test
  public void pairingValidationTest() {
    var mClass = findClass("upt.ac.cti.PairingValidationTest");
    assertNotNull(mClass);

    var fieldAperturesExpected = List.of(3, 5, 5, 15, 15, 25);
    var fieldPairs = mClass.susceptibleFieldPairs().getElements();
    assertEquals(6, fieldPairs.size());

    var fieldApertures =
        new ArrayList<>(fieldPairs.stream()
            .map(MFieldPair::aperture)
            .toList());

    var fieldCoverageSum = fieldPairs.stream()
        .map(MFieldPair::coveredTypePairsFlowInsensitive)
        .collect(Collectors.summingInt(g -> g.getElements().size()));

    assertEquals(0, fieldCoverageSum);

    fieldApertures.sort(Comparator.naturalOrder());

    assertTrue(fieldAperturesExpected.equals(fieldApertures));

    var parameterAperturesExpected = List.of(5, 5, 25);
    var parameterPairs = mClass.susceptibleParameterPairs().getElements();
    assertEquals(3, parameterPairs.size());

    var parameterApertures =
        new ArrayList<>(parameterPairs.stream()
            .map(MParameterPair::aperture)
            .toList());

    var parameterCoverageSum = parameterPairs.stream()
        .map(MParameterPair::coveredTypePairsFlowInsensitive)
        .collect(Collectors.summingInt(g -> g.getElements().size()));

    assertEquals(0, parameterCoverageSum);

    parameterApertures.sort(Comparator.naturalOrder());

    assertTrue(parameterAperturesExpected.equals(parameterApertures));



  }
}
