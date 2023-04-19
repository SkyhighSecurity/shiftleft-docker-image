/*    */ package org.springframework.scheduling.annotation;
/*    */ 
/*    */ import org.springframework.context.annotation.AdviceMode;
/*    */ import org.springframework.context.annotation.AdviceModeImportSelector;
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
/*    */ public class AsyncConfigurationSelector
/*    */   extends AdviceModeImportSelector<EnableAsync>
/*    */ {
/*    */   private static final String ASYNC_EXECUTION_ASPECT_CONFIGURATION_CLASS_NAME = "org.springframework.scheduling.aspectj.AspectJAsyncConfiguration";
/*    */   
/*    */   public String[] selectImports(AdviceMode adviceMode) {
/* 46 */     switch (adviceMode) {
/*    */       case PROXY:
/* 48 */         return new String[] { ProxyAsyncConfiguration.class.getName() };
/*    */       case ASPECTJ:
/* 50 */         return new String[] { "org.springframework.scheduling.aspectj.AspectJAsyncConfiguration" };
/*    */     } 
/* 52 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\scheduling\annotation\AsyncConfigurationSelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */