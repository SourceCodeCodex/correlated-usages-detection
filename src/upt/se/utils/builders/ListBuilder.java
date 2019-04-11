package upt.se.utils.builders;

import java.util.Collection;
import java.util.List;

public class ListBuilder {

  public static <T> io.vavr.collection.List<T> toList(Collection<T> list) {
    return io.vavr.collection.List.ofAll(list);
  }

  public static <T> io.vavr.collection.List<T> toList(T[] array) {
    return io.vavr.collection.List.of(array);
  }
  
  public static <T> List<T> toList(io.vavr.collection.List<T> list) {
    return list.asJava();
  }
}
