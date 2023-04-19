/*    */ package org.springframework.objenesis;
/*    */ 
/*    */ import org.springframework.objenesis.strategy.InstantiatorStrategy;
/*    */ import org.springframework.objenesis.strategy.SerializingInstantiatorStrategy;
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
/*    */ public class ObjenesisSerializer
/*    */   extends ObjenesisBase
/*    */ {
/*    */   public ObjenesisSerializer() {
/* 31 */     super((InstantiatorStrategy)new SerializingInstantiatorStrategy());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ObjenesisSerializer(boolean useCache) {
/* 41 */     super((InstantiatorStrategy)new SerializingInstantiatorStrategy(), useCache);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\objenesis\ObjenesisSerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */