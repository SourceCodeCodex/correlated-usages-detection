package upt.se.utils.helpers;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class Converter {

  public static double round(double value, int places) {
    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }  
  
}
