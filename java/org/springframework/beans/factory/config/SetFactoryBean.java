/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
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
/*     */ public class SetFactoryBean
/*     */   extends AbstractFactoryBean<Set<Object>>
/*     */ {
/*     */   private Set<?> sourceSet;
/*     */   private Class<? extends Set> targetSetClass;
/*     */   
/*     */   public void setSourceSet(Set<?> sourceSet) {
/*  47 */     this.sourceSet = sourceSet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetSetClass(Class<? extends Set> targetSetClass) {
/*  58 */     if (targetSetClass == null) {
/*  59 */       throw new IllegalArgumentException("'targetSetClass' must not be null");
/*     */     }
/*  61 */     if (!Set.class.isAssignableFrom(targetSetClass)) {
/*  62 */       throw new IllegalArgumentException("'targetSetClass' must implement [java.util.Set]");
/*     */     }
/*  64 */     this.targetSetClass = targetSetClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<Set> getObjectType() {
/*  71 */     return Set.class;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Set<Object> createInstance() {
/*  77 */     if (this.sourceSet == null) {
/*  78 */       throw new IllegalArgumentException("'sourceSet' is required");
/*     */     }
/*  80 */     Set<Object> result = null;
/*  81 */     if (this.targetSetClass != null) {
/*  82 */       result = (Set<Object>)BeanUtils.instantiateClass(this.targetSetClass);
/*     */     } else {
/*     */       
/*  85 */       result = new LinkedHashSet(this.sourceSet.size());
/*     */     } 
/*  87 */     Class<?> valueType = null;
/*  88 */     if (this.targetSetClass != null) {
/*  89 */       valueType = ResolvableType.forClass(this.targetSetClass).asCollection().resolveGeneric(new int[0]);
/*     */     }
/*  91 */     if (valueType != null) {
/*  92 */       TypeConverter converter = getBeanTypeConverter();
/*  93 */       for (Object elem : this.sourceSet) {
/*  94 */         result.add(converter.convertIfNecessary(elem, valueType));
/*     */       }
/*     */     } else {
/*     */       
/*  98 */       result.addAll(this.sourceSet);
/*     */     } 
/* 100 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\config\SetFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */