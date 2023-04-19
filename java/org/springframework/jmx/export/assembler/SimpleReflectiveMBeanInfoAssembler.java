/*    */ package org.springframework.jmx.export.assembler;
/*    */ 
/*    */ import java.lang.reflect.Method;
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
/*    */ public class SimpleReflectiveMBeanInfoAssembler
/*    */   extends AbstractConfigurableMBeanInfoAssembler
/*    */ {
/*    */   protected boolean includeReadAttribute(Method method, String beanKey) {
/* 37 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean includeWriteAttribute(Method method, String beanKey) {
/* 45 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean includeOperation(Method method, String beanKey) {
/* 53 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jmx\export\assembler\SimpleReflectiveMBeanInfoAssembler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */