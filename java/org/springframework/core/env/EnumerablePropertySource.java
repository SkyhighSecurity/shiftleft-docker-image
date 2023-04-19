/*    */ package org.springframework.core.env;
/*    */ 
/*    */ import org.springframework.util.ObjectUtils;
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
/*    */ 
/*    */ public abstract class EnumerablePropertySource<T>
/*    */   extends PropertySource<T>
/*    */ {
/*    */   public EnumerablePropertySource(String name, T source) {
/* 47 */     super(name, source);
/*    */   }
/*    */   
/*    */   protected EnumerablePropertySource(String name) {
/* 51 */     super(name);
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
/*    */   public boolean containsProperty(String name) {
/* 63 */     return ObjectUtils.containsElement((Object[])getPropertyNames(), name);
/*    */   }
/*    */   
/*    */   public abstract String[] getPropertyNames();
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\env\EnumerablePropertySource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */