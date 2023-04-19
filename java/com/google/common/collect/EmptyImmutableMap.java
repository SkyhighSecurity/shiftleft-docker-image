/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import javax.annotation.Nullable;
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
/*    */ @GwtCompatible(serializable = true)
/*    */ final class EmptyImmutableMap
/*    */   extends ImmutableMap<Object, Object>
/*    */ {
/* 33 */   static final EmptyImmutableMap INSTANCE = new EmptyImmutableMap();
/*    */   
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   public Object get(Object key) {
/* 38 */     return null;
/*    */   }
/*    */   
/*    */   public int size() {
/* 42 */     return 0;
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 46 */     return true;
/*    */   }
/*    */   
/*    */   public boolean containsKey(Object key) {
/* 50 */     return false;
/*    */   }
/*    */   
/*    */   public boolean containsValue(Object value) {
/* 54 */     return false;
/*    */   }
/*    */   
/*    */   public ImmutableSet<Map.Entry<Object, Object>> entrySet() {
/* 58 */     return ImmutableSet.of();
/*    */   }
/*    */   
/*    */   public ImmutableSet<Object> keySet() {
/* 62 */     return ImmutableSet.of();
/*    */   }
/*    */   
/*    */   public ImmutableCollection<Object> values() {
/* 66 */     return ImmutableCollection.EMPTY_IMMUTABLE_COLLECTION;
/*    */   }
/*    */   
/*    */   public boolean equals(@Nullable Object object) {
/* 70 */     if (object instanceof Map) {
/* 71 */       Map<?, ?> that = (Map<?, ?>)object;
/* 72 */       return that.isEmpty();
/*    */     } 
/* 74 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 78 */     return 0;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 82 */     return "{}";
/*    */   }
/*    */   
/*    */   Object readResolve() {
/* 86 */     return INSTANCE;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\EmptyImmutableMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */