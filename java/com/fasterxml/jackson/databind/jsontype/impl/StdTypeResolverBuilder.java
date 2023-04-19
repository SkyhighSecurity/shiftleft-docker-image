/*     */ package com.fasterxml.jackson.databind.jsontype.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonTypeInfo;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.annotation.NoClass;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.jsontype.NamedType;
/*     */ import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.util.Collection;
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
/*     */ public class StdTypeResolverBuilder
/*     */   implements TypeResolverBuilder<StdTypeResolverBuilder>
/*     */ {
/*     */   protected JsonTypeInfo.Id _idType;
/*     */   protected JsonTypeInfo.As _includeAs;
/*     */   protected String _typeProperty;
/*     */   protected boolean _typeIdVisible = false;
/*     */   protected Class<?> _defaultImpl;
/*     */   protected TypeIdResolver _customIdResolver;
/*     */   
/*     */   protected StdTypeResolverBuilder(JsonTypeInfo.Id idType, JsonTypeInfo.As idAs, String propName) {
/*  56 */     this._idType = idType;
/*  57 */     this._includeAs = idAs;
/*  58 */     this._typeProperty = propName;
/*     */   }
/*     */   
/*     */   public static StdTypeResolverBuilder noTypeInfoBuilder() {
/*  62 */     return (new StdTypeResolverBuilder()).init(JsonTypeInfo.Id.NONE, (TypeIdResolver)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StdTypeResolverBuilder init(JsonTypeInfo.Id idType, TypeIdResolver idRes) {
/*  69 */     if (idType == null) {
/*  70 */       throw new IllegalArgumentException("idType cannot be null");
/*     */     }
/*  72 */     this._idType = idType;
/*  73 */     this._customIdResolver = idRes;
/*     */     
/*  75 */     this._typeProperty = idType.getDefaultPropertyName();
/*  76 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeSerializer buildTypeSerializer(SerializationConfig config, JavaType baseType, Collection<NamedType> subtypes) {
/*  83 */     if (this._idType == JsonTypeInfo.Id.NONE) return null;
/*     */ 
/*     */     
/*  86 */     if (baseType.isPrimitive()) {
/*  87 */       return null;
/*     */     }
/*  89 */     TypeIdResolver idRes = idResolver((MapperConfig<?>)config, baseType, subTypeValidator((MapperConfig<?>)config), subtypes, true, false);
/*     */     
/*  91 */     switch (this._includeAs) {
/*     */       case CLASS:
/*  93 */         return new AsArrayTypeSerializer(idRes, null);
/*     */       case MINIMAL_CLASS:
/*  95 */         return new AsPropertyTypeSerializer(idRes, null, this._typeProperty);
/*     */       case NAME:
/*  97 */         return new AsWrapperTypeSerializer(idRes, null);
/*     */       case NONE:
/*  99 */         return new AsExternalTypeSerializer(idRes, null, this._typeProperty);
/*     */       
/*     */       case CUSTOM:
/* 102 */         return new AsExistingPropertyTypeSerializer(idRes, null, this._typeProperty);
/*     */     } 
/* 104 */     throw new IllegalStateException("Do not know how to construct standard type serializer for inclusion type: " + this._includeAs);
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
/*     */   public TypeDeserializer buildTypeDeserializer(DeserializationConfig config, JavaType baseType, Collection<NamedType> subtypes) {
/* 117 */     if (this._idType == JsonTypeInfo.Id.NONE) return null;
/*     */ 
/*     */     
/* 120 */     if (baseType.isPrimitive()) {
/* 121 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 126 */     PolymorphicTypeValidator subTypeValidator = verifyBaseTypeValidity((MapperConfig<?>)config, baseType);
/*     */     
/* 128 */     TypeIdResolver idRes = idResolver((MapperConfig<?>)config, baseType, subTypeValidator, subtypes, false, true);
/*     */     
/* 130 */     JavaType defaultImpl = defineDefaultImpl(config, baseType);
/*     */ 
/*     */     
/* 133 */     switch (this._includeAs) {
/*     */       case CLASS:
/* 135 */         return new AsArrayTypeDeserializer(baseType, idRes, this._typeProperty, this._typeIdVisible, defaultImpl);
/*     */       
/*     */       case MINIMAL_CLASS:
/*     */       case CUSTOM:
/* 139 */         return new AsPropertyTypeDeserializer(baseType, idRes, this._typeProperty, this._typeIdVisible, defaultImpl, this._includeAs);
/*     */       
/*     */       case NAME:
/* 142 */         return new AsWrapperTypeDeserializer(baseType, idRes, this._typeProperty, this._typeIdVisible, defaultImpl);
/*     */       
/*     */       case NONE:
/* 145 */         return new AsExternalTypeDeserializer(baseType, idRes, this._typeProperty, this._typeIdVisible, defaultImpl);
/*     */     } 
/*     */     
/* 148 */     throw new IllegalStateException("Do not know how to construct standard type serializer for inclusion type: " + this._includeAs);
/*     */   }
/*     */   
/*     */   protected JavaType defineDefaultImpl(DeserializationConfig config, JavaType baseType) {
/*     */     JavaType defaultImpl;
/* 153 */     if (this._defaultImpl == null) {
/*     */       
/* 155 */       if (config.isEnabled(MapperFeature.USE_BASE_TYPE_AS_DEFAULT_IMPL) && !baseType.isAbstract()) {
/* 156 */         defaultImpl = baseType;
/*     */       } else {
/* 158 */         defaultImpl = null;
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 167 */     else if (this._defaultImpl == Void.class || this._defaultImpl == NoClass.class) {
/*     */       
/* 169 */       defaultImpl = config.getTypeFactory().constructType(this._defaultImpl);
/*     */     }
/* 171 */     else if (baseType.hasRawClass(this._defaultImpl)) {
/* 172 */       defaultImpl = baseType;
/* 173 */     } else if (baseType.isTypeOrSuperTypeOf(this._defaultImpl)) {
/*     */ 
/*     */       
/* 176 */       defaultImpl = config.getTypeFactory().constructSpecializedType(baseType, this._defaultImpl);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 188 */       defaultImpl = null;
/*     */     } 
/*     */ 
/*     */     
/* 192 */     return defaultImpl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StdTypeResolverBuilder inclusion(JsonTypeInfo.As includeAs) {
/* 203 */     if (includeAs == null) {
/* 204 */       throw new IllegalArgumentException("includeAs cannot be null");
/*     */     }
/* 206 */     this._includeAs = includeAs;
/* 207 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StdTypeResolverBuilder typeProperty(String typeIdPropName) {
/* 217 */     if (typeIdPropName == null || typeIdPropName.length() == 0) {
/* 218 */       typeIdPropName = this._idType.getDefaultPropertyName();
/*     */     }
/* 220 */     this._typeProperty = typeIdPropName;
/* 221 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public StdTypeResolverBuilder defaultImpl(Class<?> defaultImpl) {
/* 226 */     this._defaultImpl = defaultImpl;
/* 227 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public StdTypeResolverBuilder typeIdVisibility(boolean isVisible) {
/* 232 */     this._typeIdVisible = isVisible;
/* 233 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getDefaultImpl() {
/* 242 */     return this._defaultImpl;
/*     */   }
/* 244 */   public String getTypeProperty() { return this._typeProperty; } public boolean isTypeIdVisible() {
/* 245 */     return this._typeIdVisible;
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
/*     */ 
/*     */   
/*     */   protected TypeIdResolver idResolver(MapperConfig<?> config, JavaType baseType, PolymorphicTypeValidator subtypeValidator, Collection<NamedType> subtypes, boolean forSer, boolean forDeser) {
/* 263 */     if (this._customIdResolver != null) return this._customIdResolver; 
/* 264 */     if (this._idType == null) throw new IllegalStateException("Cannot build, 'init()' not yet called"); 
/* 265 */     switch (this._idType) {
/*     */       case CLASS:
/* 267 */         return ClassNameIdResolver.construct(baseType, config, subtypeValidator);
/*     */       case MINIMAL_CLASS:
/* 269 */         return MinimalClassNameIdResolver.construct(baseType, config, subtypeValidator);
/*     */       case NAME:
/* 271 */         return TypeNameIdResolver.construct(config, baseType, subtypes, forSer, forDeser);
/*     */       case NONE:
/* 273 */         return null;
/*     */     } 
/*     */     
/* 276 */     throw new IllegalStateException("Do not know how to construct standard type id resolver for idType: " + this._idType);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public PolymorphicTypeValidator subTypeValidator(MapperConfig<?> config) {
/* 295 */     return config.getPolymorphicTypeValidator();
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
/*     */   protected PolymorphicTypeValidator verifyBaseTypeValidity(MapperConfig<?> config, JavaType baseType) {
/* 308 */     PolymorphicTypeValidator ptv = subTypeValidator(config);
/* 309 */     if (this._idType == JsonTypeInfo.Id.CLASS || this._idType == JsonTypeInfo.Id.MINIMAL_CLASS) {
/* 310 */       PolymorphicTypeValidator.Validity validity = ptv.validateBaseType(config, baseType);
/*     */       
/* 312 */       if (validity == PolymorphicTypeValidator.Validity.DENIED) {
/* 313 */         return reportInvalidBaseType(config, baseType, ptv);
/*     */       }
/*     */       
/* 316 */       if (validity == PolymorphicTypeValidator.Validity.ALLOWED) {
/* 317 */         return (PolymorphicTypeValidator)LaissezFaireSubTypeValidator.instance;
/*     */       }
/*     */     } 
/*     */     
/* 321 */     return ptv;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected PolymorphicTypeValidator reportInvalidBaseType(MapperConfig<?> config, JavaType baseType, PolymorphicTypeValidator ptv) {
/* 330 */     throw new IllegalArgumentException(String.format("Configured `PolymorphicTypeValidator` (of type %s) denied resolution of all subtypes of base type %s", new Object[] {
/*     */             
/* 332 */             ClassUtil.classNameOf(ptv), ClassUtil.classNameOf(baseType.getRawClass())
/*     */           }));
/*     */   }
/*     */   
/*     */   public StdTypeResolverBuilder() {}
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\jsontype\impl\StdTypeResolverBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */