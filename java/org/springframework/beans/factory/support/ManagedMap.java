/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.beans.BeanMetadataElement;
/*     */ import org.springframework.beans.Mergeable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ManagedMap<K, V>
/*     */   extends LinkedHashMap<K, V>
/*     */   implements Mergeable, BeanMetadataElement
/*     */ {
/*     */   private Object source;
/*     */   private String keyTypeName;
/*     */   private String valueTypeName;
/*     */   private boolean mergeEnabled;
/*     */   
/*     */   public ManagedMap() {}
/*     */   
/*     */   public ManagedMap(int initialCapacity) {
/*  49 */     super(initialCapacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSource(Object source) {
/*  58 */     this.source = source;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getSource() {
/*  63 */     return this.source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeyTypeName(String keyTypeName) {
/*  70 */     this.keyTypeName = keyTypeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getKeyTypeName() {
/*  77 */     return this.keyTypeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValueTypeName(String valueTypeName) {
/*  84 */     this.valueTypeName = valueTypeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getValueTypeName() {
/*  91 */     return this.valueTypeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMergeEnabled(boolean mergeEnabled) {
/*  99 */     this.mergeEnabled = mergeEnabled;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMergeEnabled() {
/* 104 */     return this.mergeEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object merge(Object parent) {
/* 110 */     if (!this.mergeEnabled) {
/* 111 */       throw new IllegalStateException("Not allowed to merge when the 'mergeEnabled' property is set to 'false'");
/*     */     }
/* 113 */     if (parent == null) {
/* 114 */       return this;
/*     */     }
/* 116 */     if (!(parent instanceof Map)) {
/* 117 */       throw new IllegalArgumentException("Cannot merge with object of type [" + parent.getClass() + "]");
/*     */     }
/* 119 */     Map<K, V> merged = new ManagedMap();
/* 120 */     merged.putAll((Map<? extends K, ? extends V>)parent);
/* 121 */     merged.putAll(this);
/* 122 */     return merged;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\support\ManagedMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */