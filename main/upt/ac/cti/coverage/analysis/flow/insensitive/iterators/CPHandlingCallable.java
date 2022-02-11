package upt.ac.cti.coverage.analysis.flow.insensitive.iterators;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.logging.Logger;
import org.javatuples.Pair;
import upt.ac.cti.coverage.analysis.flow.insensitive.iterators.handlers.CPFieldHandler;
import upt.ac.cti.coverage.analysis.flow.insensitive.model.CPHandlingResult;
import upt.ac.cti.coverage.analysis.flow.insensitive.model.CPIndex;
import upt.ac.cti.coverage.analysis.flow.insensitive.model.CorelationPair;
import upt.ac.cti.utils.resolvers.AsgmtBindingResolver;

class CPHandlingCallable implements Callable<CPHandlingResult> {
  private final CorelationPair pair;
  private static int counter = 0;
  private final Logger logger = Logger.getLogger(this.getClass().getSimpleName() + "_" + counter);

  public CPHandlingCallable(CorelationPair pair) {
    this.pair = pair;
    counter++;
  }

  @Override
  public CPHandlingResult call() throws Exception {
    // Throw away pairs that are incompatible with the targeted pattern

    if (CPHandlingInvalidator.isInvalid(pair)) {
      logger.info("Pair invalidated: " + pair);

      return new CPHandlingResult(List.of(), Optional.empty());
    }

    // Check if current right sides are bound to a concrete type which is a leaf in the hierarchy
    // Also cache bindings if already found
    var f1Binding = pair.field1Binding().isPresent() ? pair.field1Binding()
        : AsgmtBindingResolver.instance().resolveConcreteBinding(pair.field1Asgmt());

    var f2Binding = pair.field2Binding().isPresent() ? pair.field2Binding()
        : AsgmtBindingResolver.instance().resolveConcreteBinding(pair.field2Asgmt());

    // Stop condition 1: both have leaf bindings
    if (f1Binding.isPresent() && f2Binding.isPresent()) {
      // Base object differs
      if (pair.field1Asgmt().baseObject().equals(pair.field2Asgmt().baseObject())) {
        logger.info("Both bindings resolved: " + f1Binding.get().getName() + " & "
            + f2Binding.get().getName());

        return new CPHandlingResult(List.of(), Optional
            .of(Pair.with(f1Binding.get(), f2Binding.get())));
      }

      return new CPHandlingResult(List.of(), Optional.empty());
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
    return new CPHandlingResult(List.of(), Optional.empty());
  }

}
