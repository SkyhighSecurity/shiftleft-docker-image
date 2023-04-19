/*     */ package com.fasterxml.jackson.databind;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonIgnore;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonLocation;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JsonMappingException
/*     */   extends JsonProcessingException
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   static final int MAX_REFS_TO_LIST = 1000;
/*     */   protected LinkedList<Reference> _path;
/*     */   protected transient Closeable _processor;
/*     */   
/*     */   public static class Reference
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 2L;
/*     */     protected transient Object _from;
/*     */     protected String _fieldName;
/*  67 */     protected int _index = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected String _desc;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected Reference() {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Reference(Object from) {
/*  84 */       this._from = from;
/*     */     }
/*     */     public Reference(Object from, String fieldName) {
/*  87 */       this._from = from;
/*  88 */       if (fieldName == null) {
/*  89 */         throw new NullPointerException("Cannot pass null fieldName");
/*     */       }
/*  91 */       this._fieldName = fieldName;
/*     */     }
/*     */     
/*     */     public Reference(Object from, int index) {
/*  95 */       this._from = from;
/*  96 */       this._index = index;
/*     */     }
/*     */     
/*     */     void setFieldName(String n) {
/* 100 */       this._fieldName = n;
/* 101 */     } void setIndex(int ix) { this._index = ix; } void setDescription(String d) {
/* 102 */       this._desc = d;
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
/*     */     @JsonIgnore
/*     */     public Object getFrom() {
/* 115 */       return this._from;
/*     */     }
/* 117 */     public String getFieldName() { return this._fieldName; } public int getIndex() {
/* 118 */       return this._index;
/*     */     } public String getDescription() {
/* 120 */       if (this._desc == null) {
/* 121 */         StringBuilder sb = new StringBuilder();
/*     */         
/* 123 */         if (this._from == null) {
/* 124 */           sb.append("UNKNOWN");
/*     */         } else {
/* 126 */           Class<?> cls = (this._from instanceof Class) ? (Class)this._from : this._from.getClass();
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 131 */           int arrays = 0;
/* 132 */           while (cls.isArray()) {
/* 133 */             cls = cls.getComponentType();
/* 134 */             arrays++;
/*     */           } 
/* 136 */           sb.append(cls.getName());
/* 137 */           while (--arrays >= 0) {
/* 138 */             sb.append("[]");
/*     */           }
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 148 */         sb.append('[');
/* 149 */         if (this._fieldName != null) {
/* 150 */           sb.append('"');
/* 151 */           sb.append(this._fieldName);
/* 152 */           sb.append('"');
/* 153 */         } else if (this._index >= 0) {
/* 154 */           sb.append(this._index);
/*     */         } else {
/* 156 */           sb.append('?');
/*     */         } 
/* 158 */         sb.append(']');
/* 159 */         this._desc = sb.toString();
/*     */       } 
/* 161 */       return this._desc;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 166 */       return getDescription();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Object writeReplace() {
/* 177 */       getDescription();
/* 178 */       return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public JsonMappingException(String msg) {
/* 214 */     super(msg);
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public JsonMappingException(String msg, Throwable rootCause) {
/* 220 */     super(msg, rootCause);
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public JsonMappingException(String msg, JsonLocation loc) {
/* 226 */     super(msg, loc);
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public JsonMappingException(String msg, JsonLocation loc, Throwable rootCause) {
/* 232 */     super(msg, loc, rootCause);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonMappingException(Closeable processor, String msg) {
/* 238 */     super(msg);
/* 239 */     this._processor = processor;
/* 240 */     if (processor instanceof JsonParser)
/*     */     {
/*     */ 
/*     */       
/* 244 */       this._location = ((JsonParser)processor).getTokenLocation();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonMappingException(Closeable processor, String msg, Throwable problem) {
/* 252 */     super(msg, problem);
/* 253 */     this._processor = processor;
/* 254 */     if (processor instanceof JsonParser) {
/* 255 */       this._location = ((JsonParser)processor).getTokenLocation();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonMappingException(Closeable processor, String msg, JsonLocation loc) {
/* 263 */     super(msg, loc);
/* 264 */     this._processor = processor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JsonMappingException from(JsonParser p, String msg) {
/* 271 */     return new JsonMappingException((Closeable)p, msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JsonMappingException from(JsonParser p, String msg, Throwable problem) {
/* 278 */     return new JsonMappingException((Closeable)p, msg, problem);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JsonMappingException from(JsonGenerator g, String msg) {
/* 285 */     return new JsonMappingException((Closeable)g, msg, (Throwable)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JsonMappingException from(JsonGenerator g, String msg, Throwable problem) {
/* 292 */     return new JsonMappingException((Closeable)g, msg, problem);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JsonMappingException from(DeserializationContext ctxt, String msg) {
/* 299 */     return new JsonMappingException((Closeable)ctxt.getParser(), msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JsonMappingException from(DeserializationContext ctxt, String msg, Throwable t) {
/* 306 */     return new JsonMappingException((Closeable)ctxt.getParser(), msg, t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JsonMappingException from(SerializerProvider ctxt, String msg) {
/* 313 */     return new JsonMappingException((Closeable)ctxt.getGenerator(), msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JsonMappingException from(SerializerProvider ctxt, String msg, Throwable problem) {
/* 323 */     return new JsonMappingException((Closeable)ctxt.getGenerator(), msg, problem);
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
/*     */   public static JsonMappingException fromUnexpectedIOE(IOException src) {
/* 337 */     return new JsonMappingException(null, 
/* 338 */         String.format("Unexpected IOException (of type %s): %s", new Object[] {
/* 339 */             src.getClass().getName(), 
/* 340 */             ClassUtil.exceptionMessage(src)
/*     */           }));
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
/*     */   public static JsonMappingException wrapWithPath(Throwable src, Object refFrom, String refFieldName) {
/* 353 */     return wrapWithPath(src, new Reference(refFrom, refFieldName));
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
/*     */   public static JsonMappingException wrapWithPath(Throwable src, Object refFrom, int index) {
/* 365 */     return wrapWithPath(src, new Reference(refFrom, index));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JsonMappingException wrapWithPath(Throwable src, Reference ref) {
/*     */     JsonMappingException jme;
/* 377 */     if (src instanceof JsonMappingException) {
/* 378 */       jme = (JsonMappingException)src;
/*     */     } else {
/*     */       
/* 381 */       String msg = ClassUtil.exceptionMessage(src);
/*     */       
/* 383 */       if (msg == null || msg.length() == 0) {
/* 384 */         msg = "(was " + src.getClass().getName() + ")";
/*     */       }
/*     */       
/* 387 */       Closeable proc = null;
/* 388 */       if (src instanceof JsonProcessingException) {
/* 389 */         Object proc0 = ((JsonProcessingException)src).getProcessor();
/* 390 */         if (proc0 instanceof Closeable) {
/* 391 */           proc = (Closeable)proc0;
/*     */         }
/*     */       } 
/* 394 */       jme = new JsonMappingException(proc, msg, src);
/*     */     } 
/* 396 */     jme.prependPath(ref);
/* 397 */     return jme;
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
/*     */   public List<Reference> getPath() {
/* 412 */     if (this._path == null) {
/* 413 */       return Collections.emptyList();
/*     */     }
/* 415 */     return Collections.unmodifiableList(this._path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPathReference() {
/* 424 */     return getPathReference(new StringBuilder()).toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public StringBuilder getPathReference(StringBuilder sb) {
/* 429 */     _appendPathDesc(sb);
/* 430 */     return sb;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prependPath(Object referrer, String fieldName) {
/* 439 */     Reference ref = new Reference(referrer, fieldName);
/* 440 */     prependPath(ref);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prependPath(Object referrer, int index) {
/* 448 */     Reference ref = new Reference(referrer, index);
/* 449 */     prependPath(ref);
/*     */   }
/*     */ 
/*     */   
/*     */   public void prependPath(Reference r) {
/* 454 */     if (this._path == null) {
/* 455 */       this._path = new LinkedList<>();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 461 */     if (this._path.size() < 1000) {
/* 462 */       this._path.addFirst(r);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @JsonIgnore
/*     */   public Object getProcessor() {
/* 474 */     return this._processor;
/*     */   }
/*     */   
/*     */   public String getLocalizedMessage() {
/* 478 */     return _buildMessage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage() {
/* 487 */     return _buildMessage();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected String _buildMessage() {
/* 493 */     String msg = super.getMessage();
/* 494 */     if (this._path == null) {
/* 495 */       return msg;
/*     */     }
/* 497 */     StringBuilder sb = (msg == null) ? new StringBuilder() : new StringBuilder(msg);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 503 */     sb.append(" (through reference chain: ");
/* 504 */     sb = getPathReference(sb);
/* 505 */     sb.append(')');
/* 506 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 512 */     return getClass().getName() + ": " + getMessage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void _appendPathDesc(StringBuilder sb) {
/* 523 */     if (this._path == null) {
/*     */       return;
/*     */     }
/* 526 */     Iterator<Reference> it = this._path.iterator();
/* 527 */     while (it.hasNext()) {
/* 528 */       sb.append(((Reference)it.next()).toString());
/* 529 */       if (it.hasNext())
/* 530 */         sb.append("->"); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\JsonMappingException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */