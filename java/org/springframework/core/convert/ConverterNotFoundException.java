/*    */ package org.springframework.core.convert;
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
/*    */ public class ConverterNotFoundException
/*    */   extends ConversionException
/*    */ {
/*    */   private final TypeDescriptor sourceType;
/*    */   private final TypeDescriptor targetType;
/*    */   
/*    */   public ConverterNotFoundException(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 41 */     super("No converter found capable of converting from type [" + sourceType + "] to type [" + targetType + "]");
/* 42 */     this.sourceType = sourceType;
/* 43 */     this.targetType = targetType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeDescriptor getSourceType() {
/* 51 */     return this.sourceType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeDescriptor getTargetType() {
/* 58 */     return this.targetType;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\convert\ConverterNotFoundException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */