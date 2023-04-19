/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.text.MessageFormat;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class MessageSourceSupport
/*     */ {
/*  43 */   private static final MessageFormat INVALID_MESSAGE_FORMAT = new MessageFormat("");
/*     */ 
/*     */   
/*  46 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean alwaysUseMessageFormat = false;
/*     */ 
/*     */ 
/*     */   
/*  55 */   private final Map<String, Map<Locale, MessageFormat>> messageFormatsPerMessage = new HashMap<String, Map<Locale, MessageFormat>>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAlwaysUseMessageFormat(boolean alwaysUseMessageFormat) {
/*  74 */     this.alwaysUseMessageFormat = alwaysUseMessageFormat;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isAlwaysUseMessageFormat() {
/*  82 */     return this.alwaysUseMessageFormat;
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
/*     */   protected String renderDefaultMessage(String defaultMessage, Object[] args, Locale locale) {
/* 101 */     return formatMessage(defaultMessage, args, locale);
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
/*     */   protected String formatMessage(String msg, Object[] args, Locale locale) {
/* 115 */     if (msg == null || (!isAlwaysUseMessageFormat() && ObjectUtils.isEmpty(args))) {
/* 116 */       return msg;
/*     */     }
/* 118 */     MessageFormat messageFormat = null;
/* 119 */     synchronized (this.messageFormatsPerMessage) {
/* 120 */       Map<Locale, MessageFormat> messageFormatsPerLocale = this.messageFormatsPerMessage.get(msg);
/* 121 */       if (messageFormatsPerLocale != null) {
/* 122 */         messageFormat = messageFormatsPerLocale.get(locale);
/*     */       } else {
/*     */         
/* 125 */         messageFormatsPerLocale = new HashMap<Locale, MessageFormat>();
/* 126 */         this.messageFormatsPerMessage.put(msg, messageFormatsPerLocale);
/*     */       } 
/* 128 */       if (messageFormat == null) {
/*     */         try {
/* 130 */           messageFormat = createMessageFormat(msg, locale);
/*     */         }
/* 132 */         catch (IllegalArgumentException ex) {
/*     */ 
/*     */           
/* 135 */           if (isAlwaysUseMessageFormat()) {
/* 136 */             throw ex;
/*     */           }
/*     */           
/* 139 */           messageFormat = INVALID_MESSAGE_FORMAT;
/*     */         } 
/* 141 */         messageFormatsPerLocale.put(locale, messageFormat);
/*     */       } 
/*     */     } 
/* 144 */     if (messageFormat == INVALID_MESSAGE_FORMAT) {
/* 145 */       return msg;
/*     */     }
/* 147 */     synchronized (messageFormat) {
/* 148 */       return messageFormat.format(resolveArguments(args, locale));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MessageFormat createMessageFormat(String msg, Locale locale) {
/* 159 */     return new MessageFormat((msg != null) ? msg : "", locale);
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
/*     */   protected Object[] resolveArguments(Object[] args, Locale locale) {
/* 171 */     return args;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\support\MessageSourceSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */