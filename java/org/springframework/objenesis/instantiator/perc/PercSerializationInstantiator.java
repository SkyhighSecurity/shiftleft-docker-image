/*    */ package org.springframework.objenesis.instantiator.perc;
/*    */ 
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.Serializable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Instantiator(Typology.SERIALIZATION)
/*    */ public class PercSerializationInstantiator<T>
/*    */   implements ObjectInstantiator<T>
/*    */ {
/*    */   private Object[] typeArgs;
/*    */   private final Method newInstanceMethod;
/*    */   
/*    */   public PercSerializationInstantiator(Class<T> type) {
/* 48 */     Class<? super T> unserializableType = type;
/*    */     
/* 50 */     while (Serializable.class.isAssignableFrom(unserializableType)) {
/* 51 */       unserializableType = unserializableType.getSuperclass();
/*    */     }
/*    */ 
/*    */     
/*    */     try {
/* 56 */       Class<?> percMethodClass = Class.forName("COM.newmonics.PercClassLoader.Method");
/*    */       
/* 58 */       this.newInstanceMethod = ObjectInputStream.class.getDeclaredMethod("noArgConstruct", new Class[] { Class.class, Object.class, percMethodClass });
/*    */       
/* 60 */       this.newInstanceMethod.setAccessible(true);
/*    */ 
/*    */       
/* 63 */       Class<?> percClassClass = Class.forName("COM.newmonics.PercClassLoader.PercClass");
/* 64 */       Method getPercClassMethod = percClassClass.getDeclaredMethod("getPercClass", new Class[] { Class.class });
/* 65 */       Object someObject = getPercClassMethod.invoke((Object)null, new Object[] { unserializableType });
/* 66 */       Method findMethodMethod = someObject.getClass().getDeclaredMethod("findMethod", new Class[] { String.class });
/*    */       
/* 68 */       Object percMethod = findMethodMethod.invoke(someObject, new Object[] { "<init>()V" });
/*    */       
/* 70 */       this.typeArgs = new Object[] { unserializableType, type, percMethod };
/*    */     
/*    */     }
/* 73 */     catch (ClassNotFoundException e) {
/* 74 */       throw new ObjenesisException(e);
/*    */     }
/* 76 */     catch (NoSuchMethodException e) {
/* 77 */       throw new ObjenesisException(e);
/*    */     }
/* 79 */     catch (InvocationTargetException e) {
/* 80 */       throw new ObjenesisException(e);
/*    */     }
/* 82 */     catch (IllegalAccessException e) {
/* 83 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public T newInstance() {
/*    */     try {
/* 90 */       return (T)this.newInstanceMethod.invoke((Object)null, this.typeArgs);
/*    */     }
/* 92 */     catch (IllegalAccessException e) {
/* 93 */       throw new ObjenesisException(e);
/*    */     }
/* 95 */     catch (InvocationTargetException e) {
/* 96 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\objenesis\instantiator\perc\PercSerializationInstantiator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */