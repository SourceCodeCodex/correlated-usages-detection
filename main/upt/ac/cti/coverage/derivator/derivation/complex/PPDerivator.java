package upt.ac.cti.coverage.derivator.derivation.complex;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.dom.Name;
import upt.ac.cti.coverage.derivator.derivation.IWritingsDerivator;
import upt.ac.cti.coverage.model.Writing;
import upt.ac.cti.coverage.model.derivation.NewWritingPairs;
import upt.ac.cti.util.parsing.CodeParser;
import upt.ac.cti.util.search.JavaEntitySearcher;

class PPDerivator<J extends IJavaElement> implements IWritingsDerivator<J> {

  private final PPSameMethodDerivator<J> pps;
  private final PPDifferentMethodDerivator<J> ppd;

  public PPDerivator(JavaEntitySearcher javaEntitySearcher, CodeParser codeParser) {
    this.pps = new PPSameMethodDerivator<>(javaEntitySearcher, codeParser);
    this.ppd = new PPDifferentMethodDerivator<>(javaEntitySearcher, codeParser);
  }

  @Override
  public NewWritingPairs<J> derive(Writing<J> w1, Writing<J> w2) {
    var lv1 = getParameter(w1);
    var lv2 = getParameter(w2);
    if (lv1.getDeclaringMember().equals(lv2.getDeclaringMember())) {
      return pps.derive(w1, w2);
    }
    return ppd.derive(w1, w2);
  }

  private ILocalVariable getParameter(Writing<J> w) {
    var name = (Name) w.writingExpression();
    var binding = name.resolveBinding();

    return (ILocalVariable) binding.getJavaElement();
  }

}
