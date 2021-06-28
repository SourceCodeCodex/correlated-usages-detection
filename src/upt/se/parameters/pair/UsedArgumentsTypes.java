package upt.se.parameters.pair;

import static upt.se.utils.helpers.Helper.isAbstract;
import static upt.se.utils.helpers.LoggerHelper.LOGGER;
import static upt.se.utils.helpers.LoggerHelper.NULL_PROGRESS_MONITOR;

import java.util.LinkedList;
import java.util.logging.Level;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
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
				.map(usedArgumentPairs -> getHierarchy(usedArgumentPairs, entity.getUnderlyingObject()))
				.map(usedArgumentPairs -> usedArgumentPairs.toMap(pair -> toString(pair)).values().toList())
				.map(usedArgumentPairs -> usedArgumentPairs.map(Factory.getInstance()::createMClassPair))
				.onFailure(exception -> LOGGER.log(Level.SEVERE,
						"An error occurred while trying to get all the parameters for: "
								+ entity.getUnderlyingObject().getFirst().getQualifiedName() + ", and "
								+ entity.getUnderlyingObject().getSecond().getQualifiedName(),
						exception))
				.orElse(() -> Try.success(List.empty()))
				.map(GroupBuilder::wrap)
				.get();
	}
	
	private Tuple2<String,ArgumentPair> toString(ArgumentPair pair) {
		String key = "(" + pair.getFirst().getFullyQualifiedName() + "," + pair.getSecond().getFullyQualifiedName() + ")";		
		return Tuple.of(key, pair);
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
	
	private List<ArgumentPair> getHierarchy(List<Tuple2<ITypeBinding, ITypeBinding>> pairs, ParameterPair parameterPair) {
		return pairs.flatMap(pair -> {
			ITypeBinding firstType = pair._1;
			ITypeBinding secondType = pair._2;
			List<IType> firstTypeHierarchy = getHierarchy(firstType, parameterPair.getFirst());
			List<IType> secondTypeHierarchy = getHierarchy(secondType, parameterPair.getSecond());
			LinkedList<ArgumentPair> result = new LinkedList<>();
			firstTypeHierarchy.forEach(first -> {
				secondTypeHierarchy.forEach(second -> {
					result.add(new ArgumentPair(first, second));
				});
			});
			return List.ofAll(result);
		});
	}
		
	private List<IType> getHierarchy(ITypeBinding argument, ITypeBinding parameter) {
		try {
			IType tmp;
			if (argument.isWildcardType()) {
				if (argument.isUpperbound()) {
					tmp = (IType) argument.getBound().getJavaElement();
				} else {
					tmp = (IType) parameter.getTypeBounds()[0].getJavaElement();					
				}
			} else {
				tmp = (IType) argument.getJavaElement();
			}
			return List.of(tmp.newTypeHierarchy(NULL_PROGRESS_MONITOR).getAllSubtypes(tmp))
					.append(tmp).filter(tpe -> !isAbstract(tpe));
		} catch (JavaModelException e) {
				e.printStackTrace();
		}
		return List.of();
	}

}
