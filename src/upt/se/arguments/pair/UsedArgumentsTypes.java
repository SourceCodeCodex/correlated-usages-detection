package upt.se.arguments.pair;

import static upt.se.utils.visitors.IType2ITypeDeclarationBindingConverter.convert;
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
				.map(usedArgumentPairs -> usedArgumentPairs.toMap(pair -> toString(pair))
						.values()
						.toList())
				.map(usedArgumentPairs -> usedArgumentPairs.map(pair -> new ArgumentPair(pair._1, pair._2)))
				.map(usedArgumentPairs -> getHierarchy(usedArgumentPairs))
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
	
	private Tuple2<String,Tuple2<IType,IType>> toString(Tuple2<IType,IType> pair) {
		String key = "(" + pair._1.getFullyQualifiedName() + "," + pair._2.getFullyQualifiedName() + ")";
		
		return Tuple.of(key, pair);
	}
	
	private List<Tuple2<IType,IType>> getInheritanceUsages(ParameterPair pair) {
		ITypeBinding firstParameter = pair.getFirst();
		ITypeBinding secondParameter = pair.getSecond();
		List<List<ITypeBinding>> usages = InheritanceArgumentTypes.getUsages(firstParameter.getDeclaringClass());
		
		int firstParameterPos = InheritanceArgumentTypes.getParameterNumber(firstParameter);
		int secondParameterPos = InheritanceArgumentTypes.getParameterNumber(secondParameter);
		
		return usages.map(types -> Tuple.of(convert(types.get(firstParameterPos)), convert(types.get(secondParameterPos))));
	}	
	
	private List<Tuple2<IType,IType>> getVariablesUsages(ParameterPair pair) {
		ITypeBinding firstParameter = pair.getFirst();
		ITypeBinding secondParameter = pair.getSecond();
		List<List<ITypeBinding>> usages = VariablesArgumentTypes.getUsages(firstParameter.getDeclaringClass());
		
		int firstParameterPos = VariablesArgumentTypes.getParameterNumber(firstParameter);
		int secondParameterPos = VariablesArgumentTypes.getParameterNumber(secondParameter);
		
		return usages.map(types -> Tuple.of(convert(types.get(firstParameterPos)), convert(types.get(secondParameterPos))));
	}
	
	private List<ArgumentPair> getHierarchy(List<ArgumentPair> pairs) {
		return pairs.flatMap(pair -> {
			IType firstType = pair.getFirst();
			IType secondType = pair.getSecond();
			
			List<IType> firstTypeHierarchy = getHierarchy(firstType);
			List<IType> secondTypeHierarchy = getHierarchy(secondType);
			
			LinkedList<ArgumentPair> result = new LinkedList<>();
			firstTypeHierarchy.forEach(first -> {
				secondTypeHierarchy.forEach(second -> {
					result.add(new ArgumentPair(first, second));
				});
			});
			
			return List.ofAll(result);
		});
	}
		
	private List<IType> getHierarchy(IType type) {
		if(isAbstract(type)) {
			try {
				return List.of(type.newTypeHierarchy(NULL_PROGRESS_MONITOR).getAllSubtypes(type)).filter(tpe -> !isAbstract(tpe));
			} catch (JavaModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return List.of(type);
	}

}
