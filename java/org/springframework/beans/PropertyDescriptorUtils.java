/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.beans.IntrospectionException;
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Enumeration;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ class PropertyDescriptorUtils
/*     */ {
/*     */   public static void copyNonMethodProperties(PropertyDescriptor source, PropertyDescriptor target) throws IntrospectionException {
/*  40 */     target.setExpert(source.isExpert());
/*  41 */     target.setHidden(source.isHidden());
/*  42 */     target.setPreferred(source.isPreferred());
/*  43 */     target.setName(source.getName());
/*  44 */     target.setShortDescription(source.getShortDescription());
/*  45 */     target.setDisplayName(source.getDisplayName());
/*     */ 
/*     */     
/*  48 */     Enumeration<String> keys = source.attributeNames();
/*  49 */     while (keys.hasMoreElements()) {
/*  50 */       String key = keys.nextElement();
/*  51 */       target.setValue(key, source.getValue(key));
/*     */     } 
/*     */ 
/*     */     
/*  55 */     target.setPropertyEditorClass(source.getPropertyEditorClass());
/*  56 */     target.setBound(source.isBound());
/*  57 */     target.setConstrained(source.isConstrained());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<?> findPropertyType(Method readMethod, Method writeMethod) throws IntrospectionException {
/*  64 */     Class<?> propertyType = null;
/*     */     
/*  66 */     if (readMethod != null) {
/*  67 */       Class<?>[] params = readMethod.getParameterTypes();
/*  68 */       if (params.length != 0) {
/*  69 */         throw new IntrospectionException("Bad read method arg count: " + readMethod);
/*     */       }
/*  71 */       propertyType = readMethod.getReturnType();
/*  72 */       if (propertyType == void.class) {
/*  73 */         throw new IntrospectionException("Read method returns void: " + readMethod);
/*     */       }
/*     */     } 
/*     */     
/*  77 */     if (writeMethod != null) {
/*  78 */       Class<?>[] params = writeMethod.getParameterTypes();
/*  79 */       if (params.length != 1) {
/*  80 */         throw new IntrospectionException("Bad write method arg count: " + writeMethod);
/*     */       }
/*  82 */       if (propertyType != null) {
/*  83 */         if (propertyType.isAssignableFrom(params[0]))
/*     */         {
/*  85 */           propertyType = params[0];
/*     */         }
/*  87 */         else if (!params[0].isAssignableFrom(propertyType))
/*     */         {
/*     */ 
/*     */           
/*  91 */           throw new IntrospectionException("Type mismatch between read and write methods: " + readMethod + " - " + writeMethod);
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/*  96 */         propertyType = params[0];
/*     */       } 
/*     */     } 
/*     */     
/* 100 */     return propertyType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<?> findIndexedPropertyType(String name, Class<?> propertyType, Method indexedReadMethod, Method indexedWriteMethod) throws IntrospectionException {
/* 109 */     Class<?> indexedPropertyType = null;
/*     */     
/* 111 */     if (indexedReadMethod != null) {
/* 112 */       Class<?>[] params = indexedReadMethod.getParameterTypes();
/* 113 */       if (params.length != 1) {
/* 114 */         throw new IntrospectionException("Bad indexed read method arg count: " + indexedReadMethod);
/*     */       }
/* 116 */       if (params[0] != int.class) {
/* 117 */         throw new IntrospectionException("Non int index to indexed read method: " + indexedReadMethod);
/*     */       }
/* 119 */       indexedPropertyType = indexedReadMethod.getReturnType();
/* 120 */       if (indexedPropertyType == void.class) {
/* 121 */         throw new IntrospectionException("Indexed read method returns void: " + indexedReadMethod);
/*     */       }
/*     */     } 
/*     */     
/* 125 */     if (indexedWriteMethod != null) {
/* 126 */       Class<?>[] params = indexedWriteMethod.getParameterTypes();
/* 127 */       if (params.length != 2) {
/* 128 */         throw new IntrospectionException("Bad indexed write method arg count: " + indexedWriteMethod);
/*     */       }
/* 130 */       if (params[0] != int.class) {
/* 131 */         throw new IntrospectionException("Non int index to indexed write method: " + indexedWriteMethod);
/*     */       }
/* 133 */       if (indexedPropertyType != null) {
/* 134 */         if (indexedPropertyType.isAssignableFrom(params[1]))
/*     */         {
/* 136 */           indexedPropertyType = params[1];
/*     */         }
/* 138 */         else if (!params[1].isAssignableFrom(indexedPropertyType))
/*     */         {
/*     */ 
/*     */           
/* 142 */           throw new IntrospectionException("Type mismatch between indexed read and write methods: " + indexedReadMethod + " - " + indexedWriteMethod);
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 147 */         indexedPropertyType = params[1];
/*     */       } 
/*     */     } 
/*     */     
/* 151 */     if (propertyType != null && (!propertyType.isArray() || propertyType
/* 152 */       .getComponentType() != indexedPropertyType)) {
/* 153 */       throw new IntrospectionException("Type mismatch between indexed and non-indexed methods: " + indexedReadMethod + " - " + indexedWriteMethod);
/*     */     }
/*     */ 
/*     */     
/* 157 */     return indexedPropertyType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean equals(PropertyDescriptor pd, PropertyDescriptor otherPd) {
/* 167 */     return (ObjectUtils.nullSafeEquals(pd.getReadMethod(), otherPd.getReadMethod()) && 
/* 168 */       ObjectUtils.nullSafeEquals(pd.getWriteMethod(), otherPd.getWriteMethod()) && 
/* 169 */       ObjectUtils.nullSafeEquals(pd.getPropertyType(), otherPd.getPropertyType()) && 
/* 170 */       ObjectUtils.nullSafeEquals(pd.getPropertyEditorClass(), otherPd.getPropertyEditorClass()) && pd
/* 171 */       .isBound() == otherPd.isBound() && pd.isConstrained() == otherPd.isConstrained());
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\PropertyDescriptorUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */