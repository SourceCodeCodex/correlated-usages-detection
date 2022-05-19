package upt.ac.cti.util.pool;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

import upt.ac.cti.config.Config;

public class Pools {

	public static final ConcurrentHashMap<String, ForkJoinTask<Double>> apertureCoverageTasks = new ConcurrentHashMap<>();

	private ForkJoinPool classPool = null;
	private ForkJoinPool apertuteCoveragePool = null;
	private ForkJoinPool derivationPool = null;

	private static volatile Pools instance = null;

	private Pools() {
		classPool = new ForkJoinPool(Config.CLASS_ANALYSIS_POOL_SIZE);
		apertuteCoveragePool = new ForkJoinPool(Config.CLASS_ANALYSIS_POOL_SIZE);
		derivationPool = new ForkJoinPool(Config.DERIVATION_POOL_SIZE);
	}

	public ForkJoinPool getClassPool() {
		return classPool;
	}

	public ForkJoinPool getDerivationPool() {
		return derivationPool;
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
