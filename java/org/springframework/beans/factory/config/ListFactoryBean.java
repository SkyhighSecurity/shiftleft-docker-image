/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class ListFactoryBean
/*     */   extends AbstractFactoryBean<List<Object>>
/*     */ {
/*     */   private List<?> sourceList;
/*     */   private Class<? extends List> targetListClass;
/*     */   
/*     */   public void setSourceList(List<?> sourceList) {
/*  47 */     this.sourceList = sourceList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetListClass(Class<? extends List> targetListClass) {
/*  58 */     if (targetListClass == null) {
/*  59 */       throw new IllegalArgumentException("'targetListClass' must not be null");
/*     */     }
/*  61 */     if (!List.class.isAssignableFrom(targetListClass)) {
/*  62 */       throw new IllegalArgumentException("'targetListClass' must implement [java.util.List]");
/*     */     }
/*  64 */     this.targetListClass = targetListClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<List> getObjectType() {
/*  71 */     return List.class;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<Object> createInstance() {
/*  77 */     if (this.sourceList == null) {
/*  78 */       throw new IllegalArgumentException("'sourceList' is required");
/*     */     }
/*  80 */     List<Object> result = null;
/*  81 */     if (this.targetListClass != null) {
/*  82 */       result = (List<Object>)BeanUtils.instantiateClass(this.targetListClass);
/*     */     } else {
/*     */       
/*  85 */       result = new ArrayList(this.sourceList.size());
/*     */     } 
/*  87 */     Class<?> valueType = null;
/*  88 */     if (this.targetListClass != null) {
/*  89 */       valueType = ResolvableType.forClass(this.targetListClass).asCollection().resolveGeneric(new int[0]);
/*     */     }
/*  91 */     if (valueType != null) {
/*  92 */       TypeConverter converter = getBeanTypeConverter();
/*  93 */       for (Object elem : this.sourceList) {
/*  94 */         result.add(converter.convertIfNecessary(elem, valueType));
/*     */       }
/*     */     } else {
/*     */       
/*  98 */       result.addAll(this.sourceList);
/*     */     } 
/* 100 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\config\ListFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */