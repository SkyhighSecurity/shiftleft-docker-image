/*    */ package org.springframework.objenesis.instantiator.gcj;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import org.springframework.objenesis.ObjenesisException;
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
/*    */ @Instantiator(Typology.STANDARD)
/*    */ public class GCJInstantiator<T>
/*    */   extends GCJInstantiatorBase<T>
/*    */ {
/*    */   public GCJInstantiator(Class<T> type) {
/* 34 */     super(type);
/*    */   }
/*    */ 
/*    */   
/*    */   public T newInstance() {
/*    */     try {
/* 40 */       return this.type.cast(newObjectMethod.invoke(dummyStream, new Object[] { this.type, Object.class }));
/*    */     }
/* 42 */     catch (RuntimeException e) {
/* 43 */       throw new ObjenesisException(e);
/*    */     }
/* 45 */     catch (IllegalAccessException e) {
/* 46 */       throw new ObjenesisException(e);
/*    */     }
/* 48 */     catch (InvocationTargetException e) {
/* 49 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\objenesis\instantiator\gcj\GCJInstantiator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */