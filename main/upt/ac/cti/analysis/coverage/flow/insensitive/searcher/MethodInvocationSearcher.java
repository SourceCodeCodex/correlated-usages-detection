package upt.ac.cti.analysis.coverage.flow.insensitive.searcher;

import java.util.logging.Logger;

public final class MethodInvocationSearcher {

  private static MethodInvocationSearcher instance = new MethodInvocationSearcher();

  private static final Logger logger = Logger.getLogger(MethodInvocationSearcher.class.getName());

  private MethodInvocationSearcher() {

  }

  public static MethodInvocationSearcher instance() {
    return instance;
  }



}
