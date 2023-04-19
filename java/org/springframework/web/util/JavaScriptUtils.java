/*     */ package org.springframework.web.util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JavaScriptUtils
/*     */ {
/*     */   public static String javaScriptEscape(String input) {
/*  40 */     if (input == null) {
/*  41 */       return input;
/*     */     }
/*     */     
/*  44 */     StringBuilder filtered = new StringBuilder(input.length());
/*  45 */     char prevChar = Character.MIN_VALUE;
/*     */     
/*  47 */     for (int i = 0; i < input.length(); i++) {
/*  48 */       char c = input.charAt(i);
/*  49 */       if (c == '"') {
/*  50 */         filtered.append("\\\"");
/*     */       }
/*  52 */       else if (c == '\'') {
/*  53 */         filtered.append("\\'");
/*     */       }
/*  55 */       else if (c == '\\') {
/*  56 */         filtered.append("\\\\");
/*     */       }
/*  58 */       else if (c == '/') {
/*  59 */         filtered.append("\\/");
/*     */       }
/*  61 */       else if (c == '\t') {
/*  62 */         filtered.append("\\t");
/*     */       }
/*  64 */       else if (c == '\n') {
/*  65 */         if (prevChar != '\r') {
/*  66 */           filtered.append("\\n");
/*     */         }
/*     */       }
/*  69 */       else if (c == '\r') {
/*  70 */         filtered.append("\\n");
/*     */       }
/*  72 */       else if (c == '\f') {
/*  73 */         filtered.append("\\f");
/*     */       }
/*  75 */       else if (c == '\b') {
/*  76 */         filtered.append("\\b");
/*     */       
/*     */       }
/*  79 */       else if (c == '\013') {
/*  80 */         filtered.append("\\v");
/*     */       }
/*  82 */       else if (c == '<') {
/*  83 */         filtered.append("\\u003C");
/*     */       }
/*  85 */       else if (c == '>') {
/*  86 */         filtered.append("\\u003E");
/*     */       
/*     */       }
/*  89 */       else if (c == ' ') {
/*  90 */         filtered.append("\\u2028");
/*     */       
/*     */       }
/*  93 */       else if (c == ' ') {
/*  94 */         filtered.append("\\u2029");
/*     */       } else {
/*     */         
/*  97 */         filtered.append(c);
/*     */       } 
/*  99 */       prevChar = c;
/*     */     } 
/*     */     
/* 102 */     return filtered.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\we\\util\JavaScriptUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */