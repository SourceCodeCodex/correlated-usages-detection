package upt.ac.cti.analysis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IType;
import familypolymorphismdetection.metamodel.entity.MClass;
import ro.lrg.xcore.metametamodel.Group;

public class ProjectReport {

  private final Map<String, Double> report = new HashMap<>();
  private final String projectName;

  public ProjectReport(String projectName, Group<MClass> group) {
    this.projectName = projectName;
    group.getElements()
        .forEach(
            mClass -> report.put(((IType) mClass.getUnderlyingObject()).getFullyQualifiedName(),
                mClass.apertureCoverage()));
  }

  public void writeReport() throws IOException {
    var url = Platform.getBundle("FamilyPolymorphismDetection").getEntry("/");
    url = FileLocator.resolve(url);
    var path = url.getPath() + "res/reports/";
    var file = new File(path + projectName + ".txt");
    var writer = new FileWriter(file);
    if (!file.createNewFile()) {
      if (!file.delete()) {
        writer.close();
        throw new IOException();
      }
    }
    report.forEach((name, index) -> {
      try {
        writer.write("" + name + " => " + index + "\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    writer.close();
  }

}
