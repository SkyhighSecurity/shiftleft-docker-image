/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.beans.PropertyEditorSupport;
/*    */ import org.springframework.core.convert.ConversionService;
/*    */ import org.springframework.core.convert.TypeDescriptor;
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
/*    */ 
/*    */ 
/*    */ public class ConvertingPropertyEditorAdapter
/*    */   extends PropertyEditorSupport
/*    */ {
/*    */   private final ConversionService conversionService;
/*    */   private final TypeDescriptor targetDescriptor;
/*    */   private final boolean canConvertToString;
/*    */   
/*    */   public ConvertingPropertyEditorAdapter(ConversionService conversionService, TypeDescriptor targetDescriptor) {
/* 49 */     Assert.notNull(conversionService, "ConversionService must not be null");
/* 50 */     Assert.notNull(targetDescriptor, "TypeDescriptor must not be null");
/* 51 */     this.conversionService = conversionService;
/* 52 */     this.targetDescriptor = targetDescriptor;
/* 53 */     this.canConvertToString = conversionService.canConvert(this.targetDescriptor, TypeDescriptor.valueOf(String.class));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setAsText(String text) throws IllegalArgumentException {
/* 59 */     setValue(this.conversionService.convert(text, TypeDescriptor.valueOf(String.class), this.targetDescriptor));
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAsText() {
/* 64 */     if (this.canConvertToString) {
/* 65 */       return (String)this.conversionService.convert(getValue(), this.targetDescriptor, TypeDescriptor.valueOf(String.class));
/*    */     }
/*    */     
/* 68 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\convert\support\ConvertingPropertyEditorAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */