/*     */ package org.springframework.beans.propertyeditors;
/*     */ 
/*     */ import java.beans.PropertyEditorSupport;
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
/*     */ public class CharacterEditor
/*     */   extends PropertyEditorSupport
/*     */ {
/*     */   private static final String UNICODE_PREFIX = "\\u";
/*     */   private static final int UNICODE_LENGTH = 6;
/*     */   private final boolean allowEmpty;
/*     */   
/*     */   public CharacterEditor(boolean allowEmpty) {
/*  67 */     this.allowEmpty = allowEmpty;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAsText(String text) throws IllegalArgumentException {
/*  73 */     if (this.allowEmpty && !StringUtils.hasLength(text)) {
/*     */       
/*  75 */       setValue(null);
/*     */     } else {
/*  77 */       if (text == null) {
/*  78 */         throw new IllegalArgumentException("null String cannot be converted to char type");
/*     */       }
/*  80 */       if (isUnicodeCharacterSequence(text)) {
/*  81 */         setAsUnicode(text);
/*     */       }
/*  83 */       else if (text.length() == 1) {
/*  84 */         setValue(Character.valueOf(text.charAt(0)));
/*     */       } else {
/*     */         
/*  87 */         throw new IllegalArgumentException("String [" + text + "] with length " + text
/*  88 */             .length() + " cannot be converted to char type: neither Unicode nor single character");
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getAsText() {
/*  94 */     Object value = getValue();
/*  95 */     return (value != null) ? value.toString() : "";
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isUnicodeCharacterSequence(String sequence) {
/* 100 */     return (sequence.startsWith("\\u") && sequence.length() == 6);
/*     */   }
/*     */   
/*     */   private void setAsUnicode(String text) {
/* 104 */     int code = Integer.parseInt(text.substring("\\u".length()), 16);
/* 105 */     setValue(Character.valueOf((char)code));
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\propertyeditors\CharacterEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */