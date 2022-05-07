package upt.ac.cti.util;

import java.util.Comparator;
import java.util.List;

public class ApertureCoverageUtil {

  public static Double combine(List<Double> apertureCoverages) {
    var none = apertureCoverages.isEmpty();
    var allNotResolved = apertureCoverages.stream().allMatch(d -> d < 0);

    if (none || allNotResolved) {
      return -1.;
    }

    return apertureCoverages.stream()
        .filter(d -> d >= 0)
        .min(Comparator.naturalOrder())
        .orElse(-1.);
  }

}
