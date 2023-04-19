/*    */ package org.springframework.objenesis.instantiator.gcj;
/*    */ 
/*    */ import org.springframework.objenesis.ObjenesisException;
/*    */ import org.springframework.objenesis.instantiator.SerializationInstantiatorHelper;
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
/*    */ public class GCJSerializationInstantiator<T>
/*    */   extends GCJInstantiatorBase<T>
/*    */ {
/*    */   private Class<? super T> superType;
/*    */   
/*    */   public GCJSerializationInstantiator(Class<T> type) {
/* 36 */     super(type);
/* 37 */     this.superType = SerializationInstantiatorHelper.getNonSerializableSuperClass(type);
/*    */   }
/*    */ 
/*    */   
/*    */   public T newInstance() {
/*    */     try {
/* 43 */       return this.type.cast(newObjectMethod.invoke(dummyStream, new Object[] { this.type, this.superType }));
/*    */     }
/* 45 */     catch (Exception e) {
/* 46 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\objenesis\instantiator\gcj\GCJSerializationInstantiator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */