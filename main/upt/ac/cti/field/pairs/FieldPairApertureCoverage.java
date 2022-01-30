package upt.ac.cti.field.pairs;

import org.eclipse.jdt.core.IField;
import org.javatuples.Pair;
import familypolymorphismdetection.metamodel.entity.MFieldPair;
import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import upt.ac.cti.coverage.analysis.iterative.ParallelIterativeAnalysis;

@PropertyComputer
public class FieldPairApertureCoverage implements IPropertyComputer<Integer, MFieldPair> {

  @Override
  public Integer compute(MFieldPair mFieldPair) {
    @SuppressWarnings("unchecked")
    var pair = (Pair<IField, IField>) mFieldPair.getUnderlyingObject();
    var iterativeAnalysis = new ParallelIterativeAnalysis(pair.getValue0(), pair.getValue1());
    return iterativeAnalysis.coverage();
  }

}
