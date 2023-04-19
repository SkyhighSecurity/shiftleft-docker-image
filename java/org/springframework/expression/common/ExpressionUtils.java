/*     */ package org.springframework.expression.common;
/*     */ 
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypeConverter;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public abstract class ExpressionUtils
/*     */ {
/*     */   public static <T> T convertTypedValue(EvaluationContext context, TypedValue typedValue, Class<T> targetType) {
/*  48 */     Object value = typedValue.getValue();
/*  49 */     if (targetType == null) {
/*  50 */       return (T)value;
/*     */     }
/*  52 */     if (context != null) {
/*  53 */       return (T)context.getTypeConverter().convertValue(value, typedValue
/*  54 */           .getTypeDescriptor(), TypeDescriptor.valueOf(targetType));
/*     */     }
/*  56 */     if (ClassUtils.isAssignableValue(targetType, value)) {
/*  57 */       return (T)value;
/*     */     }
/*  59 */     throw new EvaluationException("Cannot convert value '" + value + "' to type '" + targetType.getName() + "'");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int toInt(TypeConverter typeConverter, TypedValue typedValue) {
/*  66 */     return ((Integer)typeConverter.convertValue(typedValue.getValue(), typedValue.getTypeDescriptor(), 
/*  67 */         TypeDescriptor.valueOf(Integer.class))).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean toBoolean(TypeConverter typeConverter, TypedValue typedValue) {
/*  74 */     return ((Boolean)typeConverter.convertValue(typedValue.getValue(), typedValue.getTypeDescriptor(), 
/*  75 */         TypeDescriptor.valueOf(Boolean.class))).booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double toDouble(TypeConverter typeConverter, TypedValue typedValue) {
/*  82 */     return ((Double)typeConverter.convertValue(typedValue.getValue(), typedValue.getTypeDescriptor(), 
/*  83 */         TypeDescriptor.valueOf(Double.class))).doubleValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long toLong(TypeConverter typeConverter, TypedValue typedValue) {
/*  90 */     return ((Long)typeConverter.convertValue(typedValue.getValue(), typedValue.getTypeDescriptor(), 
/*  91 */         TypeDescriptor.valueOf(Long.class))).longValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static char toChar(TypeConverter typeConverter, TypedValue typedValue) {
/*  98 */     return ((Character)typeConverter.convertValue(typedValue.getValue(), typedValue.getTypeDescriptor(), 
/*  99 */         TypeDescriptor.valueOf(Character.class))).charValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static short toShort(TypeConverter typeConverter, TypedValue typedValue) {
/* 106 */     return ((Short)typeConverter.convertValue(typedValue.getValue(), typedValue.getTypeDescriptor(), 
/* 107 */         TypeDescriptor.valueOf(Short.class))).shortValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float toFloat(TypeConverter typeConverter, TypedValue typedValue) {
/* 114 */     return ((Float)typeConverter.convertValue(typedValue.getValue(), typedValue.getTypeDescriptor(), 
/* 115 */         TypeDescriptor.valueOf(Float.class))).floatValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte toByte(TypeConverter typeConverter, TypedValue typedValue) {
/* 122 */     return ((Byte)typeConverter.convertValue(typedValue.getValue(), typedValue.getTypeDescriptor(), 
/* 123 */         TypeDescriptor.valueOf(Byte.class))).byteValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\common\ExpressionUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */