package upt.ac.cti.util.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.javatuples.Pair;
import upt.ac.cti.config.Config;
import upt.ac.cti.util.time.StopWatch;

public class ReportUtil {

  public static void createReport(String projectName, Stream<Pair<String, Double>> results,
      Config config) {
    var formatter = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
    var date = new Date();
    var timestamp = formatter.format(date);

    try {
      var stopWatch = new StopWatch();
      stopWatch.start();
      var url = Platform.getBundle("FamilyPolymorphismDetection").getEntry("/");
      url = FileLocator.resolve(url);

      var reportsPathFragment = url.getPath() + "reports/" + projectName + "-" + timestamp;
      var resultsPath = reportsPathFragment + "-results.csv";
      var configPath = reportsPathFragment + "-config.csv";
      var zipPath = reportsPathFragment + ".zip";

      var rOut = new FileWriter(resultsPath);
      var cOut = new FileWriter(configPath);

      var rFormat = CSVFormat.Builder.create()
          .setHeader("Class", "Aperture Coverage")
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
            synchronized (rPrinter) {
              rPrinter.printRecord(p.getValue0(), p.getValue1());
            }
          } catch (IOException e) {
            e.printStackTrace();
          }
        });

        config.asStrings().entrySet().stream()
            .forEach(e -> {
              try {
                cPrinter.printRecord(e.getKey(), e.getValue());
              } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
              }
            });

        stopWatch.stop();
        cPrinter.printRecord("", "");
        cPrinter.printRecord("DURATION", stopWatch.getDuration().toSeconds() + " seconds");

        cPrinter.flush();
        rPrinter.flush();

        var zipOutStream = new FileOutputStream(zipPath);
        var zipOut = new ZipOutputStream(zipOutStream);

        for (String path : List.of(resultsPath, configPath)) {
          var file = new File(path);
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
}


