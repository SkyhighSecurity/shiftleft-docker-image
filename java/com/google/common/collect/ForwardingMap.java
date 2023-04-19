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
/*    */ public abstract class ForwardingMap<K, V>
/*    */   extends ForwardingObject
/*    */   implements Map<K, V>
/*    */ {
/*    */   public int size() {
/* 44 */     return delegate().size();
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 48 */     return delegate().isEmpty();
/*    */   }
/*    */   
/*    */   public V remove(Object object) {
/* 52 */     return delegate().remove(object);
/*    */   }
/*    */   
/*    */   public void clear() {
/* 56 */     delegate().clear();
/*    */   }
/*    */   
/*    */   public boolean containsKey(Object key) {
/* 60 */     return delegate().containsKey(key);
/*    */   }
/*    */   
/*    */   public boolean containsValue(Object value) {
/* 64 */     return delegate().containsValue(value);
/*    */   }
/*    */   
/*    */   public V get(Object key) {
/* 68 */     return delegate().get(key);
/*    */   }
/*    */   
/*    */   public V put(K key, V value) {
/* 72 */     return delegate().put(key, value);
/*    */   }
/*    */   
/*    */   public void putAll(Map<? extends K, ? extends V> map) {
/* 76 */     delegate().putAll(map);
/*    */   }
/*    */   
/*    */   public Set<K> keySet() {
/* 80 */     return delegate().keySet();
/*    */   }
/*    */   
/*    */   public Collection<V> values() {
/* 84 */     return delegate().values();
/*    */   }
/*    */   
/*    */   public Set<Map.Entry<K, V>> entrySet() {
/* 88 */     return delegate().entrySet();
/*    */   }
/*    */   
/*    */   public boolean equals(@Nullable Object object) {
/* 92 */     return (object == this || delegate().equals(object));
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 96 */     return delegate().hashCode();
/*    */   }
/*    */   
/*    */   protected abstract Map<K, V> delegate();
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\ForwardingMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */