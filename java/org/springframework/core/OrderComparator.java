/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OrderComparator
/*     */   implements Comparator<Object>
/*     */ {
/*  53 */   public static final OrderComparator INSTANCE = new OrderComparator();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Comparator<Object> withSourceProvider(final OrderSourceProvider sourceProvider) {
/*  63 */     return new Comparator()
/*     */       {
/*     */         public int compare(Object o1, Object o2) {
/*  66 */           return OrderComparator.this.doCompare(o1, o2, sourceProvider);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public int compare(Object o1, Object o2) {
/*  73 */     return doCompare(o1, o2, null);
/*     */   } public static interface OrderSourceProvider {
/*     */     Object getOrderSource(Object param1Object); }
/*     */   private int doCompare(Object o1, Object o2, OrderSourceProvider sourceProvider) {
/*  77 */     boolean p1 = o1 instanceof PriorityOrdered;
/*  78 */     boolean p2 = o2 instanceof PriorityOrdered;
/*  79 */     if (p1 && !p2) {
/*  80 */       return -1;
/*     */     }
/*  82 */     if (p2 && !p1) {
/*  83 */       return 1;
/*     */     }
/*     */ 
/*     */     
/*  87 */     int i1 = getOrder(o1, sourceProvider);
/*  88 */     int i2 = getOrder(o2, sourceProvider);
/*  89 */     return (i1 < i2) ? -1 : ((i1 > i2) ? 1 : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getOrder(Object obj, OrderSourceProvider sourceProvider) {
/* 100 */     Integer order = null;
/* 101 */     if (sourceProvider != null) {
/* 102 */       Object orderSource = sourceProvider.getOrderSource(obj);
/* 103 */       if (orderSource != null && orderSource.getClass().isArray()) {
/* 104 */         Object[] sources = ObjectUtils.toObjectArray(orderSource);
/* 105 */         for (Object source : sources) {
/* 106 */           order = findOrder(source);
/* 107 */           if (order != null) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } else {
/*     */         
/* 113 */         order = findOrder(orderSource);
/*     */       } 
/*     */     } 
/* 116 */     return (order != null) ? order.intValue() : getOrder(obj);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getOrder(Object obj) {
/* 127 */     Integer order = findOrder(obj);
/* 128 */     return (order != null) ? order.intValue() : Integer.MAX_VALUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Integer findOrder(Object obj) {
/* 139 */     return (obj instanceof Ordered) ? Integer.valueOf(((Ordered)obj).getOrder()) : null;
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
/*     */   public Integer getPriority(Object obj) {
/* 154 */     return null;
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
/*     */   public static void sort(List<?> list) {
/* 166 */     if (list.size() > 1) {
/* 167 */       Collections.sort(list, INSTANCE);
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
/*     */   public static void sort(Object[] array) {
/* 179 */     if (array.length > 1) {
/* 180 */       Arrays.sort(array, INSTANCE);
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
/*     */   public static void sortIfNecessary(Object value) {
/* 193 */     if (value instanceof Object[]) {
/* 194 */       sort((Object[])value);
/*     */     }
/* 196 */     else if (value instanceof List) {
/* 197 */       sort((List)value);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\OrderComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */