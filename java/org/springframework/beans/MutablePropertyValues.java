/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MutablePropertyValues
/*     */   implements PropertyValues, Serializable
/*     */ {
/*     */   private final List<PropertyValue> propertyValueList;
/*     */   private Set<String> processedProperties;
/*     */   private volatile boolean converted = false;
/*     */   
/*     */   public MutablePropertyValues() {
/*  54 */     this.propertyValueList = new ArrayList<PropertyValue>(0);
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
/*     */   public MutablePropertyValues(PropertyValues original) {
/*  67 */     if (original != null) {
/*  68 */       PropertyValue[] pvs = original.getPropertyValues();
/*  69 */       this.propertyValueList = new ArrayList<PropertyValue>(pvs.length);
/*  70 */       for (PropertyValue pv : pvs) {
/*  71 */         this.propertyValueList.add(new PropertyValue(pv));
/*     */       }
/*     */     } else {
/*     */       
/*  75 */       this.propertyValueList = new ArrayList<PropertyValue>(0);
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
/*     */   public MutablePropertyValues(Map<?, ?> original) {
/*  87 */     if (original != null) {
/*  88 */       this.propertyValueList = new ArrayList<PropertyValue>(original.size());
/*  89 */       for (Map.Entry<?, ?> entry : original.entrySet()) {
/*  90 */         this.propertyValueList.add(new PropertyValue(entry.getKey().toString(), entry.getValue()));
/*     */       }
/*     */     } else {
/*     */       
/*  94 */       this.propertyValueList = new ArrayList<PropertyValue>(0);
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
/*     */   public MutablePropertyValues(List<PropertyValue> propertyValueList) {
/* 106 */     this.propertyValueList = (propertyValueList != null) ? propertyValueList : new ArrayList<PropertyValue>();
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
/*     */   public List<PropertyValue> getPropertyValueList() {
/* 118 */     return this.propertyValueList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 125 */     return this.propertyValueList.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MutablePropertyValues addPropertyValues(PropertyValues other) {
/* 136 */     if (other != null) {
/* 137 */       PropertyValue[] pvs = other.getPropertyValues();
/* 138 */       for (PropertyValue pv : pvs) {
/* 139 */         addPropertyValue(new PropertyValue(pv));
/*     */       }
/*     */     } 
/* 142 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MutablePropertyValues addPropertyValues(Map<?, ?> other) {
/* 152 */     if (other != null) {
/* 153 */       for (Map.Entry<?, ?> entry : other.entrySet()) {
/* 154 */         addPropertyValue(new PropertyValue(entry.getKey().toString(), entry.getValue()));
/*     */       }
/*     */     }
/* 157 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MutablePropertyValues addPropertyValue(PropertyValue pv) {
/* 167 */     for (int i = 0; i < this.propertyValueList.size(); i++) {
/* 168 */       PropertyValue currentPv = this.propertyValueList.get(i);
/* 169 */       if (currentPv.getName().equals(pv.getName())) {
/* 170 */         pv = mergeIfRequired(pv, currentPv);
/* 171 */         setPropertyValueAt(pv, i);
/* 172 */         return this;
/*     */       } 
/*     */     } 
/* 175 */     this.propertyValueList.add(pv);
/* 176 */     return this;
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
/*     */   public void addPropertyValue(String propertyName, Object propertyValue) {
/* 189 */     addPropertyValue(new PropertyValue(propertyName, propertyValue));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MutablePropertyValues add(String propertyName, Object propertyValue) {
/* 200 */     addPropertyValue(new PropertyValue(propertyName, propertyValue));
/* 201 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPropertyValueAt(PropertyValue pv, int i) {
/* 209 */     this.propertyValueList.set(i, pv);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PropertyValue mergeIfRequired(PropertyValue newPv, PropertyValue currentPv) {
/* 218 */     Object value = newPv.getValue();
/* 219 */     if (value instanceof Mergeable) {
/* 220 */       Mergeable mergeable = (Mergeable)value;
/* 221 */       if (mergeable.isMergeEnabled()) {
/* 222 */         Object merged = mergeable.merge(currentPv.getValue());
/* 223 */         return new PropertyValue(newPv.getName(), merged);
/*     */       } 
/*     */     } 
/* 226 */     return newPv;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removePropertyValue(PropertyValue pv) {
/* 234 */     this.propertyValueList.remove(pv);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removePropertyValue(String propertyName) {
/* 243 */     this.propertyValueList.remove(getPropertyValue(propertyName));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyValue[] getPropertyValues() {
/* 249 */     return this.propertyValueList.<PropertyValue>toArray(new PropertyValue[this.propertyValueList.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public PropertyValue getPropertyValue(String propertyName) {
/* 254 */     for (PropertyValue pv : this.propertyValueList) {
/* 255 */       if (pv.getName().equals(propertyName)) {
/* 256 */         return pv;
/*     */       }
/*     */     } 
/* 259 */     return null;
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
/*     */   public Object get(String propertyName) {
/* 271 */     PropertyValue pv = getPropertyValue(propertyName);
/* 272 */     return (pv != null) ? pv.getValue() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public PropertyValues changesSince(PropertyValues old) {
/* 277 */     MutablePropertyValues changes = new MutablePropertyValues();
/* 278 */     if (old == this) {
/* 279 */       return changes;
/*     */     }
/*     */ 
/*     */     
/* 283 */     for (PropertyValue newPv : this.propertyValueList) {
/*     */       
/* 285 */       PropertyValue pvOld = old.getPropertyValue(newPv.getName());
/* 286 */       if (pvOld == null || !pvOld.equals(newPv)) {
/* 287 */         changes.addPropertyValue(newPv);
/*     */       }
/*     */     } 
/* 290 */     return changes;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(String propertyName) {
/* 295 */     return (getPropertyValue(propertyName) != null || (this.processedProperties != null && this.processedProperties
/* 296 */       .contains(propertyName)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 301 */     return this.propertyValueList.isEmpty();
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
/*     */   public void registerProcessedProperty(String propertyName) {
/* 314 */     if (this.processedProperties == null) {
/* 315 */       this.processedProperties = new HashSet<String>();
/*     */     }
/* 317 */     this.processedProperties.add(propertyName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearProcessedProperty(String propertyName) {
/* 325 */     if (this.processedProperties != null) {
/* 326 */       this.processedProperties.remove(propertyName);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConverted() {
/* 335 */     this.converted = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConverted() {
/* 343 */     return this.converted;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 349 */     if (this == other) {
/* 350 */       return true;
/*     */     }
/* 352 */     if (!(other instanceof MutablePropertyValues)) {
/* 353 */       return false;
/*     */     }
/* 355 */     MutablePropertyValues that = (MutablePropertyValues)other;
/* 356 */     return this.propertyValueList.equals(that.propertyValueList);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 361 */     return this.propertyValueList.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 366 */     PropertyValue[] pvs = getPropertyValues();
/* 367 */     StringBuilder sb = (new StringBuilder("PropertyValues: length=")).append(pvs.length);
/* 368 */     if (pvs.length > 0) {
/* 369 */       sb.append("; ").append(StringUtils.arrayToDelimitedString((Object[])pvs, "; "));
/*     */     }
/* 371 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\MutablePropertyValues.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */