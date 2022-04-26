package upt.ac.cti.analysis.coverage.flow.insensitive.derivator.internal.algorithm;

import java.util.Optional;
import org.eclipse.jdt.core.IField;
import org.javatuples.Pair;
import upt.ac.cti.analysis.coverage.flow.insensitive.derivator.internal.visitors.FieldAssignmentVisitor;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.DerivationResult;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.FieldWriting;
import upt.ac.cti.analysis.coverage.flow.insensitive.model.NewWritingPairs;
import upt.ac.cti.analysis.coverage.flow.insensitive.parser.CodeParser;
import upt.ac.cti.analysis.coverage.flow.insensitive.searcher.JavaEntitySearcher;

public final class FieldDerivatorAlgorithm {

  private final JavaEntitySearcher javaEntitySearcher;
  private final CodeParser codeParser;

  public FieldDerivatorAlgorithm(JavaEntitySearcher javaEntitySearcher, CodeParser codeParser) {
    this.javaEntitySearcher = javaEntitySearcher;
    this.codeParser = codeParser;
  }

  public DerivationResult derive(FieldWriting deriver, FieldWriting constant, IField field) {

    var writingMethods = javaEntitySearcher.searchFieldWritings(field);

    var derivations = writingMethods.stream()
        .map(method -> codeParser.parse(method))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .flatMap(methodDeclaration -> {
          var visitor = new FieldAssignmentVisitor(deriver, field);
          var node = methodDeclaration.getBody();
          node.accept(visitor);
          return visitor.derivations().stream();
        }).toList();

    var init = getFieldInitialization(field);
    if (init.isPresent()) {
      derivations.add(init.get());
    }

    var newWritingPairs = derivations.stream()
        .map(derivation -> Pair.with(derivation, constant))
        .toList();;

    return new NewWritingPairs(newWritingPairs);
  }

  private Optional<FieldWriting> getFieldInitialization(IField field) {
    var variableDeclarationFragmentOpt = codeParser.parse(field);
    return variableDeclarationFragmentOpt
        .flatMap(vdf -> Optional.ofNullable(vdf.getInitializer()))
        .map(initializer -> new FieldWriting(field, initializer, Optional.empty()));

  }
}
