/*     */ package com.fasterxml.jackson.databind.ser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class PropertySerializerMap
/*     */ {
/*     */   protected final boolean _resetWhenFull;
/*     */   
/*     */   protected PropertySerializerMap(boolean resetWhenFull) {
/*  36 */     this._resetWhenFull = resetWhenFull;
/*     */   }
/*     */   
/*     */   protected PropertySerializerMap(PropertySerializerMap base) {
/*  40 */     this._resetWhenFull = base._resetWhenFull;
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
/*     */   public abstract JsonSerializer<Object> serializerFor(Class<?> paramClass);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final SerializerAndMapResult findAndAddPrimarySerializer(Class<?> type, SerializerProvider provider, BeanProperty property) throws JsonMappingException {
/*  64 */     JsonSerializer<Object> serializer = provider.findPrimaryPropertySerializer(type, property);
/*  65 */     return new SerializerAndMapResult(serializer, newWith(type, serializer));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final SerializerAndMapResult findAndAddPrimarySerializer(JavaType type, SerializerProvider provider, BeanProperty property) throws JsonMappingException {
/*  72 */     JsonSerializer<Object> serializer = provider.findPrimaryPropertySerializer(type, property);
/*  73 */     return new SerializerAndMapResult(serializer, newWith(type.getRawClass(), serializer));
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
/*     */   public final SerializerAndMapResult findAndAddSecondarySerializer(Class<?> type, SerializerProvider provider, BeanProperty property) throws JsonMappingException {
/*  90 */     JsonSerializer<Object> serializer = provider.findValueSerializer(type, property);
/*  91 */     return new SerializerAndMapResult(serializer, newWith(type, serializer));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final SerializerAndMapResult findAndAddSecondarySerializer(JavaType type, SerializerProvider provider, BeanProperty property) throws JsonMappingException {
/*  98 */     JsonSerializer<Object> serializer = provider.findValueSerializer(type, property);
/*  99 */     return new SerializerAndMapResult(serializer, newWith(type.getRawClass(), serializer));
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
/*     */   public final SerializerAndMapResult findAndAddRootValueSerializer(Class<?> type, SerializerProvider provider) throws JsonMappingException {
/* 117 */     JsonSerializer<Object> serializer = provider.findTypedValueSerializer(type, false, null);
/* 118 */     return new SerializerAndMapResult(serializer, newWith(type, serializer));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final SerializerAndMapResult findAndAddRootValueSerializer(JavaType type, SerializerProvider provider) throws JsonMappingException {
/* 128 */     JsonSerializer<Object> serializer = provider.findTypedValueSerializer(type, false, null);
/* 129 */     return new SerializerAndMapResult(serializer, newWith(type.getRawClass(), serializer));
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
/*     */   public final SerializerAndMapResult findAndAddKeySerializer(Class<?> type, SerializerProvider provider, BeanProperty property) throws JsonMappingException {
/* 144 */     JsonSerializer<Object> serializer = provider.findKeySerializer(type, property);
/* 145 */     return new SerializerAndMapResult(serializer, newWith(type, serializer));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final SerializerAndMapResult addSerializer(Class<?> type, JsonSerializer<Object> serializer) {
/* 155 */     return new SerializerAndMapResult(serializer, newWith(type, serializer));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final SerializerAndMapResult addSerializer(JavaType type, JsonSerializer<Object> serializer) {
/* 162 */     return new SerializerAndMapResult(serializer, newWith(type.getRawClass(), serializer));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract PropertySerializerMap newWith(Class<?> paramClass, JsonSerializer<Object> paramJsonSerializer);
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static PropertySerializerMap emptyMap() {
/* 172 */     return emptyForProperties();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PropertySerializerMap emptyForProperties() {
/* 179 */     return Empty.FOR_PROPERTIES;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PropertySerializerMap emptyForRootValues() {
/* 186 */     return Empty.FOR_ROOT_VALUES;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class SerializerAndMapResult
/*     */   {
/*     */     public final JsonSerializer<Object> serializer;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final PropertySerializerMap map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public SerializerAndMapResult(JsonSerializer<Object> serializer, PropertySerializerMap map) {
/* 207 */       this.serializer = serializer;
/* 208 */       this.map = map;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class TypeAndSerializer
/*     */   {
/*     */     public final Class<?> type;
/*     */     
/*     */     public final JsonSerializer<Object> serializer;
/*     */ 
/*     */     
/*     */     public TypeAndSerializer(Class<?> type, JsonSerializer<Object> serializer) {
/* 221 */       this.type = type;
/* 222 */       this.serializer = serializer;
/*     */     }
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
/*     */   private static final class Empty
/*     */     extends PropertySerializerMap
/*     */   {
/* 239 */     public static final Empty FOR_PROPERTIES = new Empty(false);
/*     */ 
/*     */     
/* 242 */     public static final Empty FOR_ROOT_VALUES = new Empty(true);
/*     */     
/*     */     protected Empty(boolean resetWhenFull) {
/* 245 */       super(resetWhenFull);
/*     */     }
/*     */ 
/*     */     
/*     */     public JsonSerializer<Object> serializerFor(Class<?> type) {
/* 250 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public PropertySerializerMap newWith(Class<?> type, JsonSerializer<Object> serializer) {
/* 255 */       return new PropertySerializerMap.Single(this, type, serializer);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class Single
/*     */     extends PropertySerializerMap
/*     */   {
/*     */     private final Class<?> _type;
/*     */ 
/*     */     
/*     */     private final JsonSerializer<Object> _serializer;
/*     */ 
/*     */     
/*     */     public Single(PropertySerializerMap base, Class<?> type, JsonSerializer<Object> serializer) {
/* 271 */       super(base);
/* 272 */       this._type = type;
/* 273 */       this._serializer = serializer;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public JsonSerializer<Object> serializerFor(Class<?> type) {
/* 279 */       if (type == this._type) {
/* 280 */         return this._serializer;
/*     */       }
/* 282 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public PropertySerializerMap newWith(Class<?> type, JsonSerializer<Object> serializer) {
/* 287 */       return new PropertySerializerMap.Double(this, this._type, this._serializer, type, serializer);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Double
/*     */     extends PropertySerializerMap
/*     */   {
/*     */     private final Class<?> _type1;
/*     */     private final Class<?> _type2;
/*     */     private final JsonSerializer<Object> _serializer1;
/*     */     private final JsonSerializer<Object> _serializer2;
/*     */     
/*     */     public Double(PropertySerializerMap base, Class<?> type1, JsonSerializer<Object> serializer1, Class<?> type2, JsonSerializer<Object> serializer2) {
/* 300 */       super(base);
/* 301 */       this._type1 = type1;
/* 302 */       this._serializer1 = serializer1;
/* 303 */       this._type2 = type2;
/* 304 */       this._serializer2 = serializer2;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public JsonSerializer<Object> serializerFor(Class<?> type) {
/* 310 */       if (type == this._type1) {
/* 311 */         return this._serializer1;
/*     */       }
/* 313 */       if (type == this._type2) {
/* 314 */         return this._serializer2;
/*     */       }
/* 316 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public PropertySerializerMap newWith(Class<?> type, JsonSerializer<Object> serializer) {
/* 322 */       PropertySerializerMap.TypeAndSerializer[] ts = new PropertySerializerMap.TypeAndSerializer[3];
/* 323 */       ts[0] = new PropertySerializerMap.TypeAndSerializer(this._type1, this._serializer1);
/* 324 */       ts[1] = new PropertySerializerMap.TypeAndSerializer(this._type2, this._serializer2);
/* 325 */       ts[2] = new PropertySerializerMap.TypeAndSerializer(type, serializer);
/* 326 */       return new PropertySerializerMap.Multi(this, ts);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class Multi
/*     */     extends PropertySerializerMap
/*     */   {
/*     */     private static final int MAX_ENTRIES = 8;
/*     */ 
/*     */ 
/*     */     
/*     */     private final PropertySerializerMap.TypeAndSerializer[] _entries;
/*     */ 
/*     */ 
/*     */     
/*     */     public Multi(PropertySerializerMap base, PropertySerializerMap.TypeAndSerializer[] entries) {
/* 345 */       super(base);
/* 346 */       this._entries = entries;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public JsonSerializer<Object> serializerFor(Class<?> type) {
/* 354 */       PropertySerializerMap.TypeAndSerializer entry = this._entries[0];
/* 355 */       if (entry.type == type) return entry.serializer; 
/* 356 */       entry = this._entries[1];
/* 357 */       if (entry.type == type) return entry.serializer; 
/* 358 */       entry = this._entries[2];
/* 359 */       if (entry.type == type) return entry.serializer;
/*     */       
/* 361 */       switch (this._entries.length) {
/*     */         case 8:
/* 363 */           entry = this._entries[7];
/* 364 */           if (entry.type == type) return entry.serializer; 
/*     */         case 7:
/* 366 */           entry = this._entries[6];
/* 367 */           if (entry.type == type) return entry.serializer; 
/*     */         case 6:
/* 369 */           entry = this._entries[5];
/* 370 */           if (entry.type == type) return entry.serializer; 
/*     */         case 5:
/* 372 */           entry = this._entries[4];
/* 373 */           if (entry.type == type) return entry.serializer; 
/*     */         case 4:
/* 375 */           entry = this._entries[3];
/* 376 */           if (entry.type == type) return entry.serializer; 
/*     */           break;
/*     */       } 
/* 379 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public PropertySerializerMap newWith(Class<?> type, JsonSerializer<Object> serializer) {
/* 385 */       int len = this._entries.length;
/*     */ 
/*     */       
/* 388 */       if (len == 8) {
/* 389 */         if (this._resetWhenFull) {
/* 390 */           return new PropertySerializerMap.Single(this, type, serializer);
/*     */         }
/* 392 */         return this;
/*     */       } 
/* 394 */       PropertySerializerMap.TypeAndSerializer[] entries = Arrays.<PropertySerializerMap.TypeAndSerializer>copyOf(this._entries, len + 1);
/* 395 */       entries[len] = new PropertySerializerMap.TypeAndSerializer(type, serializer);
/* 396 */       return new Multi(this, entries);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\impl\PropertySerializerMap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */