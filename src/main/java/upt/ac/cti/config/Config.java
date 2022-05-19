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
  public static int APERTURE_COVERAGE_TIMEOUT_SECOUNDS = 180;
  public static int CLASS_ANALYSIS_POOL_SIZE = 8;
  public static int DERIVATION_POOL_SIZE = 16;
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
    APERTURE_COVERAGE_TIMEOUT_SECOUNDS =
        Optional.ofNullable(config.getProperty("APERTURE_COVERAGE_TIMEOUT_SECOUNDS"))
            .map(Integer::parseInt)
            .orElse(APERTURE_COVERAGE_TIMEOUT_SECOUNDS);
    CLASS_ANALYSIS_POOL_SIZE =
        Optional.ofNullable(config.getProperty("CLASS_ANALYSIS_POOL_SIZE")).map(Integer::parseInt)
            .orElse(CLASS_ANALYSIS_POOL_SIZE);
    DERIVATION_POOL_SIZE =
        Optional.ofNullable(config.getProperty("DERIVATION_POOL_SIZE")).map(Integer::parseInt)
            .orElse(DERIVATION_POOL_SIZE);
    TOKENS_MAX_DIFF =
        Optional.ofNullable(config.getProperty("TOKENS_MAX_DIFF")).map(Integer::parseInt)
            .orElse(TOKENS_MAX_DIFF);
    TOKENS_THRESHOLD =
        Optional.ofNullable(config.getProperty("TOKENS_THRESHOLD")).map(Double::parseDouble)
            .orElse(TOKENS_THRESHOLD);

  }

  private static void addCommon(Map<String, String> map) {
    map.put("MIN_HIERARCHY_SIZE", "" + MIN_HIERARCHY_SIZE);
    map.put("APERTURE_COVERAGE_TIMEOUT_SECOUNDS", "" + APERTURE_COVERAGE_TIMEOUT_SECOUNDS);
  }

  public static Map<String, String> asStringsFlowInsensitive() {
    var map = new HashMap<String, String>();

    addCommon(map);

    map.put("MAX_DEPTH_THRESHOLD", "" + MAX_DEPTH_THRESHOLD);

    return map;
  }

  public static Map<String, String> asStringsNameSimilarity() {
    var map = new HashMap<String, String>();

    addCommon(map);

    map.put("TOKENS_MAX_DIFF", "" + TOKENS_MAX_DIFF);
    map.put("TOKENS_THRESHOLD", "" + TOKENS_THRESHOLD);

    return map;
  }

}
