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
/*    */ public class UnrecognizedPropertyException
/*    */   extends PropertyBindingException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public UnrecognizedPropertyException(JsonParser p, String msg, JsonLocation loc, Class<?> referringClass, String propName, Collection<Object> propertyIds) {
/* 24 */     super(p, msg, loc, referringClass, propName, propertyIds);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public UnrecognizedPropertyException(String msg, JsonLocation loc, Class<?> referringClass, String propName, Collection<Object> propertyIds) {
/* 35 */     super(msg, loc, referringClass, propName, propertyIds);
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
/*    */   public static UnrecognizedPropertyException from(JsonParser p, Object fromObjectOrClass, String propertyName, Collection<Object> propertyIds) {
/*    */     Class<?> ref;
/* 53 */     if (fromObjectOrClass instanceof Class) {
/* 54 */       ref = (Class)fromObjectOrClass;
/*    */     } else {
/* 56 */       ref = fromObjectOrClass.getClass();
/*    */     } 
/* 58 */     String msg = String.format("Unrecognized field \"%s\" (class %s), not marked as ignorable", new Object[] { propertyName, ref
/* 59 */           .getName() });
/*    */     
/* 61 */     UnrecognizedPropertyException e = new UnrecognizedPropertyException(p, msg, p.getCurrentLocation(), ref, propertyName, propertyIds);
/*    */     
/* 63 */     e.prependPath(fromObjectOrClass, propertyName);
/* 64 */     return e;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\exc\UnrecognizedPropertyException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */