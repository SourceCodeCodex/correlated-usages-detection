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
   public MParameterPair createMParameterPair(upt.se.utils.ParameterPair obj) {
       XEntity instance = lruCache_.get(obj);
        if (null == instance) {
           instance = new MParameterPairImpl(obj);
           lruCache_.put(obj, instance);
        }
        return (MParameterPair)instance;
    }
   public MArgumentPair createMArgumentPair(upt.se.utils.ArgumentPair obj) {
       XEntity instance = lruCache_.get(obj);
        if (null == instance) {
           instance = new MArgumentPairImpl(obj);
           lruCache_.put(obj, instance);
        }
        return (MArgumentPair)instance;
    }
   public MArgument createMArgument(org.eclipse.jdt.core.IType obj) {
       XEntity instance = lruCache_.get(obj);
        if (null == instance) {
           instance = new MArgumentImpl(obj);
           lruCache_.put(obj, instance);
        }
        return (MArgument)instance;
    }
   public MParameter createMParameter(org.eclipse.jdt.core.dom.ITypeBinding obj) {
       XEntity instance = lruCache_.get(obj);
        if (null == instance) {
           instance = new MParameterImpl(obj);
           lruCache_.put(obj, instance);
        }
        return (MParameter)instance;
    }
}
