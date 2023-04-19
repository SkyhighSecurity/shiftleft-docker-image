/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Locale;
/*     */ import java.util.ResourceBundle;
/*     */ import org.springframework.context.MessageSource;
/*     */ import org.springframework.context.NoSuchMessageException;
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
/*     */ public class MessageSourceResourceBundle
/*     */   extends ResourceBundle
/*     */ {
/*     */   private final MessageSource messageSource;
/*     */   private final Locale locale;
/*     */   
/*     */   public MessageSourceResourceBundle(MessageSource source, Locale locale) {
/*  51 */     Assert.notNull(source, "MessageSource must not be null");
/*  52 */     this.messageSource = source;
/*  53 */     this.locale = locale;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageSourceResourceBundle(MessageSource source, Locale locale, ResourceBundle parent) {
/*  63 */     this(source, locale);
/*  64 */     setParent(parent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object handleGetObject(String key) {
/*     */     try {
/*  75 */       return this.messageSource.getMessage(key, null, this.locale);
/*     */     }
/*  77 */     catch (NoSuchMessageException ex) {
/*  78 */       return null;
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
/*     */   public boolean containsKey(String key) {
/*     */     try {
/*  91 */       this.messageSource.getMessage(key, null, this.locale);
/*  92 */       return true;
/*     */     }
/*  94 */     catch (NoSuchMessageException ex) {
/*  95 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration<String> getKeys() {
/* 105 */     throw new UnsupportedOperationException("MessageSourceResourceBundle does not support enumerating its keys");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Locale getLocale() {
/* 114 */     return this.locale;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\support\MessageSourceResourceBundle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */