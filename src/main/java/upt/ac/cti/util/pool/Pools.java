package upt.ac.cti.util.pool;

import java.util.concurrent.ForkJoinPool;
import upt.ac.cti.config.Config;

public class Pools {

  private ForkJoinPool classPool = null;
  private ForkJoinPool strategyPool = null;
  private ForkJoinPool apertuteCoveragePool = null;

  private static volatile Pools instance = null;

  private Pools() {
    classPool = new ForkJoinPool(Config.CLASS_ANALYSIS_POOL_SIZE);
    strategyPool = new ForkJoinPool(Config.STRATEGY_POOL_SIZE);
    apertuteCoveragePool = new ForkJoinPool(Config.APERTURE_COVERAGE_POOL_SIZE);
  }

  public ForkJoinPool getClassPool() {
    return classPool;
  }

  public ForkJoinPool getStrategyPool() {
    return strategyPool;
  }

  public ForkJoinPool getApertuteCoveragePool() {
    return apertuteCoveragePool;
  }

  public static Pools instance() {
    var local = instance;
    if (local == null) {
      synchronized (Pools.class) {
        local = instance;
        if (local == null) {
          instance = local = new Pools();
        }
      }
    }
    return local;
  }


}
