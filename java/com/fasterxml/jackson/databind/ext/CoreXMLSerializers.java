/*     */ package com.fasterxml.jackson.databind.ext;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ContextualSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.Serializers;
/*     */ import com.fasterxml.jackson.databind.ser.std.CalendarSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.std.StdSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
/*     */ import java.io.IOException;
/*     */ import java.util.Calendar;
/*     */ import javax.xml.datatype.Duration;
/*     */ import javax.xml.datatype.XMLGregorianCalendar;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CoreXMLSerializers
/*     */   extends Serializers.Base
/*     */ {
/*     */   public JsonSerializer<?> findSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc) {
/*  35 */     Class<?> raw = type.getRawClass();
/*  36 */     if (Duration.class.isAssignableFrom(raw) || QName.class.isAssignableFrom(raw)) {
/*  37 */       return (JsonSerializer<?>)ToStringSerializer.instance;
/*     */     }
/*  39 */     if (XMLGregorianCalendar.class.isAssignableFrom(raw)) {
/*  40 */       return (JsonSerializer<?>)XMLGregorianCalendarSerializer.instance;
/*     */     }
/*  42 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class XMLGregorianCalendarSerializer
/*     */     extends StdSerializer<XMLGregorianCalendar>
/*     */     implements ContextualSerializer
/*     */   {
/*  50 */     static final XMLGregorianCalendarSerializer instance = new XMLGregorianCalendarSerializer();
/*     */     
/*     */     final JsonSerializer<Object> _delegate;
/*     */     
/*     */     public XMLGregorianCalendarSerializer() {
/*  55 */       this((JsonSerializer<?>)CalendarSerializer.instance);
/*     */     }
/*     */ 
/*     */     
/*     */     protected XMLGregorianCalendarSerializer(JsonSerializer<?> del) {
/*  60 */       super(XMLGregorianCalendar.class);
/*  61 */       this._delegate = (JsonSerializer)del;
/*     */     }
/*     */ 
/*     */     
/*     */     public JsonSerializer<?> getDelegatee() {
/*  66 */       return this._delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty(SerializerProvider provider, XMLGregorianCalendar value) {
/*  71 */       return this._delegate.isEmpty(provider, _convert(value));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void serialize(XMLGregorianCalendar value, JsonGenerator gen, SerializerProvider provider) throws IOException {
/*  77 */       this._delegate.serialize(_convert(value), gen, provider);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void serializeWithType(XMLGregorianCalendar value, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
/*  84 */       this._delegate.serializeWithType(_convert(value), gen, provider, typeSer);
/*     */     }
/*     */ 
/*     */     
/*     */     public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
/*  89 */       this._delegate.acceptJsonFormatVisitor(visitor, null);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
/*  95 */       JsonSerializer<?> ser = prov.handlePrimaryContextualization(this._delegate, property);
/*  96 */       if (ser != this._delegate) {
/*  97 */         return (JsonSerializer<?>)new XMLGregorianCalendarSerializer(ser);
/*     */       }
/*  99 */       return (JsonSerializer<?>)this;
/*     */     }
/*     */     
/*     */     protected Calendar _convert(XMLGregorianCalendar input) {
/* 103 */       return (input == null) ? null : input.toGregorianCalendar();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ext\CoreXMLSerializers.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */