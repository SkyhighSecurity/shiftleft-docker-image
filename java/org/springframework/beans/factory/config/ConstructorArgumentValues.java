/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.BeanMetadataElement;
/*     */ import org.springframework.beans.Mergeable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class ConstructorArgumentValues
/*     */ {
/*  45 */   private final Map<Integer, ValueHolder> indexedArgumentValues = new LinkedHashMap<Integer, ValueHolder>(0);
/*     */   
/*  47 */   private final List<ValueHolder> genericArgumentValues = new LinkedList<ValueHolder>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstructorArgumentValues() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstructorArgumentValues(ConstructorArgumentValues original) {
/*  61 */     addArgumentValues(original);
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
/*     */   public void addArgumentValues(ConstructorArgumentValues other) {
/*  73 */     if (other != null) {
/*  74 */       for (Map.Entry<Integer, ValueHolder> entry : other.indexedArgumentValues.entrySet()) {
/*  75 */         addOrMergeIndexedArgumentValue(entry.getKey(), ((ValueHolder)entry.getValue()).copy());
/*     */       }
/*  77 */       for (ValueHolder valueHolder : other.genericArgumentValues) {
/*  78 */         if (!this.genericArgumentValues.contains(valueHolder)) {
/*  79 */           addOrMergeGenericArgumentValue(valueHolder.copy());
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addIndexedArgumentValue(int index, Object value) {
/*  92 */     addIndexedArgumentValue(index, new ValueHolder(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addIndexedArgumentValue(int index, Object value, String type) {
/* 102 */     addIndexedArgumentValue(index, new ValueHolder(value, type));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addIndexedArgumentValue(int index, ValueHolder newValue) {
/* 111 */     Assert.isTrue((index >= 0), "Index must not be negative");
/* 112 */     Assert.notNull(newValue, "ValueHolder must not be null");
/* 113 */     addOrMergeIndexedArgumentValue(Integer.valueOf(index), newValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addOrMergeIndexedArgumentValue(Integer key, ValueHolder newValue) {
/* 124 */     ValueHolder currentValue = this.indexedArgumentValues.get(key);
/* 125 */     if (currentValue != null && newValue.getValue() instanceof Mergeable) {
/* 126 */       Mergeable mergeable = (Mergeable)newValue.getValue();
/* 127 */       if (mergeable.isMergeEnabled()) {
/* 128 */         newValue.setValue(mergeable.merge(currentValue.getValue()));
/*     */       }
/*     */     } 
/* 131 */     this.indexedArgumentValues.put(key, newValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasIndexedArgumentValue(int index) {
/* 139 */     return this.indexedArgumentValues.containsKey(Integer.valueOf(index));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValueHolder getIndexedArgumentValue(int index, Class<?> requiredType) {
/* 150 */     return getIndexedArgumentValue(index, requiredType, null);
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
/*     */   public ValueHolder getIndexedArgumentValue(int index, Class<?> requiredType, String requiredName) {
/* 163 */     Assert.isTrue((index >= 0), "Index must not be negative");
/* 164 */     ValueHolder valueHolder = this.indexedArgumentValues.get(Integer.valueOf(index));
/* 165 */     if (valueHolder != null && (valueHolder
/* 166 */       .getType() == null || (requiredType != null && 
/* 167 */       ClassUtils.matchesTypeName(requiredType, valueHolder.getType()))) && (valueHolder
/* 168 */       .getName() == null || "".equals(requiredName) || (requiredName != null && requiredName
/* 169 */       .equals(valueHolder.getName())))) {
/* 170 */       return valueHolder;
/*     */     }
/* 172 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<Integer, ValueHolder> getIndexedArgumentValues() {
/* 181 */     return Collections.unmodifiableMap(this.indexedArgumentValues);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addGenericArgumentValue(Object value) {
/* 192 */     this.genericArgumentValues.add(new ValueHolder(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addGenericArgumentValue(Object value, String type) {
/* 203 */     this.genericArgumentValues.add(new ValueHolder(value, type));
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
/*     */   public void addGenericArgumentValue(ValueHolder newValue) {
/* 216 */     Assert.notNull(newValue, "ValueHolder must not be null");
/* 217 */     if (!this.genericArgumentValues.contains(newValue)) {
/* 218 */       addOrMergeGenericArgumentValue(newValue);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addOrMergeGenericArgumentValue(ValueHolder newValue) {
/* 228 */     if (newValue.getName() != null) {
/* 229 */       for (Iterator<ValueHolder> it = this.genericArgumentValues.iterator(); it.hasNext(); ) {
/* 230 */         ValueHolder currentValue = it.next();
/* 231 */         if (newValue.getName().equals(currentValue.getName())) {
/* 232 */           if (newValue.getValue() instanceof Mergeable) {
/* 233 */             Mergeable mergeable = (Mergeable)newValue.getValue();
/* 234 */             if (mergeable.isMergeEnabled()) {
/* 235 */               newValue.setValue(mergeable.merge(currentValue.getValue()));
/*     */             }
/*     */           } 
/* 238 */           it.remove();
/*     */         } 
/*     */       } 
/*     */     }
/* 242 */     this.genericArgumentValues.add(newValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValueHolder getGenericArgumentValue(Class<?> requiredType) {
/* 251 */     return getGenericArgumentValue(requiredType, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValueHolder getGenericArgumentValue(Class<?> requiredType, String requiredName) {
/* 261 */     return getGenericArgumentValue(requiredType, requiredName, null);
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
/*     */   public ValueHolder getGenericArgumentValue(Class<?> requiredType, String requiredName, Set<ValueHolder> usedValueHolders) {
/* 277 */     for (ValueHolder valueHolder : this.genericArgumentValues) {
/* 278 */       if (usedValueHolders != null && usedValueHolders.contains(valueHolder)) {
/*     */         continue;
/*     */       }
/* 281 */       if (valueHolder.getName() != null && !"".equals(requiredName) && (requiredName == null || 
/* 282 */         !valueHolder.getName().equals(requiredName))) {
/*     */         continue;
/*     */       }
/* 285 */       if (valueHolder.getType() != null && (requiredType == null || 
/* 286 */         !ClassUtils.matchesTypeName(requiredType, valueHolder.getType()))) {
/*     */         continue;
/*     */       }
/* 289 */       if (requiredType != null && valueHolder.getType() == null && valueHolder.getName() == null && 
/* 290 */         !ClassUtils.isAssignableValue(requiredType, valueHolder.getValue())) {
/*     */         continue;
/*     */       }
/* 293 */       return valueHolder;
/*     */     } 
/* 295 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ValueHolder> getGenericArgumentValues() {
/* 304 */     return Collections.unmodifiableList(this.genericArgumentValues);
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
/*     */   public ValueHolder getArgumentValue(int index, Class<?> requiredType) {
/* 316 */     return getArgumentValue(index, requiredType, null, null);
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
/*     */   public ValueHolder getArgumentValue(int index, Class<?> requiredType, String requiredName) {
/* 328 */     return getArgumentValue(index, requiredType, requiredName, null);
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
/*     */   public ValueHolder getArgumentValue(int index, Class<?> requiredType, String requiredName, Set<ValueHolder> usedValueHolders) {
/* 346 */     Assert.isTrue((index >= 0), "Index must not be negative");
/* 347 */     ValueHolder valueHolder = getIndexedArgumentValue(index, requiredType, requiredName);
/* 348 */     if (valueHolder == null) {
/* 349 */       valueHolder = getGenericArgumentValue(requiredType, requiredName, usedValueHolders);
/*     */     }
/* 351 */     return valueHolder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getArgumentCount() {
/* 359 */     return this.indexedArgumentValues.size() + this.genericArgumentValues.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 367 */     return (this.indexedArgumentValues.isEmpty() && this.genericArgumentValues.isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 374 */     this.indexedArgumentValues.clear();
/* 375 */     this.genericArgumentValues.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 381 */     if (this == other) {
/* 382 */       return true;
/*     */     }
/* 384 */     if (!(other instanceof ConstructorArgumentValues)) {
/* 385 */       return false;
/*     */     }
/* 387 */     ConstructorArgumentValues that = (ConstructorArgumentValues)other;
/* 388 */     if (this.genericArgumentValues.size() != that.genericArgumentValues.size() || this.indexedArgumentValues
/* 389 */       .size() != that.indexedArgumentValues.size()) {
/* 390 */       return false;
/*     */     }
/* 392 */     Iterator<ValueHolder> it1 = this.genericArgumentValues.iterator();
/* 393 */     Iterator<ValueHolder> it2 = that.genericArgumentValues.iterator();
/* 394 */     while (it1.hasNext() && it2.hasNext()) {
/* 395 */       ValueHolder vh1 = it1.next();
/* 396 */       ValueHolder vh2 = it2.next();
/* 397 */       if (!vh1.contentEquals(vh2)) {
/* 398 */         return false;
/*     */       }
/*     */     } 
/* 401 */     for (Map.Entry<Integer, ValueHolder> entry : this.indexedArgumentValues.entrySet()) {
/* 402 */       ValueHolder vh1 = entry.getValue();
/* 403 */       ValueHolder vh2 = that.indexedArgumentValues.get(entry.getKey());
/* 404 */       if (!vh1.contentEquals(vh2)) {
/* 405 */         return false;
/*     */       }
/*     */     } 
/* 408 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 413 */     int hashCode = 7;
/* 414 */     for (ValueHolder valueHolder : this.genericArgumentValues) {
/* 415 */       hashCode = 31 * hashCode + valueHolder.contentHashCode();
/*     */     }
/* 417 */     hashCode = 29 * hashCode;
/* 418 */     for (Map.Entry<Integer, ValueHolder> entry : this.indexedArgumentValues.entrySet()) {
/* 419 */       hashCode = 31 * hashCode + (((ValueHolder)entry.getValue()).contentHashCode() ^ ((Integer)entry.getKey()).hashCode());
/*     */     }
/* 421 */     return hashCode;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class ValueHolder
/*     */     implements BeanMetadataElement
/*     */   {
/*     */     private Object value;
/*     */ 
/*     */     
/*     */     private String type;
/*     */ 
/*     */     
/*     */     private String name;
/*     */ 
/*     */     
/*     */     private Object source;
/*     */ 
/*     */     
/*     */     private boolean converted = false;
/*     */ 
/*     */     
/*     */     private Object convertedValue;
/*     */ 
/*     */     
/*     */     public ValueHolder(Object value) {
/* 448 */       this.value = value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ValueHolder(Object value, String type) {
/* 457 */       this.value = value;
/* 458 */       this.type = type;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ValueHolder(Object value, String type, String name) {
/* 468 */       this.value = value;
/* 469 */       this.type = type;
/* 470 */       this.name = name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setValue(Object value) {
/* 478 */       this.value = value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object getValue() {
/* 485 */       return this.value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setType(String type) {
/* 492 */       this.type = type;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getType() {
/* 499 */       return this.type;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setName(String name) {
/* 506 */       this.name = name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getName() {
/* 513 */       return this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setSource(Object source) {
/* 521 */       this.source = source;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object getSource() {
/* 526 */       return this.source;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public synchronized boolean isConverted() {
/* 534 */       return this.converted;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public synchronized void setConvertedValue(Object value) {
/* 542 */       this.converted = true;
/* 543 */       this.convertedValue = value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public synchronized Object getConvertedValue() {
/* 551 */       return this.convertedValue;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean contentEquals(ValueHolder other) {
/* 562 */       return (this == other || (
/* 563 */         ObjectUtils.nullSafeEquals(this.value, other.value) && ObjectUtils.nullSafeEquals(this.type, other.type)));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private int contentHashCode() {
/* 573 */       return ObjectUtils.nullSafeHashCode(this.value) * 29 + ObjectUtils.nullSafeHashCode(this.type);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ValueHolder copy() {
/* 581 */       ValueHolder copy = new ValueHolder(this.value, this.type, this.name);
/* 582 */       copy.setSource(this.source);
/* 583 */       return copy;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\config\ConstructorArgumentValues.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */