/*    */ package org.springframework.context.support;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import org.springframework.context.HierarchicalMessageSource;
/*    */ import org.springframework.context.MessageSource;
/*    */ import org.springframework.context.MessageSourceResolvable;
/*    */ import org.springframework.context.NoSuchMessageException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DelegatingMessageSource
/*    */   extends MessageSourceSupport
/*    */   implements HierarchicalMessageSource
/*    */ {
/*    */   private MessageSource parentMessageSource;
/*    */   
/*    */   public void setParentMessageSource(MessageSource parent) {
/* 44 */     this.parentMessageSource = parent;
/*    */   }
/*    */ 
/*    */   
/*    */   public MessageSource getParentMessageSource() {
/* 49 */     return this.parentMessageSource;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
/* 55 */     if (this.parentMessageSource != null) {
/* 56 */       return this.parentMessageSource.getMessage(code, args, defaultMessage, locale);
/*    */     }
/*    */     
/* 59 */     return renderDefaultMessage(defaultMessage, args, locale);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
/* 65 */     if (this.parentMessageSource != null) {
/* 66 */       return this.parentMessageSource.getMessage(code, args, locale);
/*    */     }
/*    */     
/* 69 */     throw new NoSuchMessageException(code, locale);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
/* 75 */     if (this.parentMessageSource != null) {
/* 76 */       return this.parentMessageSource.getMessage(resolvable, locale);
/*    */     }
/*    */     
/* 79 */     if (resolvable.getDefaultMessage() != null) {
/* 80 */       return renderDefaultMessage(resolvable.getDefaultMessage(), resolvable.getArguments(), locale);
/*    */     }
/* 82 */     String[] codes = resolvable.getCodes();
/* 83 */     String code = (codes != null && codes.length > 0) ? codes[0] : null;
/* 84 */     throw new NoSuchMessageException(code, locale);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\support\DelegatingMessageSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */