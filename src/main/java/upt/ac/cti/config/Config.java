package upt.ac.cti.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

public class Config {
  public static int MAX_DEPTH_THRESHOLD = 1;
  public static int MIN_HIERARCHY_SIZE = 1;
  public static int MAX_DEPTH_DIFF = 2;
  public static int CLASS_ANALYSIS_PARALLELISM = 4;

  private Config() {

  }

  public static void init() {

    var config = new Properties();

    try {
      var fis = Config.class.getClassLoader().getResourceAsStream("../resources/config.properties");

      config.load(fis);

      fis.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    MAX_DEPTH_THRESHOLD =
        Optional.ofNullable(config.getProperty("MAX_DEPTH_THRESHOLD")).map(Integer::parseInt)
            .orElse(MAX_DEPTH_THRESHOLD);
    MIN_HIERARCHY_SIZE =
        Optional.ofNullable(config.getProperty("MIN_HIERARCHY_SIZE")).map(Integer::parseInt)
            .orElse(MIN_HIERARCHY_SIZE);
    MAX_DEPTH_DIFF =
        Optional.ofNullable(config.getProperty("MAX_DEPTH_DIFF")).map(Integer::parseInt)
            .orElse(MAX_DEPTH_DIFF);
    CLASS_ANALYSIS_PARALLELISM =
        Optional.ofNullable(config.getProperty("CLASS_ANALYSIS_PARALLELISM")).map(Integer::parseInt)
            .orElse(CLASS_ANALYSIS_PARALLELISM);

  }

  public static Map<String, String> asStrings() {
    var map = new HashMap<String, String>();

    map.put("MAX_DEPTH_THRESHOLD", "" + MAX_DEPTH_THRESHOLD);
    map.put("MIN_HIERARCHY_SIZE", "" + MIN_HIERARCHY_SIZE);
    map.put("MAX_DEPTH_DIFF", "" + MAX_DEPTH_DIFF);
    map.put("CLASS_ANALYSIS_PARALLELISM", "" + CLASS_ANALYSIS_PARALLELISM);

    return map;
  }

}
