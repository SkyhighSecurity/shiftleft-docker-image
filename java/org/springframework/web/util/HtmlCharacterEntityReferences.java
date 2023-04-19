/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class HtmlCharacterEntityReferences
/*     */ {
/*     */   private static final String PROPERTIES_FILE = "HtmlCharacterEntityReferences.properties";
/*     */   static final char REFERENCE_START = '&';
/*     */   static final String DECIMAL_REFERENCE_START = "&#";
/*     */   static final String HEX_REFERENCE_START = "&#x";
/*     */   static final char REFERENCE_END = ';';
/*     */   static final char CHAR_NULL = '￿';
/*  53 */   private final String[] characterToEntityReferenceMap = new String[3000];
/*     */   
/*  55 */   private final Map<String, Character> entityReferenceToCharacterMap = new HashMap<String, Character>(512);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HtmlCharacterEntityReferences() {
/*  62 */     Properties entityReferences = new Properties();
/*     */ 
/*     */     
/*  65 */     InputStream is = HtmlCharacterEntityReferences.class.getResourceAsStream("HtmlCharacterEntityReferences.properties");
/*  66 */     if (is == null) {
/*  67 */       throw new IllegalStateException("Cannot find reference definition file [HtmlCharacterEntityReferences.properties] as class path resource");
/*     */     }
/*     */     
/*     */     try {
/*     */       try {
/*  72 */         entityReferences.load(is);
/*     */       } finally {
/*     */         
/*  75 */         is.close();
/*     */       }
/*     */     
/*  78 */     } catch (IOException ex) {
/*  79 */       throw new IllegalStateException("Failed to parse reference definition file [HtmlCharacterEntityReferences.properties]: " + ex
/*  80 */           .getMessage());
/*     */     } 
/*     */ 
/*     */     
/*  84 */     Enumeration<?> keys = entityReferences.propertyNames();
/*  85 */     while (keys.hasMoreElements()) {
/*  86 */       String key = (String)keys.nextElement();
/*  87 */       int referredChar = Integer.parseInt(key);
/*  88 */       if (referredChar >= 1000 && (referredChar < 8000 || referredChar >= 10000)) {
/*  89 */         throw new IllegalArgumentException("Invalid reference to special HTML entity: " + referredChar);
/*     */       }
/*  91 */       int index = (referredChar < 1000) ? referredChar : (referredChar - 7000);
/*  92 */       String reference = entityReferences.getProperty(key);
/*  93 */       this.characterToEntityReferenceMap[index] = '&' + reference + ';';
/*  94 */       this.entityReferenceToCharacterMap.put(reference, Character.valueOf((char)referredChar));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSupportedReferenceCount() {
/* 103 */     return this.entityReferenceToCharacterMap.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMappedToReference(char character) {
/* 110 */     return isMappedToReference(character, "ISO-8859-1");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMappedToReference(char character, String encoding) {
/* 117 */     return (convertToReference(character, encoding) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String convertToReference(char character) {
/* 124 */     return convertToReference(character, "ISO-8859-1");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String convertToReference(char character, String encoding) {
/* 132 */     if (encoding.startsWith("UTF-")) {
/* 133 */       switch (character) {
/*     */         case '<':
/* 135 */           return "&lt;";
/*     */         case '>':
/* 137 */           return "&gt;";
/*     */         case '"':
/* 139 */           return "&quot;";
/*     */         case '&':
/* 141 */           return "&amp;";
/*     */         case '\'':
/* 143 */           return "&#39;";
/*     */       } 
/*     */     
/* 146 */     } else if (character < 'Ϩ' || (character >= 'ὀ' && character < '✐')) {
/* 147 */       int index = (character < 'Ϩ') ? character : (character - 7000);
/* 148 */       String entityReference = this.characterToEntityReferenceMap[index];
/* 149 */       if (entityReference != null) {
/* 150 */         return entityReference;
/*     */       }
/*     */     } 
/* 153 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char convertToCharacter(String entityReference) {
/* 160 */     Character referredCharacter = this.entityReferenceToCharacterMap.get(entityReference);
/* 161 */     if (referredCharacter != null) {
/* 162 */       return referredCharacter.charValue();
/*     */     }
/* 164 */     return Character.MAX_VALUE;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\we\\util\HtmlCharacterEntityReferences.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */