/*    */ package com.fasterxml.jackson.databind.exc;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonLocation;
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import java.util.Collection;
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
/*    */ public class IgnoredPropertyException
/*    */   extends PropertyBindingException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public IgnoredPropertyException(JsonParser p, String msg, JsonLocation loc, Class<?> referringClass, String propName, Collection<Object> propertyIds) {
/* 28 */     super(p, msg, loc, referringClass, propName, propertyIds);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public IgnoredPropertyException(String msg, JsonLocation loc, Class<?> referringClass, String propName, Collection<Object> propertyIds) {
/* 39 */     super(msg, loc, referringClass, propName, propertyIds);
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static IgnoredPropertyException from(JsonParser p, Object fromObjectOrClass, String propertyName, Collection<Object> propertyIds) {
/*    */     Class<?> ref;
/* 57 */     if (fromObjectOrClass instanceof Class) {
/* 58 */       ref = (Class)fromObjectOrClass;
/*    */     } else {
/* 60 */       ref = fromObjectOrClass.getClass();
/*    */     } 
/* 62 */     String msg = String.format("Ignored field \"%s\" (class %s) encountered; mapper configured not to allow this", new Object[] { propertyName, ref
/* 63 */           .getName() });
/*    */     
/* 65 */     IgnoredPropertyException e = new IgnoredPropertyException(p, msg, p.getCurrentLocation(), ref, propertyName, propertyIds);
/*    */     
/* 67 */     e.prependPath(fromObjectOrClass, propertyName);
/* 68 */     return e;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\exc\IgnoredPropertyException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */