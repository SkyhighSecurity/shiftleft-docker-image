/*    */ package org.springframework.web.multipart.support;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MissingServletRequestPartException
/*    */   extends ServletException
/*    */ {
/*    */   private final String partName;
/*    */   
/*    */   public MissingServletRequestPartException(String partName) {
/* 42 */     super("Required request part '" + partName + "' is not present");
/* 43 */     this.partName = partName;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getRequestPartName() {
/* 48 */     return this.partName;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\multipart\support\MissingServletRequestPartException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */