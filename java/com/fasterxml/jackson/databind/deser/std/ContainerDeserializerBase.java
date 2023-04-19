/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.deser.NullValueProvider;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*     */ import com.fasterxml.jackson.databind.deser.impl.NullsConstantProvider;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import com.fasterxml.jackson.databind.util.AccessPattern;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ContainerDeserializerBase<T>
/*     */   extends StdDeserializer<T>
/*     */   implements ValueInstantiator.Gettable
/*     */ {
/*     */   protected final JavaType _containerType;
/*     */   protected final NullValueProvider _nullProvider;
/*     */   protected final Boolean _unwrapSingle;
/*     */   protected final boolean _skipNullValues;
/*     */   
/*     */   protected ContainerDeserializerBase(JavaType selfType, NullValueProvider nuller, Boolean unwrapSingle) {
/*  52 */     super(selfType);
/*  53 */     this._containerType = selfType;
/*  54 */     this._unwrapSingle = unwrapSingle;
/*  55 */     this._nullProvider = nuller;
/*  56 */     this._skipNullValues = NullsConstantProvider.isSkipper(nuller);
/*     */   }
/*     */   
/*     */   protected ContainerDeserializerBase(JavaType selfType) {
/*  60 */     this(selfType, (NullValueProvider)null, (Boolean)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ContainerDeserializerBase(ContainerDeserializerBase<?> base) {
/*  67 */     this(base, base._nullProvider, base._unwrapSingle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ContainerDeserializerBase(ContainerDeserializerBase<?> base, NullValueProvider nuller, Boolean unwrapSingle) {
/*  75 */     super(base._containerType);
/*  76 */     this._containerType = base._containerType;
/*  77 */     this._nullProvider = nuller;
/*  78 */     this._unwrapSingle = unwrapSingle;
/*  79 */     this._skipNullValues = NullsConstantProvider.isSkipper(nuller);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaType getValueType() {
/*  89 */     return this._containerType;
/*     */   }
/*     */   
/*     */   public Boolean supportsUpdate(DeserializationConfig config) {
/*  93 */     return Boolean.TRUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public SettableBeanProperty findBackReference(String refName) {
/*  98 */     JsonDeserializer<Object> valueDeser = getContentDeserializer();
/*  99 */     if (valueDeser == null) {
/* 100 */       throw new IllegalArgumentException(String.format("Cannot handle managed/back reference '%s': type: container deserializer of type %s returned null for 'getContentDeserializer()'", new Object[] { refName, 
/*     */               
/* 102 */               getClass().getName() }));
/*     */     }
/* 104 */     return valueDeser.findBackReference(refName);
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
/*     */   public JavaType getContentType() {
/* 118 */     if (this._containerType == null) {
/* 119 */       return TypeFactory.unknownType();
/*     */     }
/* 121 */     return this._containerType.getContentType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract JsonDeserializer<Object> getContentDeserializer();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ValueInstantiator getValueInstantiator() {
/* 134 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AccessPattern getEmptyAccessPattern() {
/* 141 */     return AccessPattern.DYNAMIC;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getEmptyValue(DeserializationContext ctxt) throws JsonMappingException {
/* 146 */     ValueInstantiator vi = getValueInstantiator();
/* 147 */     if (vi == null || !vi.canCreateUsingDefault()) {
/* 148 */       JavaType type = getValueType();
/* 149 */       ctxt.reportBadDefinition(type, 
/* 150 */           String.format("Cannot create empty instance of %s, no default Creator", new Object[] { type }));
/*     */     } 
/*     */     try {
/* 153 */       return vi.createUsingDefault(ctxt);
/* 154 */     } catch (IOException e) {
/* 155 */       return ClassUtil.throwAsMappingException(ctxt, e);
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
/*     */   
/*     */   protected <BOGUS> BOGUS wrapAndThrow(Throwable t, Object ref, String key) throws IOException {
/* 171 */     while (t instanceof java.lang.reflect.InvocationTargetException && t.getCause() != null) {
/* 172 */       t = t.getCause();
/*     */     }
/*     */     
/* 175 */     ClassUtil.throwIfError(t);
/*     */     
/* 177 */     if (t instanceof IOException && !(t instanceof JsonMappingException)) {
/* 178 */       throw (IOException)t;
/*     */     }
/*     */     
/* 181 */     throw JsonMappingException.wrapWithPath(t, ref, 
/* 182 */         (String)ClassUtil.nonNull(key, "N/A"));
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\deser\std\ContainerDeserializerBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */