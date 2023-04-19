/*     */ package com.fasterxml.jackson.databind;
/*     */ 
/*     */ import com.fasterxml.jackson.core.FormatSchema;
/*     */ import com.fasterxml.jackson.core.JsonLocation;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonStreamContext;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ public class MappingIterator<T> implements Iterator<T>, Closeable {
/*  16 */   protected static final MappingIterator<?> EMPTY_ITERATOR = new MappingIterator(null, null, null, null, false, null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final int STATE_CLOSED = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final int STATE_NEED_RESYNC = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final int STATE_MAY_HAVE_VALUE = 2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final int STATE_HAS_VALUE = 3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JavaType _type;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final DeserializationContext _context;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JsonDeserializer<T> _deserializer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JsonParser _parser;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JsonStreamContext _seqContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final T _updatedValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final boolean _closeParser;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int _state;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MappingIterator(JavaType type, JsonParser p, DeserializationContext ctxt, JsonDeserializer<?> deser, boolean managedParser, Object valueToUpdate) {
/* 122 */     this._type = type;
/* 123 */     this._parser = p;
/* 124 */     this._context = ctxt;
/* 125 */     this._deserializer = (JsonDeserializer)deser;
/* 126 */     this._closeParser = managedParser;
/* 127 */     if (valueToUpdate == null) {
/* 128 */       this._updatedValue = null;
/*     */     } else {
/* 130 */       this._updatedValue = (T)valueToUpdate;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 143 */     if (p == null) {
/* 144 */       this._seqContext = null;
/* 145 */       this._state = 0;
/*     */     } else {
/* 147 */       JsonStreamContext sctxt = p.getParsingContext();
/* 148 */       if (managedParser && p.isExpectedStartArrayToken()) {
/*     */         
/* 150 */         p.clearCurrentToken();
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 155 */         JsonToken t = p.getCurrentToken();
/* 156 */         if (t == JsonToken.START_OBJECT || t == JsonToken.START_ARRAY) {
/* 157 */           sctxt = sctxt.getParent();
/*     */         }
/*     */       } 
/* 160 */       this._seqContext = sctxt;
/* 161 */       this._state = 2;
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
/*     */   public static <T> MappingIterator<T> emptyIterator() {
/* 173 */     return (MappingIterator)EMPTY_ITERATOR;
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
/*     */   public boolean hasNext() {
/*     */     try {
/* 186 */       return hasNextValue();
/* 187 */     } catch (JsonMappingException e) {
/* 188 */       return ((Boolean)_handleMappingException(e)).booleanValue();
/* 189 */     } catch (IOException e) {
/* 190 */       return ((Boolean)_handleIOException(e)).booleanValue();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T next() {
/*     */     try {
/* 199 */       return nextValue();
/* 200 */     } catch (JsonMappingException e) {
/* 201 */       return _handleMappingException(e);
/* 202 */     } catch (IOException e) {
/* 203 */       return _handleIOException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove() {
/* 209 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 214 */     if (this._state != 0) {
/* 215 */       this._state = 0;
/* 216 */       if (this._parser != null) {
/* 217 */         this._parser.close();
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
/*     */   public boolean hasNextValue() throws IOException {
/*     */     JsonToken t;
/* 234 */     switch (this._state) {
/*     */       case 0:
/* 236 */         return false;
/*     */       case 1:
/* 238 */         _resync();
/*     */       
/*     */       case 2:
/* 241 */         t = this._parser.getCurrentToken();
/* 242 */         if (t == null) {
/* 243 */           t = this._parser.nextToken();
/*     */           
/* 245 */           if (t == null || t == JsonToken.END_ARRAY) {
/* 246 */             this._state = 0;
/* 247 */             if (this._closeParser && this._parser != null) {
/* 248 */               this._parser.close();
/*     */             }
/* 250 */             return false;
/*     */           } 
/*     */         } 
/* 253 */         this._state = 3;
/* 254 */         return true;
/*     */     } 
/*     */ 
/*     */     
/* 258 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public T nextValue() throws IOException {
/* 263 */     switch (this._state) {
/*     */       case 0:
/* 265 */         return _throwNoSuchElement();
/*     */       case 1:
/*     */       case 2:
/* 268 */         if (!hasNextValue()) {
/* 269 */           return _throwNoSuchElement();
/*     */         }
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 276 */     int nextState = 1;
/*     */     try {
/*     */       T value;
/* 279 */       if (this._updatedValue == null) {
/* 280 */         value = this._deserializer.deserialize(this._parser, this._context);
/*     */       } else {
/* 282 */         this._deserializer.deserialize(this._parser, this._context, this._updatedValue);
/* 283 */         value = this._updatedValue;
/*     */       } 
/* 285 */       nextState = 2;
/* 286 */       return value;
/*     */     } finally {
/* 288 */       this._state = nextState;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 293 */       this._parser.clearCurrentToken();
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
/*     */   public List<T> readAll() throws IOException {
/* 306 */     return readAll(new ArrayList<>());
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
/*     */   public <L extends List<? super T>> L readAll(L resultList) throws IOException {
/* 319 */     while (hasNextValue()) {
/* 320 */       resultList.add(nextValue());
/*     */     }
/* 322 */     return resultList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <C extends java.util.Collection<? super T>> C readAll(C results) throws IOException {
/* 333 */     while (hasNextValue()) {
/* 334 */       results.add(nextValue());
/*     */     }
/* 336 */     return results;
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
/*     */   public JsonParser getParser() {
/* 351 */     return this._parser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FormatSchema getParserSchema() {
/* 362 */     return this._parser.getSchema();
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
/*     */   public JsonLocation getCurrentLocation() {
/* 376 */     return this._parser.getCurrentLocation();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void _resync() throws IOException {
/* 387 */     JsonParser p = this._parser;
/*     */     
/* 389 */     if (p.getParsingContext() == this._seqContext) {
/*     */       return;
/*     */     }
/*     */     
/*     */     while (true) {
/* 394 */       JsonToken t = p.nextToken();
/* 395 */       if (t == JsonToken.END_ARRAY || t == JsonToken.END_OBJECT) {
/* 396 */         if (p.getParsingContext() == this._seqContext) {
/* 397 */           p.clearCurrentToken(); return;
/*     */         }  continue;
/*     */       } 
/* 400 */       if (t == JsonToken.START_ARRAY || t == JsonToken.START_OBJECT) {
/* 401 */         p.skipChildren(); continue;
/* 402 */       }  if (t == null) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected <R> R _throwNoSuchElement() {
/* 409 */     throw new NoSuchElementException();
/*     */   }
/*     */   
/*     */   protected <R> R _handleMappingException(JsonMappingException e) {
/* 413 */     throw new RuntimeJsonMappingException(e.getMessage(), e);
/*     */   }
/*     */   
/*     */   protected <R> R _handleIOException(IOException e) {
/* 417 */     throw new RuntimeException(e.getMessage(), e);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\MappingIterator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */