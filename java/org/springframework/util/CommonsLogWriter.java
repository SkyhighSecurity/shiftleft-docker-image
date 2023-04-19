/*    */ package org.springframework.util;
/*    */ 
/*    */ import java.io.Writer;
/*    */ import org.apache.commons.logging.Log;
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
/*    */ public class CommonsLogWriter
/*    */   extends Writer
/*    */ {
/*    */   private final Log logger;
/* 33 */   private final StringBuilder buffer = new StringBuilder();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CommonsLogWriter(Log logger) {
/* 41 */     Assert.notNull(logger, "Logger must not be null");
/* 42 */     this.logger = logger;
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(char ch) {
/* 47 */     if (ch == '\n' && this.buffer.length() > 0) {
/* 48 */       this.logger.debug(this.buffer.toString());
/* 49 */       this.buffer.setLength(0);
/*    */     } else {
/*    */       
/* 52 */       this.buffer.append(ch);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(char[] buffer, int offset, int length) {
/* 58 */     for (int i = 0; i < length; i++) {
/* 59 */       char ch = buffer[offset + i];
/* 60 */       if (ch == '\n' && this.buffer.length() > 0) {
/* 61 */         this.logger.debug(this.buffer.toString());
/* 62 */         this.buffer.setLength(0);
/*    */       } else {
/*    */         
/* 65 */         this.buffer.append(ch);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void flush() {}
/*    */   
/*    */   public void close() {}
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\CommonsLogWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */