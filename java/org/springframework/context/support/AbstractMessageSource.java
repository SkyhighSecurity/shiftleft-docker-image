/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Properties;
/*     */ import org.springframework.context.HierarchicalMessageSource;
/*     */ import org.springframework.context.MessageSource;
/*     */ import org.springframework.context.MessageSourceResolvable;
/*     */ import org.springframework.context.NoSuchMessageException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractMessageSource
/*     */   extends MessageSourceSupport
/*     */   implements HierarchicalMessageSource
/*     */ {
/*     */   private MessageSource parentMessageSource;
/*     */   private Properties commonMessages;
/*     */   private boolean useCodeAsDefaultMessage = false;
/*     */   
/*     */   public void setParentMessageSource(MessageSource parent) {
/*  75 */     this.parentMessageSource = parent;
/*     */   }
/*     */ 
/*     */   
/*     */   public MessageSource getParentMessageSource() {
/*  80 */     return this.parentMessageSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCommonMessages(Properties commonMessages) {
/*  90 */     this.commonMessages = commonMessages;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Properties getCommonMessages() {
/*  97 */     return this.commonMessages;
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
/*     */   public void setUseCodeAsDefaultMessage(boolean useCodeAsDefaultMessage) {
/* 118 */     this.useCodeAsDefaultMessage = useCodeAsDefaultMessage;
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
/*     */   protected boolean isUseCodeAsDefaultMessage() {
/* 130 */     return this.useCodeAsDefaultMessage;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
/* 136 */     String msg = getMessageInternal(code, args, locale);
/* 137 */     if (msg != null) {
/* 138 */       return msg;
/*     */     }
/* 140 */     if (defaultMessage == null) {
/* 141 */       String fallback = getDefaultMessage(code);
/* 142 */       if (fallback != null) {
/* 143 */         return fallback;
/*     */       }
/*     */     } 
/* 146 */     return renderDefaultMessage(defaultMessage, args, locale);
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
/* 151 */     String msg = getMessageInternal(code, args, locale);
/* 152 */     if (msg != null) {
/* 153 */       return msg;
/*     */     }
/* 155 */     String fallback = getDefaultMessage(code);
/* 156 */     if (fallback != null) {
/* 157 */       return fallback;
/*     */     }
/* 159 */     throw new NoSuchMessageException(code, locale);
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
/* 164 */     String[] codes = resolvable.getCodes();
/* 165 */     if (codes != null) {
/* 166 */       for (String code : codes) {
/* 167 */         String message = getMessageInternal(code, resolvable.getArguments(), locale);
/* 168 */         if (message != null) {
/* 169 */           return message;
/*     */         }
/*     */       } 
/*     */     }
/* 173 */     String defaultMessage = getDefaultMessage(resolvable, locale);
/* 174 */     if (defaultMessage != null) {
/* 175 */       return defaultMessage;
/*     */     }
/* 177 */     throw new NoSuchMessageException(!ObjectUtils.isEmpty(codes) ? codes[codes.length - 1] : null, locale);
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
/*     */   protected String getMessageInternal(String code, Object[] args, Locale locale) {
/* 196 */     if (code == null) {
/* 197 */       return null;
/*     */     }
/* 199 */     if (locale == null) {
/* 200 */       locale = Locale.getDefault();
/*     */     }
/* 202 */     Object[] argsToUse = args;
/*     */     
/* 204 */     if (!isAlwaysUseMessageFormat() && ObjectUtils.isEmpty(args)) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 209 */       String message = resolveCodeWithoutArguments(code, locale);
/* 210 */       if (message != null) {
/* 211 */         return message;
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 219 */       argsToUse = resolveArguments(args, locale);
/*     */       
/* 221 */       MessageFormat messageFormat = resolveCode(code, locale);
/* 222 */       if (messageFormat != null) {
/* 223 */         synchronized (messageFormat) {
/* 224 */           return messageFormat.format(argsToUse);
/*     */         } 
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 230 */     Properties commonMessages = getCommonMessages();
/* 231 */     if (commonMessages != null) {
/* 232 */       String commonMessage = commonMessages.getProperty(code);
/* 233 */       if (commonMessage != null) {
/* 234 */         return formatMessage(commonMessage, args, locale);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 239 */     return getMessageFromParent(code, argsToUse, locale);
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
/*     */   protected String getMessageFromParent(String code, Object[] args, Locale locale) {
/* 252 */     MessageSource parent = getParentMessageSource();
/* 253 */     if (parent != null) {
/* 254 */       if (parent instanceof AbstractMessageSource)
/*     */       {
/*     */         
/* 257 */         return ((AbstractMessageSource)parent).getMessageInternal(code, args, locale);
/*     */       }
/*     */ 
/*     */       
/* 261 */       return parent.getMessage(code, args, null, locale);
/*     */     } 
/*     */ 
/*     */     
/* 265 */     return null;
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
/*     */   protected String getDefaultMessage(MessageSourceResolvable resolvable, Locale locale) {
/* 281 */     String defaultMessage = resolvable.getDefaultMessage();
/* 282 */     String[] codes = resolvable.getCodes();
/* 283 */     if (defaultMessage != null) {
/* 284 */       if (!ObjectUtils.isEmpty((Object[])codes) && defaultMessage.equals(codes[0]))
/*     */       {
/* 286 */         return defaultMessage;
/*     */       }
/* 288 */       return renderDefaultMessage(defaultMessage, resolvable.getArguments(), locale);
/*     */     } 
/* 290 */     return !ObjectUtils.isEmpty((Object[])codes) ? getDefaultMessage(codes[0]) : null;
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
/*     */   protected String getDefaultMessage(String code) {
/* 304 */     if (isUseCodeAsDefaultMessage()) {
/* 305 */       return code;
/*     */     }
/* 307 */     return null;
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
/*     */   protected Object[] resolveArguments(Object[] args, Locale locale) {
/* 321 */     if (args == null) {
/* 322 */       return new Object[0];
/*     */     }
/* 324 */     List<Object> resolvedArgs = new ArrayList(args.length);
/* 325 */     for (Object arg : args) {
/* 326 */       if (arg instanceof MessageSourceResolvable) {
/* 327 */         resolvedArgs.add(getMessage((MessageSourceResolvable)arg, locale));
/*     */       } else {
/*     */         
/* 330 */         resolvedArgs.add(arg);
/*     */       } 
/*     */     } 
/* 333 */     return resolvedArgs.toArray();
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
/*     */   protected String resolveCodeWithoutArguments(String code, Locale locale) {
/* 354 */     MessageFormat messageFormat = resolveCode(code, locale);
/* 355 */     if (messageFormat != null) {
/* 356 */       synchronized (messageFormat) {
/* 357 */         return messageFormat.format(new Object[0]);
/*     */       } 
/*     */     }
/* 360 */     return null;
/*     */   }
/*     */   
/*     */   protected abstract MessageFormat resolveCode(String paramString, Locale paramLocale);
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\support\AbstractMessageSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */