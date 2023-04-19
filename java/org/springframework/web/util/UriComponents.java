/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URI;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class UriComponents
/*     */   implements Serializable
/*     */ {
/*     */   private static final String DEFAULT_ENCODING = "UTF-8";
/*  50 */   private static final Pattern NAMES_PATTERN = Pattern.compile("\\{([^/]+?)\\}");
/*     */ 
/*     */   
/*     */   private final String scheme;
/*     */   
/*     */   private final String fragment;
/*     */ 
/*     */   
/*     */   protected UriComponents(String scheme, String fragment) {
/*  59 */     this.scheme = scheme;
/*  60 */     this.fragment = fragment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getScheme() {
/*  70 */     return this.scheme;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getFragment() {
/*  77 */     return this.fragment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String getSchemeSpecificPart();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String getUserInfo();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String getHost();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract int getPort();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String getPath();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract List<String> getPathSegments();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String getQuery();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract MultiValueMap<String, String> getQueryParams();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final UriComponents encode() {
/*     */     try {
/* 128 */       return encode("UTF-8");
/*     */     }
/* 130 */     catch (UnsupportedEncodingException ex) {
/*     */       
/* 132 */       throw new IllegalStateException(ex);
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
/*     */   public abstract UriComponents encode(String paramString) throws UnsupportedEncodingException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final UriComponents expand(Map<String, ?> uriVariables) {
/* 153 */     Assert.notNull(uriVariables, "'uriVariables' must not be null");
/* 154 */     return expandInternal(new MapTemplateVariables(uriVariables));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final UriComponents expand(Object... uriVariableValues) {
/* 164 */     Assert.notNull(uriVariableValues, "'uriVariableValues' must not be null");
/* 165 */     return expandInternal(new VarArgsTemplateVariables(uriVariableValues));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final UriComponents expand(UriTemplateVariables uriVariables) {
/* 175 */     Assert.notNull(uriVariables, "'uriVariables' must not be null");
/* 176 */     return expandInternal(uriVariables);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract UriComponents expandInternal(UriTemplateVariables paramUriTemplateVariables);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract UriComponents normalize();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String toUriString();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract URI toUri();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String toString() {
/* 205 */     return toUriString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void copyToUriComponentsBuilder(UriComponentsBuilder paramUriComponentsBuilder);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String expandUriComponent(String source, UriTemplateVariables uriVariables) {
/* 218 */     if (source == null) {
/* 219 */       return null;
/*     */     }
/* 221 */     if (source.indexOf('{') == -1) {
/* 222 */       return source;
/*     */     }
/* 224 */     if (source.indexOf(':') != -1) {
/* 225 */       source = sanitizeSource(source);
/*     */     }
/* 227 */     Matcher matcher = NAMES_PATTERN.matcher(source);
/* 228 */     StringBuffer sb = new StringBuffer();
/* 229 */     while (matcher.find()) {
/* 230 */       String match = matcher.group(1);
/* 231 */       String variableName = getVariableName(match);
/* 232 */       Object variableValue = uriVariables.getValue(variableName);
/* 233 */       if (UriTemplateVariables.SKIP_VALUE.equals(variableValue)) {
/*     */         continue;
/*     */       }
/* 236 */       String variableValueString = getVariableValueAsString(variableValue);
/* 237 */       String replacement = Matcher.quoteReplacement(variableValueString);
/* 238 */       matcher.appendReplacement(sb, replacement);
/*     */     } 
/* 240 */     matcher.appendTail(sb);
/* 241 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String sanitizeSource(String source) {
/* 248 */     int level = 0;
/* 249 */     StringBuilder sb = new StringBuilder();
/* 250 */     for (char c : source.toCharArray()) {
/* 251 */       if (c == '{') {
/* 252 */         level++;
/*     */       }
/* 254 */       if (c == '}') {
/* 255 */         level--;
/*     */       }
/* 257 */       if (level <= 1 && (level != 1 || c != '}'))
/*     */       {
/*     */         
/* 260 */         sb.append(c); } 
/*     */     } 
/* 262 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private static String getVariableName(String match) {
/* 266 */     int colonIdx = match.indexOf(':');
/* 267 */     return (colonIdx != -1) ? match.substring(0, colonIdx) : match;
/*     */   }
/*     */   
/*     */   private static String getVariableValueAsString(Object variableValue) {
/* 271 */     return (variableValue != null) ? variableValue.toString() : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface UriTemplateVariables
/*     */   {
/* 281 */     public static final Object SKIP_VALUE = UriTemplateVariables.class;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Object getValue(String param1String);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class MapTemplateVariables
/*     */     implements UriTemplateVariables
/*     */   {
/*     */     private final Map<String, ?> uriVariables;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MapTemplateVariables(Map<String, ?> uriVariables) {
/* 302 */       this.uriVariables = uriVariables;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object getValue(String name) {
/* 307 */       if (!this.uriVariables.containsKey(name)) {
/* 308 */         throw new IllegalArgumentException("Map has no value for '" + name + "'");
/*     */       }
/* 310 */       return this.uriVariables.get(name);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class VarArgsTemplateVariables
/*     */     implements UriTemplateVariables
/*     */   {
/*     */     private final Iterator<Object> valueIterator;
/*     */ 
/*     */     
/*     */     public VarArgsTemplateVariables(Object... uriVariableValues) {
/* 323 */       this.valueIterator = Arrays.<Object>asList(uriVariableValues).iterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public Object getValue(String name) {
/* 328 */       if (!this.valueIterator.hasNext()) {
/* 329 */         throw new IllegalArgumentException("Not enough variable values available to expand '" + name + "'");
/*     */       }
/* 331 */       return this.valueIterator.next();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\we\\util\UriComponents.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */