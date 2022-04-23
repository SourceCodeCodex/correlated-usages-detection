package upt.ac.cti.analysis.coverage.flow.insensitive.derivator;

import java.util.concurrent.Callable;
import java.util.logging.Logger;
import org.eclipse.jdt.core.dom.Expression;
import org.javatuples.Pair;
import upt.ac.cti.analysis.coverage.flow.insensitive.derivator.internal.FieldWritingsDerivator;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.DerivationResult;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.FieldWriting;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.NewWritingPairs;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.ResolvedBindings;
import upt.ac.cti.analysis.coverage.flow.insensitive.util.FieldWritingBindingResolver;

class DerivationJob implements Callable<DerivationResult> {

  private final Pair<FieldWriting, FieldWriting> writingPair;

  private final FieldWritingBindingResolver assignmentBindingResolver;
  private final FieldWritingsDerivator derivator;

  private static final Logger logger = Logger.getLogger(DerivationJob.class.getSimpleName());

  public DerivationJob(
      FieldWritingBindingResolver assignmentBindingResolver,
      FieldWritingsDerivator derivator,
      Pair<FieldWriting, FieldWriting> writingPair) {
    this.assignmentBindingResolver = assignmentBindingResolver;
    this.derivator = derivator;
    this.writingPair = writingPair;
  }

  @Override
  public DerivationResult call() throws Exception {

    // Check if writing expressions are bound to a concrete type which is a leaf in a type hierarchy
    var f1Binding =
        assignmentBindingResolver.resolveBinding(writingPair.getValue0());

    var f2Binding =
        assignmentBindingResolver.resolveBinding(writingPair.getValue1());

    // Stop condition 1: both have leaf bindings
    if (f1Binding.isPresent() && f2Binding.isPresent()) {
      var accessExpr1 = writingPair.getValue0().accessExpression();
      var accessExpr2 = writingPair.getValue1().accessExpression();

      var areAccessingExpressionsEqual = accessExpr1.equals(accessExpr2);

      var areAccessingExpressionsBindingsEqual = accessExpr1.map(Expression::resolveTypeBinding)
          .equals(accessExpr2.map(Expression::resolveTypeBinding));

      if (areAccessingExpressionsEqual || areAccessingExpressionsBindingsEqual) {
        return new ResolvedBindings(Pair.with(f1Binding.get(), f2Binding.get()));
      }

      return NewWritingPairs.NULL;
    }


    if (f1Binding.isEmpty()) {
      return derivator.derive(writingPair.getValue0(), writingPair.getValue1());
    }

    if (f2Binding.isEmpty()) {
      return derivator.derive(writingPair.getValue1(), writingPair.getValue0());
    }

    logger.severe(
        "The program ran into an inconsistent state! Check the DerivationJob: " + writingPair);
    return NewWritingPairs.NULL;
  }

}
