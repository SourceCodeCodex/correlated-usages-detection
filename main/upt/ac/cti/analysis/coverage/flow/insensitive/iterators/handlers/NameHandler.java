package upt.ac.cti.analysis.coverage.flow.insensitive.iterators.handlers;

import java.util.logging.Logger;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.Name;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.DerivationResult;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.CPIndex;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.CorelationPair;

final class NameHandler extends RightSideHandler {

  private final Logger logger = Logger.getGlobal();

  public NameHandler(CorelationPair cp, CPIndex index) {
    super(cp, index);
  }

  @Override
  public DerivationResult handle() {
    var name = (Name) cp.fieldAsgmt(index).rightSide();
    var binding = name.resolveBinding();

    var varBinding = (IVariableBinding) binding;

    if (varBinding.isParameter()) {
      logger.info("Handle parameter " + binding.getName());

      var ph = new NameParameterHandler(cp, index, (ILocalVariable) binding.getJavaElement());
      return ph.handle();
    }

    if (varBinding.isField()) {
      logger.info("Handle field " + binding.getName());

      var fh = new NameFieldHandler(cp, index, (IField) binding.getJavaElement());
      return fh.handle();
    }

    logger.info("Handle local var " + binding.getName());

    var lvh = new NameLocalVarHandler(cp, index, (ILocalVariable) binding.getJavaElement());
    return lvh.handle();
  }

}


