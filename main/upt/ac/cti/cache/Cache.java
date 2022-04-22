package upt.ac.cti.cache;


import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class Cache<K, T> implements ICache<K, T> {

  private final ConcurrentHashMap<K, T> map = new ConcurrentHashMap<>();


  public Cache() {

  }

  @Override
  public void put(K key, T value) {
    map.put(key, value);

  }

  @Override
  public Optional<T> get(K key) {

    var c = map.get(key);

    if (c == null) {
      return Optional.empty();
    }

    return Optional.of(c);
  }



  @Override
  public void remove(K key) {
    map.remove(key);
  }


  @Override
  public void clear() {
    map.clear();
  }
}
