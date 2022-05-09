package upt.ac.cti.util.cache;

import java.util.Optional;
import org.apache.commons.collections4.map.LRUMap;

public final class Cache<K, T> implements ICache<K, T> {

  private static final int DEFAULT_CACHE_SIZE = 1024;

  private final LRUMap<K, T> map;


  public Cache() {
    map = new LRUMap<>(DEFAULT_CACHE_SIZE);
  }

  public Cache(int size) {
    map = new LRUMap<>(size);
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
