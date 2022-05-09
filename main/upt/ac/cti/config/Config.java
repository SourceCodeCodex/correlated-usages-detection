package upt.ac.cti.config;

import java.util.HashMap;
import java.util.Map;

public class Config {
  public static final int MAX_DEPTH_THRESHOLD = 1;
  public static final int MIN_HIERARCHY_SIZE = 1;

  public static Map<String, String> asStrings() {
    var map = new HashMap<String, String>();

    map.put("MAX_DEPTH_THRESHOLD", "" + MAX_DEPTH_THRESHOLD);
    map.put("MIN_HIERARCHY_SIZE", "" + MIN_HIERARCHY_SIZE);

    return map;

  }

}
