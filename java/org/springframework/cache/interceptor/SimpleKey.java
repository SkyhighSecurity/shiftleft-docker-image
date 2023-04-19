/*    */ package org.springframework.cache.interceptor;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.Arrays;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ public class SimpleKey
/*    */   implements Serializable
/*    */ {
/* 35 */   public static final SimpleKey EMPTY = new SimpleKey(new Object[0]);
/*    */ 
/*    */   
/*    */   private final Object[] params;
/*    */ 
/*    */   
/*    */   private final int hashCode;
/*    */ 
/*    */ 
/*    */   
/*    */   public SimpleKey(Object... elements) {
/* 46 */     Assert.notNull(elements, "Elements must not be null");
/* 47 */     this.params = new Object[elements.length];
/* 48 */     System.arraycopy(elements, 0, this.params, 0, elements.length);
/* 49 */     this.hashCode = Arrays.deepHashCode(this.params);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 54 */     return (this == obj || (obj instanceof SimpleKey && 
/* 55 */       Arrays.deepEquals(this.params, ((SimpleKey)obj).params)));
/*    */   }
/*    */ 
/*    */   
/*    */   public final int hashCode() {
/* 60 */     return this.hashCode;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 65 */     return getClass().getSimpleName() + " [" + StringUtils.arrayToCommaDelimitedString(this.params) + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\cache\interceptor\SimpleKey.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */