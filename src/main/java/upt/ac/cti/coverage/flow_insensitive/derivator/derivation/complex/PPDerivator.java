package upt.ac.cti.coverage.flow_insensitive.derivator.derivation.complex;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.dom.Name;
import upt.ac.cti.coverage.flow_insensitive.derivator.derivation.IWritingsDerivator;
import upt.ac.cti.coverage.flow_insensitive.model.Writing;
import upt.ac.cti.coverage.flow_insensitive.model.derivation.NewWritingPairs;

class PPDerivator<J extends IJavaElement> implements IWritingsDerivator<J> {

  private final PPSameMethodDerivator<J> pps = new PPSameMethodDerivator<>();
  private final PPDifferentMethodDerivator<J> ppd = new PPDifferentMethodDerivator<>();

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
