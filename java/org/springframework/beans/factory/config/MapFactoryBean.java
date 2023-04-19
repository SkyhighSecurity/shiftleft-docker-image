/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.TypeConverter;
/*     */ import org.springframework.core.ResolvableType;
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
/*     */ public class MapFactoryBean
/*     */   extends AbstractFactoryBean<Map<Object, Object>>
/*     */ {
/*     */   private Map<?, ?> sourceMap;
/*     */   private Class<? extends Map> targetMapClass;
/*     */   
/*     */   public void setSourceMap(Map<?, ?> sourceMap) {
/*  47 */     this.sourceMap = sourceMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetMapClass(Class<? extends Map> targetMapClass) {
/*  58 */     if (targetMapClass == null) {
/*  59 */       throw new IllegalArgumentException("'targetMapClass' must not be null");
/*     */     }
/*  61 */     if (!Map.class.isAssignableFrom(targetMapClass)) {
/*  62 */       throw new IllegalArgumentException("'targetMapClass' must implement [java.util.Map]");
/*     */     }
/*  64 */     this.targetMapClass = targetMapClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<Map> getObjectType() {
/*  71 */     return Map.class;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map<Object, Object> createInstance() {
/*  77 */     if (this.sourceMap == null) {
/*  78 */       throw new IllegalArgumentException("'sourceMap' is required");
/*     */     }
/*  80 */     Map<Object, Object> result = null;
/*  81 */     if (this.targetMapClass != null) {
/*  82 */       result = (Map<Object, Object>)BeanUtils.instantiateClass(this.targetMapClass);
/*     */     } else {
/*     */       
/*  85 */       result = new LinkedHashMap<Object, Object>(this.sourceMap.size());
/*     */     } 
/*  87 */     Class<?> keyType = null;
/*  88 */     Class<?> valueType = null;
/*  89 */     if (this.targetMapClass != null) {
/*  90 */       ResolvableType mapType = ResolvableType.forClass(this.targetMapClass).asMap();
/*  91 */       keyType = mapType.resolveGeneric(new int[] { 0 });
/*  92 */       valueType = mapType.resolveGeneric(new int[] { 1 });
/*     */     } 
/*  94 */     if (keyType != null || valueType != null) {
/*  95 */       TypeConverter converter = getBeanTypeConverter();
/*  96 */       for (Map.Entry<?, ?> entry : this.sourceMap.entrySet()) {
/*  97 */         Object convertedKey = converter.convertIfNecessary(entry.getKey(), keyType);
/*  98 */         Object convertedValue = converter.convertIfNecessary(entry.getValue(), valueType);
/*  99 */         result.put(convertedKey, convertedValue);
/*     */       } 
/*     */     } else {
/*     */       
/* 103 */       result.putAll(this.sourceMap);
/*     */     } 
/* 105 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\config\MapFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */