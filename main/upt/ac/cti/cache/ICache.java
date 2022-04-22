package upt.ac.cti.cache;

import java.util.Optional;


public interface ICache<K, T> {

  void put(K key, T value);

  Optional<T> get(K key);

  void remove(K key);

  void clear();

}
