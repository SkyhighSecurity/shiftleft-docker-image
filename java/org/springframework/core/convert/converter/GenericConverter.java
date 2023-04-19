/*     */ package org.springframework.core.convert.converter;
/*     */ 
/*     */ import java.util.Set;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface GenericConverter
/*     */ {
/*     */   Set<ConvertiblePair> getConvertibleTypes();
/*     */   
/*     */   Object convert(Object paramObject, TypeDescriptor paramTypeDescriptor1, TypeDescriptor paramTypeDescriptor2);
/*     */   
/*     */   public static final class ConvertiblePair
/*     */   {
/*     */     private final Class<?> sourceType;
/*     */     private final Class<?> targetType;
/*     */     
/*     */     public ConvertiblePair(Class<?> sourceType, Class<?> targetType) {
/*  82 */       Assert.notNull(sourceType, "Source type must not be null");
/*  83 */       Assert.notNull(targetType, "Target type must not be null");
/*  84 */       this.sourceType = sourceType;
/*  85 */       this.targetType = targetType;
/*     */     }
/*     */     
/*     */     public Class<?> getSourceType() {
/*  89 */       return this.sourceType;
/*     */     }
/*     */     
/*     */     public Class<?> getTargetType() {
/*  93 */       return this.targetType;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/*  98 */       if (this == other) {
/*  99 */         return true;
/*     */       }
/* 101 */       if (other == null || other.getClass() != ConvertiblePair.class) {
/* 102 */         return false;
/*     */       }
/* 104 */       ConvertiblePair otherPair = (ConvertiblePair)other;
/* 105 */       return (this.sourceType == otherPair.sourceType && this.targetType == otherPair.targetType);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 110 */       return this.sourceType.hashCode() * 31 + this.targetType.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 115 */       return this.sourceType.getName() + " -> " + this.targetType.getName();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\convert\converter\GenericConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */