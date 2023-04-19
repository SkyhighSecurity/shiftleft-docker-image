/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat;
/*     */ import java.io.IOException;
/*     */ import java.util.UUID;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UUIDSerializer
/*     */   extends StdScalarSerializer<UUID>
/*     */ {
/*  23 */   static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();
/*     */   public UUIDSerializer() {
/*  25 */     super(UUID.class);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty(SerializerProvider prov, UUID value) {
/*  31 */     if (value.getLeastSignificantBits() == 0L && value
/*  32 */       .getMostSignificantBits() == 0L) {
/*  33 */       return true;
/*     */     }
/*  35 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void serialize(UUID value, JsonGenerator gen, SerializerProvider provider) throws IOException {
/*  43 */     if (gen.canWriteBinaryNatively())
/*     */     {
/*     */ 
/*     */ 
/*     */       
/*  48 */       if (!(gen instanceof com.fasterxml.jackson.databind.util.TokenBuffer)) {
/*  49 */         gen.writeBinary(_asBytes(value));
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*  57 */     char[] ch = new char[36];
/*  58 */     long msb = value.getMostSignificantBits();
/*  59 */     _appendInt((int)(msb >> 32L), ch, 0);
/*  60 */     ch[8] = '-';
/*  61 */     int i = (int)msb;
/*  62 */     _appendShort(i >>> 16, ch, 9);
/*  63 */     ch[13] = '-';
/*  64 */     _appendShort(i, ch, 14);
/*  65 */     ch[18] = '-';
/*     */     
/*  67 */     long lsb = value.getLeastSignificantBits();
/*  68 */     _appendShort((int)(lsb >>> 48L), ch, 19);
/*  69 */     ch[23] = '-';
/*  70 */     _appendShort((int)(lsb >>> 32L), ch, 24);
/*  71 */     _appendInt((int)lsb, ch, 28);
/*     */     
/*  73 */     gen.writeString(ch, 0, 36);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
/*  81 */     visitStringFormat(visitor, typeHint, JsonValueFormat.UUID);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void _appendInt(int bits, char[] ch, int offset) {
/*  86 */     _appendShort(bits >> 16, ch, offset);
/*  87 */     _appendShort(bits, ch, offset + 4);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void _appendShort(int bits, char[] ch, int offset) {
/*  92 */     ch[offset] = HEX_CHARS[bits >> 12 & 0xF];
/*  93 */     ch[++offset] = HEX_CHARS[bits >> 8 & 0xF];
/*  94 */     ch[++offset] = HEX_CHARS[bits >> 4 & 0xF];
/*  95 */     ch[++offset] = HEX_CHARS[bits & 0xF];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final byte[] _asBytes(UUID uuid) {
/* 101 */     byte[] buffer = new byte[16];
/* 102 */     long hi = uuid.getMostSignificantBits();
/* 103 */     long lo = uuid.getLeastSignificantBits();
/* 104 */     _appendInt((int)(hi >> 32L), buffer, 0);
/* 105 */     _appendInt((int)hi, buffer, 4);
/* 106 */     _appendInt((int)(lo >> 32L), buffer, 8);
/* 107 */     _appendInt((int)lo, buffer, 12);
/* 108 */     return buffer;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final void _appendInt(int value, byte[] buffer, int offset) {
/* 113 */     buffer[offset] = (byte)(value >> 24);
/* 114 */     buffer[++offset] = (byte)(value >> 16);
/* 115 */     buffer[++offset] = (byte)(value >> 8);
/* 116 */     buffer[++offset] = (byte)value;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ser\std\UUIDSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */