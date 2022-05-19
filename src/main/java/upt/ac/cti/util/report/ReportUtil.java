package upt.ac.cti.util.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.javatuples.Pair;
import upt.ac.cti.config.Config;
import upt.ac.cti.coverage.CoverageStrategy;

public class ReportUtil {

  /**
   * Used only for testing purposes.
   */
  public static volatile boolean USE_TEST_PATH = false;
  public static final String TEST_DIR_NAME = "test_zip";

  private ReportUtil() {

  }

  public static final MutexRule MUTEX_RULE = new MutexRule();

  public static void createReport(
      String projectName,
      Set<CoverageStrategy> strategies,
      Stream<Pair<String, Stream<Double>>> results) {
    var formatter = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
    var date = new Date();
    var timestamp = formatter.format(date);

    try {
      var url = Platform.getBundle("FamilyPolymorphismDetection").getEntry("/");
      url = FileLocator.resolve(url);

      var dirPath = url.getPath() + "target/" + (USE_TEST_PATH ? TEST_DIR_NAME + "/" : "");
      var dir = new File(dirPath);
      dir.mkdirs();

      var reportsPathFragment = dirPath + projectName + "-"
          + String.join("-", strategies.stream().map(s -> s.name).toList())
          + "-" + timestamp;
      var resultsPath = reportsPathFragment + "-results.csv";
      var configPath = reportsPathFragment + "-config.csv";
      var zipPath = reportsPathFragment + ".zip";

      var rFile = new File(resultsPath);
      rFile.createNewFile();

      var cFile = new File(configPath);
      cFile.createNewFile();

      var rOut = new FileWriter(rFile);
      var cOut = new FileWriter(cFile);

      var rHeader = new ArrayList<>(strategies.stream().map(s -> s.name).toList());
      rHeader.add(0, "Class");

      var rFormat = CSVFormat.Builder.create()
          .setHeader(rHeader.toArray(new String[rHeader.size()]))
          .setSkipHeaderRecord(false)
          .build();

      var cFormat = CSVFormat.Builder.create()
          .setHeader("Config", "Value")
          .setSkipHeaderRecord(false)
          .build();



      try (var rPrinter = new CSVPrinter(rOut, rFormat);
          var cPrinter = new CSVPrinter(cOut, cFormat)) {

        results.forEach(p -> {
          try {
            var entry = new ArrayList<Object>(p.getValue1().toList());
            entry.add(0, p.getValue0());
            synchronized (rPrinter) {
              rPrinter.printRecord(entry);
            }
          } catch (IOException e) {
            e.printStackTrace();
          }
        });

        Map<String, String> configs = new HashMap<>();
        if (strategies.contains(CoverageStrategy.CONSERVING_FLOW_INSENSITIVE)
            || strategies.contains(CoverageStrategy.SQUANDERING_FLOW_INSENSITIVE)) {
          configs.putAll(Config.asStringsFlowInsensitive());
        }

        if (strategies.contains(CoverageStrategy.CONSERVING_NAME_SIMILARITY)
            || strategies.contains(CoverageStrategy.SQUANDERING_NAME_SIMILARITY)) {
          configs.putAll(Config.asStringsNameSimilarity());
        }

        configs.entrySet().stream()
            .forEach(e -> {
              try {
                cPrinter.printRecord(e.getKey(), e.getValue());
              } catch (IOException e1) {
                e1.printStackTrace();
              }
            });

        cPrinter.flush();
        rPrinter.flush();
        rPrinter.close();
        cPrinter.close();
        rOut.close();
        cOut.close();

        var zipOutStream = new FileOutputStream(zipPath);
        var zipOut = new ZipOutputStream(zipOutStream);

        for (File file : List.of(rFile, cFile)) {
          var stream = new FileInputStream(file);
          var zipEntry = new ZipEntry(file.getName());
          zipOut.putNextEntry(zipEntry);

          var bytes = new byte[1024];
          int length;
          while ((length = stream.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
          }
          stream.close();
          file.delete();
        }


        zipOut.close();
        zipOutStream.close();
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public static void diplayNotPermittedBox() {
    var box = new MessageBox(Display.getCurrent().getActiveShell(), SWT.CANCEL | SWT.OK);
    box.setText("Export Aperture Coverage Report: Operation not permited");
    box.setMessage(
        "There is an export ongoing, which is a highly demanding in terms of memory. The operation you currently requested was canceled. Wait for the export to finish, or cancel it.");

    box.open();
  }
}


