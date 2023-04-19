/*    */ package org.springframework.objenesis.strategy;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import org.springframework.objenesis.ObjenesisException;
/*    */ import org.springframework.objenesis.instantiator.ObjectInstantiator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SingleInstantiatorStrategy
/*    */   implements InstantiatorStrategy
/*    */ {
/*    */   private Constructor<?> constructor;
/*    */   
/*    */   public <T extends ObjectInstantiator<?>> SingleInstantiatorStrategy(Class<T> instantiator) {
/*    */     try {
/* 43 */       this.constructor = instantiator.getConstructor(new Class[] { Class.class });
/*    */     }
/* 45 */     catch (NoSuchMethodException e) {
/* 46 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> type) {
/*    */     try {
/* 61 */       return (ObjectInstantiator<T>)this.constructor.newInstance(new Object[] { type });
/* 62 */     } catch (InstantiationException e) {
/* 63 */       throw new ObjenesisException(e);
/* 64 */     } catch (IllegalAccessException e) {
/* 65 */       throw new ObjenesisException(e);
/* 66 */     } catch (InvocationTargetException e) {
/* 67 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\objenesis\strategy\SingleInstantiatorStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */