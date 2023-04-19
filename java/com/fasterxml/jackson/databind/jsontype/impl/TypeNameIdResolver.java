/*     */ package com.fasterxml.jackson.databind.jsontype.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonTypeInfo;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.DatabindContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.jsontype.NamedType;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TypeNameIdResolver
/*     */   extends TypeIdResolverBase
/*     */ {
/*     */   protected final MapperConfig<?> _config;
/*     */   protected final Map<String, String> _typeToId;
/*     */   protected final Map<String, JavaType> _idToType;
/*     */   
/*     */   protected TypeNameIdResolver(MapperConfig<?> config, JavaType baseType, Map<String, String> typeToId, Map<String, JavaType> idToType) {
/*  29 */     super(baseType, config.getTypeFactory());
/*  30 */     this._config = config;
/*  31 */     this._typeToId = typeToId;
/*  32 */     this._idToType = idToType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TypeNameIdResolver construct(MapperConfig<?> config, JavaType baseType, Collection<NamedType> subtypes, boolean forSer, boolean forDeser) {
/*  39 */     if (forSer == forDeser) throw new IllegalArgumentException(); 
/*  40 */     Map<String, String> typeToId = null;
/*  41 */     Map<String, JavaType> idToType = null;
/*     */     
/*  43 */     if (forSer) {
/*  44 */       typeToId = new HashMap<>();
/*     */     }
/*  46 */     if (forDeser) {
/*  47 */       idToType = new HashMap<>();
/*     */ 
/*     */       
/*  50 */       typeToId = new TreeMap<>();
/*     */     } 
/*  52 */     if (subtypes != null) {
/*  53 */       for (NamedType t : subtypes) {
/*     */ 
/*     */ 
/*     */         
/*  57 */         Class<?> cls = t.getType();
/*  58 */         String id = t.hasName() ? t.getName() : _defaultTypeId(cls);
/*  59 */         if (forSer) {
/*  60 */           typeToId.put(cls.getName(), id);
/*     */         }
/*  62 */         if (forDeser) {
/*     */ 
/*     */           
/*  65 */           JavaType prev = idToType.get(id);
/*  66 */           if (prev != null && 
/*  67 */             cls.isAssignableFrom(prev.getRawClass())) {
/*     */             continue;
/*     */           }
/*     */           
/*  71 */           idToType.put(id, config.constructType(cls));
/*     */         } 
/*     */       } 
/*     */     }
/*  75 */     return new TypeNameIdResolver(config, baseType, typeToId, idToType);
/*     */   }
/*     */   
/*     */   public JsonTypeInfo.Id getMechanism() {
/*  79 */     return JsonTypeInfo.Id.NAME;
/*     */   }
/*     */   
/*     */   public String idFromValue(Object value) {
/*  83 */     return idFromClass(value.getClass());
/*     */   }
/*     */   
/*     */   protected String idFromClass(Class<?> clazz) {
/*     */     String name;
/*  88 */     if (clazz == null) {
/*  89 */       return null;
/*     */     }
/*  91 */     Class<?> cls = this._typeFactory.constructType(clazz).getRawClass();
/*  92 */     String key = cls.getName();
/*     */ 
/*     */     
/*  95 */     synchronized (this._typeToId) {
/*  96 */       name = this._typeToId.get(key);
/*  97 */       if (name == null) {
/*     */ 
/*     */         
/* 100 */         if (this._config.isAnnotationProcessingEnabled()) {
/* 101 */           BeanDescription beanDesc = this._config.introspectClassAnnotations(cls);
/* 102 */           name = this._config.getAnnotationIntrospector().findTypeName(beanDesc.getClassInfo());
/*     */         } 
/* 104 */         if (name == null)
/*     */         {
/* 106 */           name = _defaultTypeId(cls);
/*     */         }
/* 108 */         this._typeToId.put(key, name);
/*     */       } 
/*     */     } 
/* 111 */     return name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String idFromValueAndType(Object value, Class<?> type) {
/* 118 */     if (value == null) {
/* 119 */       return idFromClass(type);
/*     */     }
/* 121 */     return idFromValue(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaType typeFromId(DatabindContext context, String id) {
/* 126 */     return _typeFromId(id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JavaType _typeFromId(String id) {
/* 134 */     return this._idToType.get(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescForKnownTypeIds() {
/* 139 */     return (new TreeSet(this._idToType.keySet())).toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 144 */     return String.format("[%s; id-to-type=%s]", new Object[] { getClass().getName(), this._idToType });
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
/*     */   protected static String _defaultTypeId(Class<?> cls) {
/* 159 */     String n = cls.getName();
/* 160 */     int ix = n.lastIndexOf('.');
/* 161 */     return (ix < 0) ? n : n.substring(ix + 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\jsontype\impl\TypeNameIdResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */