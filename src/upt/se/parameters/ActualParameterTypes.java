package upt.se.parameters;

import java.util.stream.Collectors;

import org.eclipse.jdt.core.JavaModelException;

import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import thesis.metamodel.entity.MClass;
import thesis.metamodel.entity.MTypeParameter;
import thesis.metamodel.factory.Factory;
import upt.se.utils.store.ITypeStore;

@RelationBuilder
public class ActualParameterTypes implements IRelationBuilder<MClass, MTypeParameter> {
    @Override
    public Group<MClass> buildGroup(MTypeParameter entity) {
        Group<MClass> group = new Group<>();
        try {
            if (entity.getUnderlyingObject().getSuperclass().getQualifiedName()
                    .equals(Object.class.getName())) { return group; }
            
            group.addAll(ITypeStore.inheritanceUsages(entity).stream().map(Factory.getInstance()::createMClass)
                    .collect(Collectors.toList()));
            group.addAll(ITypeStore.attributesUsages(entity).stream().map(Factory.getInstance()::createMClass)
                    .collect(Collectors.toList()));
            
        } catch (JavaModelException e) {
            e.printStackTrace();
        }
        
        Group<MClass> result = new Group<>();
        result.addAll(group.getElements().stream().distinct().collect(Collectors.toList()));
        return result;
    }
    
}
