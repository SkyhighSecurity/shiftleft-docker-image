/*     */ package org.springframework.beans.propertyeditors;
/*     */ 
/*     */ import java.beans.PropertyEditorSupport;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
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
/*     */ public class CustomMapEditor
/*     */   extends PropertyEditorSupport
/*     */ {
/*     */   private final Class<? extends Map> mapType;
/*     */   private final boolean nullAsEmptyMap;
/*     */   
/*     */   public CustomMapEditor(Class<? extends Map> mapType) {
/*  54 */     this(mapType, false);
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
/*     */   public CustomMapEditor(Class<? extends Map> mapType, boolean nullAsEmptyMap) {
/*  76 */     if (mapType == null) {
/*  77 */       throw new IllegalArgumentException("Map type is required");
/*     */     }
/*  79 */     if (!Map.class.isAssignableFrom(mapType)) {
/*  80 */       throw new IllegalArgumentException("Map type [" + mapType
/*  81 */           .getName() + "] does not implement [java.util.Map]");
/*     */     }
/*  83 */     this.mapType = mapType;
/*  84 */     this.nullAsEmptyMap = nullAsEmptyMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAsText(String text) throws IllegalArgumentException {
/*  93 */     setValue(text);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(Object value) {
/* 101 */     if (value == null && this.nullAsEmptyMap) {
/* 102 */       super.setValue(createMap(this.mapType, 0));
/*     */     }
/* 104 */     else if (value == null || (this.mapType.isInstance(value) && !alwaysCreateNewMap())) {
/*     */       
/* 106 */       super.setValue(value);
/*     */     }
/* 108 */     else if (value instanceof Map) {
/*     */       
/* 110 */       Map<?, ?> source = (Map<?, ?>)value;
/* 111 */       Map<Object, Object> target = createMap(this.mapType, source.size());
/* 112 */       for (Map.Entry<?, ?> entry : source.entrySet()) {
/* 113 */         target.put(convertKey(entry.getKey()), convertValue(entry.getValue()));
/*     */       }
/* 115 */       super.setValue(target);
/*     */     } else {
/*     */       
/* 118 */       throw new IllegalArgumentException("Value cannot be converted to Map: " + value);
/*     */     } 
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
/*     */   protected Map<Object, Object> createMap(Class<? extends Map> mapType, int initialCapacity) {
/* 131 */     if (!mapType.isInterface()) {
/*     */       try {
/* 133 */         return mapType.newInstance();
/*     */       }
/* 135 */       catch (Throwable ex) {
/* 136 */         throw new IllegalArgumentException("Could not instantiate map class: " + mapType
/* 137 */             .getName(), ex);
/*     */       } 
/*     */     }
/* 140 */     if (SortedMap.class == mapType) {
/* 141 */       return new TreeMap<Object, Object>();
/*     */     }
/*     */     
/* 144 */     return new LinkedHashMap<Object, Object>(initialCapacity);
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
/*     */   protected boolean alwaysCreateNewMap() {
/* 157 */     return false;
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
/*     */   protected Object convertKey(Object key) {
/* 174 */     return key;
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
/*     */   protected Object convertValue(Object value) {
/* 191 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAsText() {
/* 201 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\propertyeditors\CustomMapEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */