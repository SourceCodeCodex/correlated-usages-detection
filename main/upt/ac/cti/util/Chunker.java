package upt.ac.cti.util;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Chunker {

  public static <T> Stream<List<T>> chunked(Stream<T> stream, int chunkSize) {
    var index = new AtomicInteger(0);

    return stream.collect(Collectors.groupingBy(x -> index.getAndIncrement() / chunkSize))
        .entrySet().stream()
        .sorted(Map.Entry.comparingByKey()).map(Map.Entry::getValue);
  }

}
