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
/*     */ class HtmlCharacterEntityDecoder
/*     */ {
/*     */   private static final int MAX_REFERENCE_SIZE = 10;
/*     */   private final HtmlCharacterEntityReferences characterEntityReferences;
/*     */   private final String originalMessage;
/*     */   private final StringBuilder decodedMessage;
/*  38 */   private int currentPosition = 0;
/*     */   
/*  40 */   private int nextPotentialReferencePosition = -1;
/*     */   
/*  42 */   private int nextSemicolonPosition = -2;
/*     */ 
/*     */   
/*     */   public HtmlCharacterEntityDecoder(HtmlCharacterEntityReferences characterEntityReferences, String original) {
/*  46 */     this.characterEntityReferences = characterEntityReferences;
/*  47 */     this.originalMessage = original;
/*  48 */     this.decodedMessage = new StringBuilder(original.length());
/*     */   }
/*     */ 
/*     */   
/*     */   public String decode() {
/*  53 */     while (this.currentPosition < this.originalMessage.length()) {
/*  54 */       findNextPotentialReference(this.currentPosition);
/*  55 */       copyCharactersTillPotentialReference();
/*  56 */       processPossibleReference();
/*     */     } 
/*  58 */     return this.decodedMessage.toString();
/*     */   }
/*     */   
/*     */   private void findNextPotentialReference(int startPosition) {
/*  62 */     this.nextPotentialReferencePosition = Math.max(startPosition, this.nextSemicolonPosition - 10);
/*     */     
/*     */     do {
/*  65 */       this
/*  66 */         .nextPotentialReferencePosition = this.originalMessage.indexOf('&', this.nextPotentialReferencePosition);
/*     */       
/*  68 */       if (this.nextSemicolonPosition != -1 && this.nextSemicolonPosition < this.nextPotentialReferencePosition)
/*     */       {
/*  70 */         this.nextSemicolonPosition = this.originalMessage.indexOf(';', this.nextPotentialReferencePosition + 1);
/*     */       }
/*  72 */       boolean isPotentialReference = (this.nextPotentialReferencePosition != -1 && this.nextSemicolonPosition != -1 && this.nextPotentialReferencePosition - this.nextSemicolonPosition < 10);
/*     */ 
/*     */ 
/*     */       
/*  76 */       if (isPotentialReference) {
/*     */         break;
/*     */       }
/*  79 */       if (this.nextPotentialReferencePosition == -1) {
/*     */         break;
/*     */       }
/*  82 */       if (this.nextSemicolonPosition == -1) {
/*  83 */         this.nextPotentialReferencePosition = -1;
/*     */         
/*     */         break;
/*     */       } 
/*  87 */       this.nextPotentialReferencePosition++;
/*     */     }
/*  89 */     while (this.nextPotentialReferencePosition != -1);
/*     */   }
/*     */   
/*     */   private void copyCharactersTillPotentialReference() {
/*  93 */     if (this.nextPotentialReferencePosition != this.currentPosition) {
/*     */       
/*  95 */       int skipUntilIndex = (this.nextPotentialReferencePosition != -1) ? this.nextPotentialReferencePosition : this.originalMessage.length();
/*  96 */       if (skipUntilIndex - this.currentPosition > 3) {
/*  97 */         this.decodedMessage.append(this.originalMessage.substring(this.currentPosition, skipUntilIndex));
/*  98 */         this.currentPosition = skipUntilIndex;
/*     */       } else {
/*     */         
/* 101 */         while (this.currentPosition < skipUntilIndex)
/* 102 */           this.decodedMessage.append(this.originalMessage.charAt(this.currentPosition++)); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void processPossibleReference() {
/* 108 */     if (this.nextPotentialReferencePosition != -1) {
/* 109 */       boolean isNumberedReference = (this.originalMessage.charAt(this.currentPosition + 1) == '#');
/* 110 */       boolean wasProcessable = isNumberedReference ? processNumberedReference() : processNamedReference();
/* 111 */       if (wasProcessable) {
/* 112 */         this.currentPosition = this.nextSemicolonPosition + 1;
/*     */       } else {
/*     */         
/* 115 */         char currentChar = this.originalMessage.charAt(this.currentPosition);
/* 116 */         this.decodedMessage.append(currentChar);
/* 117 */         this.currentPosition++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean processNumberedReference() {
/* 123 */     char referenceChar = this.originalMessage.charAt(this.nextPotentialReferencePosition + 2);
/* 124 */     boolean isHexNumberedReference = (referenceChar == 'x' || referenceChar == 'X');
/*     */ 
/*     */     
/*     */     try {
/* 128 */       int value = !isHexNumberedReference ? Integer.parseInt(getReferenceSubstring(2)) : Integer.parseInt(getReferenceSubstring(3), 16);
/* 129 */       this.decodedMessage.append((char)value);
/* 130 */       return true;
/*     */     }
/* 132 */     catch (NumberFormatException ex) {
/* 133 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean processNamedReference() {
/* 138 */     String referenceName = getReferenceSubstring(1);
/* 139 */     char mappedCharacter = this.characterEntityReferences.convertToCharacter(referenceName);
/* 140 */     if (mappedCharacter != Character.MAX_VALUE) {
/* 141 */       this.decodedMessage.append(mappedCharacter);
/* 142 */       return true;
/*     */     } 
/* 144 */     return false;
/*     */   }
/*     */   
/*     */   private String getReferenceSubstring(int referenceOffset) {
/* 148 */     return this.originalMessage.substring(this.nextPotentialReferencePosition + referenceOffset, this.nextSemicolonPosition);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\we\\util\HtmlCharacterEntityDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */