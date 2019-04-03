package upt.se.pair;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.IType;

import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MTypePair;
import thesis.metamodel.entity.MTypeParameter;
import thesis.metamodel.factory.Factory;
import upt.se.utils.TypePair;
import upt.se.utils.store.ITypeStore;

@RelationBuilder
public class AllParameterTypes implements IRelationBuilder<MTypePair, MTypePair> {
    
    @Override
    public Group<MTypePair> buildGroup(MTypePair entity) {
        MTypeParameter firstParameter = Factory.getInstance()
                .createMTypeParameter(entity.getUnderlyingObject().getFirst());
        MTypeParameter secondParameter = Factory.getInstance()
                .createMTypeParameter(entity.getUnderlyingObject().getSecond());
        
        List<IType> firstParameterSubtypes = null;
        List<IType> secondParameterSubtypes = null;
        if (firstParameter.getUnderlyingObject().getSuperclass().getQualifiedName()
                .equals(Object.class.getCanonicalName())) {
            firstParameterSubtypes = firstParameter.actualParameterTypes().getElements().stream()
                    .map(m -> m.getUnderlyingObject()).collect(Collectors.toList());
        } else {
            firstParameterSubtypes = firstParameter.allParameterTypes().getElements().stream()
                    .map(m -> m.getUnderlyingObject()).collect(Collectors.toList());
        }
        if (secondParameter.getUnderlyingObject().getSuperclass().getQualifiedName()
                .equals(Object.class.getCanonicalName())) {
            secondParameterSubtypes = secondParameter.actualParameterTypes().getElements().stream()
                    .map(m -> m.getUnderlyingObject()).collect(Collectors.toList());
        } else {
            secondParameterSubtypes = secondParameter.allParameterTypes().getElements().stream()
                    .map(m -> m.getUnderlyingObject()).collect(Collectors.toList());
        }
        
        Group<MTypePair> group = new Group<>();
        
        Set<TypePair> pairs = new HashSet<>();
        for (IType first : firstParameterSubtypes) {
            for (IType second : secondParameterSubtypes) {
                group.add(Factory.getInstance().createMTypePair(
                        new TypePair(ITypeStore.convert(first).get(), ITypeStore.convert(second).get())));
            }
        }
        group.addAll(pairs.stream().map(Factory.getInstance()::createMTypePair).collect(Collectors.toSet()));
        return removeDuplicates(group);
    }
    
    private Group<MTypePair> removeDuplicates(Group<MTypePair> group) {
        Group<MTypePair> result = new Group<>();
        for (MTypePair element : group.getElements()) {
            if (result.getElements().stream().noneMatch(p -> isEqual(p, element))) {
                result.add(element);
            }
        }
        
        return result;
    }
    
    private boolean isEqual(MTypePair e1, MTypePair e2) {
        String e1FirstName = e1.getUnderlyingObject().getFirst().getQualifiedName();
        String e1SecondName = e1.getUnderlyingObject().getSecond().getQualifiedName();
        String e2FirstName = e2.getUnderlyingObject().getFirst().getQualifiedName();
        String e2SecondName = e2.getUnderlyingObject().getSecond().getQualifiedName();
        
        return (e1FirstName.equals(e2FirstName) && e1SecondName.equals(e2SecondName))
                || (e1FirstName.equals(e2SecondName) && e1SecondName.equals(e2FirstName));
    }
    
}
