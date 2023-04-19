/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import java.lang.reflect.Array;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible(emulated = true)
/*    */ class Platform
/*    */ {
/*    */   @GwtIncompatible("List.subList")
/*    */   static <T> List<T> subList(List<T> list, int fromIndex, int toIndex) {
/* 43 */     return list.subList(fromIndex, toIndex);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @GwtIncompatible("Class.isInstance")
/*    */   static boolean isInstance(Class<?> clazz, Object obj) {
/* 52 */     return clazz.isInstance(obj);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static <T> T[] clone(T[] array) {
/* 60 */     return (T[])array.clone();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @GwtIncompatible("Array.newInstance(Class, int)")
/*    */   static <T> T[] newArray(Class<T> type, int length) {
/* 72 */     return (T[])Array.newInstance(type, length);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static <T> T[] newArray(T[] reference, int length) {
/* 83 */     Class<?> type = reference.getClass().getComponentType();
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 88 */     T[] result = (T[])Array.newInstance(type, length);
/* 89 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\Platform.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */