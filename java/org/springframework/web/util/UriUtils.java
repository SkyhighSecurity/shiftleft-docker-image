/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class UriUtils
/*     */ {
/*     */   public static String encodeScheme(String scheme, String encoding) throws UnsupportedEncodingException {
/*  51 */     return HierarchicalUriComponents.encodeUriComponent(scheme, encoding, HierarchicalUriComponents.Type.SCHEME);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodeAuthority(String authority, String encoding) throws UnsupportedEncodingException {
/*  62 */     return HierarchicalUriComponents.encodeUriComponent(authority, encoding, HierarchicalUriComponents.Type.AUTHORITY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodeUserInfo(String userInfo, String encoding) throws UnsupportedEncodingException {
/*  73 */     return HierarchicalUriComponents.encodeUriComponent(userInfo, encoding, HierarchicalUriComponents.Type.USER_INFO);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodeHost(String host, String encoding) throws UnsupportedEncodingException {
/*  84 */     return HierarchicalUriComponents.encodeUriComponent(host, encoding, HierarchicalUriComponents.Type.HOST_IPV4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodePort(String port, String encoding) throws UnsupportedEncodingException {
/*  95 */     return HierarchicalUriComponents.encodeUriComponent(port, encoding, HierarchicalUriComponents.Type.PORT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodePath(String path, String encoding) throws UnsupportedEncodingException {
/* 106 */     return HierarchicalUriComponents.encodeUriComponent(path, encoding, HierarchicalUriComponents.Type.PATH);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodePathSegment(String segment, String encoding) throws UnsupportedEncodingException {
/* 117 */     return HierarchicalUriComponents.encodeUriComponent(segment, encoding, HierarchicalUriComponents.Type.PATH_SEGMENT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodeQuery(String query, String encoding) throws UnsupportedEncodingException {
/* 128 */     return HierarchicalUriComponents.encodeUriComponent(query, encoding, HierarchicalUriComponents.Type.QUERY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodeQueryParam(String queryParam, String encoding) throws UnsupportedEncodingException {
/* 139 */     return HierarchicalUriComponents.encodeUriComponent(queryParam, encoding, HierarchicalUriComponents.Type.QUERY_PARAM);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodeFragment(String fragment, String encoding) throws UnsupportedEncodingException {
/* 150 */     return HierarchicalUriComponents.encodeUriComponent(fragment, encoding, HierarchicalUriComponents.Type.FRAGMENT);
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
/*     */   public static String encode(String source, String encoding) throws UnsupportedEncodingException {
/* 164 */     HierarchicalUriComponents.Type type = HierarchicalUriComponents.Type.URI;
/* 165 */     return HierarchicalUriComponents.encodeUriComponent(source, encoding, type);
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
/*     */   public static String decode(String source, String encoding) throws UnsupportedEncodingException {
/* 184 */     if (source == null) {
/* 185 */       return null;
/*     */     }
/* 187 */     Assert.hasLength(encoding, "Encoding must not be empty");
/* 188 */     int length = source.length();
/* 189 */     ByteArrayOutputStream bos = new ByteArrayOutputStream(length);
/* 190 */     boolean changed = false;
/* 191 */     for (int i = 0; i < length; i++) {
/* 192 */       int ch = source.charAt(i);
/* 193 */       if (ch == 37) {
/* 194 */         if (i + 2 < length) {
/* 195 */           char hex1 = source.charAt(i + 1);
/* 196 */           char hex2 = source.charAt(i + 2);
/* 197 */           int u = Character.digit(hex1, 16);
/* 198 */           int l = Character.digit(hex2, 16);
/* 199 */           if (u == -1 || l == -1) {
/* 200 */             throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
/*     */           }
/* 202 */           bos.write((char)((u << 4) + l));
/* 203 */           i += 2;
/* 204 */           changed = true;
/*     */         } else {
/*     */           
/* 207 */           throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
/*     */         } 
/*     */       } else {
/*     */         
/* 211 */         bos.write(ch);
/*     */       } 
/*     */     } 
/* 214 */     return changed ? new String(bos.toByteArray(), encoding) : source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String extractFileExtension(String path) {
/* 224 */     int end = path.indexOf('?');
/* 225 */     int fragmentIndex = path.indexOf('#');
/* 226 */     if (fragmentIndex != -1 && (end == -1 || fragmentIndex < end)) {
/* 227 */       end = fragmentIndex;
/*     */     }
/* 229 */     if (end == -1) {
/* 230 */       end = path.length();
/*     */     }
/* 232 */     int begin = path.lastIndexOf('/', end) + 1;
/* 233 */     int paramIndex = path.indexOf(';', begin);
/* 234 */     end = (paramIndex != -1 && paramIndex < end) ? paramIndex : end;
/* 235 */     int extIndex = path.lastIndexOf('.', end);
/* 236 */     if (extIndex != -1 && extIndex > begin) {
/* 237 */       return path.substring(extIndex + 1, end);
/*     */     }
/* 239 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\we\\util\UriUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */