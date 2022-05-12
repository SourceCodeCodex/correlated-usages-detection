package upt.ac.cti.util.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.jcs3.JCS;
import org.apache.commons.jcs3.access.CacheAccess;

public final class Cache<K, T> implements ICache<K, T> {

  private final CacheAccess<K, T> cache;

  private static List<Cache<?, ?>> caches = new ArrayList<>();

  public static void clearAllCache() {
    caches.forEach(Cache::clear);
    caches = new ArrayList<>();
  }

  public Cache(CacheRegions region) {
    cache = JCS.getInstance(region.region);
    caches.add(this);
  }

  @Override
  public synchronized void put(K key, T value) {
    cache.put(key, value);
  }

  @Override
  public synchronized Optional<T> get(K key) {
    return Optional.ofNullable(cache.get(key));
  }


  @Override
  public synchronized void remove(K key) {
    cache.remove(key);
  }


  @Override
  public synchronized void clear() {
    cache.clear();
  }
}
