package upt.ac.cti.config;

import java.io.IOException;
import java.util.Properties;

public class CacheConfig {

  private final Properties properties;

  public Properties getProperties() {
    return properties;
  }

  public CacheConfig() {

    this.properties = new Properties();

    try {
      var fis = getClass().getClassLoader().getResourceAsStream("../resources/cache.ccf");

      properties.load(fis);

      fis.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
