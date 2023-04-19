/*    */ package org.apache.commons.collections.functors;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.apache.commons.collections.Transformer;
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
/*    */ public final class StringValueTransformer
/*    */   implements Transformer, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 7511110693171758606L;
/* 38 */   public static final Transformer INSTANCE = new StringValueTransformer();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Transformer getInstance() {
/* 47 */     return INSTANCE;
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
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object transform(Object input) {
/* 64 */     return String.valueOf(input);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\functors\StringValueTransformer.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */