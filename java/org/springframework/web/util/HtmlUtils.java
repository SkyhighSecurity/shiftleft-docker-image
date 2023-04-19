/*     */ package org.springframework.web.util;
/*     */ 
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
/*     */ public abstract class HtmlUtils
/*     */ {
/*  45 */   private static final HtmlCharacterEntityReferences characterEntityReferences = new HtmlCharacterEntityReferences();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String htmlEscape(String input) {
/*  62 */     return htmlEscape(input, "ISO-8859-1");
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
/*     */   public static String htmlEscape(String input, String encoding) {
/*  82 */     Assert.notNull(encoding, "Encoding is required");
/*  83 */     if (input == null) {
/*  84 */       return null;
/*     */     }
/*  86 */     StringBuilder escaped = new StringBuilder(input.length() * 2);
/*  87 */     for (int i = 0; i < input.length(); i++) {
/*  88 */       char character = input.charAt(i);
/*  89 */       String reference = characterEntityReferences.convertToReference(character, encoding);
/*  90 */       if (reference != null) {
/*  91 */         escaped.append(reference);
/*     */       } else {
/*     */         
/*  94 */         escaped.append(character);
/*     */       } 
/*     */     } 
/*  97 */     return escaped.toString();
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
/*     */   public static String htmlEscapeDecimal(String input) {
/* 113 */     return htmlEscapeDecimal(input, "ISO-8859-1");
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
/*     */   public static String htmlEscapeDecimal(String input, String encoding) {
/* 133 */     Assert.notNull(encoding, "Encoding is required");
/* 134 */     if (input == null) {
/* 135 */       return null;
/*     */     }
/* 137 */     StringBuilder escaped = new StringBuilder(input.length() * 2);
/* 138 */     for (int i = 0; i < input.length(); i++) {
/* 139 */       char character = input.charAt(i);
/* 140 */       if (characterEntityReferences.isMappedToReference(character, encoding)) {
/* 141 */         escaped.append("&#");
/* 142 */         escaped.append(character);
/* 143 */         escaped.append(';');
/*     */       } else {
/*     */         
/* 146 */         escaped.append(character);
/*     */       } 
/*     */     } 
/* 149 */     return escaped.toString();
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
/*     */   public static String htmlEscapeHex(String input) {
/* 165 */     return htmlEscapeHex(input, "ISO-8859-1");
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
/*     */   public static String htmlEscapeHex(String input, String encoding) {
/* 185 */     Assert.notNull(encoding, "Encoding is required");
/* 186 */     if (input == null) {
/* 187 */       return null;
/*     */     }
/* 189 */     StringBuilder escaped = new StringBuilder(input.length() * 2);
/* 190 */     for (int i = 0; i < input.length(); i++) {
/* 191 */       char character = input.charAt(i);
/* 192 */       if (characterEntityReferences.isMappedToReference(character, encoding)) {
/* 193 */         escaped.append("&#x");
/* 194 */         escaped.append(Integer.toString(character, 16));
/* 195 */         escaped.append(';');
/*     */       } else {
/*     */         
/* 198 */         escaped.append(character);
/*     */       } 
/*     */     } 
/* 201 */     return escaped.toString();
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
/*     */   public static String htmlUnescape(String input) {
/* 224 */     if (input == null) {
/* 225 */       return null;
/*     */     }
/* 227 */     return (new HtmlCharacterEntityDecoder(characterEntityReferences, input)).decode();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\we\\util\HtmlUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */