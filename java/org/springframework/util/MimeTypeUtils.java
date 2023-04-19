/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import java.security.SecureRandom;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
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
/*     */ public abstract class MimeTypeUtils
/*     */ {
/*  44 */   private static final byte[] BOUNDARY_CHARS = new byte[] { 45, 95, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  50 */   private static final Random RND = new SecureRandom();
/*     */   
/*  52 */   private static Charset US_ASCII = Charset.forName("US-ASCII");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   public static final Comparator<MimeType> SPECIFICITY_COMPARATOR = new MimeType.SpecificityComparator<MimeType>();
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
/*     */ 
/*     */ 
/*     */   
/* 217 */   public static final MimeType ALL = MimeType.valueOf("*/*"); public static final String ALL_VALUE = "*/*"; @Deprecated
/* 218 */   public static final MimeType APPLICATION_ATOM_XML = MimeType.valueOf("application/atom+xml"); @Deprecated
/* 219 */   public static final MimeType APPLICATION_FORM_URLENCODED = MimeType.valueOf("application/x-www-form-urlencoded"); @Deprecated public static final String APPLICATION_ATOM_XML_VALUE = "application/atom+xml"; @Deprecated
/* 220 */   public static final String APPLICATION_FORM_URLENCODED_VALUE = "application/x-www-form-urlencoded"; public static final MimeType APPLICATION_JSON = MimeType.valueOf("application/json"); public static final String APPLICATION_JSON_VALUE = "application/json";
/* 221 */   public static final MimeType APPLICATION_OCTET_STREAM = MimeType.valueOf("application/octet-stream"); public static final String APPLICATION_OCTET_STREAM_VALUE = "application/octet-stream"; @Deprecated
/* 222 */   public static final MimeType APPLICATION_XHTML_XML = MimeType.valueOf("application/xhtml+xml"); @Deprecated
/* 223 */   public static final String APPLICATION_XHTML_XML_VALUE = "application/xhtml+xml"; public static final MimeType APPLICATION_XML = MimeType.valueOf("application/xml"); public static final String APPLICATION_XML_VALUE = "application/xml";
/* 224 */   public static final MimeType IMAGE_GIF = MimeType.valueOf("image/gif"); public static final String IMAGE_GIF_VALUE = "image/gif";
/* 225 */   public static final MimeType IMAGE_JPEG = MimeType.valueOf("image/jpeg"); public static final String IMAGE_JPEG_VALUE = "image/jpeg";
/* 226 */   public static final MimeType IMAGE_PNG = MimeType.valueOf("image/png"); public static final String IMAGE_PNG_VALUE = "image/png"; @Deprecated
/* 227 */   public static final MimeType MULTIPART_FORM_DATA = MimeType.valueOf("multipart/form-data"); @Deprecated
/* 228 */   public static final String MULTIPART_FORM_DATA_VALUE = "multipart/form-data"; public static final MimeType TEXT_HTML = MimeType.valueOf("text/html"); public static final String TEXT_HTML_VALUE = "text/html";
/* 229 */   public static final MimeType TEXT_PLAIN = MimeType.valueOf("text/plain"); public static final String TEXT_PLAIN_VALUE = "text/plain";
/* 230 */   public static final MimeType TEXT_XML = MimeType.valueOf("text/xml");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String TEXT_XML_VALUE = "text/xml";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MimeType parseMimeType(String mimeType) {
/* 241 */     if (!StringUtils.hasLength(mimeType)) {
/* 242 */       throw new InvalidMimeTypeException(mimeType, "'mimeType' must not be empty");
/*     */     }
/*     */     
/* 245 */     int index = mimeType.indexOf(';');
/* 246 */     String fullType = ((index >= 0) ? mimeType.substring(0, index) : mimeType).trim();
/* 247 */     if (fullType.isEmpty()) {
/* 248 */       throw new InvalidMimeTypeException(mimeType, "'mimeType' must not be empty");
/*     */     }
/*     */ 
/*     */     
/* 252 */     if ("*".equals(fullType)) {
/* 253 */       fullType = "*/*";
/*     */     }
/* 255 */     int subIndex = fullType.indexOf('/');
/* 256 */     if (subIndex == -1) {
/* 257 */       throw new InvalidMimeTypeException(mimeType, "does not contain '/'");
/*     */     }
/* 259 */     if (subIndex == fullType.length() - 1) {
/* 260 */       throw new InvalidMimeTypeException(mimeType, "does not contain subtype after '/'");
/*     */     }
/* 262 */     String type = fullType.substring(0, subIndex);
/* 263 */     String subtype = fullType.substring(subIndex + 1, fullType.length());
/* 264 */     if ("*".equals(type) && !"*".equals(subtype)) {
/* 265 */       throw new InvalidMimeTypeException(mimeType, "wildcard type is legal only in '*/*' (all mime types)");
/*     */     }
/*     */     
/* 268 */     Map<String, String> parameters = null;
/*     */     do {
/* 270 */       int nextIndex = index + 1;
/* 271 */       boolean quoted = false;
/* 272 */       while (nextIndex < mimeType.length()) {
/* 273 */         char ch = mimeType.charAt(nextIndex);
/* 274 */         if (ch == ';') {
/* 275 */           if (!quoted) {
/*     */             break;
/*     */           }
/*     */         }
/* 279 */         else if (ch == '"') {
/* 280 */           quoted = !quoted;
/*     */         } 
/* 282 */         nextIndex++;
/*     */       } 
/* 284 */       String parameter = mimeType.substring(index + 1, nextIndex).trim();
/* 285 */       if (parameter.length() > 0) {
/* 286 */         if (parameters == null) {
/* 287 */           parameters = new LinkedHashMap<String, String>(4);
/*     */         }
/* 289 */         int eqIndex = parameter.indexOf('=');
/* 290 */         if (eqIndex >= 0) {
/* 291 */           String attribute = parameter.substring(0, eqIndex).trim();
/* 292 */           String value = parameter.substring(eqIndex + 1, parameter.length()).trim();
/* 293 */           parameters.put(attribute, value);
/*     */         } 
/*     */       } 
/* 296 */       index = nextIndex;
/*     */     }
/* 298 */     while (index < mimeType.length());
/*     */     
/*     */     try {
/* 301 */       return new MimeType(type, subtype, parameters);
/*     */     }
/* 303 */     catch (UnsupportedCharsetException ex) {
/* 304 */       throw new InvalidMimeTypeException(mimeType, "unsupported charset '" + ex.getCharsetName() + "'");
/*     */     }
/* 306 */     catch (IllegalArgumentException ex) {
/* 307 */       throw new InvalidMimeTypeException(mimeType, ex.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<MimeType> parseMimeTypes(String mimeTypes) {
/* 318 */     if (!StringUtils.hasLength(mimeTypes)) {
/* 319 */       return Collections.emptyList();
/*     */     }
/* 321 */     String[] tokens = StringUtils.tokenizeToStringArray(mimeTypes, ",");
/* 322 */     List<MimeType> result = new ArrayList<MimeType>(tokens.length);
/* 323 */     for (String token : tokens) {
/* 324 */       result.add(parseMimeType(token));
/*     */     }
/* 326 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toString(Collection<? extends MimeType> mimeTypes) {
/* 336 */     StringBuilder builder = new StringBuilder();
/* 337 */     for (Iterator<? extends MimeType> iterator = mimeTypes.iterator(); iterator.hasNext(); ) {
/* 338 */       MimeType mimeType = iterator.next();
/* 339 */       mimeType.appendTo(builder);
/* 340 */       if (iterator.hasNext()) {
/* 341 */         builder.append(", ");
/*     */       }
/*     */     } 
/* 344 */     return builder.toString();
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
/*     */   public static void sortBySpecificity(List<MimeType> mimeTypes) {
/* 373 */     Assert.notNull(mimeTypes, "'mimeTypes' must not be null");
/* 374 */     if (mimeTypes.size() > 1) {
/* 375 */       Collections.sort(mimeTypes, SPECIFICITY_COMPARATOR);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] generateMultipartBoundary() {
/* 383 */     byte[] boundary = new byte[RND.nextInt(11) + 30];
/* 384 */     for (int i = 0; i < boundary.length; i++) {
/* 385 */       boundary[i] = BOUNDARY_CHARS[RND.nextInt(BOUNDARY_CHARS.length)];
/*     */     }
/* 387 */     return boundary;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String generateMultipartBoundaryString() {
/* 394 */     return new String(generateMultipartBoundary(), US_ASCII);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\MimeTypeUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */