package upt.ac.cti.coverage.analysis.iterative.iterators.handlers;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.Name;
import upt.ac.cti.coverage.analysis.iterative.model.CPHandlingResult;
import upt.ac.cti.coverage.analysis.iterative.model.CorelationPair;
import upt.ac.cti.coverage.analysis.iterative.model.FieldAsgmt;
import upt.ac.cti.coverage.analysis.iterative.visitors.AsgmtVisitor;
import upt.ac.cti.utils.parsers.CachedParser;

//@formatter:off
abstract class NameHandler<
H     extends CPFieldHandler<H, FAHT, MIHT, SFAHT, SMIHT, AHT, PEHT, CEHT, NHT>,
FAHT  extends FieldAccessHandler<H, FAHT, MIHT, SFAHT, SMIHT, AHT, PEHT, CEHT, NHT>,
MIHT  extends MethodInvocationHandler<H, FAHT, MIHT, SFAHT, SMIHT, AHT, PEHT, CEHT, NHT>,
SFAHT extends SuperFieldAccessHandler<H, FAHT, MIHT, SFAHT, SMIHT, AHT, PEHT, CEHT, NHT>,
SMIHT extends SuperMethodInvocationHandler<H, FAHT, MIHT, SFAHT, SMIHT, AHT, PEHT, CEHT, NHT>,
AHT   extends AssignmentHandler<H, FAHT, MIHT, SFAHT, SMIHT, AHT, PEHT, CEHT, NHT>,
PEHT  extends ParenthesizedExpressionHandler<H, FAHT, MIHT, SFAHT, SMIHT, AHT, PEHT, CEHT, NHT>,
CEHT  extends ConditionalExpressionHandler<H, FAHT, MIHT, SFAHT, SMIHT, AHT, PEHT, CEHT, NHT>,
NHT   extends NameHandler<H, FAHT, MIHT, SFAHT, SMIHT, AHT, PEHT, CEHT, NHT>
> extends RightSideHandler {
//@formatter:on

  private final Logger logger = Logger.getGlobal();

  public NameHandler(CorelationPair cp) {
    super(cp);
  }

  @Override
  public CPHandlingResult handle() {
    var binding = resolveBinding();
    logger.info("Handle binding " + binding.getName());
    if (binding.getKind() == IBinding.VARIABLE) {
      logger.info("Handle variable binding " + binding.getName());
      var varBinding = (IVariableBinding) binding;
      if (varBinding.isParameter()) {
        logger.warning("WELL, FUCK!");
        return voidResult();
      }

      if (varBinding.isField()) {
        logger.warning("WELL, FUCK!1");
        return voidResult();
      }
      logger.info("Handle local var " + binding.getName());
      return handleLocalVar((ILocalVariable) binding.getJavaElement());
    }
    return voidResult();
  }

  private CPHandlingResult handleLocalVar(ILocalVariable localVar) {

    var visitor = new AsgmtVisitor(localVar, cp.field1Asgmt().iField(), currentMethod());
    var node = CachedParser.instance().parse(currentMethod()).getBody();
    node.accept(visitor);
    var asgmts = visitor.result();
    var newPairs = updatePairs(asgmts);
    return new CPHandlingResult(newPairs, Optional.empty());
  }


  protected abstract IBinding resolveBinding();

  protected abstract IMethod currentMethod();

  protected abstract List<CorelationPair> updatePairs(List<FieldAsgmt> asgmts);
}


//@formatter:off
class Name1Handler extends NameHandler<
CPField1Handler,
FieldAccess1Handler,
MethodInvocation1Handler,
SuperFieldAccess1Handler,
SuperMethodInvocation1Handler,
Assignment1Handler,
ParenthesizedExpression1Handler,
ConditionalExpression1Handler,
Name1Handler
> {
//@formatter:on

  public Name1Handler(CorelationPair cp) {
    super(cp);
  }


  @Override
  protected List<CorelationPair> updatePairs(List<FieldAsgmt> asgmts) {
    return asgmts.stream().map(asgmt -> cp.withField1Data(asgmt)).toList();
  }


  @Override
  protected IBinding resolveBinding() {
    var name = (Name) cp.field1Asgmt().rightSide();
    return name.resolveBinding();
  }


  @Override
  protected IMethod currentMethod() {
    return cp.field1Asgmt().currentMethod();
  }
}


//@formatter:off
class Name2Handler extends NameHandler<
CPField2Handler,
FieldAccess2Handler,
MethodInvocation2Handler,
SuperFieldAccess2Handler,
SuperMethodInvocation2Handler,
Assignment2Handler,
ParenthesizedExpression2Handler,
ConditionalExpression2Handler,
Name2Handler
> {
//@formatter:on

  public Name2Handler(CorelationPair cp) {
    super(cp);
  }

  @Override
  protected List<CorelationPair> updatePairs(List<FieldAsgmt> asgmts) {
    return asgmts.stream().map(asgmt -> cp.withField2Data(asgmt)).toList();
  }

  @Override
  protected IBinding resolveBinding() {
    var name = (Name) cp.field2Asgmt().rightSide();
    return name.resolveBinding();
  }

  // TODO: NOT LIKE THIS
  @Override
  protected IMethod currentMethod() {
    return cp.field2Asgmt().currentMethod();
  }
}
