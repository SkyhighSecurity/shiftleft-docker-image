/*    */ package org.springframework.core;
/*    */ 
/*    */ import org.springframework.util.Assert;
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
/*    */ public class NamedInheritableThreadLocal<T>
/*    */   extends InheritableThreadLocal<T>
/*    */ {
/*    */   private final String name;
/*    */   
/*    */   public NamedInheritableThreadLocal(String name) {
/* 39 */     Assert.hasText(name, "Name must not be empty");
/* 40 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 45 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\NamedInheritableThreadLocal.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */