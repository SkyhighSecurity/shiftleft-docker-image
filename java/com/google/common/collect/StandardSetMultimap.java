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
/*    */ abstract class StandardSetMultimap<K, V>
/*    */   extends StandardMultimap<K, V>
/*    */   implements SetMultimap<K, V>
/*    */ {
/*    */   protected StandardSetMultimap(Map<K, Collection<V>> map) {
/* 44 */     super(map);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<V> get(@Nullable K key) {
/* 50 */     return (Set<V>)super.get(key);
/*    */   }
/*    */   
/*    */   public Set<Map.Entry<K, V>> entries() {
/* 54 */     return (Set<Map.Entry<K, V>>)super.entries();
/*    */   }
/*    */   
/*    */   public Set<V> removeAll(@Nullable Object key) {
/* 58 */     return (Set<V>)super.removeAll(key);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<V> replaceValues(@Nullable K key, Iterable<? extends V> values) {
/* 68 */     return (Set<V>)super.replaceValues(key, values);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean put(K key, V value) {
/* 80 */     return super.put(key, value);
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
/* 91 */     return super.equals(object);
/*    */   }
/*    */   
/*    */   abstract Set<V> createCollection();
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\StandardSetMultimap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */