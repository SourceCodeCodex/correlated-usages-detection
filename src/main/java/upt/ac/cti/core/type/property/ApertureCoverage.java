package upt.ac.cti.core.type.property;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.logging.Logger;

import org.eclipse.jdt.core.IType;

import familypolymorphismdetection.metamodel.entity.MClass;
import familypolymorphismdetection.metamodel.entity.MFieldPair;
import familypolymorphismdetection.metamodel.entity.MParameterPair;
import upt.ac.cti.config.Config;
import upt.ac.cti.coverage.CoverageStrategy;
import upt.ac.cti.util.computation.ApertureCoverageUtil;
import upt.ac.cti.util.logging.RLogger;
import upt.ac.cti.util.pool.Pools;
import upt.ac.cti.util.time.StopWatch;

abstract class ApertureCoverage {

	private final Logger logger;

	private final CoverageStrategy strategy;

	public ApertureCoverage(CoverageStrategy strategy) {
		this.strategy = strategy;
		logger = RLogger.get("ApertureCoverage-" + strategy.name);
	}

	public Double compute(MClass mClass) {

		Callable<Double> callable = () -> computeInternal(mClass);
		var task = Pools.instance().getApertuteCoveragePool().submit(callable);
		Pools.apertureCoverageTasks.put(((IType) mClass.getUnderlyingObject()).getElementName(), task);
		try {
			return task.get(Config.APERTURE_COVERAGE_TIMEOUT_SECOUNDS, TimeUnit.SECONDS);
		} catch (TimeoutException e) {
			logger.info("Timeout: Strategy: " + strategy.name + " Class: " + mClass);
			logger.info("Aperture coverage cancellation status: " + task.cancel(true));
			return -2.;
		} catch (InterruptedException | ExecutionException e) {
			logger.info("Aperture coverage cancellation status: " + task.cancel(true));
			e.printStackTrace();
			return -3.;
		}
	}

	public Double computeInternal(MClass mClass) {
		var stopWatch = new StopWatch();

		stopWatch.start();

		var fieldApertureCoverage = fieldApertureCoverage(mClass);

		if (fieldApertureCoverage > 0 && fieldApertureCoverage < 1) {
			stopWatch.stop();
			logResult(fieldApertureCoverage, fieldApertureCoverage, Double.NaN, mClass, stopWatch);

			return fieldApertureCoverage;
		}

		var parameterApertureCoverage = parameterApertureCoverage(mClass);

		var apertureCoverage = ApertureCoverageUtil.combine(List.of(fieldApertureCoverage, parameterApertureCoverage));
		stopWatch.stop();

		logResult(apertureCoverage, fieldApertureCoverage, parameterApertureCoverage, mClass, stopWatch);

		return apertureCoverage;
	}

	protected abstract Function<MFieldPair, Double> fieldPairApertureCoverage();

	protected abstract Function<MParameterPair, Double> parameterPairApertureCoverage();

	private Double fieldApertureCoverage(MClass mClass) {
		var apertureCoverages = mClass.susceptibleFieldPairs().getElements().stream().map(fieldPairApertureCoverage())
				.toList();

		return ApertureCoverageUtil.combine(apertureCoverages);
	}

	private Double parameterApertureCoverage(MClass mClass) {
		var apertureCoverages = mClass.susceptibleParameterPairs().getElements().stream()
				.map(parameterPairApertureCoverage()).toList();

		return ApertureCoverageUtil.combine(apertureCoverages);
	}

	private void logResult(Double apertureCoverage, Double fieldApertureCoverage, Double parameterApertureCoverage,
			MClass mClass, StopWatch stopWatch) {
		var log = String.format("(\u001B[1mAC: %.10f\u001B[22m, F: %.7f, P: %.7f): %s in %d ms", apertureCoverage,
				fieldApertureCoverage, parameterApertureCoverage, mClass.toString(),
				stopWatch.getDuration().toMillis());
		logger.info(log);
	}

}
