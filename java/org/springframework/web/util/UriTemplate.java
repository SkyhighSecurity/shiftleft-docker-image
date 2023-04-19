/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public class UriTemplate
/*     */   implements Serializable
/*     */ {
/*     */   private final String uriTemplate;
/*     */   private final UriComponents uriComponents;
/*     */   private final List<String> variableNames;
/*     */   private final Pattern matchPattern;
/*     */   
/*     */   public UriTemplate(String uriTemplate) {
/*  63 */     Assert.hasText(uriTemplate, "'uriTemplate' must not be null");
/*  64 */     this.uriTemplate = uriTemplate;
/*  65 */     this.uriComponents = UriComponentsBuilder.fromUriString(uriTemplate).build();
/*     */     
/*  67 */     TemplateInfo info = TemplateInfo.parse(uriTemplate);
/*  68 */     this.variableNames = Collections.unmodifiableList(info.getVariableNames());
/*  69 */     this.matchPattern = info.getMatchPattern();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getVariableNames() {
/*  78 */     return this.variableNames;
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
/*     */   public URI expand(Map<String, ?> uriVariables) {
/*  99 */     UriComponents expandedComponents = this.uriComponents.expand(uriVariables);
/* 100 */     UriComponents encodedComponents = expandedComponents.encode();
/* 101 */     return encodedComponents.toUri();
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
/*     */   public URI expand(Object... uriVariableValues) {
/* 119 */     UriComponents expandedComponents = this.uriComponents.expand(uriVariableValues);
/* 120 */     UriComponents encodedComponents = expandedComponents.encode();
/* 121 */     return encodedComponents.toUri();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matches(String uri) {
/* 130 */     if (uri == null) {
/* 131 */       return false;
/*     */     }
/* 133 */     Matcher matcher = this.matchPattern.matcher(uri);
/* 134 */     return matcher.matches();
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
/*     */   public Map<String, String> match(String uri) {
/* 150 */     Assert.notNull(uri, "'uri' must not be null");
/* 151 */     Map<String, String> result = new LinkedHashMap<String, String>(this.variableNames.size());
/* 152 */     Matcher matcher = this.matchPattern.matcher(uri);
/* 153 */     if (matcher.find()) {
/* 154 */       for (int i = 1; i <= matcher.groupCount(); i++) {
/* 155 */         String name = this.variableNames.get(i - 1);
/* 156 */         String value = matcher.group(i);
/* 157 */         result.put(name, value);
/*     */       } 
/*     */     }
/* 160 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 165 */     return this.uriTemplate;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class TemplateInfo
/*     */   {
/*     */     private final List<String> variableNames;
/*     */ 
/*     */     
/*     */     private final Pattern pattern;
/*     */ 
/*     */     
/*     */     private TemplateInfo(List<String> vars, Pattern pattern) {
/* 179 */       this.variableNames = vars;
/* 180 */       this.pattern = pattern;
/*     */     }
/*     */     
/*     */     public List<String> getVariableNames() {
/* 184 */       return this.variableNames;
/*     */     }
/*     */     
/*     */     public Pattern getMatchPattern() {
/* 188 */       return this.pattern;
/*     */     }
/*     */     
/*     */     public static TemplateInfo parse(String uriTemplate) {
/* 192 */       int level = 0;
/* 193 */       List<String> variableNames = new ArrayList<String>();
/* 194 */       StringBuilder pattern = new StringBuilder();
/* 195 */       StringBuilder builder = new StringBuilder();
/* 196 */       for (int i = 0; i < uriTemplate.length(); i++) {
/* 197 */         char c = uriTemplate.charAt(i);
/* 198 */         if (c == '{') {
/* 199 */           level++;
/* 200 */           if (level == 1) {
/*     */             
/* 202 */             pattern.append(quote(builder));
/* 203 */             builder = new StringBuilder();
/*     */             
/*     */             continue;
/*     */           } 
/*     */         } else {
/* 208 */           level--;
/* 209 */           if (c == '}' && level == 0) {
/*     */             
/* 211 */             String variable = builder.toString();
/* 212 */             int idx = variable.indexOf(':');
/* 213 */             if (idx == -1) {
/* 214 */               pattern.append("(.*)");
/* 215 */               variableNames.add(variable);
/*     */             } else {
/*     */               
/* 218 */               if (idx + 1 == variable.length()) {
/* 219 */                 throw new IllegalArgumentException("No custom regular expression specified after ':' in \"" + variable + "\"");
/*     */               }
/*     */               
/* 222 */               String regex = variable.substring(idx + 1, variable.length());
/* 223 */               pattern.append('(');
/* 224 */               pattern.append(regex);
/* 225 */               pattern.append(')');
/* 226 */               variableNames.add(variable.substring(0, idx));
/*     */             } 
/* 228 */             builder = new StringBuilder();
/*     */             continue;
/*     */           } 
/*     */         } 
/* 232 */         builder.append(c); continue;
/*     */       } 
/* 234 */       if (builder.length() > 0) {
/* 235 */         pattern.append(quote(builder));
/*     */       }
/* 237 */       return new TemplateInfo(variableNames, Pattern.compile(pattern.toString()));
/*     */     }
/*     */     
/*     */     private static String quote(StringBuilder builder) {
/* 241 */       return (builder.length() > 0) ? Pattern.quote(builder.toString()) : "";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\we\\util\UriTemplate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */