/*    */ package org.springframework.validation;
/*    */ 
/*    */ import java.util.Map;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class BindingResultUtils
/*    */ {
/*    */   public static BindingResult getBindingResult(Map<?, ?> model, String name) {
/* 40 */     Assert.notNull(model, "Model map must not be null");
/* 41 */     Assert.notNull(name, "Name must not be null");
/* 42 */     Object attr = model.get(BindingResult.MODEL_KEY_PREFIX + name);
/* 43 */     if (attr != null && !(attr instanceof BindingResult)) {
/* 44 */       throw new IllegalStateException("BindingResult attribute is not of type BindingResult: " + attr);
/*    */     }
/* 46 */     return (BindingResult)attr;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static BindingResult getRequiredBindingResult(Map<?, ?> model, String name) {
/* 57 */     BindingResult bindingResult = getBindingResult(model, name);
/* 58 */     if (bindingResult == null) {
/* 59 */       throw new IllegalStateException("No BindingResult attribute found for name '" + name + "'- have you exposed the correct model?");
/*    */     }
/*    */     
/* 62 */     return bindingResult;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\validation\BindingResultUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */