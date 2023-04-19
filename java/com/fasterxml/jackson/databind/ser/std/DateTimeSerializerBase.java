/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat;
/*     */ import com.fasterxml.jackson.databind.ser.ContextualSerializer;
/*     */ import com.fasterxml.jackson.databind.util.StdDateFormat;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DateTimeSerializerBase<T>
/*     */   extends StdScalarSerializer<T>
/*     */   implements ContextualSerializer
/*     */ {
/*     */   protected final Boolean _useTimestamp;
/*     */   protected final DateFormat _customFormat;
/*     */   protected final AtomicReference<DateFormat> _reusedCustomFormat;
/*     */   
/*     */   protected DateTimeSerializerBase(Class<T> type, Boolean useTimestamp, DateFormat customFormat) {
/*  53 */     super(type);
/*  54 */     this._useTimestamp = useTimestamp;
/*  55 */     this._customFormat = customFormat;
/*  56 */     this._reusedCustomFormat = (customFormat == null) ? null : new AtomicReference<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract DateTimeSerializerBase<T> withFormat(Boolean paramBoolean, DateFormat paramDateFormat);
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonSerializer<?> createContextual(SerializerProvider serializers, BeanProperty property) throws JsonMappingException {
/*  67 */     JsonFormat.Value format = findFormatOverrides(serializers, property, handledType());
/*  68 */     if (format == null) {
/*  69 */       return this;
/*     */     }
/*     */     
/*  72 */     JsonFormat.Shape shape = format.getShape();
/*  73 */     if (shape.isNumeric()) {
/*  74 */       return withFormat(Boolean.TRUE, (DateFormat)null);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  79 */     if (format.hasPattern()) {
/*     */ 
/*     */       
/*  82 */       Locale loc = format.hasLocale() ? format.getLocale() : serializers.getLocale();
/*  83 */       SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format.getPattern(), loc);
/*     */       
/*  85 */       TimeZone tz = format.hasTimeZone() ? format.getTimeZone() : serializers.getTimeZone();
/*  86 */       simpleDateFormat.setTimeZone(tz);
/*  87 */       return withFormat(Boolean.FALSE, simpleDateFormat);
/*     */     } 
/*     */ 
/*     */     
/*  91 */     boolean hasLocale = format.hasLocale();
/*  92 */     boolean hasTZ = format.hasTimeZone();
/*  93 */     boolean asString = (shape == JsonFormat.Shape.STRING);
/*     */     
/*  95 */     if (!hasLocale && !hasTZ && !asString) {
/*  96 */       return this;
/*     */     }
/*     */     
/*  99 */     DateFormat df0 = serializers.getConfig().getDateFormat();
/*     */     
/* 101 */     if (df0 instanceof StdDateFormat) {
/* 102 */       StdDateFormat std = (StdDateFormat)df0;
/* 103 */       if (format.hasLocale()) {
/* 104 */         std = std.withLocale(format.getLocale());
/*     */       }
/* 106 */       if (format.hasTimeZone()) {
/* 107 */         std = std.withTimeZone(format.getTimeZone());
/*     */       }
/* 109 */       return withFormat(Boolean.FALSE, (DateFormat)std);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 115 */     if (!(df0 instanceof SimpleDateFormat)) {
/* 116 */       serializers.reportBadDefinition(handledType(), String.format("Configured `DateFormat` (%s) not a `SimpleDateFormat`; cannot configure `Locale` or `TimeZone`", new Object[] { df0
/*     */               
/* 118 */               .getClass().getName() }));
/*     */     }
/* 120 */     SimpleDateFormat df = (SimpleDateFormat)df0;
/* 121 */     if (hasLocale) {
/*     */       
/* 123 */       df = new SimpleDateFormat(df.toPattern(), format.getLocale());
/*     */     } else {
/* 125 */       df = (SimpleDateFormat)df.clone();
/*     */     } 
/* 127 */     TimeZone newTz = format.getTimeZone();
/* 128 */     boolean changeTZ = (newTz != null && !newTz.equals(df.getTimeZone()));
/* 129 */     if (changeTZ) {
/* 130 */       df.setTimeZone(newTz);
/*     */     }
/* 132 */     return withFormat(Boolean.FALSE, df);
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
/*     */   public boolean isEmpty(SerializerProvider serializers, T value) {
/* 146 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract long _timestamp(T paramT);
/*     */ 
/*     */   
/*     */   public JsonNode getSchema(SerializerProvider serializers, Type typeHint) {
/* 154 */     return (JsonNode)createSchemaNode(_asTimestamp(serializers) ? "number" : "string", true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
/* 160 */     _acceptJsonFormatVisitor(visitor, typeHint, _asTimestamp(visitor.getProvider()));
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
/*     */   public abstract void serialize(T paramT, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider) throws IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean _asTimestamp(SerializerProvider serializers) {
/* 181 */     if (this._useTimestamp != null) {
/* 182 */       return this._useTimestamp.booleanValue();
/*     */     }
/* 184 */     if (this._customFormat == null) {
/* 185 */       if (serializers != null) {
/* 186 */         return serializers.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
/*     */       }
/*     */       
/* 189 */       throw new IllegalArgumentException("Null SerializerProvider passed for " + handledType().getName());
/*     */     } 
/* 191 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void _acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint, boolean asNumber) throws JsonMappingException {
/* 197 */     if (asNumber) {
/* 198 */       visitIntFormat(visitor, typeHint, JsonParser.NumberType.LONG, JsonValueFormat.UTC_MILLISEC);
/*     */     } else {
/*     */       
/* 201 */       visitStringFormat(visitor, typeHint, JsonValueFormat.DATE_TIME);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void _serializeAsString(Date value, JsonGenerator g, SerializerProvider provider) throws IOException {
/* 210 */     if (this._customFormat == null) {
/* 211 */       provider.defaultSerializeDateValue(value, g);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 222 */     DateFormat f = this._reusedCustomFormat.getAndSet(null);
/* 223 */     if (f == null) {
/* 224 */       f = (DateFormat)this._customFormat.clone();
/*     */     }
/* 226 */     g.writeString(f.format(value));
/* 227 */     this._reusedCustomFormat.compareAndSet(null, f);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\std\DateTimeSerializerBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */