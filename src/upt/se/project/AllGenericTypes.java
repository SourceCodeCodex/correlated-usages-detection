package upt.se.project;

import static java.util.function.Function.identity;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Option;
import io.vavr.control.Try;
import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MClass;
import thesis.metamodel.entity.MProject;
import thesis.metamodel.factory.Factory;
import upt.se.utils.helpers.Converter;
import upt.se.utils.helpers.GroupBuilder;

@RelationBuilder
public class AllGenericTypes implements IRelationBuilder<MClass, MProject> {

  @Override
  public Group<MClass> buildGroup(MProject project) {
    return Try.of(() -> project.getUnderlyingObject())
        .mapTry(javaProject -> javaProject.getPackageFragmentRoots())
        .map(List::of)
        .map(packages -> packages.map(pack -> Try.of(() -> pack)
            .mapTry(IPackageFragmentRoot::getChildren)
            .map(List::of)))
        .flatMap(this::join)
        .map(javaElements -> javaElements
            .filter(element -> element.getElementType() == IJavaElement.PACKAGE_FRAGMENT)
            .map(javaElement -> (IPackageFragment) javaElement)
            .map(packageFragment -> Try.of(() -> packageFragment)
                .mapTry(IPackageFragment::getCompilationUnits)
                .map(List::of)
                .map(compilationUnits -> compilationUnits
                    .filter(compilationUnit -> Try.of(() -> compilationUnit.isStructureKnown())
                        .getOrElse(false))
                    .map(compilationUnit -> Try.of(() -> compilationUnit)
                        .mapTry(ICompilationUnit::getTypes)
                        .map(List::of)))))
        .flatMap(this::join)
        .flatMap(this::join)
        .orElse(Try.success(List.empty()))
        .map(types -> types.filter(type -> Try.of(() -> type)
            .map(Converter::convert)
            .flatMap(Option::toTry)
            .map(typeBinding -> List.of(typeBinding.getTypeParameters()))
            .map(parameters -> parameters.size() > 0)
            .getOrElse(false)))
        .map(types -> types.map(Factory.getInstance()::createMClass))
        .map(Seq::toList)
        .map(GroupBuilder::wrap)
        .get();
  }

  private <T> Try<List<T>> join(List<Try<List<T>>> list) {
    return Try.sequence(list)
        .map(seq -> seq.flatMap(identity()).toList());
  }

}
