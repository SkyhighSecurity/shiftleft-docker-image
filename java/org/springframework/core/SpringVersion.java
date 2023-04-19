/*    */ package org.springframework.core;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SpringVersion
/*    */ {
/*    */   public static String getVersion() {
/* 40 */     Package pkg = SpringVersion.class.getPackage();
/* 41 */     return (pkg != null) ? pkg.getImplementationVersion() : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\SpringVersion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */