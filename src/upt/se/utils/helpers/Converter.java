package upt.se.utils.helpers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ITypeBinding;
import io.vavr.collection.List;
import io.vavr.control.Option;
import upt.se.utils.visitors.ITypeConverter;

public final class Converter {

  public static final Option<ITypeBinding> convert(IType type) {
    return ITypeConverter.convert(type);
  }

  public static final Option<List<ITypeBinding>> convert(List<IType> types) {
    System.out.println(types.filter(type -> type.getCompilationUnit() == null).mkString("\n"));
    return types.foldLeft(Option.some(List.empty()),
        (res, type) -> convert(type).map(typeBinding -> res.get().append(typeBinding)));
  }

  public static double round(double value, int places) {
    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }
}
