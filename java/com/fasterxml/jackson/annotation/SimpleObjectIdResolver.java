/*    */ package com.fasterxml.jackson.annotation;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ public class SimpleObjectIdResolver
/*    */   implements ObjectIdResolver
/*    */ {
/*    */   protected Map<ObjectIdGenerator.IdKey, Object> _items;
/*    */   
/*    */   public void bindItem(ObjectIdGenerator.IdKey id, Object ob) {
/* 21 */     if (this._items == null) {
/* 22 */       this._items = new HashMap<ObjectIdGenerator.IdKey, Object>();
/* 23 */     } else if (this._items.containsKey(id)) {
/* 24 */       throw new IllegalStateException("Already had POJO for id (" + id.key.getClass().getName() + ") [" + id + "]");
/*    */     } 
/*    */     
/* 27 */     this._items.put(id, ob);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object resolveId(ObjectIdGenerator.IdKey id) {
/* 32 */     return (this._items == null) ? null : this._items.get(id);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUseFor(ObjectIdResolver resolverType) {
/* 37 */     return (resolverType.getClass() == getClass());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ObjectIdResolver newForDeserialization(Object context) {
/* 44 */     return new SimpleObjectIdResolver();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\annotation\SimpleObjectIdResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */