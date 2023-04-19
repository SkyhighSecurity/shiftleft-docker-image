/*    */ package org.springframework.core;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
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
/*    */ public class PrioritizedParameterNameDiscoverer
/*    */   implements ParameterNameDiscoverer
/*    */ {
/* 37 */   private final List<ParameterNameDiscoverer> parameterNameDiscoverers = new LinkedList<ParameterNameDiscoverer>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addDiscoverer(ParameterNameDiscoverer pnd) {
/* 46 */     this.parameterNameDiscoverers.add(pnd);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String[] getParameterNames(Method method) {
/* 52 */     for (ParameterNameDiscoverer pnd : this.parameterNameDiscoverers) {
/* 53 */       String[] result = pnd.getParameterNames(method);
/* 54 */       if (result != null) {
/* 55 */         return result;
/*    */       }
/*    */     } 
/* 58 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public String[] getParameterNames(Constructor<?> ctor) {
/* 63 */     for (ParameterNameDiscoverer pnd : this.parameterNameDiscoverers) {
/* 64 */       String[] result = pnd.getParameterNames(ctor);
/* 65 */       if (result != null) {
/* 66 */         return result;
/*    */       }
/*    */     } 
/* 69 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\PrioritizedParameterNameDiscoverer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */