package upt.ac.cti.util.cache;

import java.util.Optional;
import org.apache.commons.jcs3.JCS;
import org.apache.commons.jcs3.access.CacheAccess;

public final class Cache<K, T> implements ICache<K, T> {

  private final CacheAccess<K, T> cache;


  public Cache(CacheRegions region) {
    cache = JCS.getInstance(region.region);
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
