/*     */ package org.springframework.http.converter.json;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.ObjectIdGenerator;
/*     */ import com.fasterxml.jackson.annotation.ObjectIdResolver;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.KeyDeserializer;
/*     */ import com.fasterxml.jackson.databind.PropertyNamingStrategy;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*     */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*     */ import com.fasterxml.jackson.databind.ser.VirtualBeanPropertyWriter;
/*     */ import com.fasterxml.jackson.databind.util.Converter;
/*     */ import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SpringHandlerInstantiator
/*     */   extends HandlerInstantiator
/*     */ {
/*     */   private final AutowireCapableBeanFactory beanFactory;
/*     */   
/*     */   public SpringHandlerInstantiator(AutowireCapableBeanFactory beanFactory) {
/*  67 */     Assert.notNull(beanFactory, "BeanFactory must not be null");
/*  68 */     this.beanFactory = beanFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonDeserializer<?> deserializerInstance(DeserializationConfig config, Annotated annotated, Class<?> implClass) {
/*  74 */     return (JsonDeserializer)this.beanFactory.createBean(implClass);
/*     */   }
/*     */ 
/*     */   
/*     */   public KeyDeserializer keyDeserializerInstance(DeserializationConfig config, Annotated annotated, Class<?> implClass) {
/*  79 */     return (KeyDeserializer)this.beanFactory.createBean(implClass);
/*     */   }
/*     */ 
/*     */   
/*     */   public JsonSerializer<?> serializerInstance(SerializationConfig config, Annotated annotated, Class<?> implClass) {
/*  84 */     return (JsonSerializer)this.beanFactory.createBean(implClass);
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeResolverBuilder<?> typeResolverBuilderInstance(MapperConfig<?> config, Annotated annotated, Class<?> implClass) {
/*  89 */     return (TypeResolverBuilder)this.beanFactory.createBean(implClass);
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeIdResolver typeIdResolverInstance(MapperConfig<?> config, Annotated annotated, Class<?> implClass) {
/*  94 */     return (TypeIdResolver)this.beanFactory.createBean(implClass);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ValueInstantiator valueInstantiatorInstance(MapperConfig<?> config, Annotated annotated, Class<?> implClass) {
/* 100 */     return (ValueInstantiator)this.beanFactory.createBean(implClass);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectIdGenerator<?> objectIdGeneratorInstance(MapperConfig<?> config, Annotated annotated, Class<?> implClass) {
/* 106 */     return (ObjectIdGenerator)this.beanFactory.createBean(implClass);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectIdResolver resolverIdGeneratorInstance(MapperConfig<?> config, Annotated annotated, Class<?> implClass) {
/* 112 */     return (ObjectIdResolver)this.beanFactory.createBean(implClass);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyNamingStrategy namingStrategyInstance(MapperConfig<?> config, Annotated annotated, Class<?> implClass) {
/* 118 */     return (PropertyNamingStrategy)this.beanFactory.createBean(implClass);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Converter<?, ?> converterInstance(MapperConfig<?> config, Annotated annotated, Class<?> implClass) {
/* 124 */     return (Converter<?, ?>)this.beanFactory.createBean(implClass);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public VirtualBeanPropertyWriter virtualPropertyWriterInstance(MapperConfig<?> config, Class<?> implClass) {
/* 130 */     return (VirtualBeanPropertyWriter)this.beanFactory.createBean(implClass);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\converter\json\SpringHandlerInstantiator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */