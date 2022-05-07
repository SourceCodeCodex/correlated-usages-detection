package upt.ac.cti.util;

import java.util.Collection;
import java.util.List;
import org.javatuples.Pair;

public class CartesianProduct {
  public static <T, C extends Collection<T>> List<Pair<T, T>> product(C s1, C s2) {
    return s1.stream().flatMap(it -> s2.stream().map(el -> Pair.with(it, el))).toList();
  }
}
