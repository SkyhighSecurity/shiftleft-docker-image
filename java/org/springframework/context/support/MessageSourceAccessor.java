/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.springframework.context.MessageSource;
/*     */ import org.springframework.context.MessageSourceResolvable;
/*     */ import org.springframework.context.NoSuchMessageException;
/*     */ import org.springframework.context.i18n.LocaleContextHolder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MessageSourceAccessor
/*     */ {
/*     */   private final MessageSource messageSource;
/*     */   private final Locale defaultLocale;
/*     */   
/*     */   public MessageSourceAccessor(MessageSource messageSource) {
/*  51 */     this.messageSource = messageSource;
/*  52 */     this.defaultLocale = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageSourceAccessor(MessageSource messageSource, Locale defaultLocale) {
/*  61 */     this.messageSource = messageSource;
/*  62 */     this.defaultLocale = defaultLocale;
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
/*     */   protected Locale getDefaultLocale() {
/*  75 */     return (this.defaultLocale != null) ? this.defaultLocale : LocaleContextHolder.getLocale();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage(String code, String defaultMessage) {
/*  85 */     return this.messageSource.getMessage(code, null, defaultMessage, getDefaultLocale());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage(String code, String defaultMessage, Locale locale) {
/*  96 */     return this.messageSource.getMessage(code, null, defaultMessage, locale);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage(String code, Object[] args, String defaultMessage) {
/* 107 */     return this.messageSource.getMessage(code, args, defaultMessage, getDefaultLocale());
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
/*     */   public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
/* 119 */     return this.messageSource.getMessage(code, args, defaultMessage, locale);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage(String code) throws NoSuchMessageException {
/* 129 */     return this.messageSource.getMessage(code, null, getDefaultLocale());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage(String code, Locale locale) throws NoSuchMessageException {
/* 140 */     return this.messageSource.getMessage(code, null, locale);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage(String code, Object[] args) throws NoSuchMessageException {
/* 151 */     return this.messageSource.getMessage(code, args, getDefaultLocale());
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
/*     */   public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
/* 163 */     return this.messageSource.getMessage(code, args, locale);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage(MessageSourceResolvable resolvable) throws NoSuchMessageException {
/* 174 */     return this.messageSource.getMessage(resolvable, getDefaultLocale());
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
/*     */   public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
/* 186 */     return this.messageSource.getMessage(resolvable, locale);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\support\MessageSourceAccessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */