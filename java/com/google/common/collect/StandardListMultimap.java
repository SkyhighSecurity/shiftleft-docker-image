/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.Map;
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
/*    */ @GwtCompatible
/*    */ abstract class StandardListMultimap<K, V>
/*    */   extends StandardMultimap<K, V>
/*    */   implements ListMultimap<K, V>
/*    */ {
/*    */   protected StandardListMultimap(Map<K, Collection<V>> map) {
/* 45 */     super(map);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public List<V> get(@Nullable K key) {
/* 51 */     return (List<V>)super.get(key);
/*    */   }
/*    */   
/*    */   public List<V> removeAll(@Nullable Object key) {
/* 55 */     return (List<V>)super.removeAll(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<V> replaceValues(@Nullable K key, Iterable<? extends V> values) {
/* 60 */     return (List<V>)super.replaceValues(key, values);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean put(@Nullable K key, @Nullable V value) {
/* 71 */     return super.put(key, value);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(@Nullable Object object) {
/* 82 */     return super.equals(object);
/*    */   }
/*    */   
/*    */   abstract List<V> createCollection();
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\StandardListMultimap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */