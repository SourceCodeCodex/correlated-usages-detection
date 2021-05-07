package upt.se.arguments.pair;

import static upt.se.utils.helpers.Converter.round;
import static upt.se.utils.helpers.LoggerHelper.NULL_PROGRESS_MONITOR;
import io.vavr.collection.List;
import io.vavr.control.Try;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import thesis.metamodel.entity.MParameterPair;

@PropertyComputer
public class Aperture implements IPropertyComputer<Double, MParameterPair> {

	@Override
	public Double compute(MParameterPair entity) {
		int usedTypesCount = List.ofAll(entity.usedArgumentsTypes().getElements())
				.map(pair -> pair.getUnderlyingObject())
				.flatMap(pair -> getAllConcreteTypes(pair.getFirst()).crossProduct(getAllConcreteTypes(pair.getSecond())))
				.distinctBy(pair -> Try.of(() -> pair._1.getFullyQualifiedParameterizedName()).getOrElse("") + ","
						+ Try.of(() -> pair._2.getFullyQualifiedParameterizedName()).getOrElse(""))
				.size();

		double apperture = usedTypesCount * 100d / entity.allPossibleArgumentsTypes().getElements().size();

		return round(apperture, 2);
	}

	//TODO: if a class is not extended this will return an empty list
	private List<IType> getAllConcreteTypes(IType type) {
		return Try.of(() -> type.newTypeHierarchy(NULL_PROGRESS_MONITOR))
				.map(hierarchy -> List.of(hierarchy.getAllSubtypes(type)).append(type))
				.map(con -> con.filter(elem -> {
					try {
						boolean isAbstract = Flags.isAbstract(type.getFlags());
						return !elem.isInterface() && !isAbstract;
					} catch (JavaModelException e) {
						e.printStackTrace();
					}
					return false;
				})).getOrElse(List.of(type));
	}

}
