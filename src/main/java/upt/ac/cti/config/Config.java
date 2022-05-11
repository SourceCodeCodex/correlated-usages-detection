package upt.ac.cti.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;

public class Config {
  public final int MAX_DEPTH_THRESHOLD;
  public final int MIN_HIERARCHY_SIZE;
  public final int MAX_CACHE_SIZE;


  public Config() {

    var config = new Properties();

    try {
      var url = Platform.getBundle("FamilyPolymorphismDetection").getEntry("/");
      url = FileLocator.resolve(url);

      var configFile = url.getPath() + "res/config.properties";

      var fis = new FileInputStream(configFile);

      config.load(fis);

      fis.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    MAX_DEPTH_THRESHOLD = Integer
        .parseInt(Optional.ofNullable(config.getProperty("MAX_DEPTH_THRESHOLD")).orElse("1"));
    MIN_HIERARCHY_SIZE = Integer
        .parseInt(Optional.ofNullable(config.getProperty("MIN_HIERARCHY_SIZE")).orElse("1"));
    MAX_CACHE_SIZE =
        Integer.parseInt(Optional.ofNullable(config.getProperty("MAX_CACHE_SIZE")).orElse("256"));

  }

  public Map<String, String> asStrings() {
    var map = new HashMap<String, String>();

    map.put("MAX_DEPTH_THRESHOLD", "" + MAX_DEPTH_THRESHOLD);
    map.put("MIN_HIERARCHY_SIZE", "" + MIN_HIERARCHY_SIZE);
    map.put("MAX_CACHE_SIZE", "" + MAX_CACHE_SIZE);

    return map;
  }


}
