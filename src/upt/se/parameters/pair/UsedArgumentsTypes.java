package upt.se.parameters.pair;

import static upt.se.utils.helpers.Helper.isAbstract;
import static upt.se.utils.helpers.LoggerHelper.LOGGER;
import static upt.se.utils.helpers.LoggerHelper.NULL_PROGRESS_MONITOR;

import java.util.ArrayList;
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
import upt.se.utils.crawlers.TypeArgumentsCrawler;
import upt.se.utils.helpers.GroupBuilder;

@RelationBuilder
public class UsedArgumentsTypes implements IRelationBuilder<MClassPair, MParameterPair> {

	@Override
	public Group<MClassPair> buildGroup(MParameterPair entity) {
		return Try.of(() -> entity.getUnderlyingObject())
				.map(pair -> getUsages(pair))
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
	
	private List<Tuple2<ITypeBinding,ITypeBinding>> getUsages(ParameterPair pair) {
		ITypeBinding firstParameter = pair.getFirst();
		ITypeBinding secondParameter = pair.getSecond();
		List<List<ITypeBinding>> usages = TypeArgumentsCrawler.getUsages(firstParameter.getDeclaringClass());
		
		int firstParameterPos = TypeArgumentsCrawler.getParameterNumber(firstParameter);
		int secondParameterPos = TypeArgumentsCrawler.getParameterNumber(secondParameter);
		
		return usages.map(types -> Tuple.of(types.get(firstParameterPos), types.get(secondParameterPos)));
	}
	
	private List<ArgumentPair> getHierarchy(List<Tuple2<ITypeBinding, ITypeBinding>> pairs, ParameterPair parameterPair) {
		return pairs.flatMap(pair -> {
			ITypeBinding firstType = pair._1;
			ITypeBinding secondType = pair._2;
			List<IType> firstTypeHierarchy = getHierarchy(firstType, parameterPair.getFirst());
			List<IType> secondTypeHierarchy = getHierarchy(secondType, parameterPair.getSecond());
			ArrayList<ArgumentPair> result = new ArrayList<>();
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
