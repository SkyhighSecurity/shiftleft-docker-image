/*    */ package org.springframework.objenesis.instantiator.sun;
/*    */ 
/*    */ import org.springframework.objenesis.ObjenesisException;
/*    */ import org.springframework.objenesis.instantiator.ObjectInstantiator;
/*    */ import org.springframework.objenesis.instantiator.annotations.Instantiator;
/*    */ import org.springframework.objenesis.instantiator.annotations.Typology;
/*    */ import org.springframework.objenesis.instantiator.util.UnsafeUtils;
/*    */ import sun.misc.Unsafe;
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
/*    */ @Instantiator(Typology.STANDARD)
/*    */ public class UnsafeFactoryInstantiator<T>
/*    */   implements ObjectInstantiator<T>
/*    */ {
/*    */   private final Unsafe unsafe;
/*    */   private final Class<T> type;
/*    */   
/*    */   public UnsafeFactoryInstantiator(Class<T> type) {
/* 43 */     this.unsafe = UnsafeUtils.getUnsafe();
/* 44 */     this.type = type;
/*    */   }
/*    */   
/*    */   public T newInstance() {
/*    */     try {
/* 49 */       return this.type.cast(this.unsafe.allocateInstance(this.type));
/* 50 */     } catch (InstantiationException e) {
/* 51 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\objenesis\instantiator\sun\UnsafeFactoryInstantiator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */