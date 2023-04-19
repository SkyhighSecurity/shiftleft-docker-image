/*    */ package org.apache.commons.collections.bidimap;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.apache.commons.collections.BidiMap;
/*    */ import org.apache.commons.collections.MapIterator;
/*    */ import org.apache.commons.collections.map.AbstractMapDecorator;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractBidiMapDecorator
/*    */   extends AbstractMapDecorator
/*    */   implements BidiMap
/*    */ {
/*    */   protected AbstractBidiMapDecorator(BidiMap map) {
/* 50 */     super((Map)map);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected BidiMap getBidiMap() {
/* 59 */     return (BidiMap)this.map;
/*    */   }
/*    */ 
/*    */   
/*    */   public MapIterator mapIterator() {
/* 64 */     return getBidiMap().mapIterator();
/*    */   }
/*    */   
/*    */   public Object getKey(Object value) {
/* 68 */     return getBidiMap().getKey(value);
/*    */   }
/*    */   
/*    */   public Object removeValue(Object value) {
/* 72 */     return getBidiMap().removeValue(value);
/*    */   }
/*    */   
/*    */   public BidiMap inverseBidiMap() {
/* 76 */     return getBidiMap().inverseBidiMap();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\bidimap\AbstractBidiMapDecorator.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */