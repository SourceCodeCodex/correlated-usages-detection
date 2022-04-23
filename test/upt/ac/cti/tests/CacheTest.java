package upt.ac.cti.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Optional;
import org.junit.Test;
import upt.ac.cti.util.cache.Cache;

public class CacheTest {

  private final Cache<Object, Object> cache = new Cache<>();

  @Test
  public void putGetTest() {
    cache.put(1, 2);

    assertEquals(Optional.of(2), cache.get(1));
  }

  @Test
  public void removeItemTest() {
    cache.put(1, 2);

    assertEquals(Optional.of(2), cache.get(1));

    cache.remove(1);

    assertEquals(Optional.empty(), cache.get(1));
  }

  @Test
  public void clearCacheTest() {
    cache.put(1, 2);
    cache.put(10, 20);

    assertEquals(Optional.of(2), cache.get(1));
    assertEquals(Optional.of(20), cache.get(10));

    cache.clear();

    assertEquals(Optional.empty(), cache.get(1));
    assertEquals(Optional.empty(), cache.get(10));


  }
}
