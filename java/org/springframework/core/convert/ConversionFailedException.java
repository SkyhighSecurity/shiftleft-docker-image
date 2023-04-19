/*    */ package org.springframework.core.convert;
/*    */ 
/*    */ import org.springframework.util.ObjectUtils;
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
/*    */ public class ConversionFailedException
/*    */   extends ConversionException
/*    */ {
/*    */   private final TypeDescriptor sourceType;
/*    */   private final TypeDescriptor targetType;
/*    */   private final Object value;
/*    */   
/*    */   public ConversionFailedException(TypeDescriptor sourceType, TypeDescriptor targetType, Object value, Throwable cause) {
/* 46 */     super("Failed to convert from type [" + sourceType + "] to type [" + targetType + "] for value '" + 
/* 47 */         ObjectUtils.nullSafeToString(value) + "'", cause);
/* 48 */     this.sourceType = sourceType;
/* 49 */     this.targetType = targetType;
/* 50 */     this.value = value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeDescriptor getSourceType() {
/* 58 */     return this.sourceType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeDescriptor getTargetType() {
/* 65 */     return this.targetType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getValue() {
/* 72 */     return this.value;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\convert\ConversionFailedException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */