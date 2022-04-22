package upt.ac.cti.ui;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.ui.IStartup;
import org.javatuples.Pair;
import ro.lrg.insider.view.ToolRegistration;

public final class Startup implements IStartup {

  @Override
  public void earlyStartup() {
    ToolRegistration.getInstance().registerXEntityConverter(element -> {
      if (element instanceof IJavaProject) {
        return Factory.getInstance().createMProject(element);
      }
      if (element instanceof IType) {
        return Factory.getInstance().createMClass(element);
      }
      if (element instanceof Pair<?, ?> p) {
        if (p.getValue0() instanceof IType && p.getValue1() instanceof IType) {
          return Factory.getInstance().createMTypePair(element);
        }

        if (p.getValue0() instanceof IField && p.getValue1() instanceof IField) {
          return Factory.getInstance().createMFieldPair(element);
        }
      }
      return null;
    });
  }

}
