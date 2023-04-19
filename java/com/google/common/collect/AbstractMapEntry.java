/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Objects;
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
/*    */ @GwtCompatible
/*    */ abstract class AbstractMapEntry<K, V>
/*    */   implements Map.Entry<K, V>
/*    */ {
/*    */   public abstract K getKey();
/*    */   
/*    */   public abstract V getValue();
/*    */   
/*    */   public V setValue(V value) {
/* 40 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public boolean equals(@Nullable Object object) {
/* 44 */     if (object instanceof Map.Entry) {
/* 45 */       Map.Entry<?, ?> that = (Map.Entry<?, ?>)object;
/* 46 */       return (Objects.equal(getKey(), that.getKey()) && Objects.equal(getValue(), that.getValue()));
/*    */     } 
/*    */     
/* 49 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 53 */     K k = getKey();
/* 54 */     V v = getValue();
/* 55 */     return ((k == null) ? 0 : k.hashCode()) ^ ((v == null) ? 0 : v.hashCode());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 62 */     return (new StringBuilder()).append(getKey()).append("=").append(getValue()).toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\AbstractMapEntry.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */