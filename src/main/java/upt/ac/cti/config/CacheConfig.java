package upt.ac.cti.config;

import java.io.IOException;
import java.util.Properties;

public class CacheConfig {

  private static final Properties properties = new Properties();

  private CacheConfig() {

  }

  public static Properties getProperties() {
    return properties;
  }

  public static void init() {

    try {
      var fis = CacheConfig.class.getClassLoader().getResourceAsStream("../resources/cache.ccf");

      properties.load(fis);

      fis.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
