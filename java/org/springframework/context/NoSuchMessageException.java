/*    */ package org.springframework.context;
/*    */ 
/*    */ import java.util.Locale;
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
/*    */ public class NoSuchMessageException
/*    */   extends RuntimeException
/*    */ {
/*    */   public NoSuchMessageException(String code, Locale locale) {
/* 35 */     super("No message found under code '" + code + "' for locale '" + locale + "'.");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NoSuchMessageException(String code) {
/* 43 */     super("No message found under code '" + code + "' for locale '" + Locale.getDefault() + "'.");
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\NoSuchMessageException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */