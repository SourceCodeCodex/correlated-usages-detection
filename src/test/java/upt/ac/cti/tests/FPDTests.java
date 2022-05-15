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
import java.util.stream.Stream;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.junit.Test;
import familypolymorphismdetection.metamodel.entity.MFieldPair;
import familypolymorphismdetection.metamodel.entity.MParameterPair;
import upt.ac.cti.core.project.action.ExportReport;

public class FPDTests {

  @Test
  public void susceptribleClassesTest() {
    assertEquals(17, FPDTestSuite.countSusceptibleClasses());
  }

  @Test
  public void exportReportTest() throws IOException {
    ExportReport.BLOCKING = true;
    FPDTestSuite.getProject().exportReport();
    ExportReport.BLOCKING = false;
    var url = Platform.getBundle("FamilyPolymorphismDetection").getEntry("/");
    var path = FileLocator.resolve(url).getPath() + "target/";

    var folder = new File(path);

    assertTrue(Stream.of(folder.listFiles()).anyMatch(f -> f.getName().endsWith(".zip")));
  }

  @Test
  public void downcastTest() {
    assertApertureCoverage("upt.ac.cti.DowncastTest", 1. / 6.);
  }

  @Test
  public void inconclusiveMethodInvocationInitTest() {
    assertApertureCoverage("upt.ac.cti.InconclusiveMethodInvocationInitTest", 0.2);
  }

  @Test
  public void listTest() {
    assertApertureCoverage("upt.ac.cti.ListTest", 0.08);
  }

  @Test
  public void listTest2() {
    assertApertureCoverage("upt.ac.cti.ListTest2", 0.2);
  }

  @Test
  public void accessObjectTest() {
    assertApertureCoverage("upt.ac.cti.AccessObjectTest", 0.12);
  }

  @Test
  public void thisApertureCoverageTest() {
    assertApertureCoverage("upt.ac.cti.ThisApertureCoverageTest", 1. / 3.);
  }

  @Test
  public void simpleDerivationTest() {
    assertApertureCoverage("upt.ac.cti.SimpleDerivationTest", 0.16);
  }

  @Test
  public void complexDerivationTest() {
    assertApertureCoverage("upt.ac.cti.ComplexDerivationTest", 0.04);
  }

  @Test
  public void inheritanceTest() {
    assertApertureCoverage("upt.ac.cti.InheritanceTest", 0.08);
    assertApertureCoverage("upt.ac.cti.InheritanceTest1", 0.04);
  }

  @Test
  public void inheritanceTest2() {
    assertApertureCoverage("upt.ac.cti.InheritanceTest2", 0.12);
    assertApertureCoverage("upt.ac.cti.InheritanceTest21", 0.08);
  }

  @Test
  public void constructorParametersTest() {
    assertApertureCoverage("upt.ac.cti.ConstructorParametersTest", 1. / 3.);
  }

  @Test
  public void partialDerivationTest() {
    assertApertureCoverage("upt.ac.cti.PartialDerivationTest", 0.2);
  }

  @Test
  public void partialDerivationTest2() {
    assertApertureCoverage("upt.ac.cti.APartialDerivationTest2", 0.04);
    assertApertureCoverage("upt.ac.cti.PartialDerivationTest2", 0.12);
  }

  private void assertApertureCoverage(String className, Double expected) {
    var mClass = findClass(className);
    assertNotNull(mClass);
    assertEquals(expected, mClass.apertureCoverage());
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
        .map(MFieldPair::coveredTypePairs)
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
        .map(MParameterPair::coveredTypePairs)
        .collect(Collectors.summingInt(g -> g.getElements().size()));

    assertEquals(0, parameterCoverageSum);

    parameterApertures.sort(Comparator.naturalOrder());

    assertTrue(parameterAperturesExpected.equals(parameterApertures));



  }
}
