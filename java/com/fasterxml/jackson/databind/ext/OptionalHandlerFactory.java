/*     */ package com.fasterxml.jackson.databind.ext;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.deser.Deserializers;
/*     */ import com.fasterxml.jackson.databind.ser.Serializers;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.io.Serializable;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Node;
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
/*     */ public class OptionalHandlerFactory
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private static final String PACKAGE_PREFIX_JAVAX_XML = "javax.xml.";
/*     */   private static final String SERIALIZERS_FOR_JAVAX_XML = "com.fasterxml.jackson.databind.ext.CoreXMLSerializers";
/*     */   private static final String DESERIALIZERS_FOR_JAVAX_XML = "com.fasterxml.jackson.databind.ext.CoreXMLDeserializers";
/*     */   private static final String SERIALIZER_FOR_DOM_NODE = "com.fasterxml.jackson.databind.ext.DOMSerializer";
/*     */   private static final String DESERIALIZER_FOR_DOM_DOCUMENT = "com.fasterxml.jackson.databind.ext.DOMDeserializer$DocumentDeserializer";
/*     */   private static final String DESERIALIZER_FOR_DOM_NODE = "com.fasterxml.jackson.databind.ext.DOMDeserializer$NodeDeserializer";
/*     */   private static final Class<?> CLASS_DOM_NODE;
/*     */   private static final Class<?> CLASS_DOM_DOCUMENT;
/*     */   private static final Java7Handlers _jdk7Helper;
/*     */   
/*     */   static {
/*  50 */     Class<?> doc = null, node = null;
/*     */     try {
/*  52 */       node = Node.class;
/*  53 */       doc = Document.class;
/*  54 */     } catch (Exception e) {
/*     */       
/*  56 */       Logger.getLogger(OptionalHandlerFactory.class.getName())
/*  57 */         .log(Level.INFO, "Could not load DOM `Node` and/or `Document` classes: no DOM support");
/*     */     } 
/*  59 */     CLASS_DOM_NODE = node;
/*  60 */     CLASS_DOM_DOCUMENT = doc;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  69 */     Java7Handlers x = null;
/*     */     try {
/*  71 */       x = Java7Handlers.instance();
/*  72 */     } catch (Throwable throwable) {}
/*  73 */     _jdk7Helper = x;
/*     */   }
/*     */   
/*  76 */   public static final OptionalHandlerFactory instance = new OptionalHandlerFactory();
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
/*     */   public JsonSerializer<?> findSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc) {
/*     */     String factoryName;
/*  89 */     Class<?> rawType = type.getRawClass();
/*     */     
/*  91 */     if (CLASS_DOM_NODE != null && CLASS_DOM_NODE.isAssignableFrom(rawType)) {
/*  92 */       return (JsonSerializer)instantiate("com.fasterxml.jackson.databind.ext.DOMSerializer");
/*     */     }
/*     */     
/*  95 */     if (_jdk7Helper != null) {
/*  96 */       JsonSerializer<?> ser = _jdk7Helper.getSerializerForJavaNioFilePath(rawType);
/*  97 */       if (ser != null) {
/*  98 */         return ser;
/*     */       }
/*     */     } 
/*     */     
/* 102 */     String className = rawType.getName();
/*     */     
/* 104 */     if (className.startsWith("javax.xml.") || hasSuperClassStartingWith(rawType, "javax.xml.")) {
/* 105 */       factoryName = "com.fasterxml.jackson.databind.ext.CoreXMLSerializers";
/*     */     } else {
/* 107 */       return null;
/*     */     } 
/*     */     
/* 110 */     Object ob = instantiate(factoryName);
/* 111 */     if (ob == null) {
/* 112 */       return null;
/*     */     }
/* 114 */     return ((Serializers)ob).findSerializer(config, type, beanDesc);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonDeserializer<?> findDeserializer(JavaType type, DeserializationConfig config, BeanDescription beanDesc) throws JsonMappingException {
/*     */     String factoryName;
/* 121 */     Class<?> rawType = type.getRawClass();
/*     */     
/* 123 */     if (_jdk7Helper != null) {
/* 124 */       JsonDeserializer<?> deser = _jdk7Helper.getDeserializerForJavaNioFilePath(rawType);
/* 125 */       if (deser != null) {
/* 126 */         return deser;
/*     */       }
/*     */     } 
/* 129 */     if (CLASS_DOM_NODE != null && CLASS_DOM_NODE.isAssignableFrom(rawType)) {
/* 130 */       return (JsonDeserializer)instantiate("com.fasterxml.jackson.databind.ext.DOMDeserializer$NodeDeserializer");
/*     */     }
/* 132 */     if (CLASS_DOM_DOCUMENT != null && CLASS_DOM_DOCUMENT.isAssignableFrom(rawType)) {
/* 133 */       return (JsonDeserializer)instantiate("com.fasterxml.jackson.databind.ext.DOMDeserializer$DocumentDeserializer");
/*     */     }
/* 135 */     String className = rawType.getName();
/*     */     
/* 137 */     if (className.startsWith("javax.xml.") || 
/* 138 */       hasSuperClassStartingWith(rawType, "javax.xml.")) {
/* 139 */       factoryName = "com.fasterxml.jackson.databind.ext.CoreXMLDeserializers";
/*     */     } else {
/* 141 */       return null;
/*     */     } 
/* 143 */     Object ob = instantiate(factoryName);
/* 144 */     if (ob == null) {
/* 145 */       return null;
/*     */     }
/* 147 */     return ((Deserializers)ob).findBeanDeserializer(type, config, beanDesc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object instantiate(String className) {
/*     */     try {
/* 159 */       return ClassUtil.createInstance(Class.forName(className), false);
/* 160 */     } catch (LinkageError linkageError) {
/*     */     
/* 162 */     } catch (Exception exception) {}
/* 163 */     return null;
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
/*     */   private boolean hasSuperClassStartingWith(Class<?> rawType, String prefix) {
/* 176 */     for (Class<?> supertype = rawType.getSuperclass(); supertype != null; supertype = supertype.getSuperclass()) {
/* 177 */       if (supertype == Object.class) {
/* 178 */         return false;
/*     */       }
/* 180 */       if (supertype.getName().startsWith(prefix)) {
/* 181 */         return true;
/*     */       }
/*     */     } 
/* 184 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ext\OptionalHandlerFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */