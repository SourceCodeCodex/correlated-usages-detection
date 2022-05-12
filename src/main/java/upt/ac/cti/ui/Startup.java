package upt.ac.cti.ui;

import org.apache.commons.jcs3.JCS;
import org.apache.commons.jcs3.log.LogManager;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IType;
import org.eclipse.ui.IStartup;
import org.javatuples.Pair;
import familypolymorphismdetection.metamodel.factory.Factory;
import ro.lrg.insider.view.ToolRegistration;
import upt.ac.cti.config.CacheConfig;
import upt.ac.cti.config.Config;
import upt.ac.cti.dependency.Dependencies;
import upt.ac.cti.util.logging.RLogger;

public final class Startup implements IStartup {

  @Override
  public void earlyStartup() {
    System.setProperty("java.util.logging.SimpleFormatter.format",
        RLogger.format());
    ToolRegistration.getInstance().registerXEntityConverter(element -> {
      if (element instanceof IJavaProject) {
        return Factory.getInstance().createMProject(element);
      }
      if (element instanceof IType) {
        return Factory.getInstance().createMClass(element);
      }
      if (element instanceof Pair<?, ?> p) {
        if (p.getValue0() instanceof IType t1
            && p.getValue1() instanceof IType t2
            && !t1.equals(t2)) {
          return Factory.getInstance().createMTypePair(element);
        }

        if (p.getValue0() instanceof IField f1
            && p.getValue1() instanceof IField f2
            && !f1.equals(f2)) {
          return Factory.getInstance().createMFieldPair(element);
        }

        if (p.getValue0() instanceof ILocalVariable l1
            && p.getValue1() instanceof ILocalVariable l2
            && l1.isParameter()
            && l2.isParameter()
            && l1.getDeclaringMember().equals(l2.getDeclaringMember())
            && !l1.equals(l2)) {
          return Factory.getInstance().createMParameterPair(element);
        }
      }
      return null;
    });
    JCS.setConfigProperties(new CacheConfig().getProperties());
    JCS.setLogSystem(LogManager.LOGSYSTEM_JAVA_UTIL_LOGGING);

    Dependencies.init(new Config());

  }

}
