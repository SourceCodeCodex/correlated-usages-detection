package upt.se.utils.helpers;

import static upt.se.utils.helpers.LoggerHelper.LOGGER;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.logging.Level;
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
    return types.foldLeft(Option.some(List.empty()),
        (res, type) -> convert(type).onEmpty(() -> LOGGER.log(Level.SEVERE,
            "An error occurred while trying to get all the parameters for: "
                + type.getFullyQualifiedName()))
        .map(typeBinding -> res.get().append(typeBinding)));
  }

  public static double round(double value, int places) {
    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }
}
