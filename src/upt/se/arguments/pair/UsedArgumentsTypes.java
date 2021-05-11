package upt.se.arguments.pair;

import static upt.se.utils.helpers.Equals.isEqualPairBindings;
import static upt.se.utils.helpers.Converter.convert;
import static upt.se.utils.helpers.LoggerHelper.LOGGER;
import java.util.logging.Level;

import org.eclipse.jdt.core.dom.ITypeBinding;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.control.Try;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MClassPair;
import thesis.metamodel.entity.MParameterPair;
import thesis.metamodel.factory.Factory;
import upt.se.utils.ArgumentPair;
import upt.se.utils.ParameterPair;
import upt.se.utils.crawlers.InheritanceArgumentTypes;
import upt.se.utils.crawlers.VariablesArgumentTypes;
import upt.se.utils.helpers.GroupBuilder;

@RelationBuilder
public class UsedArgumentsTypes implements IRelationBuilder<MClassPair, MParameterPair> {

	@Override
	public Group<MClassPair> buildGroup(MParameterPair entity) {
		return Try.of(() -> entity.getUnderlyingObject())
				.map(pair -> getInheritanceUsages(pair).appendAll(getVariablesUsages(pair)))
				.map(usedArgumentPairs -> usedArgumentPairs
						.distinctBy((first, second) -> isEqualPairBindings(first, second) ? 0 : 1))
				.map(usedArgumentPairs -> usedArgumentPairs.map(pair -> new ArgumentPair(convert(pair._1), convert(pair._2))))
				.map(usedArgumentPairs -> usedArgumentPairs.map(Factory.getInstance()::createMClassPair))
				.onFailure(exception -> LOGGER.log(Level.SEVERE,
						"An error occurred while trying to get all the parameters for: "
								+ entity.getUnderlyingObject().getFirst().getQualifiedName() + ", and "
								+ entity.getUnderlyingObject().getSecond().getQualifiedName(),
						exception))
				.orElse(() -> Try.success(List.empty())).map(GroupBuilder::wrap).get();
	}
	
	private List<Tuple2<ITypeBinding,ITypeBinding>> getInheritanceUsages(ParameterPair pair) {
		ITypeBinding firstParameter = pair.getFirst();
		ITypeBinding secondParameter = pair.getSecond();
		List<List<ITypeBinding>> usages = InheritanceArgumentTypes.getUsages(firstParameter.getDeclaringClass());
		
		int firstParameterPos = InheritanceArgumentTypes.getParameterNumber(firstParameter);
		int secondParameterPos = InheritanceArgumentTypes.getParameterNumber(secondParameter);
		
		return usages.map(types -> Tuple.of(types.get(firstParameterPos), types.get(secondParameterPos)));
	}	
	
	private List<Tuple2<ITypeBinding,ITypeBinding>> getVariablesUsages(ParameterPair pair) {
		ITypeBinding firstParameter = pair.getFirst();
		ITypeBinding secondParameter = pair.getSecond();
		List<List<ITypeBinding>> usages = VariablesArgumentTypes.getUsages(firstParameter.getDeclaringClass());
		
		int firstParameterPos = VariablesArgumentTypes.getParameterNumber(firstParameter);
		int secondParameterPos = VariablesArgumentTypes.getParameterNumber(secondParameter);
		
		return usages.map(types -> Tuple.of(types.get(firstParameterPos), types.get(secondParameterPos)));
	}

}
