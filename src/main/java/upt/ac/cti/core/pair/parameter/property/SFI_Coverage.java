package upt.ac.cti.core.pair.parameter.property;

import org.eclipse.jdt.core.ILocalVariable;
import familypolymorphismdetection.metamodel.entity.MParameterPair;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import upt.ac.cti.coverage.ICoveredTypesResolver;
import upt.ac.cti.dependency.Dependencies;

@PropertyComputer
public final class SFI_Coverage extends Coverage
    implements IPropertyComputer<Integer, MParameterPair> {

  @Override
  protected ICoveredTypesResolver<ILocalVariable> resolver() {
    return Dependencies.squanderingFlowInsensitiveParameterCoveredTypesResolver;
  }


}
