package upt.ac.cti.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

public class Config {
  public static final String JOB_FAMILY = "export";

  public static int MAX_DEPTH_THRESHOLD = 3;
  public static int MIN_HIERARCHY_SIZE = 1;
  public static int CLASS_ANALYSIS_PARALLELISM = 4;
  public static int TOKENS_MAX_DIFF = 3;
  public static double TOKENS_THRESHOLD = 0.5;

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
    CLASS_ANALYSIS_PARALLELISM =
        Optional.ofNullable(config.getProperty("CLASS_ANALYSIS_PARALLELISM")).map(Integer::parseInt)
            .orElse(CLASS_ANALYSIS_PARALLELISM);
    TOKENS_MAX_DIFF =
        Optional.ofNullable(config.getProperty("TOKENS_MAX_DIFF")).map(Integer::parseInt)
            .orElse(TOKENS_MAX_DIFF);
    TOKENS_THRESHOLD =
        Optional.ofNullable(config.getProperty("TOKENS_THRESHOLD")).map(Double::parseDouble)
            .orElse(TOKENS_THRESHOLD);

  }

  public static Map<String, String> asStringsFlowInsensitive() {
    var map = new HashMap<String, String>();

    map.put("MAX_DEPTH_THRESHOLD", "" + MAX_DEPTH_THRESHOLD);
    map.put("MIN_HIERARCHY_SIZE", "" + MIN_HIERARCHY_SIZE);
    map.put("CLASS_ANALYSIS_PARALLELISM", "" + CLASS_ANALYSIS_PARALLELISM);

    return map;
  }

  public static Map<String, String> asStringsNameSimilarity() {
    var map = new HashMap<String, String>();

    map.put("TOKENS_MAX_DIFF", "" + TOKENS_MAX_DIFF);
    map.put("TOKENS_THRESHOLD", "" + TOKENS_THRESHOLD);
    map.put("MIN_HIERARCHY_SIZE", "" + MIN_HIERARCHY_SIZE);

    return map;
  }

}
