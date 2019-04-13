package thesis.metamodel.factory;

import ro.lrg.xcore.metametamodel.XEntity;
import thesis.metamodel.entity.*;
import thesis.metamodel.impl.*;

public class Factory {
   protected static Factory singleInstance = new Factory();
   public static Factory getInstance() { return singleInstance;}
   protected Factory() {}
   private LRUCache<Object, XEntity> lruCache_ = new LRUCache<>(1000);
   public void setCacheCapacity(int capacity) {
       lruCache_.setCapacity(capacity);
   }
   public void clearCache() {lruCache_.clear();}
   public MArgumentType createMArgumentType(org.eclipse.jdt.core.dom.ITypeBinding obj) {
       XEntity instance = lruCache_.get(obj);
        if (null == instance) {
           instance = new MArgumentTypeImpl(obj);
           lruCache_.put(obj, instance);
        }
        return (MArgumentType)instance;
    }
   public MTypeParameter createMTypeParameter(org.eclipse.jdt.core.dom.ITypeBinding obj) {
       XEntity instance = lruCache_.get(obj);
        if (null == instance) {
           instance = new MTypeParameterImpl(obj);
           lruCache_.put(obj, instance);
        }
        return (MTypeParameter)instance;
    }
   public MClass createMClass(org.eclipse.jdt.core.IType obj) {
       XEntity instance = lruCache_.get(obj);
        if (null == instance) {
           instance = new MClassImpl(obj);
           lruCache_.put(obj, instance);
        }
        return (MClass)instance;
    }
   public MTypePair createMTypePair(upt.se.utils.TypePair obj) {
       XEntity instance = lruCache_.get(obj);
        if (null == instance) {
           instance = new MTypePairImpl(obj);
           lruCache_.put(obj, instance);
        }
        return (MTypePair)instance;
    }
}
