/*     */ package com.fasterxml.jackson.databind;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat;
/*     */ import com.fasterxml.jackson.annotation.ObjectIdGenerator;
/*     */ import com.fasterxml.jackson.annotation.ObjectIdResolver;
/*     */ import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*     */ import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
/*     */ import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import com.fasterxml.jackson.databind.util.Converter;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
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
/*     */ public abstract class DatabindContext
/*     */ {
/*     */   private static final int MAX_ERROR_STR_LEN = 500;
/*     */   
/*     */   public abstract MapperConfig<?> getConfig();
/*     */   
/*     */   public abstract AnnotationIntrospector getAnnotationIntrospector();
/*     */   
/*     */   public abstract boolean isEnabled(MapperFeature paramMapperFeature);
/*     */   
/*     */   public abstract boolean canOverrideAccessModifiers();
/*     */   
/*     */   public abstract Class<?> getActiveView();
/*     */   
/*     */   public abstract Locale getLocale();
/*     */   
/*     */   public abstract TimeZone getTimeZone();
/*     */   
/*     */   public abstract JsonFormat.Value getDefaultPropertyFormat(Class<?> paramClass);
/*     */   
/*     */   public abstract Object getAttribute(Object paramObject);
/*     */   
/*     */   public abstract DatabindContext setAttribute(Object paramObject1, Object paramObject2);
/*     */   
/*     */   public JavaType constructType(Type type) {
/* 148 */     if (type == null) {
/* 149 */       return null;
/*     */     }
/* 151 */     return getTypeFactory().constructType(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaType constructSpecializedType(JavaType baseType, Class<?> subclass) {
/* 160 */     if (baseType.getRawClass() == subclass) {
/* 161 */       return baseType;
/*     */     }
/* 163 */     return getConfig().constructSpecializedType(baseType, subclass);
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
/*     */   public JavaType resolveSubType(JavaType baseType, String subClassName) throws JsonMappingException {
/* 181 */     if (subClassName.indexOf('<') > 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 186 */       JavaType t = getTypeFactory().constructFromCanonical(subClassName);
/* 187 */       if (t.isTypeOrSubTypeOf(baseType.getRawClass())) {
/* 188 */         return t;
/*     */       }
/*     */     } else {
/*     */       Class<?> cls;
/*     */       try {
/* 193 */         cls = getTypeFactory().findClass(subClassName);
/* 194 */       } catch (ClassNotFoundException e) {
/* 195 */         return null;
/* 196 */       } catch (Exception e) {
/* 197 */         throw invalidTypeIdException(baseType, subClassName, String.format("problem: (%s) %s", new Object[] { e
/*     */                 
/* 199 */                 .getClass().getName(), 
/* 200 */                 ClassUtil.exceptionMessage(e) }));
/*     */       } 
/* 202 */       if (baseType.isTypeOrSuperTypeOf(cls)) {
/* 203 */         return getTypeFactory().constructSpecializedType(baseType, cls);
/*     */       }
/*     */     } 
/* 206 */     throw invalidTypeIdException(baseType, subClassName, "Not a subtype");
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
/*     */   public JavaType resolveAndValidateSubType(JavaType baseType, String subClass, PolymorphicTypeValidator ptv) throws JsonMappingException {
/*     */     Class<?> cls;
/* 220 */     int ltIndex = subClass.indexOf('<');
/* 221 */     if (ltIndex > 0) {
/* 222 */       return _resolveAndValidateGeneric(baseType, subClass, ptv, ltIndex);
/*     */     }
/* 224 */     MapperConfig<?> config = getConfig();
/* 225 */     PolymorphicTypeValidator.Validity vld = ptv.validateSubClassName(config, baseType, subClass);
/* 226 */     if (vld == PolymorphicTypeValidator.Validity.DENIED) {
/* 227 */       return _throwSubtypeNameNotAllowed(baseType, subClass, ptv);
/*     */     }
/*     */     
/*     */     try {
/* 231 */       cls = getTypeFactory().findClass(subClass);
/* 232 */     } catch (ClassNotFoundException e) {
/* 233 */       return null;
/* 234 */     } catch (Exception e) {
/* 235 */       throw invalidTypeIdException(baseType, subClass, String.format("problem: (%s) %s", new Object[] { e
/*     */               
/* 237 */               .getClass().getName(), 
/* 238 */               ClassUtil.exceptionMessage(e) }));
/*     */     } 
/* 240 */     if (!baseType.isTypeOrSuperTypeOf(cls)) {
/* 241 */       return _throwNotASubtype(baseType, subClass);
/*     */     }
/* 243 */     JavaType subType = config.getTypeFactory().constructSpecializedType(baseType, cls);
/*     */     
/* 245 */     if (vld != PolymorphicTypeValidator.Validity.ALLOWED && 
/* 246 */       ptv.validateSubType(config, baseType, subType) != PolymorphicTypeValidator.Validity.ALLOWED) {
/* 247 */       return _throwSubtypeClassNotAllowed(baseType, subClass, ptv);
/*     */     }
/*     */     
/* 250 */     return subType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JavaType _resolveAndValidateGeneric(JavaType baseType, String subClass, PolymorphicTypeValidator ptv, int ltIndex) throws JsonMappingException {
/* 257 */     MapperConfig<?> config = getConfig();
/*     */ 
/*     */ 
/*     */     
/* 261 */     PolymorphicTypeValidator.Validity vld = ptv.validateSubClassName(config, baseType, subClass.substring(0, ltIndex));
/* 262 */     if (vld == PolymorphicTypeValidator.Validity.DENIED) {
/* 263 */       return _throwSubtypeNameNotAllowed(baseType, subClass, ptv);
/*     */     }
/* 265 */     JavaType subType = getTypeFactory().constructFromCanonical(subClass);
/* 266 */     if (!subType.isTypeOrSubTypeOf(baseType.getRawClass())) {
/* 267 */       return _throwNotASubtype(baseType, subClass);
/*     */     }
/*     */     
/* 270 */     if (vld != PolymorphicTypeValidator.Validity.ALLOWED && 
/* 271 */       ptv.validateSubType(config, baseType, subType) != PolymorphicTypeValidator.Validity.ALLOWED) {
/* 272 */       return _throwSubtypeClassNotAllowed(baseType, subClass, ptv);
/*     */     }
/*     */     
/* 275 */     return subType;
/*     */   }
/*     */   
/*     */   protected <T> T _throwNotASubtype(JavaType baseType, String subType) throws JsonMappingException {
/* 279 */     throw invalidTypeIdException(baseType, subType, "Not a subtype");
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> T _throwSubtypeNameNotAllowed(JavaType baseType, String subType, PolymorphicTypeValidator ptv) throws JsonMappingException {
/* 284 */     throw invalidTypeIdException(baseType, subType, "Configured `PolymorphicTypeValidator` (of type " + 
/* 285 */         ClassUtil.classNameOf(ptv) + ") denied resolution");
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> T _throwSubtypeClassNotAllowed(JavaType baseType, String subType, PolymorphicTypeValidator ptv) throws JsonMappingException {
/* 290 */     throw invalidTypeIdException(baseType, subType, "Configured `PolymorphicTypeValidator` (of type " + 
/* 291 */         ClassUtil.classNameOf(ptv) + ") denied resolution");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract JsonMappingException invalidTypeIdException(JavaType paramJavaType, String paramString1, String paramString2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract TypeFactory getTypeFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectIdGenerator<?> objectIdGeneratorInstance(Annotated annotated, ObjectIdInfo objectIdInfo) throws JsonMappingException {
/* 320 */     Class<?> implClass = objectIdInfo.getGeneratorType();
/* 321 */     MapperConfig<?> config = getConfig();
/* 322 */     HandlerInstantiator hi = config.getHandlerInstantiator();
/* 323 */     ObjectIdGenerator<?> gen = (hi == null) ? null : hi.objectIdGeneratorInstance(config, annotated, implClass);
/* 324 */     if (gen == null) {
/* 325 */       gen = (ObjectIdGenerator)ClassUtil.createInstance(implClass, config
/* 326 */           .canOverrideAccessModifiers());
/*     */     }
/* 328 */     return gen.forScope(objectIdInfo.getScope());
/*     */   }
/*     */ 
/*     */   
/*     */   public ObjectIdResolver objectIdResolverInstance(Annotated annotated, ObjectIdInfo objectIdInfo) {
/* 333 */     Class<? extends ObjectIdResolver> implClass = objectIdInfo.getResolverType();
/* 334 */     MapperConfig<?> config = getConfig();
/* 335 */     HandlerInstantiator hi = config.getHandlerInstantiator();
/* 336 */     ObjectIdResolver resolver = (hi == null) ? null : hi.resolverIdGeneratorInstance(config, annotated, implClass);
/* 337 */     if (resolver == null) {
/* 338 */       resolver = (ObjectIdResolver)ClassUtil.createInstance(implClass, config.canOverrideAccessModifiers());
/*     */     }
/*     */     
/* 341 */     return resolver;
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
/*     */   public Converter<Object, Object> converterInstance(Annotated annotated, Object converterDef) throws JsonMappingException {
/* 355 */     if (converterDef == null) {
/* 356 */       return null;
/*     */     }
/* 358 */     if (converterDef instanceof Converter) {
/* 359 */       return (Converter<Object, Object>)converterDef;
/*     */     }
/* 361 */     if (!(converterDef instanceof Class)) {
/* 362 */       throw new IllegalStateException("AnnotationIntrospector returned Converter definition of type " + converterDef
/* 363 */           .getClass().getName() + "; expected type Converter or Class<Converter> instead");
/*     */     }
/* 365 */     Class<?> converterClass = (Class)converterDef;
/*     */     
/* 367 */     if (converterClass == Converter.None.class || ClassUtil.isBogusClass(converterClass)) {
/* 368 */       return null;
/*     */     }
/* 370 */     if (!Converter.class.isAssignableFrom(converterClass)) {
/* 371 */       throw new IllegalStateException("AnnotationIntrospector returned Class " + converterClass
/* 372 */           .getName() + "; expected Class<Converter>");
/*     */     }
/* 374 */     MapperConfig<?> config = getConfig();
/* 375 */     HandlerInstantiator hi = config.getHandlerInstantiator();
/* 376 */     Converter<?, ?> conv = (hi == null) ? null : hi.converterInstance(config, annotated, converterClass);
/* 377 */     if (conv == null) {
/* 378 */       conv = (Converter<?, ?>)ClassUtil.createInstance(converterClass, config
/* 379 */           .canOverrideAccessModifiers());
/*     */     }
/* 381 */     return (Converter)conv;
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
/*     */   public abstract <T> T reportBadDefinition(JavaType paramJavaType, String paramString) throws JsonMappingException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T reportBadDefinition(Class<?> type, String msg) throws JsonMappingException {
/* 403 */     return reportBadDefinition(constructType(type), msg);
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
/*     */   protected final String _format(String msg, Object... msgArgs) {
/* 416 */     if (msgArgs.length > 0) {
/* 417 */       return String.format(msg, msgArgs);
/*     */     }
/* 419 */     return msg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final String _truncate(String desc) {
/* 426 */     if (desc == null) {
/* 427 */       return "";
/*     */     }
/* 429 */     if (desc.length() <= 500) {
/* 430 */       return desc;
/*     */     }
/* 432 */     return desc.substring(0, 500) + "]...[" + desc.substring(desc.length() - 500);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String _quotedString(String desc) {
/* 439 */     if (desc == null) {
/* 440 */       return "[N/A]";
/*     */     }
/*     */     
/* 443 */     return String.format("\"%s\"", new Object[] { _truncate(desc) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String _colonConcat(String msgBase, String extra) {
/* 450 */     if (extra == null) {
/* 451 */       return msgBase;
/*     */     }
/* 453 */     return msgBase + ": " + extra;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String _desc(String desc) {
/* 460 */     if (desc == null) {
/* 461 */       return "[N/A]";
/*     */     }
/*     */     
/* 464 */     return _truncate(desc);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\DatabindContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */