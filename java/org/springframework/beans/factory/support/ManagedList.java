/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class ManagedList<E>
/*     */   extends ArrayList<E>
/*     */   implements Mergeable, BeanMetadataElement
/*     */ {
/*     */   private Object source;
/*     */   private String elementTypeName;
/*     */   private boolean mergeEnabled;
/*     */   
/*     */   public ManagedList() {}
/*     */   
/*     */   public ManagedList(int initialCapacity) {
/*  48 */     super(initialCapacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSource(Object source) {
/*  57 */     this.source = source;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getSource() {
/*  62 */     return this.source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setElementTypeName(String elementTypeName) {
/*  69 */     this.elementTypeName = elementTypeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getElementTypeName() {
/*  76 */     return this.elementTypeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMergeEnabled(boolean mergeEnabled) {
/*  84 */     this.mergeEnabled = mergeEnabled;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMergeEnabled() {
/*  89 */     return this.mergeEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<E> merge(Object parent) {
/*  95 */     if (!this.mergeEnabled) {
/*  96 */       throw new IllegalStateException("Not allowed to merge when the 'mergeEnabled' property is set to 'false'");
/*     */     }
/*  98 */     if (parent == null) {
/*  99 */       return this;
/*     */     }
/* 101 */     if (!(parent instanceof List)) {
/* 102 */       throw new IllegalArgumentException("Cannot merge with object of type [" + parent.getClass() + "]");
/*     */     }
/* 104 */     List<E> merged = new ManagedList();
/* 105 */     merged.addAll((List)parent);
/* 106 */     merged.addAll(this);
/* 107 */     return merged;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\support\ManagedList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */