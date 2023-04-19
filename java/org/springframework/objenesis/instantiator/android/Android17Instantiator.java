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
/*    */ @Instantiator(Typology.STANDARD)
/*    */ public class Android17Instantiator<T>
/*    */   implements ObjectInstantiator<T>
/*    */ {
/*    */   private final Class<T> type;
/*    */   private final Method newInstanceMethod;
/*    */   private final Integer objectConstructorId;
/*    */   
/*    */   public Android17Instantiator(Class<T> type) {
/* 40 */     this.type = type;
/* 41 */     this.newInstanceMethod = getNewInstanceMethod();
/* 42 */     this.objectConstructorId = findConstructorIdForJavaLangObjectConstructor();
/*    */   }
/*    */   
/*    */   public T newInstance() {
/*    */     try {
/* 47 */       return this.type.cast(this.newInstanceMethod.invoke((Object)null, new Object[] { this.type, this.objectConstructorId }));
/*    */     }
/* 49 */     catch (Exception e) {
/* 50 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static Method getNewInstanceMethod() {
/*    */     try {
/* 56 */       Method newInstanceMethod = ObjectStreamClass.class.getDeclaredMethod("newInstance", new Class[] { Class.class, int.class });
/*    */       
/* 58 */       newInstanceMethod.setAccessible(true);
/* 59 */       return newInstanceMethod;
/*    */     }
/* 61 */     catch (RuntimeException e) {
/* 62 */       throw new ObjenesisException(e);
/*    */     }
/* 64 */     catch (NoSuchMethodException e) {
/* 65 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static Integer findConstructorIdForJavaLangObjectConstructor() {
/*    */     try {
/* 71 */       Method newInstanceMethod = ObjectStreamClass.class.getDeclaredMethod("getConstructorId", new Class[] { Class.class });
/*    */       
/* 73 */       newInstanceMethod.setAccessible(true);
/*    */       
/* 75 */       return (Integer)newInstanceMethod.invoke((Object)null, new Object[] { Object.class });
/*    */     }
/* 77 */     catch (RuntimeException e) {
/* 78 */       throw new ObjenesisException(e);
/*    */     }
/* 80 */     catch (NoSuchMethodException e) {
/* 81 */       throw new ObjenesisException(e);
/*    */     }
/* 83 */     catch (IllegalAccessException e) {
/* 84 */       throw new ObjenesisException(e);
/*    */     }
/* 86 */     catch (InvocationTargetException e) {
/* 87 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\objenesis\instantiator\android\Android17Instantiator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */