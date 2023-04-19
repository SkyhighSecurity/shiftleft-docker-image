/*     */ package org.springframework.beans.support;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeanWrapperImpl;
/*     */ import org.springframework.beans.BeansException;
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
/*     */ public class PropertyComparator<T>
/*     */   implements Comparator<T>
/*     */ {
/*  42 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private final SortDefinition sortDefinition;
/*     */   
/*  46 */   private final BeanWrapperImpl beanWrapper = new BeanWrapperImpl(false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyComparator(SortDefinition sortDefinition) {
/*  54 */     this.sortDefinition = sortDefinition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyComparator(String property, boolean ignoreCase, boolean ascending) {
/*  64 */     this.sortDefinition = new MutableSortDefinition(property, ignoreCase, ascending);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final SortDefinition getSortDefinition() {
/*  71 */     return this.sortDefinition;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int compare(T o1, T o2) {
/*     */     int result;
/*  78 */     Object v1 = getPropertyValue(o1);
/*  79 */     Object v2 = getPropertyValue(o2);
/*  80 */     if (this.sortDefinition.isIgnoreCase() && v1 instanceof String && v2 instanceof String) {
/*  81 */       v1 = ((String)v1).toLowerCase();
/*  82 */       v2 = ((String)v2).toLowerCase();
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  89 */       if (v1 != null) {
/*  90 */         result = (v2 != null) ? ((Comparable<Object>)v1).compareTo(v2) : -1;
/*     */       } else {
/*     */         
/*  93 */         result = (v2 != null) ? 1 : 0;
/*     */       }
/*     */     
/*  96 */     } catch (RuntimeException ex) {
/*  97 */       if (this.logger.isWarnEnabled()) {
/*  98 */         this.logger.warn("Could not sort objects [" + o1 + "] and [" + o2 + "]", ex);
/*     */       }
/* 100 */       return 0;
/*     */     } 
/*     */     
/* 103 */     return this.sortDefinition.isAscending() ? result : -result;
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
/*     */   private Object getPropertyValue(Object obj) {
/*     */     try {
/* 116 */       this.beanWrapper.setWrappedInstance(obj);
/* 117 */       return this.beanWrapper.getPropertyValue(this.sortDefinition.getProperty());
/*     */     }
/* 119 */     catch (BeansException ex) {
/* 120 */       this.logger.info("PropertyComparator could not access property - treating as null for sorting", (Throwable)ex);
/* 121 */       return null;
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
/*     */   
/*     */   public static void sort(List<?> source, SortDefinition sortDefinition) throws BeansException {
/* 135 */     if (StringUtils.hasText(sortDefinition.getProperty())) {
/* 136 */       Collections.sort(source, new PropertyComparator(sortDefinition));
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
/*     */   public static void sort(Object[] source, SortDefinition sortDefinition) throws BeansException {
/* 149 */     if (StringUtils.hasText(sortDefinition.getProperty()))
/* 150 */       Arrays.sort(source, new PropertyComparator(sortDefinition)); 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\support\PropertyComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */