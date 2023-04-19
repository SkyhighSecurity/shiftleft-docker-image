/*     */ package com.fasterxml.jackson.databind.ser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.SerializableString;
/*     */ import com.fasterxml.jackson.core.io.SerializedString;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
/*     */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*     */ import java.io.Serializable;
/*     */ import java.util.Iterator;
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
/*     */ public class UnwrappingBeanPropertyWriter
/*     */   extends BeanPropertyWriter
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final NameTransformer _nameTransformer;
/*     */   
/*     */   public UnwrappingBeanPropertyWriter(BeanPropertyWriter base, NameTransformer unwrapper) {
/*  43 */     super(base);
/*  44 */     this._nameTransformer = unwrapper;
/*     */   }
/*     */ 
/*     */   
/*     */   protected UnwrappingBeanPropertyWriter(UnwrappingBeanPropertyWriter base, NameTransformer transformer, SerializedString name) {
/*  49 */     super(base, name);
/*  50 */     this._nameTransformer = transformer;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UnwrappingBeanPropertyWriter rename(NameTransformer transformer) {
/*  56 */     String oldName = this._name.getValue();
/*  57 */     String newName = transformer.transform(oldName);
/*     */ 
/*     */     
/*  60 */     transformer = NameTransformer.chainedTransformer(transformer, this._nameTransformer);
/*     */     
/*  62 */     return _new(transformer, new SerializedString(newName));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected UnwrappingBeanPropertyWriter _new(NameTransformer transformer, SerializedString newName) {
/*  72 */     return new UnwrappingBeanPropertyWriter(this, transformer, newName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUnwrapping() {
/*  83 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider prov) throws Exception {
/*  90 */     Object value = get(bean);
/*  91 */     if (value == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  96 */     JsonSerializer<Object> ser = this._serializer;
/*  97 */     if (ser == null) {
/*  98 */       Class<?> cls = value.getClass();
/*  99 */       PropertySerializerMap map = this._dynamicSerializers;
/* 100 */       ser = map.serializerFor(cls);
/* 101 */       if (ser == null) {
/* 102 */         ser = _findAndAddDynamic(map, cls, prov);
/*     */       }
/*     */     } 
/* 105 */     if (this._suppressableValue != null) {
/* 106 */       if (MARKER_FOR_EMPTY == this._suppressableValue) {
/* 107 */         if (ser.isEmpty(prov, value)) {
/*     */           return;
/*     */         }
/* 110 */       } else if (this._suppressableValue.equals(value)) {
/*     */         return;
/*     */       } 
/*     */     }
/*     */     
/* 115 */     if (value == bean && 
/* 116 */       _handleSelfReference(bean, gen, prov, ser)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 122 */     if (!ser.isUnwrappingSerializer()) {
/* 123 */       gen.writeFieldName((SerializableString)this._name);
/*     */     }
/*     */     
/* 126 */     if (this._typeSerializer == null) {
/* 127 */       ser.serialize(value, gen, prov);
/*     */     } else {
/* 129 */       ser.serializeWithType(value, gen, prov, this._typeSerializer);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void assignSerializer(JsonSerializer<Object> ser) {
/* 137 */     if (ser != null) {
/* 138 */       NameTransformer t = this._nameTransformer;
/* 139 */       if (ser.isUnwrappingSerializer() && ser instanceof UnwrappingBeanSerializer)
/*     */       {
/*     */ 
/*     */         
/* 143 */         t = NameTransformer.chainedTransformer(t, ((UnwrappingBeanSerializer)ser)._nameTransformer);
/*     */       }
/* 145 */       ser = ser.unwrappingSerializer(t);
/*     */     } 
/* 147 */     super.assignSerializer(ser);
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
/*     */   public void depositSchemaProperty(final JsonObjectFormatVisitor visitor, SerializerProvider provider) throws JsonMappingException {
/* 162 */     JsonSerializer<Object> ser = provider.findValueSerializer(getType(), (BeanProperty)this).unwrappingSerializer(this._nameTransformer);
/*     */     
/* 164 */     if (ser.isUnwrappingSerializer()) {
/* 165 */       ser.acceptJsonFormatVisitor((JsonFormatVisitorWrapper)new JsonFormatVisitorWrapper.Base(provider)
/*     */           {
/*     */ 
/*     */             
/*     */             public JsonObjectFormatVisitor expectObjectFormat(JavaType type) throws JsonMappingException
/*     */             {
/* 171 */               return visitor;
/*     */             }
/* 173 */           }getType());
/*     */     } else {
/* 175 */       super.depositSchemaProperty(visitor, provider);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void _depositSchemaProperty(ObjectNode propertiesNode, JsonNode schemaNode) {
/* 183 */     JsonNode props = schemaNode.get("properties");
/* 184 */     if (props != null) {
/* 185 */       Iterator<Map.Entry<String, JsonNode>> it = props.fields();
/* 186 */       while (it.hasNext()) {
/* 187 */         Map.Entry<String, JsonNode> entry = it.next();
/* 188 */         String name = entry.getKey();
/* 189 */         if (this._nameTransformer != null) {
/* 190 */           name = this._nameTransformer.transform(name);
/*     */         }
/* 192 */         propertiesNode.set(name, entry.getValue());
/*     */       } 
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
/*     */   protected JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, Class<?> type, SerializerProvider provider) throws JsonMappingException {
/* 209 */     if (this._nonTrivialBaseType != null) {
/* 210 */       JavaType subtype = provider.constructSpecializedType(this._nonTrivialBaseType, type);
/* 211 */       serializer = provider.findValueSerializer(subtype, (BeanProperty)this);
/*     */     } else {
/* 213 */       serializer = provider.findValueSerializer(type, (BeanProperty)this);
/*     */     } 
/* 215 */     NameTransformer t = this._nameTransformer;
/* 216 */     if (serializer.isUnwrappingSerializer() && serializer instanceof UnwrappingBeanSerializer)
/*     */     {
/*     */ 
/*     */       
/* 220 */       t = NameTransformer.chainedTransformer(t, ((UnwrappingBeanSerializer)serializer)._nameTransformer);
/*     */     }
/* 222 */     JsonSerializer<Object> serializer = serializer.unwrappingSerializer(t);
/*     */     
/* 224 */     this._dynamicSerializers = this._dynamicSerializers.newWith(type, serializer);
/* 225 */     return serializer;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\impl\UnwrappingBeanPropertyWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */