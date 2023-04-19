/*    */ package org.springframework.core;
/*    */ 
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ public class DefaultParameterNameDiscoverer
/*    */   extends PrioritizedParameterNameDiscoverer
/*    */ {
/* 36 */   private static final boolean standardReflectionAvailable = ClassUtils.isPresent("java.lang.reflect.Executable", DefaultParameterNameDiscoverer.class
/* 37 */       .getClassLoader());
/*    */ 
/*    */   
/*    */   public DefaultParameterNameDiscoverer() {
/* 41 */     if (standardReflectionAvailable) {
/* 42 */       addDiscoverer(new StandardReflectionParameterNameDiscoverer());
/*    */     }
/* 44 */     addDiscoverer(new LocalVariableTableParameterNameDiscoverer());
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\DefaultParameterNameDiscoverer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */