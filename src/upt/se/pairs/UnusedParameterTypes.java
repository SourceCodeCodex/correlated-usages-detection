package upt.se.pairs;

import java.util.List;
import java.util.stream.Collectors;

import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MTypePair;

@RelationBuilder
public class UnusedParameterTypes implements IRelationBuilder<MTypePair, MTypePair> {
    
    @Override
    public Group<MTypePair> buildGroup(MTypePair entity) {
        Group<MTypePair> allParameterTypes = entity.allParameterTypes();
        Group<MTypePair> actualParameterTypes = entity.actualParameterTypes();
        
        List<MTypePair> diff = allParameterTypes.getElements().stream().filter(p -> !contains(actualParameterTypes, p))
                .collect(Collectors.toList());
        
        Group<MTypePair> result = new Group<>();
        result.addAll(diff);
        return result;
    }
    
    private boolean contains(Group<MTypePair> list, MTypePair element) {
        return list.getElements().stream()
                .anyMatch(p -> p.getUnderlyingObject().getFirst().getQualifiedName()
                        .equals(element.getUnderlyingObject().getFirst().getQualifiedName())
                        && p.getUnderlyingObject().getSecond().getQualifiedName()
                                .equals(element.getUnderlyingObject().getSecond().getQualifiedName()));
    }
    
}