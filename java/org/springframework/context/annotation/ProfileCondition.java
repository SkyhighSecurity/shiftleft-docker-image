/*    */ package org.springframework.context.annotation;
/*    */ 
/*    */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*    */ import org.springframework.util.MultiValueMap;
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
/*    */ class ProfileCondition
/*    */   implements Condition
/*    */ {
/*    */   public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
/* 35 */     if (context.getEnvironment() != null) {
/* 36 */       MultiValueMap<String, Object> attrs = metadata.getAllAnnotationAttributes(Profile.class.getName());
/* 37 */       if (attrs != null) {
/* 38 */         for (Object value : attrs.get("value")) {
/* 39 */           if (context.getEnvironment().acceptsProfiles((String[])value)) {
/* 40 */             return true;
/*    */           }
/*    */         } 
/* 43 */         return false;
/*    */       } 
/*    */     } 
/* 46 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\annotation\ProfileCondition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */