/*     */ package org.springframework.core.type.classreading;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.util.LinkedMultiValueMap;
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
/*     */ 
/*     */ abstract class AnnotationReadingVisitorUtils
/*     */ {
/*     */   public static AnnotationAttributes convertClassValues(Object annotatedElement, ClassLoader classLoader, AnnotationAttributes original, boolean classValuesAsString) {
/*  47 */     if (original == null) {
/*  48 */       return null;
/*     */     }
/*     */     
/*  51 */     AnnotationAttributes result = new AnnotationAttributes(original);
/*  52 */     AnnotationUtils.postProcessAnnotationAttributes(annotatedElement, result, classValuesAsString);
/*     */     
/*  54 */     for (Map.Entry<String, Object> entry : (Iterable<Map.Entry<String, Object>>)result.entrySet()) {
/*     */       try {
/*  56 */         Object value = entry.getValue();
/*  57 */         if (value instanceof AnnotationAttributes) {
/*  58 */           value = convertClassValues(annotatedElement, classLoader, (AnnotationAttributes)value, classValuesAsString);
/*     */         
/*     */         }
/*  61 */         else if (value instanceof AnnotationAttributes[]) {
/*  62 */           AnnotationAttributes[] values = (AnnotationAttributes[])value;
/*  63 */           for (int i = 0; i < values.length; i++) {
/*  64 */             values[i] = convertClassValues(annotatedElement, classLoader, values[i], classValuesAsString);
/*     */           }
/*  66 */           value = values;
/*     */         }
/*  68 */         else if (value instanceof Type) {
/*     */           
/*  70 */           value = classValuesAsString ? ((Type)value).getClassName() : classLoader.loadClass(((Type)value).getClassName());
/*     */         }
/*  72 */         else if (value instanceof Type[]) {
/*  73 */           Type[] array = (Type[])value;
/*  74 */           Object[] convArray = classValuesAsString ? (Object[])new String[array.length] : (Object[])new Class[array.length];
/*     */           
/*  76 */           for (int i = 0; i < array.length; i++) {
/*  77 */             convArray[i] = classValuesAsString ? array[i].getClassName() : classLoader
/*  78 */               .loadClass(array[i].getClassName());
/*     */           }
/*  80 */           value = convArray;
/*     */         }
/*  82 */         else if (classValuesAsString) {
/*  83 */           if (value instanceof Class) {
/*  84 */             value = ((Class)value).getName();
/*     */           }
/*  86 */           else if (value instanceof Class[]) {
/*  87 */             Class<?>[] clazzArray = (Class[])value;
/*  88 */             String[] newValue = new String[clazzArray.length];
/*  89 */             for (int i = 0; i < clazzArray.length; i++) {
/*  90 */               newValue[i] = clazzArray[i].getName();
/*     */             }
/*  92 */             value = newValue;
/*     */           } 
/*     */         } 
/*  95 */         entry.setValue(value);
/*     */       }
/*  97 */       catch (Throwable ex) {
/*     */         
/*  99 */         result.put(entry.getKey(), ex);
/*     */       } 
/*     */     } 
/*     */     
/* 103 */     return result;
/*     */   }
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
/*     */   public static AnnotationAttributes getMergedAnnotationAttributes(LinkedMultiValueMap<String, AnnotationAttributes> attributesMap, Map<String, Set<String>> metaAnnotationMap, String annotationName) {
/* 127 */     List<AnnotationAttributes> attributesList = attributesMap.get(annotationName);
/* 128 */     if (attributesList == null || attributesList.isEmpty()) {
/* 129 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 135 */     AnnotationAttributes result = new AnnotationAttributes(attributesList.get(0));
/*     */     
/* 137 */     Set<String> overridableAttributeNames = new HashSet<String>(result.keySet());
/* 138 */     overridableAttributeNames.remove("value");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 143 */     List<String> annotationTypes = new ArrayList<String>(attributesMap.keySet());
/* 144 */     Collections.reverse(annotationTypes);
/*     */ 
/*     */     
/* 147 */     annotationTypes.remove(annotationName);
/*     */     
/* 149 */     for (String currentAnnotationType : annotationTypes) {
/* 150 */       List<AnnotationAttributes> currentAttributesList = attributesMap.get(currentAnnotationType);
/* 151 */       if (!ObjectUtils.isEmpty(currentAttributesList)) {
/* 152 */         Set<String> metaAnns = metaAnnotationMap.get(currentAnnotationType);
/* 153 */         if (metaAnns != null && metaAnns.contains(annotationName)) {
/* 154 */           AnnotationAttributes currentAttributes = currentAttributesList.get(0);
/* 155 */           for (String overridableAttributeName : overridableAttributeNames) {
/* 156 */             Object value = currentAttributes.get(overridableAttributeName);
/* 157 */             if (value != null)
/*     */             {
/*     */               
/* 160 */               result.put(overridableAttributeName, value);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 167 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\type\classreading\AnnotationReadingVisitorUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */