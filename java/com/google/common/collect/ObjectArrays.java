/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.util.Collection;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public final class ObjectArrays
/*     */ {
/*     */   @GwtIncompatible("Array.newInstance(Class, int)")
/*     */   public static <T> T[] newArray(Class<T> type, int length) {
/*  44 */     return Platform.newArray(type, length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T[] newArray(T[] reference, int length) {
/*  55 */     return Platform.newArray(reference, length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("Array.newInstance(Class, int)")
/*     */   public static <T> T[] concat(T[] first, T[] second, Class<T> type) {
/*  67 */     T[] result = newArray(type, first.length + second.length);
/*  68 */     System.arraycopy(first, 0, result, 0, first.length);
/*  69 */     System.arraycopy(second, 0, result, first.length, second.length);
/*  70 */     return result;
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
/*     */   public static <T> T[] concat(@Nullable T element, T[] array) {
/*  83 */     T[] result = newArray(array, array.length + 1);
/*  84 */     result[0] = element;
/*  85 */     System.arraycopy(array, 0, result, 1, array.length);
/*  86 */     return result;
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
/*     */   public static <T> T[] concat(T[] array, @Nullable T element) {
/*  99 */     T[] result = arraysCopyOf(array, array.length + 1);
/* 100 */     result[array.length] = element;
/* 101 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T> T[] arraysCopyOf(T[] original, int newLength) {
/* 106 */     T[] copy = newArray(original, newLength);
/* 107 */     System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
/*     */     
/* 109 */     return copy;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> T[] toArrayImpl(Collection<?> c, T[] array) {
/* 138 */     int size = c.size();
/* 139 */     if (array.length < size) {
/* 140 */       array = newArray(array, size);
/*     */     }
/* 142 */     fillArray(c, (Object[])array);
/* 143 */     if (array.length > size) {
/* 144 */       array[size] = null;
/*     */     }
/* 146 */     return array;
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
/*     */   static Object[] toArrayImpl(Collection<?> c) {
/* 165 */     return fillArray(c, new Object[c.size()]);
/*     */   }
/*     */   
/*     */   private static Object[] fillArray(Iterable<?> elements, Object[] array) {
/* 169 */     int i = 0;
/* 170 */     for (Object element : elements) {
/* 171 */       array[i++] = element;
/*     */     }
/* 173 */     return array;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\ObjectArrays.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */