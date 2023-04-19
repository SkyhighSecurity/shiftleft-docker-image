/*    */ package org.springframework.util;
/*    */ 
/*    */ import java.util.UUID;
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
/*    */ public class JdkIdGenerator
/*    */   implements IdGenerator
/*    */ {
/*    */   public UUID generateId() {
/* 31 */     return UUID.randomUUID();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\JdkIdGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */