/*    */ package org.springframework.expression.spel;
/*    */ 
/*    */ import org.springframework.expression.ParseException;
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
/*    */ public class SpelParseException
/*    */   extends ParseException
/*    */ {
/*    */   private final SpelMessage message;
/*    */   private final Object[] inserts;
/*    */   
/*    */   public SpelParseException(String expressionString, int position, SpelMessage message, Object... inserts) {
/* 39 */     super(expressionString, position, message.formatMessage(inserts));
/* 40 */     this.message = message;
/* 41 */     this.inserts = inserts;
/*    */   }
/*    */   
/*    */   public SpelParseException(int position, SpelMessage message, Object... inserts) {
/* 45 */     super(position, message.formatMessage(inserts));
/* 46 */     this.message = message;
/* 47 */     this.inserts = inserts;
/*    */   }
/*    */   
/*    */   public SpelParseException(int position, Throwable cause, SpelMessage message, Object... inserts) {
/* 51 */     super(position, message.formatMessage(inserts), cause);
/* 52 */     this.message = message;
/* 53 */     this.inserts = inserts;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SpelMessage getMessageCode() {
/* 61 */     return this.message;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object[] getInserts() {
/* 68 */     return this.inserts;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\SpelParseException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */