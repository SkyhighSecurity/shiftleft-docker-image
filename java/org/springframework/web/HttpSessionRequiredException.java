/*    */ package org.springframework.web;
/*    */ 
/*    */ import javax.servlet.ServletException;
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
/*    */ public class HttpSessionRequiredException
/*    */   extends ServletException
/*    */ {
/*    */   private String expectedAttribute;
/*    */   
/*    */   public HttpSessionRequiredException(String msg) {
/* 38 */     super(msg);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpSessionRequiredException(String msg, String expectedAttribute) {
/* 48 */     super(msg);
/* 49 */     this.expectedAttribute = expectedAttribute;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getExpectedAttribute() {
/* 58 */     return this.expectedAttribute;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\HttpSessionRequiredException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */