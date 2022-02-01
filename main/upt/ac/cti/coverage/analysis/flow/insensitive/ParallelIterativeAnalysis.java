package upt.ac.cti.coverage.analysis.flow.insensitive;

import java.util.concurrent.ExecutionException;
import org.eclipse.jdt.core.IField;
import upt.ac.cti.coverage.analysis.CoverageAnalysis;
import upt.ac.cti.coverage.analysis.flow.insensitive.generators.AllCPGenerator;
import upt.ac.cti.coverage.analysis.flow.insensitive.iterators.ParallelCPIterator;
import upt.ac.cti.utils.parsers.CachedParser;

public class ParallelIterativeAnalysis implements CoverageAnalysis {

  private final IField field1;
  private final IField field2;

  public ParallelIterativeAnalysis(IField field1, IField field2) {
    super();
    this.field1 = field1;
    this.field2 = field2;
  }


  @Override
  public int coverage() {
    try {
      CachedParser.instance().refresh();
      return new ParallelCPIterator(new AllCPGenerator(field1, field2)).corelations().size();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
      return -1;
    }
  }

}
