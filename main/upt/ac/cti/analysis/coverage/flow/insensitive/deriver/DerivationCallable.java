package upt.ac.cti.analysis.coverage.flow.insensitive.deriver;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.logging.Logger;
import org.eclipse.jdt.core.dom.Expression;
import org.javatuples.Pair;
import upt.ac.cti.analysis.coverage.flow.insensitive.iterators.handlers.CPFieldHandler;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.CPIndex;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.DerivationResult;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.FieldWriting;
import upt.ac.cti.analysis.coverage.flow.insensitive.util.FieldWritingBindingResolver;

class DerivationCallable implements Callable<DerivationResult> {

  private final Pair<FieldWriting, FieldWriting> writingPair;

  private static int counter = 0;

  private final DerivationValidator derivationValidator = new DerivationValidator();

  private final FieldWritingBindingResolver assignmentBindingResolver;

  private static final Logger logger =
      Logger.getLogger(DerivationCallable.class.getSimpleName() + "_" + counter);

  public DerivationCallable(FieldWritingBindingResolver assignmentBindingResolver,
      Pair<FieldWriting, FieldWriting> writingPair) {
    this.assignmentBindingResolver = assignmentBindingResolver;
    this.writingPair = writingPair;
    counter++;
  }

  @Override
  public DerivationResult call() throws Exception {
    // Force discarding particular derivations as the binding cannot be assured in current
    // implementation

    if (!derivationValidator.isValid(writingPair)) {
      logger.info("Writing pair discarded: " + writingPair);

      return DerivationResult.NULL;
    }

    // Check if writing expressions are bound to a concrete type which is a leaf in a type hierarchy
    var f1Binding =
        assignmentBindingResolver.resolveHierarchyLeafBinding(writingPair.getValue0());

    var f2Binding =
        assignmentBindingResolver.resolveHierarchyLeafBinding(writingPair.getValue1());

    // Stop condition 1: both have leaf bindings
    if (f1Binding.isPresent() && f2Binding.isPresent()) {
      var accessExpr1 = writingPair.getValue0().accessExpression();
      var accessExpr2 = writingPair.getValue1().accessExpression();

      var areAccessingExpressionsEqual = accessExpr1.equals(accessExpr2);

      var areAccessingExpressionsBindingsEqual = accessExpr1.map(Expression::resolveTypeBinding)
          .equals(accessExpr2.map(Expression::resolveTypeBinding));

      if (areAccessingExpressionsEqual || areAccessingExpressionsBindingsEqual) {
        logger.info("Both field writing bindings resolved: " + f1Binding.get().getName() + " & "
            + f2Binding.get().getName());

        return new DerivationResult(List.of(), Optional
            .of(Pair.with(f1Binding.get(), f2Binding.get())));
      }

      return DerivationResult.NULL;
    }


    // If field 1 is not resolved, then expand
    if (f1Binding.isEmpty()) {
      logger.info("Handling field1: " + pair);
      var updatedBinding = pair.withFieldBinding(f2Binding, CPIndex.SECOND);
      return new CPFieldHandler(updatedBinding, CPIndex.FIRST).handle();
    }

    // If field 2 is not resolved, then expand
    if (f2Binding.isEmpty()) {
      logger.info("Handling field2: " + pair);
      var updatedBinding = pair.withFieldBinding(f1Binding, CPIndex.FIRST);
      return new CPFieldHandler(updatedBinding, CPIndex.SECOND).handle();
    }

    logger.warning("Never should the code arrive here: " + pair);
    return new DerivationResult(List.of(), Optional.empty());
  }

}
