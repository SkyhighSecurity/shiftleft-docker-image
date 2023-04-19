/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
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
/*     */ @Deprecated
/*     */ public abstract class GenericCollectionTypeResolver
/*     */ {
/*     */   public static Class<?> getCollectionType(Class<? extends Collection> collectionClass) {
/*  48 */     return ResolvableType.forClass(collectionClass).asCollection().resolveGeneric(new int[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<?> getMapKeyType(Class<? extends Map> mapClass) {
/*  59 */     return ResolvableType.forClass(mapClass).asMap().resolveGeneric(new int[] { 0 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<?> getMapValueType(Class<? extends Map> mapClass) {
/*  70 */     return ResolvableType.forClass(mapClass).asMap().resolveGeneric(new int[] { 1 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<?> getCollectionFieldType(Field collectionField) {
/*  79 */     return ResolvableType.forField(collectionField).asCollection().resolveGeneric(new int[0]);
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
/*     */   public static Class<?> getCollectionFieldType(Field collectionField, int nestingLevel) {
/*  91 */     return ResolvableType.forField(collectionField).getNested(nestingLevel).asCollection().resolveGeneric(new int[0]);
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
/*     */   @Deprecated
/*     */   public static Class<?> getCollectionFieldType(Field collectionField, int nestingLevel, Map<Integer, Integer> typeIndexesPerLevel) {
/* 107 */     return ResolvableType.forField(collectionField).getNested(nestingLevel, typeIndexesPerLevel).asCollection().resolveGeneric(new int[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<?> getMapKeyFieldType(Field mapField) {
/* 116 */     return ResolvableType.forField(mapField).asMap().resolveGeneric(new int[] { 0 });
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
/*     */   public static Class<?> getMapKeyFieldType(Field mapField, int nestingLevel) {
/* 128 */     return ResolvableType.forField(mapField).getNested(nestingLevel).asMap().resolveGeneric(new int[] { 0 });
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
/*     */   @Deprecated
/*     */   public static Class<?> getMapKeyFieldType(Field mapField, int nestingLevel, Map<Integer, Integer> typeIndexesPerLevel) {
/* 144 */     return ResolvableType.forField(mapField).getNested(nestingLevel, typeIndexesPerLevel).asMap().resolveGeneric(new int[] { 0 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<?> getMapValueFieldType(Field mapField) {
/* 153 */     return ResolvableType.forField(mapField).asMap().resolveGeneric(new int[] { 1 });
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
/*     */   public static Class<?> getMapValueFieldType(Field mapField, int nestingLevel) {
/* 165 */     return ResolvableType.forField(mapField).getNested(nestingLevel).asMap().resolveGeneric(new int[] { 1 });
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
/*     */   @Deprecated
/*     */   public static Class<?> getMapValueFieldType(Field mapField, int nestingLevel, Map<Integer, Integer> typeIndexesPerLevel) {
/* 181 */     return ResolvableType.forField(mapField).getNested(nestingLevel, typeIndexesPerLevel).asMap().resolveGeneric(new int[] { 1 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<?> getCollectionParameterType(MethodParameter methodParam) {
/* 190 */     return ResolvableType.forMethodParameter(methodParam).asCollection().resolveGeneric(new int[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<?> getMapKeyParameterType(MethodParameter methodParam) {
/* 199 */     return ResolvableType.forMethodParameter(methodParam).asMap().resolveGeneric(new int[] { 0 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<?> getMapValueParameterType(MethodParameter methodParam) {
/* 208 */     return ResolvableType.forMethodParameter(methodParam).asMap().resolveGeneric(new int[] { 1 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<?> getCollectionReturnType(Method method) {
/* 217 */     return ResolvableType.forMethodReturnType(method).asCollection().resolveGeneric(new int[0]);
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
/*     */   public static Class<?> getCollectionReturnType(Method method, int nestingLevel) {
/* 231 */     return ResolvableType.forMethodReturnType(method).getNested(nestingLevel).asCollection().resolveGeneric(new int[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<?> getMapKeyReturnType(Method method) {
/* 240 */     return ResolvableType.forMethodReturnType(method).asMap().resolveGeneric(new int[] { 0 });
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
/*     */   public static Class<?> getMapKeyReturnType(Method method, int nestingLevel) {
/* 252 */     return ResolvableType.forMethodReturnType(method).getNested(nestingLevel).asMap().resolveGeneric(new int[] { 0 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<?> getMapValueReturnType(Method method) {
/* 261 */     return ResolvableType.forMethodReturnType(method).asMap().resolveGeneric(new int[] { 1 });
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
/*     */   public static Class<?> getMapValueReturnType(Method method, int nestingLevel) {
/* 273 */     return ResolvableType.forMethodReturnType(method).getNested(nestingLevel).asMap().resolveGeneric(new int[] { 1 });
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\GenericCollectionTypeResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */