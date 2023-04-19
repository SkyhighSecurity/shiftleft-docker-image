/*     */ package org.springframework.http.converter.json;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonInclude;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.Module;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.PropertyNamingStrategy;
/*     */ import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*     */ import com.fasterxml.jackson.databind.ser.FilterProvider;
/*     */ import java.text.DateFormat;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.TimeZone;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
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
/*     */ public class Jackson2ObjectMapperFactoryBean
/*     */   implements FactoryBean<ObjectMapper>, BeanClassLoaderAware, ApplicationContextAware, InitializingBean
/*     */ {
/* 144 */   private final Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
/*     */ 
/*     */ 
/*     */   
/*     */   private ObjectMapper objectMapper;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setObjectMapper(ObjectMapper objectMapper) {
/* 154 */     this.objectMapper = objectMapper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCreateXmlMapper(boolean createXmlMapper) {
/* 163 */     this.builder.createXmlMapper(createXmlMapper);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDateFormat(DateFormat dateFormat) {
/* 173 */     this.builder.dateFormat(dateFormat);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSimpleDateFormat(String format) {
/* 183 */     this.builder.simpleDateFormat(format);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocale(Locale locale) {
/* 192 */     this.builder.locale(locale);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeZone(TimeZone timeZone) {
/* 201 */     this.builder.timeZone(timeZone);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAnnotationIntrospector(AnnotationIntrospector annotationIntrospector) {
/* 208 */     this.builder.annotationIntrospector(annotationIntrospector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPropertyNamingStrategy(PropertyNamingStrategy propertyNamingStrategy) {
/* 217 */     this.builder.propertyNamingStrategy(propertyNamingStrategy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultTyping(TypeResolverBuilder<?> typeResolverBuilder) {
/* 225 */     this.builder.defaultTyping(typeResolverBuilder);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSerializationInclusion(JsonInclude.Include serializationInclusion) {
/* 233 */     this.builder.serializationInclusion(serializationInclusion);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFilters(FilterProvider filters) {
/* 242 */     this.builder.filters(filters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMixIns(Map<Class<?>, Class<?>> mixIns) {
/* 254 */     this.builder.mixIns(mixIns);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSerializers(JsonSerializer<?>... serializers) {
/* 263 */     this.builder.serializers(serializers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSerializersByType(Map<Class<?>, JsonSerializer<?>> serializers) {
/* 271 */     this.builder.serializersByType(serializers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDeserializers(JsonDeserializer<?>... deserializers) {
/* 281 */     this.builder.deserializers(deserializers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDeserializersByType(Map<Class<?>, JsonDeserializer<?>> deserializers) {
/* 288 */     this.builder.deserializersByType(deserializers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutoDetectFields(boolean autoDetectFields) {
/* 295 */     this.builder.autoDetectFields(autoDetectFields);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutoDetectGettersSetters(boolean autoDetectGettersSetters) {
/* 304 */     this.builder.autoDetectGettersSetters(autoDetectGettersSetters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultViewInclusion(boolean defaultViewInclusion) {
/* 312 */     this.builder.defaultViewInclusion(defaultViewInclusion);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFailOnUnknownProperties(boolean failOnUnknownProperties) {
/* 320 */     this.builder.failOnUnknownProperties(failOnUnknownProperties);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFailOnEmptyBeans(boolean failOnEmptyBeans) {
/* 327 */     this.builder.failOnEmptyBeans(failOnEmptyBeans);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIndentOutput(boolean indentOutput) {
/* 334 */     this.builder.indentOutput(indentOutput);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultUseWrapper(boolean defaultUseWrapper) {
/* 343 */     this.builder.defaultUseWrapper(defaultUseWrapper);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFeaturesToEnable(Object... featuresToEnable) {
/* 355 */     this.builder.featuresToEnable(featuresToEnable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFeaturesToDisable(Object... featuresToDisable) {
/* 367 */     this.builder.featuresToDisable(featuresToDisable);
/*     */   }
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
/*     */   public void setModules(List<Module> modules) {
/* 381 */     this.builder.modules(modules);
/*     */   }
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
/*     */   public void setModulesToInstall(Class<? extends Module>... modules) {
/* 397 */     this.builder.modulesToInstall(modules);
/*     */   }
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
/*     */   public void setFindModulesViaServiceLoader(boolean findModules) {
/* 410 */     this.builder.findModulesViaServiceLoader(findModules);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader beanClassLoader) {
/* 415 */     this.builder.moduleClassLoader(beanClassLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHandlerInstantiator(HandlerInstantiator handlerInstantiator) {
/* 426 */     this.builder.handlerInstantiator(handlerInstantiator);
/*     */   }
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
/*     */   public void setApplicationContext(ApplicationContext applicationContext) {
/* 439 */     this.builder.applicationContext(applicationContext);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 445 */     if (this.objectMapper != null) {
/* 446 */       this.builder.configure(this.objectMapper);
/*     */     } else {
/*     */       
/* 449 */       this.objectMapper = this.builder.build();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectMapper getObject() {
/* 458 */     return this.objectMapper;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getObjectType() {
/* 463 */     return (this.objectMapper != null) ? this.objectMapper.getClass() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 468 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\converter\json\Jackson2ObjectMapperFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */