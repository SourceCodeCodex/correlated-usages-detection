package upt.se.arguments.pair;

import static upt.se.utils.helpers.Equals.isEqualPairTypes;
import static upt.se.utils.helpers.LoggerHelper.LOGGER;
import java.util.logging.Level;
import io.vavr.collection.List;
import io.vavr.control.Try;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ITypeBinding;
import thesis.metamodel.entity.MClassPair;
import thesis.metamodel.entity.MParameter;
import thesis.metamodel.entity.MParameterPair;
import thesis.metamodel.factory.Factory;
import upt.se.utils.ArgumentPair;
import upt.se.utils.helpers.GroupBuilder;

@RelationBuilder
public class AllPossibleArgumentsTypes implements IRelationBuilder<MClassPair, MParameterPair> {

	@Override
	public Group<MClassPair> buildGroup(MParameterPair entity) {
		return Try.of(() -> entity.getUnderlyingObject()).map(pair -> {
			List<IType> firstParamArgTypes = getAllArguments(pair.getFirst());
			List<IType> secondParamArgTypes = getAllArguments(pair.getSecond());

			return firstParamArgTypes.crossProduct(secondParamArgTypes);
		}).map(argumentPairs -> argumentPairs.distinctBy((first, second) -> isEqualPairTypes(first, second) ? 0 : 1))
				.map(argumentPairs -> argumentPairs
						.map(argumentPair -> new ArgumentPair(argumentPair._1, argumentPair._2)))
				.map(argumentPairs -> argumentPairs.map(Factory.getInstance()::createMClassPair))
				.map(argumentPairs -> argumentPairs.toList())
				.onFailure(exception -> LOGGER.log(Level.SEVERE,
						"An error occurred while trying to get all the parameters for: "
								+ entity.getUnderlyingObject().getFirst().getQualifiedName() + ", and "
								+ entity.getUnderlyingObject().getSecond().getQualifiedName(),
						exception))
				.orElse(Try.success(List.empty())).map(GroupBuilder::wrap).get();
	}

	private List<IType> getAllArguments(ITypeBinding type) {
		MParameter parameter = Factory.getInstance().createMParameter(type);
		List<IType> argTypes = GroupBuilder.unwrapArguments(parameter.allPossibleArgumentTypes());
		return argTypes;
	}

}
