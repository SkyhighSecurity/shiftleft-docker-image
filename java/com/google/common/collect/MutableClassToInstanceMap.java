/*    */ package com.google.common.collect;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
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
/*    */ public final class MutableClassToInstanceMap<B>
/*    */   extends ConstrainedMap<Class<? extends B>, B>
/*    */   implements ClassToInstanceMap<B>
/*    */ {
/*    */   public static <B> MutableClassToInstanceMap<B> create() {
/* 37 */     return new MutableClassToInstanceMap<B>(new HashMap<Class<? extends B>, B>());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <B> MutableClassToInstanceMap<B> create(Map<Class<? extends B>, B> backingMap) {
/* 48 */     return new MutableClassToInstanceMap<B>(backingMap);
/*    */   }
/*    */   
/*    */   private MutableClassToInstanceMap(Map<Class<? extends B>, B> delegate) {
/* 52 */     super(delegate, (MapConstraint)VALUE_CAN_BE_CAST_TO_KEY);
/*    */   }
/*    */   
/* 55 */   private static final MapConstraint<Class<?>, Object> VALUE_CAN_BE_CAST_TO_KEY = new MapConstraint<Class<?>, Object>()
/*    */     {
/*    */       public void checkKeyValue(Class<?> key, Object value) {
/* 58 */         MutableClassToInstanceMap.cast(key, value);
/*    */       }
/*    */     };
/*    */   
/*    */   public <T extends B> T putInstance(Class<T> type, T value) {
/* 63 */     return cast(type, put(type, (B)value));
/*    */   }
/*    */   
/*    */   public <T extends B> T getInstance(Class<T> type) {
/* 67 */     return cast(type, get(type));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   static <B, T extends B> T cast(Class<T> type, B value) {
/* 73 */     return wrap(type).cast(value);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static <T> Class<T> wrap(Class<T> c) {
/* 79 */     return c.isPrimitive() ? (Class<T>)PRIMITIVES_TO_WRAPPERS.get(c) : c;
/*    */   }
/*    */   
/* 82 */   private static final Map<Class<?>, Class<?>> PRIMITIVES_TO_WRAPPERS = (new ImmutableMap.Builder<Class<?>, Class<?>>()).put(boolean.class, Boolean.class).put(byte.class, Byte.class).put(char.class, Character.class).put(double.class, Double.class).put(float.class, Float.class).put(int.class, Integer.class).put(long.class, Long.class).put(short.class, Short.class).put(void.class, Void.class).build();
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\MutableClassToInstanceMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */