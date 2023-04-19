/*     */ package org.springframework.core.annotation;
/*     */ 
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import org.springframework.core.DecoratingProxy;
/*     */ import org.springframework.core.OrderComparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnnotationAwareOrderComparator
/*     */   extends OrderComparator
/*     */ {
/*  52 */   public static final AnnotationAwareOrderComparator INSTANCE = new AnnotationAwareOrderComparator();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Integer findOrder(Object obj) {
/*  64 */     Integer order = super.findOrder(obj);
/*  65 */     if (order != null) {
/*  66 */       return order;
/*     */     }
/*     */ 
/*     */     
/*  70 */     if (obj instanceof Class) {
/*  71 */       return OrderUtils.getOrder((Class)obj);
/*     */     }
/*  73 */     if (obj instanceof Method) {
/*  74 */       Order ann = AnnotationUtils.<Order>findAnnotation((Method)obj, Order.class);
/*  75 */       if (ann != null) {
/*  76 */         return Integer.valueOf(ann.value());
/*     */       }
/*     */     }
/*  79 */     else if (obj instanceof AnnotatedElement) {
/*  80 */       Order ann = AnnotationUtils.<Order>getAnnotation((AnnotatedElement)obj, Order.class);
/*  81 */       if (ann != null) {
/*  82 */         return Integer.valueOf(ann.value());
/*     */       }
/*     */     }
/*  85 */     else if (obj != null) {
/*  86 */       order = OrderUtils.getOrder(obj.getClass());
/*  87 */       if (order == null && obj instanceof DecoratingProxy) {
/*  88 */         order = OrderUtils.getOrder(((DecoratingProxy)obj).getDecoratedClass());
/*     */       }
/*     */     } 
/*     */     
/*  92 */     return order;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getPriority(Object obj) {
/* 103 */     Integer priority = null;
/* 104 */     if (obj instanceof Class) {
/* 105 */       priority = OrderUtils.getPriority((Class)obj);
/*     */     }
/* 107 */     else if (obj != null) {
/* 108 */       priority = OrderUtils.getPriority(obj.getClass());
/* 109 */       if (priority == null && obj instanceof DecoratingProxy) {
/* 110 */         priority = OrderUtils.getPriority(((DecoratingProxy)obj).getDecoratedClass());
/*     */       }
/*     */     } 
/* 113 */     return priority;
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
/* 125 */     if (list.size() > 1) {
/* 126 */       Collections.sort(list, (Comparator<?>)INSTANCE);
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
/* 138 */     if (array.length > 1) {
/* 139 */       Arrays.sort(array, (Comparator<? super Object>)INSTANCE);
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
/* 152 */     if (value instanceof Object[]) {
/* 153 */       sort((Object[])value);
/*     */     }
/* 155 */     else if (value instanceof List) {
/* 156 */       sort((List)value);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\annotation\AnnotationAwareOrderComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */