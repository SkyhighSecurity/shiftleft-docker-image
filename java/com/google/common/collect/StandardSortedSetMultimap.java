/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import java.util.SortedSet;
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
/*    */ abstract class StandardSortedSetMultimap<K, V>
/*    */   extends StandardSetMultimap<K, V>
/*    */   implements SortedSetMultimap<K, V>
/*    */ {
/*    */   protected StandardSortedSetMultimap(Map<K, Collection<V>> map) {
/* 45 */     super(map);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public SortedSet<V> get(@Nullable K key) {
/* 51 */     return (SortedSet<V>)super.get(key);
/*    */   }
/*    */   
/*    */   public SortedSet<V> removeAll(@Nullable Object key) {
/* 55 */     return (SortedSet<V>)super.removeAll(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedSet<V> replaceValues(K key, Iterable<? extends V> values) {
/* 60 */     return (SortedSet<V>)super.replaceValues(key, values);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Collection<V> values() {
/* 70 */     return super.values();
/*    */   }
/*    */   
/*    */   abstract SortedSet<V> createCollection();
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\StandardSortedSetMultimap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */