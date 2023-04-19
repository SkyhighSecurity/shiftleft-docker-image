/*    */ package org.springframework.objenesis.instantiator.android;
/*    */ 
/*    */ import java.io.ObjectStreamClass;
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.objenesis.ObjenesisException;
/*    */ import org.springframework.objenesis.instantiator.ObjectInstantiator;
/*    */ import org.springframework.objenesis.instantiator.annotations.Instantiator;
/*    */ import org.springframework.objenesis.instantiator.annotations.Typology;
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
/*    */ @Instantiator(Typology.SERIALIZATION)
/*    */ public class AndroidSerializationInstantiator<T>
/*    */   implements ObjectInstantiator<T>
/*    */ {
/*    */   private final Class<T> type;
/*    */   private final ObjectStreamClass objectStreamClass;
/*    */   private final Method newInstanceMethod;
/*    */   
/*    */   public AndroidSerializationInstantiator(Class<T> type) {
/* 41 */     this.type = type;
/* 42 */     this.newInstanceMethod = getNewInstanceMethod();
/* 43 */     Method m = null;
/*    */     try {
/* 45 */       m = ObjectStreamClass.class.getMethod("lookupAny", new Class[] { Class.class });
/* 46 */     } catch (NoSuchMethodException e) {
/* 47 */       throw new ObjenesisException(e);
/*    */     } 
/*    */     try {
/* 50 */       this.objectStreamClass = (ObjectStreamClass)m.invoke((Object)null, new Object[] { type });
/* 51 */     } catch (IllegalAccessException e) {
/* 52 */       throw new ObjenesisException(e);
/* 53 */     } catch (InvocationTargetException e) {
/* 54 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public T newInstance() {
/*    */     try {
/* 60 */       return this.type.cast(this.newInstanceMethod.invoke(this.objectStreamClass, new Object[] { this.type }));
/*    */     }
/* 62 */     catch (IllegalAccessException e) {
/* 63 */       throw new ObjenesisException(e);
/*    */     }
/* 65 */     catch (IllegalArgumentException e) {
/* 66 */       throw new ObjenesisException(e);
/*    */     }
/* 68 */     catch (InvocationTargetException e) {
/* 69 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static Method getNewInstanceMethod() {
/*    */     try {
/* 75 */       Method newInstanceMethod = ObjectStreamClass.class.getDeclaredMethod("newInstance", new Class[] { Class.class });
/*    */       
/* 77 */       newInstanceMethod.setAccessible(true);
/* 78 */       return newInstanceMethod;
/*    */     }
/* 80 */     catch (RuntimeException e) {
/* 81 */       throw new ObjenesisException(e);
/*    */     }
/* 83 */     catch (NoSuchMethodException e) {
/* 84 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\objenesis\instantiator\android\AndroidSerializationInstantiator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */