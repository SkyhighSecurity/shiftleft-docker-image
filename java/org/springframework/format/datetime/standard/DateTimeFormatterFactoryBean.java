/*    */ package org.springframework.format.datetime.standard;
/*    */ 
/*    */ import java.time.format.DateTimeFormatter;
/*    */ import org.springframework.beans.factory.FactoryBean;
/*    */ import org.springframework.beans.factory.InitializingBean;
/*    */ import org.springframework.lang.UsesJava8;
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
/*    */ 
/*    */ 
/*    */ @UsesJava8
/*    */ public class DateTimeFormatterFactoryBean
/*    */   extends DateTimeFormatterFactory
/*    */   implements FactoryBean<DateTimeFormatter>, InitializingBean
/*    */ {
/*    */   private DateTimeFormatter dateTimeFormatter;
/*    */   
/*    */   public void afterPropertiesSet() {
/* 46 */     this.dateTimeFormatter = createDateTimeFormatter();
/*    */   }
/*    */ 
/*    */   
/*    */   public DateTimeFormatter getObject() {
/* 51 */     return this.dateTimeFormatter;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<?> getObjectType() {
/* 56 */     return DateTimeFormatter.class;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSingleton() {
/* 61 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\format\datetime\standard\DateTimeFormatterFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */