package upt.ac.cti.util.cache;

import java.util.Optional;
import org.apache.commons.collections4.map.LRUMap;
import upt.ac.cti.config.Config;

public final class Cache<K, T> implements ICache<K, T> {

  private final LRUMap<K, T> map;


  public Cache() {
    map = new LRUMap<>(Config.MAX_CACHE_SIZE);
  }

  @Override
  public synchronized void put(K key, T value) {
    map.put(key, value);

  }

  @Override
  public Optional<T> get(K key) {
    T c;
    synchronized (this) {
      c = map.get(key);
    }

    if (c == null) {
      return Optional.empty();
    }

    return Optional.of(c);
  }



  @Override
  public synchronized void remove(K key) {
    map.remove(key);
  }


  @Override
  public synchronized void clear() {
    map.clear();
  }
}
