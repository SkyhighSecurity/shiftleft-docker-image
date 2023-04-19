/*    */ package com.fasterxml.jackson.databind.deser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.BeanDescription;
/*    */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.KeyDeserializer;
/*    */ import com.fasterxml.jackson.databind.MapperFeature;
/*    */ import com.fasterxml.jackson.databind.deser.KeyDeserializers;
/*    */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*    */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*    */ import com.fasterxml.jackson.databind.util.EnumResolver;
/*    */ import java.io.Serializable;
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.Method;
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
/*    */ public class StdKeyDeserializers
/*    */   implements KeyDeserializers, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public static KeyDeserializer constructEnumKeyDeserializer(EnumResolver enumResolver) {
/* 34 */     return new StdKeyDeserializer.EnumKD(enumResolver, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public static KeyDeserializer constructEnumKeyDeserializer(EnumResolver enumResolver, AnnotatedMethod factory) {
/* 39 */     return new StdKeyDeserializer.EnumKD(enumResolver, factory);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static KeyDeserializer constructDelegatingKeyDeserializer(DeserializationConfig config, JavaType type, JsonDeserializer<?> deser) {
/* 45 */     return new StdKeyDeserializer.DelegatingKD(type.getRawClass(), deser);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static KeyDeserializer findStringBasedKeyDeserializer(DeserializationConfig config, JavaType type) {
/* 52 */     BeanDescription beanDesc = config.introspect(type);
/*    */     
/* 54 */     Constructor<?> ctor = beanDesc.findSingleArgConstructor(new Class[] { String.class });
/* 55 */     if (ctor != null) {
/* 56 */       if (config.canOverrideAccessModifiers()) {
/* 57 */         ClassUtil.checkAndFixAccess(ctor, config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
/*    */       }
/* 59 */       return new StdKeyDeserializer.StringCtorKeyDeserializer(ctor);
/*    */     } 
/*    */ 
/*    */     
/* 63 */     Method m = beanDesc.findFactoryMethod(new Class[] { String.class });
/* 64 */     if (m != null) {
/* 65 */       if (config.canOverrideAccessModifiers()) {
/* 66 */         ClassUtil.checkAndFixAccess(m, config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
/*    */       }
/* 68 */       return new StdKeyDeserializer.StringFactoryKeyDeserializer(m);
/*    */     } 
/*    */     
/* 71 */     return null;
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
/*    */   public KeyDeserializer findKeyDeserializer(JavaType type, DeserializationConfig config, BeanDescription beanDesc) throws JsonMappingException {
/* 84 */     Class<?> raw = type.getRawClass();
/*    */     
/* 86 */     if (raw.isPrimitive()) {
/* 87 */       raw = ClassUtil.wrapperType(raw);
/*    */     }
/* 89 */     return StdKeyDeserializer.forType(raw);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\std\StdKeyDeserializers.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */