/*     */ package com.fasterxml.jackson.databind.introspect;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.type.SimpleType;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import com.fasterxml.jackson.databind.util.LRUMap;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
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
/*     */ public class BasicClassIntrospector
/*     */   extends ClassIntrospector
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  32 */   protected static final BasicBeanDescription STRING_DESC = BasicBeanDescription.forOtherUse(null, (JavaType)SimpleType.constructUnsafe(String.class), 
/*  33 */       AnnotatedClassResolver.createPrimordial(String.class));
/*     */ 
/*     */ 
/*     */   
/*  37 */   protected static final BasicBeanDescription BOOLEAN_DESC = BasicBeanDescription.forOtherUse(null, (JavaType)SimpleType.constructUnsafe(boolean.class), 
/*  38 */       AnnotatedClassResolver.createPrimordial(boolean.class));
/*     */ 
/*     */ 
/*     */   
/*  42 */   protected static final BasicBeanDescription INT_DESC = BasicBeanDescription.forOtherUse(null, (JavaType)SimpleType.constructUnsafe(int.class), 
/*  43 */       AnnotatedClassResolver.createPrimordial(int.class));
/*     */ 
/*     */ 
/*     */   
/*  47 */   protected static final BasicBeanDescription LONG_DESC = BasicBeanDescription.forOtherUse(null, (JavaType)SimpleType.constructUnsafe(long.class), 
/*  48 */       AnnotatedClassResolver.createPrimordial(long.class));
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
/*  67 */   protected final LRUMap<JavaType, BasicBeanDescription> _cachedFCA = new LRUMap(16, 64);
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassIntrospector copy() {
/*  72 */     return new BasicClassIntrospector();
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
/*     */   public BasicBeanDescription forSerialization(SerializationConfig cfg, JavaType type, ClassIntrospector.MixInResolver r) {
/*  86 */     BasicBeanDescription desc = _findStdTypeDesc(type);
/*  87 */     if (desc == null) {
/*     */ 
/*     */       
/*  90 */       desc = _findStdJdkCollectionDesc((MapperConfig<?>)cfg, type);
/*  91 */       if (desc == null) {
/*  92 */         desc = BasicBeanDescription.forSerialization(collectProperties((MapperConfig<?>)cfg, type, r, true, "set"));
/*     */       }
/*     */ 
/*     */       
/*  96 */       this._cachedFCA.putIfAbsent(type, desc);
/*     */     } 
/*  98 */     return desc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicBeanDescription forDeserialization(DeserializationConfig cfg, JavaType type, ClassIntrospector.MixInResolver r) {
/* 106 */     BasicBeanDescription desc = _findStdTypeDesc(type);
/* 107 */     if (desc == null) {
/*     */ 
/*     */       
/* 110 */       desc = _findStdJdkCollectionDesc((MapperConfig<?>)cfg, type);
/* 111 */       if (desc == null) {
/* 112 */         desc = BasicBeanDescription.forDeserialization(collectProperties((MapperConfig<?>)cfg, type, r, false, "set"));
/*     */       }
/*     */ 
/*     */       
/* 116 */       this._cachedFCA.putIfAbsent(type, desc);
/*     */     } 
/* 118 */     return desc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicBeanDescription forDeserializationWithBuilder(DeserializationConfig cfg, JavaType type, ClassIntrospector.MixInResolver r) {
/* 127 */     BasicBeanDescription desc = BasicBeanDescription.forDeserialization(collectPropertiesWithBuilder((MapperConfig<?>)cfg, type, r, false));
/*     */ 
/*     */     
/* 130 */     this._cachedFCA.putIfAbsent(type, desc);
/* 131 */     return desc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicBeanDescription forCreation(DeserializationConfig cfg, JavaType type, ClassIntrospector.MixInResolver r) {
/* 138 */     BasicBeanDescription desc = _findStdTypeDesc(type);
/* 139 */     if (desc == null) {
/*     */ 
/*     */       
/* 142 */       desc = _findStdJdkCollectionDesc((MapperConfig<?>)cfg, type);
/* 143 */       if (desc == null) {
/* 144 */         desc = BasicBeanDescription.forDeserialization(
/* 145 */             collectProperties((MapperConfig<?>)cfg, type, r, false, "set"));
/*     */       }
/*     */     } 
/*     */     
/* 149 */     return desc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicBeanDescription forClassAnnotations(MapperConfig<?> config, JavaType type, ClassIntrospector.MixInResolver r) {
/* 156 */     BasicBeanDescription desc = _findStdTypeDesc(type);
/* 157 */     if (desc == null) {
/* 158 */       desc = (BasicBeanDescription)this._cachedFCA.get(type);
/* 159 */       if (desc == null) {
/* 160 */         desc = BasicBeanDescription.forOtherUse(config, type, 
/* 161 */             _resolveAnnotatedClass(config, type, r));
/* 162 */         this._cachedFCA.put(type, desc);
/*     */       } 
/*     */     } 
/* 165 */     return desc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicBeanDescription forDirectClassAnnotations(MapperConfig<?> config, JavaType type, ClassIntrospector.MixInResolver r) {
/* 172 */     BasicBeanDescription desc = _findStdTypeDesc(type);
/* 173 */     if (desc == null) {
/* 174 */       desc = BasicBeanDescription.forOtherUse(config, type, 
/* 175 */           _resolveAnnotatedWithoutSuperTypes(config, type, r));
/*     */     }
/* 177 */     return desc;
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
/*     */   protected POJOPropertiesCollector collectProperties(MapperConfig<?> config, JavaType type, ClassIntrospector.MixInResolver r, boolean forSerialization, String mutatorPrefix) {
/* 190 */     return constructPropertyCollector(config, 
/* 191 */         _resolveAnnotatedClass(config, type, r), type, forSerialization, mutatorPrefix);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected POJOPropertiesCollector collectPropertiesWithBuilder(MapperConfig<?> config, JavaType type, ClassIntrospector.MixInResolver r, boolean forSerialization) {
/* 198 */     AnnotatedClass ac = _resolveAnnotatedClass(config, type, r);
/* 199 */     AnnotationIntrospector ai = config.isAnnotationProcessingEnabled() ? config.getAnnotationIntrospector() : null;
/* 200 */     JsonPOJOBuilder.Value builderConfig = (ai == null) ? null : ai.findPOJOBuilderConfig(ac);
/* 201 */     String mutatorPrefix = (builderConfig == null) ? "with" : builderConfig.withPrefix;
/* 202 */     return constructPropertyCollector(config, ac, type, forSerialization, mutatorPrefix);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected POJOPropertiesCollector constructPropertyCollector(MapperConfig<?> config, AnnotatedClass ac, JavaType type, boolean forSerialization, String mutatorPrefix) {
/* 212 */     return new POJOPropertiesCollector(config, forSerialization, type, ac, mutatorPrefix);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BasicBeanDescription _findStdTypeDesc(JavaType type) {
/* 221 */     Class<?> cls = type.getRawClass();
/* 222 */     if (cls.isPrimitive()) {
/* 223 */       if (cls == boolean.class) {
/* 224 */         return BOOLEAN_DESC;
/*     */       }
/* 226 */       if (cls == int.class) {
/* 227 */         return INT_DESC;
/*     */       }
/* 229 */       if (cls == long.class) {
/* 230 */         return LONG_DESC;
/*     */       }
/*     */     }
/* 233 */     else if (cls == String.class) {
/* 234 */       return STRING_DESC;
/*     */     } 
/*     */     
/* 237 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean _isStdJDKCollection(JavaType type) {
/* 247 */     if (!type.isContainerType() || type.isArrayType()) {
/* 248 */       return false;
/*     */     }
/* 250 */     Class<?> raw = type.getRawClass();
/* 251 */     String pkgName = ClassUtil.getPackageName(raw);
/* 252 */     if (pkgName != null && (
/* 253 */       pkgName.startsWith("java.lang") || pkgName
/* 254 */       .startsWith("java.util")))
/*     */     {
/*     */ 
/*     */       
/* 258 */       if (Collection.class.isAssignableFrom(raw) || Map.class
/* 259 */         .isAssignableFrom(raw)) {
/* 260 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 264 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BasicBeanDescription _findStdJdkCollectionDesc(MapperConfig<?> cfg, JavaType type) {
/* 269 */     if (_isStdJDKCollection(type)) {
/* 270 */       return BasicBeanDescription.forOtherUse(cfg, type, 
/* 271 */           _resolveAnnotatedClass(cfg, type, (ClassIntrospector.MixInResolver)cfg));
/*     */     }
/* 273 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AnnotatedClass _resolveAnnotatedClass(MapperConfig<?> config, JavaType type, ClassIntrospector.MixInResolver r) {
/* 281 */     return AnnotatedClassResolver.resolve(config, type, r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AnnotatedClass _resolveAnnotatedWithoutSuperTypes(MapperConfig<?> config, JavaType type, ClassIntrospector.MixInResolver r) {
/* 289 */     return AnnotatedClassResolver.resolveWithoutSuperTypes(config, type, r);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\introspect\BasicClassIntrospector.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */