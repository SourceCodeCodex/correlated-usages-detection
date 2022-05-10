package upt.ac.cti.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;

public class Config {
  public static int MAX_DEPTH_THRESHOLD = 1;
  public static int MIN_HIERARCHY_SIZE = 0;
  public static int MAX_CACHE_SIZE = 256;

  public static Map<String, String> asStrings() {
    var map = new HashMap<String, String>();

    map.put("MAX_DEPTH_THRESHOLD", "" + MAX_DEPTH_THRESHOLD);
    map.put("MIN_HIERARCHY_SIZE", "" + MIN_HIERARCHY_SIZE);
    map.put("MAX_CACHE_SIZE", "" + MAX_CACHE_SIZE);

    return map;
  }

  public static void readConfigFile() {

    try {
      var url = Platform.getBundle("FamilyPolymorphismDetection").getEntry("/");
      url = FileLocator.resolve(url);

      var configFile = url.getPath() + "res/config.properties";

      var fis = new FileInputStream(configFile);
      var config = new Properties();
      config.load(fis);

      MAX_DEPTH_THRESHOLD = Integer.parseInt(config.getProperty("MAX_DEPTH_THRESHOLD"));
      MIN_HIERARCHY_SIZE = Integer.parseInt(config.getProperty("MIN_HIERARCHY_SIZE"));
      MAX_CACHE_SIZE = Integer.parseInt(config.getProperty("MAX_CACHE_SIZE"));

      fis.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
