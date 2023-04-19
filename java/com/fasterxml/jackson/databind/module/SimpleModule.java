/*     */ package com.fasterxml.jackson.databind.module;
/*     */ 
/*     */ import com.fasterxml.jackson.core.Version;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.KeyDeserializer;
/*     */ import com.fasterxml.jackson.databind.Module;
/*     */ import com.fasterxml.jackson.databind.PropertyNamingStrategy;
/*     */ import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
/*     */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*     */ import com.fasterxml.jackson.databind.deser.ValueInstantiators;
/*     */ import com.fasterxml.jackson.databind.jsontype.NamedType;
/*     */ import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
/*     */ import com.fasterxml.jackson.databind.ser.Serializers;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleModule
/*     */   extends Module
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final String _name;
/*     */   protected final Version _version;
/*  48 */   protected SimpleSerializers _serializers = null;
/*  49 */   protected SimpleDeserializers _deserializers = null;
/*     */   
/*  51 */   protected SimpleSerializers _keySerializers = null;
/*  52 */   protected SimpleKeyDeserializers _keyDeserializers = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   protected SimpleAbstractTypeResolver _abstractTypes = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   protected SimpleValueInstantiators _valueInstantiators = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   protected BeanDeserializerModifier _deserializerModifier = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   protected BeanSerializerModifier _serializerModifier = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   protected HashMap<Class<?>, Class<?>> _mixins = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   protected LinkedHashSet<NamedType> _subtypes = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   protected PropertyNamingStrategy _namingStrategy = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleModule() {
/* 108 */     this
/*     */       
/* 110 */       ._name = (getClass() == SimpleModule.class) ? ("SimpleModule-" + System.identityHashCode(this)) : getClass().getName();
/* 111 */     this._version = Version.unknownVersion();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleModule(String name) {
/* 119 */     this(name, Version.unknownVersion());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleModule(Version version) {
/* 127 */     this._name = version.getArtifactId();
/* 128 */     this._version = version;
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
/*     */   public SimpleModule(String name, Version version) {
/* 141 */     this._name = name;
/* 142 */     this._version = version;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleModule(String name, Version version, Map<Class<?>, JsonDeserializer<?>> deserializers) {
/* 150 */     this(name, version, deserializers, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleModule(String name, Version version, List<JsonSerializer<?>> serializers) {
/* 158 */     this(name, version, null, serializers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleModule(String name, Version version, Map<Class<?>, JsonDeserializer<?>> deserializers, List<JsonSerializer<?>> serializers) {
/* 168 */     this._name = name;
/* 169 */     this._version = version;
/* 170 */     if (deserializers != null) {
/* 171 */       this._deserializers = new SimpleDeserializers(deserializers);
/*     */     }
/* 173 */     if (serializers != null) {
/* 174 */       this._serializers = new SimpleSerializers(serializers);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getTypeId() {
/* 185 */     if (getClass() == SimpleModule.class) {
/* 186 */       return null;
/*     */     }
/* 188 */     return super.getTypeId();
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
/*     */   public void setSerializers(SimpleSerializers s) {
/* 201 */     this._serializers = s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDeserializers(SimpleDeserializers d) {
/* 208 */     this._deserializers = d;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeySerializers(SimpleSerializers ks) {
/* 215 */     this._keySerializers = ks;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeyDeserializers(SimpleKeyDeserializers kd) {
/* 222 */     this._keyDeserializers = kd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAbstractTypes(SimpleAbstractTypeResolver atr) {
/* 229 */     this._abstractTypes = atr;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValueInstantiators(SimpleValueInstantiators svi) {
/* 236 */     this._valueInstantiators = svi;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleModule setDeserializerModifier(BeanDeserializerModifier mod) {
/* 243 */     this._deserializerModifier = mod;
/* 244 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleModule setSerializerModifier(BeanSerializerModifier mod) {
/* 251 */     this._serializerModifier = mod;
/* 252 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SimpleModule setNamingStrategy(PropertyNamingStrategy naming) {
/* 259 */     this._namingStrategy = naming;
/* 260 */     return this;
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
/*     */   public SimpleModule addSerializer(JsonSerializer<?> ser) {
/* 279 */     _checkNotNull(ser, "serializer");
/* 280 */     if (this._serializers == null) {
/* 281 */       this._serializers = new SimpleSerializers();
/*     */     }
/* 283 */     this._serializers.addSerializer(ser);
/* 284 */     return this;
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
/*     */   public <T> SimpleModule addSerializer(Class<? extends T> type, JsonSerializer<T> ser) {
/* 296 */     _checkNotNull(type, "type to register serializer for");
/* 297 */     _checkNotNull(ser, "serializer");
/* 298 */     if (this._serializers == null) {
/* 299 */       this._serializers = new SimpleSerializers();
/*     */     }
/* 301 */     this._serializers.addSerializer(type, ser);
/* 302 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> SimpleModule addKeySerializer(Class<? extends T> type, JsonSerializer<T> ser) {
/* 307 */     _checkNotNull(type, "type to register key serializer for");
/* 308 */     _checkNotNull(ser, "key serializer");
/* 309 */     if (this._keySerializers == null) {
/* 310 */       this._keySerializers = new SimpleSerializers();
/*     */     }
/* 312 */     this._keySerializers.addSerializer(type, ser);
/* 313 */     return this;
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
/*     */   public <T> SimpleModule addDeserializer(Class<T> type, JsonDeserializer<? extends T> deser) {
/* 331 */     _checkNotNull(type, "type to register deserializer for");
/* 332 */     _checkNotNull(deser, "deserializer");
/* 333 */     if (this._deserializers == null) {
/* 334 */       this._deserializers = new SimpleDeserializers();
/*     */     }
/* 336 */     this._deserializers.addDeserializer(type, deser);
/* 337 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleModule addKeyDeserializer(Class<?> type, KeyDeserializer deser) {
/* 342 */     _checkNotNull(type, "type to register key deserializer for");
/* 343 */     _checkNotNull(deser, "key deserializer");
/* 344 */     if (this._keyDeserializers == null) {
/* 345 */       this._keyDeserializers = new SimpleKeyDeserializers();
/*     */     }
/* 347 */     this._keyDeserializers.addDeserializer(type, deser);
/* 348 */     return this;
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
/*     */   public <T> SimpleModule addAbstractTypeMapping(Class<T> superType, Class<? extends T> subType) {
/* 365 */     _checkNotNull(superType, "abstract type to map");
/* 366 */     _checkNotNull(subType, "concrete type to map to");
/* 367 */     if (this._abstractTypes == null) {
/* 368 */       this._abstractTypes = new SimpleAbstractTypeResolver();
/*     */     }
/*     */     
/* 371 */     this._abstractTypes = this._abstractTypes.addMapping(superType, subType);
/* 372 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleModule registerSubtypes(Class<?>... subtypes) {
/* 382 */     if (this._subtypes == null) {
/* 383 */       this._subtypes = new LinkedHashSet<>();
/*     */     }
/* 385 */     for (Class<?> subtype : subtypes) {
/* 386 */       _checkNotNull(subtype, "subtype to register");
/* 387 */       this._subtypes.add(new NamedType(subtype));
/*     */     } 
/* 389 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleModule registerSubtypes(NamedType... subtypes) {
/* 399 */     if (this._subtypes == null) {
/* 400 */       this._subtypes = new LinkedHashSet<>();
/*     */     }
/* 402 */     for (NamedType subtype : subtypes) {
/* 403 */       _checkNotNull(subtype, "subtype to register");
/* 404 */       this._subtypes.add(subtype);
/*     */     } 
/* 406 */     return this;
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
/*     */   public SimpleModule registerSubtypes(Collection<Class<?>> subtypes) {
/* 418 */     if (this._subtypes == null) {
/* 419 */       this._subtypes = new LinkedHashSet<>();
/*     */     }
/* 421 */     for (Class<?> subtype : subtypes) {
/* 422 */       _checkNotNull(subtype, "subtype to register");
/* 423 */       this._subtypes.add(new NamedType(subtype));
/*     */     } 
/* 425 */     return this;
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
/*     */   public SimpleModule addValueInstantiator(Class<?> beanType, ValueInstantiator inst) {
/* 443 */     _checkNotNull(beanType, "class to register value instantiator for");
/* 444 */     _checkNotNull(inst, "value instantiator");
/* 445 */     if (this._valueInstantiators == null) {
/* 446 */       this._valueInstantiators = new SimpleValueInstantiators();
/*     */     }
/* 448 */     this._valueInstantiators = this._valueInstantiators.addValueInstantiator(beanType, inst);
/* 449 */     return this;
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
/*     */   public SimpleModule setMixInAnnotation(Class<?> targetType, Class<?> mixinClass) {
/* 462 */     _checkNotNull(targetType, "target type");
/* 463 */     _checkNotNull(mixinClass, "mixin class");
/* 464 */     if (this._mixins == null) {
/* 465 */       this._mixins = new HashMap<>();
/*     */     }
/* 467 */     this._mixins.put(targetType, mixinClass);
/* 468 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getModuleName() {
/* 479 */     return this._name;
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
/*     */   public void setupModule(Module.SetupContext context) {
/* 492 */     if (this._serializers != null) {
/* 493 */       context.addSerializers((Serializers)this._serializers);
/*     */     }
/* 495 */     if (this._deserializers != null) {
/* 496 */       context.addDeserializers(this._deserializers);
/*     */     }
/* 498 */     if (this._keySerializers != null) {
/* 499 */       context.addKeySerializers((Serializers)this._keySerializers);
/*     */     }
/* 501 */     if (this._keyDeserializers != null) {
/* 502 */       context.addKeyDeserializers(this._keyDeserializers);
/*     */     }
/* 504 */     if (this._abstractTypes != null) {
/* 505 */       context.addAbstractTypeResolver(this._abstractTypes);
/*     */     }
/* 507 */     if (this._valueInstantiators != null) {
/* 508 */       context.addValueInstantiators((ValueInstantiators)this._valueInstantiators);
/*     */     }
/* 510 */     if (this._deserializerModifier != null) {
/* 511 */       context.addBeanDeserializerModifier(this._deserializerModifier);
/*     */     }
/* 513 */     if (this._serializerModifier != null) {
/* 514 */       context.addBeanSerializerModifier(this._serializerModifier);
/*     */     }
/* 516 */     if (this._subtypes != null && this._subtypes.size() > 0) {
/* 517 */       context.registerSubtypes((NamedType[])this._subtypes.toArray((Object[])new NamedType[this._subtypes.size()]));
/*     */     }
/* 519 */     if (this._namingStrategy != null) {
/* 520 */       context.setNamingStrategy(this._namingStrategy);
/*     */     }
/* 522 */     if (this._mixins != null) {
/* 523 */       for (Map.Entry<Class<?>, Class<?>> entry : this._mixins.entrySet()) {
/* 524 */         context.setMixInAnnotations(entry.getKey(), entry.getValue());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Version version() {
/* 530 */     return this._version;
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
/*     */   protected void _checkNotNull(Object thingy, String type) {
/* 543 */     if (thingy == null)
/* 544 */       throw new IllegalArgumentException(String.format("Cannot pass `null` as %s", new Object[] { type })); 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\module\SimpleModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */