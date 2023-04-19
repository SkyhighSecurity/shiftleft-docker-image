/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Base64;
/*     */ import javax.xml.bind.DatatypeConverter;
/*     */ import org.apache.commons.codec.binary.Base64;
/*     */ import org.springframework.lang.UsesJava8;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Base64Utils
/*     */ {
/*  49 */   private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
/*     */   
/*     */   private static final Base64Delegate delegate;
/*     */ 
/*     */   
/*     */   static {
/*  55 */     Base64Delegate delegateToUse = null;
/*     */     
/*  57 */     if (ClassUtils.isPresent("java.util.Base64", Base64Utils.class.getClassLoader())) {
/*  58 */       delegateToUse = new JdkBase64Delegate();
/*     */     
/*     */     }
/*  61 */     else if (ClassUtils.isPresent("org.apache.commons.codec.binary.Base64", Base64Utils.class.getClassLoader())) {
/*  62 */       delegateToUse = new CommonsCodecBase64Delegate();
/*     */     } 
/*  64 */     delegate = delegateToUse;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void assertDelegateAvailable() {
/*  72 */     Assert.state((delegate != null), "Neither Java 8 nor Apache Commons Codec found - Base64 encoding between byte arrays not supported");
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
/*     */   public static byte[] encode(byte[] src) {
/*  85 */     assertDelegateAvailable();
/*  86 */     return delegate.encode(src);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] decode(byte[] src) {
/*  97 */     assertDelegateAvailable();
/*  98 */     return delegate.decode(src);
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
/*     */   public static byte[] encodeUrlSafe(byte[] src) {
/* 111 */     assertDelegateAvailable();
/* 112 */     return delegate.encodeUrlSafe(src);
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
/*     */   public static byte[] decodeUrlSafe(byte[] src) {
/* 125 */     assertDelegateAvailable();
/* 126 */     return delegate.decodeUrlSafe(src);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodeToString(byte[] src) {
/* 136 */     if (src == null) {
/* 137 */       return null;
/*     */     }
/* 139 */     if (src.length == 0) {
/* 140 */       return "";
/*     */     }
/*     */     
/* 143 */     if (delegate != null)
/*     */     {
/* 145 */       return new String(delegate.encode(src), DEFAULT_CHARSET);
/*     */     }
/*     */ 
/*     */     
/* 149 */     return DatatypeConverter.printBase64Binary(src);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] decodeFromString(String src) {
/* 159 */     if (src == null) {
/* 160 */       return null;
/*     */     }
/* 162 */     if (src.isEmpty()) {
/* 163 */       return new byte[0];
/*     */     }
/*     */     
/* 166 */     if (delegate != null)
/*     */     {
/* 168 */       return delegate.decode(src.getBytes(DEFAULT_CHARSET));
/*     */     }
/*     */ 
/*     */     
/* 172 */     return DatatypeConverter.parseBase64Binary(src);
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
/*     */   public static String encodeToUrlSafeString(byte[] src) {
/* 186 */     assertDelegateAvailable();
/* 187 */     return new String(delegate.encodeUrlSafe(src), DEFAULT_CHARSET);
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
/*     */   public static byte[] decodeFromUrlSafeString(String src) {
/* 199 */     assertDelegateAvailable();
/* 200 */     return delegate.decodeUrlSafe(src.getBytes(DEFAULT_CHARSET));
/*     */   }
/*     */ 
/*     */   
/*     */   static interface Base64Delegate
/*     */   {
/*     */     byte[] encode(byte[] param1ArrayOfbyte);
/*     */ 
/*     */     
/*     */     byte[] decode(byte[] param1ArrayOfbyte);
/*     */     
/*     */     byte[] encodeUrlSafe(byte[] param1ArrayOfbyte);
/*     */     
/*     */     byte[] decodeUrlSafe(byte[] param1ArrayOfbyte);
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   static class JdkBase64Delegate
/*     */     implements Base64Delegate
/*     */   {
/*     */     public byte[] encode(byte[] src) {
/* 221 */       if (src == null || src.length == 0) {
/* 222 */         return src;
/*     */       }
/* 224 */       return Base64.getEncoder().encode(src);
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] decode(byte[] src) {
/* 229 */       if (src == null || src.length == 0) {
/* 230 */         return src;
/*     */       }
/* 232 */       return Base64.getDecoder().decode(src);
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] encodeUrlSafe(byte[] src) {
/* 237 */       if (src == null || src.length == 0) {
/* 238 */         return src;
/*     */       }
/* 240 */       return Base64.getUrlEncoder().encode(src);
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] decodeUrlSafe(byte[] src) {
/* 245 */       if (src == null || src.length == 0) {
/* 246 */         return src;
/*     */       }
/* 248 */       return Base64.getUrlDecoder().decode(src);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class CommonsCodecBase64Delegate
/*     */     implements Base64Delegate
/*     */   {
/* 256 */     private final Base64 base64 = new Base64();
/*     */ 
/*     */     
/* 259 */     private final Base64 base64UrlSafe = new Base64(0, null, true);
/*     */ 
/*     */ 
/*     */     
/*     */     public byte[] encode(byte[] src) {
/* 264 */       return this.base64.encode(src);
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] decode(byte[] src) {
/* 269 */       return this.base64.decode(src);
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] encodeUrlSafe(byte[] src) {
/* 274 */       return this.base64UrlSafe.encode(src);
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] decodeUrlSafe(byte[] src) {
/* 279 */       return this.base64UrlSafe.decode(src);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\Base64Utils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */