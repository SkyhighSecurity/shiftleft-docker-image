/*     */ package org.springframework.core.convert.support;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.core.CollectionFactory;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.core.convert.converter.ConditionalGenericConverter;
/*     */ import org.springframework.core.convert.converter.GenericConverter;
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
/*     */ final class MapToMapConverter
/*     */   implements ConditionalGenericConverter
/*     */ {
/*     */   private final ConversionService conversionService;
/*     */   
/*     */   public MapToMapConverter(ConversionService conversionService) {
/*  48 */     this.conversionService = conversionService;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/*  54 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Map.class, Map.class));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/*  59 */     return (canConvertKey(sourceType, targetType) && canConvertValue(sourceType, targetType));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/*  65 */     if (source == null) {
/*  66 */       return null;
/*     */     }
/*  68 */     Map<Object, Object> sourceMap = (Map<Object, Object>)source;
/*     */ 
/*     */     
/*  71 */     boolean copyRequired = !targetType.getType().isInstance(source);
/*  72 */     if (!copyRequired && sourceMap.isEmpty()) {
/*  73 */       return sourceMap;
/*     */     }
/*  75 */     TypeDescriptor keyDesc = targetType.getMapKeyTypeDescriptor();
/*  76 */     TypeDescriptor valueDesc = targetType.getMapValueTypeDescriptor();
/*     */     
/*  78 */     List<MapEntry> targetEntries = new ArrayList<MapEntry>(sourceMap.size());
/*  79 */     for (Map.Entry<Object, Object> entry : sourceMap.entrySet()) {
/*  80 */       Object sourceKey = entry.getKey();
/*  81 */       Object sourceValue = entry.getValue();
/*  82 */       Object targetKey = convertKey(sourceKey, sourceType, keyDesc);
/*  83 */       Object targetValue = convertValue(sourceValue, sourceType, valueDesc);
/*  84 */       targetEntries.add(new MapEntry(targetKey, targetValue));
/*  85 */       if (sourceKey != targetKey || sourceValue != targetValue) {
/*  86 */         copyRequired = true;
/*     */       }
/*     */     } 
/*  89 */     if (!copyRequired) {
/*  90 */       return sourceMap;
/*     */     }
/*     */     
/*  93 */     Map<Object, Object> targetMap = CollectionFactory.createMap(targetType.getType(), (keyDesc != null) ? keyDesc
/*  94 */         .getType() : null, sourceMap.size());
/*     */     
/*  96 */     for (MapEntry entry : targetEntries) {
/*  97 */       entry.addToMap(targetMap);
/*     */     }
/*  99 */     return targetMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean canConvertKey(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 106 */     return ConversionUtils.canConvertElements(sourceType.getMapKeyTypeDescriptor(), targetType
/* 107 */         .getMapKeyTypeDescriptor(), this.conversionService);
/*     */   }
/*     */   
/*     */   private boolean canConvertValue(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 111 */     return ConversionUtils.canConvertElements(sourceType.getMapValueTypeDescriptor(), targetType
/* 112 */         .getMapValueTypeDescriptor(), this.conversionService);
/*     */   }
/*     */   
/*     */   private Object convertKey(Object sourceKey, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 116 */     if (targetType == null) {
/* 117 */       return sourceKey;
/*     */     }
/* 119 */     return this.conversionService.convert(sourceKey, sourceType.getMapKeyTypeDescriptor(sourceKey), targetType);
/*     */   }
/*     */   
/*     */   private Object convertValue(Object sourceValue, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 123 */     if (targetType == null) {
/* 124 */       return sourceValue;
/*     */     }
/* 126 */     return this.conversionService.convert(sourceValue, sourceType.getMapValueTypeDescriptor(sourceValue), targetType);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class MapEntry
/*     */   {
/*     */     private final Object key;
/*     */     
/*     */     private final Object value;
/*     */     
/*     */     public MapEntry(Object key, Object value) {
/* 137 */       this.key = key;
/* 138 */       this.value = value;
/*     */     }
/*     */     
/*     */     public void addToMap(Map<Object, Object> map) {
/* 142 */       map.put(this.key, this.value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\convert\support\MapToMapConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */