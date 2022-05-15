package upt.ac.cti.util.computation;

import java.util.Comparator;
import java.util.List;

public class ApertureCoverageUtil {

  public static Double combine(List<Double> apertureCoverages) {
    if (apertureCoverages.isEmpty() || apertureCoverages.stream().allMatch(d -> d < 0)) {
      return -1.;
    }

    if (apertureCoverages.stream().allMatch(d -> d == 0)) {
      return 0.;
    }

    return apertureCoverages.stream()
        .filter(d -> d > 0)
        .min(Comparator.naturalOrder())
        .orElse(0.0);
  }

}
