package upt.se.utils.builders;

import static upt.se.utils.helpers.ClassNames.isEqual;
import java.util.Collection;
import java.util.List;
import org.eclipse.jdt.core.dom.ITypeBinding;
import io.vavr.Tuple;

public class ListBuilder {

  public static <T> io.vavr.collection.List<T> toList(Collection<T> list) {
    return io.vavr.collection.List.ofAll(list);
  }

  public static <T> io.vavr.collection.List<T> toList(T[] array) {
    return io.vavr.collection.List.of(array);
  }

  public static List<ITypeBinding> diff(List<ITypeBinding> l1, List<ITypeBinding> l2) {
    return Tuple.of(toList(l1), toList(l2))
          .map((types1, types2) -> Tuple.of(types1.filter(type1 -> types2.find(type2 -> isEqual(type1, type2))
                                                                          .isEmpty()), 
                                            types2))
          .map1(list -> list.asJava())
          ._1;
  }
}
