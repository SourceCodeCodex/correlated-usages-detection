package upt.se.parameters;

import java.util.List;
import java.util.stream.Collectors;

import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MClass;
import thesis.metamodel.entity.MTypeParameter;

@RelationBuilder
public class UnusedParameterTypes implements IRelationBuilder<MClass, MTypeParameter> {
    
    @Override
    public Group<MClass> buildGroup(MTypeParameter entity) {
        Group<MClass> allParameterTypes = entity.allParameterTypes();
        Group<MClass> actualParameterTypes = entity.actualParameterTypes();
        
        List<MClass> diff = allParameterTypes
                .getElements()
                .stream()
                .filter(p -> !contains(actualParameterTypes, p))
                .collect(Collectors.toList());
        
        Group<MClass> result = new Group<>();
        result.addAll(diff);
        return result;
    }
    
    private boolean contains(Group<MClass> list, MClass element) {
        return list.getElements().stream().anyMatch(p -> p.getUnderlyingObject().getFullyQualifiedName()
                .equals(element.getUnderlyingObject().getFullyQualifiedName()));
    }
    
}