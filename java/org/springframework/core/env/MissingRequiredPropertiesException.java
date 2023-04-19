/*    */ package org.springframework.core.env;
/*    */ 
/*    */ import java.util.LinkedHashSet;
/*    */ import java.util.Set;
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
/*    */ public class MissingRequiredPropertiesException
/*    */   extends IllegalStateException
/*    */ {
/* 34 */   private final Set<String> missingRequiredProperties = new LinkedHashSet<String>();
/*    */ 
/*    */   
/*    */   void addMissingRequiredProperty(String key) {
/* 38 */     this.missingRequiredProperties.add(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 43 */     return "The following properties were declared as required but could not be resolved: " + 
/* 44 */       getMissingRequiredProperties();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<String> getMissingRequiredProperties() {
/* 54 */     return this.missingRequiredProperties;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\env\MissingRequiredPropertiesException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */