/*    */ package org.springframework.cache.support;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ 
/*    */ public final class NullValue
/*    */   implements Serializable
/*    */ {
/* 37 */   public static final Object INSTANCE = new NullValue();
/*    */ 
/*    */ 
/*    */   
/*    */   private static final long serialVersionUID = 1L;
/*    */ 
/*    */ 
/*    */   
/*    */   private Object readResolve() {
/* 46 */     return INSTANCE;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\support\NullValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */