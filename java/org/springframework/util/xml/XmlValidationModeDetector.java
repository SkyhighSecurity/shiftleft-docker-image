/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.CharConversionException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XmlValidationModeDetector
/*     */ {
/*     */   public static final int VALIDATION_NONE = 0;
/*     */   public static final int VALIDATION_AUTO = 1;
/*     */   public static final int VALIDATION_DTD = 2;
/*     */   public static final int VALIDATION_XSD = 3;
/*     */   private static final String DOCTYPE = "DOCTYPE";
/*     */   private static final String START_COMMENT = "<!--";
/*     */   private static final String END_COMMENT = "-->";
/*     */   private boolean inComment;
/*     */   
/*     */   public int detectValidationMode(InputStream inputStream) throws IOException {
/*  91 */     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
/*     */     try {
/*  93 */       boolean isDtdValidated = false;
/*     */       String content;
/*  95 */       while ((content = reader.readLine()) != null) {
/*  96 */         content = consumeCommentTokens(content);
/*  97 */         if (this.inComment || !StringUtils.hasText(content)) {
/*     */           continue;
/*     */         }
/* 100 */         if (hasDoctype(content)) {
/* 101 */           isDtdValidated = true;
/*     */           break;
/*     */         } 
/* 104 */         if (hasOpeningTag(content)) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */       
/* 109 */       return isDtdValidated ? 2 : 3;
/*     */     }
/* 111 */     catch (CharConversionException ex) {
/*     */ 
/*     */       
/* 114 */       return 1;
/*     */     } finally {
/*     */       
/* 117 */       reader.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasDoctype(String content) {
/* 126 */     return content.contains("DOCTYPE");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasOpeningTag(String content) {
/* 135 */     if (this.inComment) {
/* 136 */       return false;
/*     */     }
/* 138 */     int openTagIndex = content.indexOf('<');
/* 139 */     return (openTagIndex > -1 && content.length() > openTagIndex + 1 && 
/* 140 */       Character.isLetter(content.charAt(openTagIndex + 1)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String consumeCommentTokens(String line) {
/* 150 */     if (!line.contains("<!--") && !line.contains("-->")) {
/* 151 */       return line;
/*     */     }
/* 153 */     while ((line = consume(line)) != null) {
/* 154 */       if (!this.inComment && !line.trim().startsWith("<!--")) {
/* 155 */         return line;
/*     */       }
/*     */     } 
/* 158 */     return line;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String consume(String line) {
/* 166 */     int index = this.inComment ? endComment(line) : startComment(line);
/* 167 */     return (index == -1) ? null : line.substring(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int startComment(String line) {
/* 175 */     return commentToken(line, "<!--", true);
/*     */   }
/*     */   
/*     */   private int endComment(String line) {
/* 179 */     return commentToken(line, "-->", false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int commentToken(String line, String token, boolean inCommentIfPresent) {
/* 188 */     int index = line.indexOf(token);
/* 189 */     if (index > -1) {
/* 190 */       this.inComment = inCommentIfPresent;
/*     */     }
/* 192 */     return (index == -1) ? index : (index + token.length());
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\xml\XmlValidationModeDetector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */