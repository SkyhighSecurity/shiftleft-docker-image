/*    */ package com.fasterxml.jackson.databind.introspect;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.Method;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class MemberKey
/*    */ {
/* 13 */   static final Class<?>[] NO_CLASSES = new Class[0];
/*    */   
/*    */   final String _name;
/*    */   
/*    */   final Class<?>[] _argTypes;
/*    */   
/*    */   public MemberKey(Method m) {
/* 20 */     this(m.getName(), m.getParameterTypes());
/*    */   }
/*    */ 
/*    */   
/*    */   public MemberKey(Constructor<?> ctor) {
/* 25 */     this("", ctor.getParameterTypes());
/*    */   }
/*    */ 
/*    */   
/*    */   public MemberKey(String name, Class<?>[] argTypes) {
/* 30 */     this._name = name;
/* 31 */     this._argTypes = (argTypes == null) ? NO_CLASSES : argTypes;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 35 */     return this._name;
/*    */   }
/*    */   
/*    */   public int argCount() {
/* 39 */     return this._argTypes.length;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 44 */     return this._name + "(" + this._argTypes.length + "-args)";
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 49 */     return this._name.hashCode() + this._argTypes.length;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 55 */     if (o == this) return true; 
/* 56 */     if (o == null) return false; 
/* 57 */     if (o.getClass() != getClass()) {
/* 58 */       return false;
/*    */     }
/* 60 */     MemberKey other = (MemberKey)o;
/* 61 */     if (!this._name.equals(other._name)) {
/* 62 */       return false;
/*    */     }
/* 64 */     Class<?>[] otherArgs = other._argTypes;
/* 65 */     int len = this._argTypes.length;
/* 66 */     if (otherArgs.length != len) {
/* 67 */       return false;
/*    */     }
/* 69 */     for (int i = 0; i < len; ) {
/* 70 */       Class<?> type1 = otherArgs[i];
/* 71 */       Class<?> type2 = this._argTypes[i];
/* 72 */       if (type1 == type2) {
/*    */         i++;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/*    */         continue;
/*    */       } 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 92 */       return false;
/*    */     } 
/* 94 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\introspect\MemberKey.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */