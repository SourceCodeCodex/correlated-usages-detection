package upt.ac.cti.utils.resolvers;

import java.util.Optional;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ITypeBinding;
import upt.ac.cti.coverage.analysis.flow.insensitive.model.FieldAsgmt;

public class AsgmtBindingResolver {
  private static final AsgmtBindingResolver instance = new AsgmtBindingResolver();

  private AsgmtBindingResolver() {

  }

  public static AsgmtBindingResolver instance() {
    return instance;
  }


  public Optional<ITypeBinding> resolveConcreteBinding(FieldAsgmt fieldData) {
    var exprType = fieldData.rightSide().getNodeType();

    // cases when the binding does not need to point to a leaf in the hierarchy
    switch (exprType) {
      case ASTNode.CAST_EXPRESSION: {
        var cast = (CastExpression) fieldData.rightSide();
        return Optional.of(cast.getType().resolveBinding());
      }
      case ASTNode.CLASS_INSTANCE_CREATION: {
        var cast = (ClassInstanceCreation) fieldData.rightSide();
        return Optional.of(cast.getType().resolveBinding());
      }
      default:
        var rightTypeBinding = fieldData.rightSide().resolveTypeBinding();
        var iType = (IType) rightTypeBinding.getJavaElement();
        if (ConcreteHierarchyTypesResolver
            .instance()
            .getConcreteInHierarchyTypes(iType)
            .size() <= 1) {
          return Optional.of(rightTypeBinding);
        }
        return Optional.empty();
    }
  }

}
