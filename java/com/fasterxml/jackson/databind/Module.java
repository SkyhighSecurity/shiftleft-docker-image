/*    */ package com.fasterxml.jackson.databind;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonFactory;
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.core.Version;
/*    */ import com.fasterxml.jackson.core.Versioned;
/*    */ import com.fasterxml.jackson.databind.cfg.MutableConfigOverride;
/*    */ import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
/*    */ import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
/*    */ import com.fasterxml.jackson.databind.deser.Deserializers;
/*    */ import com.fasterxml.jackson.databind.deser.KeyDeserializers;
/*    */ import com.fasterxml.jackson.databind.deser.ValueInstantiators;
/*    */ import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
/*    */ import com.fasterxml.jackson.databind.jsontype.NamedType;
/*    */ import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
/*    */ import com.fasterxml.jackson.databind.ser.Serializers;
/*    */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*    */ import com.fasterxml.jackson.databind.type.TypeModifier;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
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
/*    */ public abstract class Module
/*    */   implements Versioned
/*    */ {
/*    */   public abstract String getModuleName();
/*    */   
/*    */   public abstract Version version();
/*    */   
/*    */   public Object getTypeId() {
/* 62 */     return getClass().getName();
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
/*    */   public abstract void setupModule(SetupContext paramSetupContext);
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
/*    */   public Iterable<? extends Module> getDependencies() {
/* 87 */     return Collections.emptyList();
/*    */   }
/*    */   
/*    */   public static interface SetupContext {
/*    */     Version getMapperVersion();
/*    */     
/*    */     <C extends com.fasterxml.jackson.core.ObjectCodec> C getOwner();
/*    */     
/*    */     TypeFactory getTypeFactory();
/*    */     
/*    */     boolean isEnabled(MapperFeature param1MapperFeature);
/*    */     
/*    */     boolean isEnabled(DeserializationFeature param1DeserializationFeature);
/*    */     
/*    */     boolean isEnabled(SerializationFeature param1SerializationFeature);
/*    */     
/*    */     boolean isEnabled(JsonFactory.Feature param1Feature);
/*    */     
/*    */     boolean isEnabled(JsonParser.Feature param1Feature);
/*    */     
/*    */     boolean isEnabled(JsonGenerator.Feature param1Feature);
/*    */     
/*    */     MutableConfigOverride configOverride(Class<?> param1Class);
/*    */     
/*    */     void addDeserializers(Deserializers param1Deserializers);
/*    */     
/*    */     void addKeyDeserializers(KeyDeserializers param1KeyDeserializers);
/*    */     
/*    */     void addSerializers(Serializers param1Serializers);
/*    */     
/*    */     void addKeySerializers(Serializers param1Serializers);
/*    */     
/*    */     void addBeanDeserializerModifier(BeanDeserializerModifier param1BeanDeserializerModifier);
/*    */     
/*    */     void addBeanSerializerModifier(BeanSerializerModifier param1BeanSerializerModifier);
/*    */     
/*    */     void addAbstractTypeResolver(AbstractTypeResolver param1AbstractTypeResolver);
/*    */     
/*    */     void addTypeModifier(TypeModifier param1TypeModifier);
/*    */     
/*    */     void addValueInstantiators(ValueInstantiators param1ValueInstantiators);
/*    */     
/*    */     void setClassIntrospector(ClassIntrospector param1ClassIntrospector);
/*    */     
/*    */     void insertAnnotationIntrospector(AnnotationIntrospector param1AnnotationIntrospector);
/*    */     
/*    */     void appendAnnotationIntrospector(AnnotationIntrospector param1AnnotationIntrospector);
/*    */     
/*    */     void registerSubtypes(Class<?>... param1VarArgs);
/*    */     
/*    */     void registerSubtypes(NamedType... param1VarArgs);
/*    */     
/*    */     void registerSubtypes(Collection<Class<?>> param1Collection);
/*    */     
/*    */     void setMixInAnnotations(Class<?> param1Class1, Class<?> param1Class2);
/*    */     
/*    */     void addDeserializationProblemHandler(DeserializationProblemHandler param1DeserializationProblemHandler);
/*    */     
/*    */     void setNamingStrategy(PropertyNamingStrategy param1PropertyNamingStrategy);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\Module.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */