/*    */ package org.springframework.jmx.export.metadata;
/*    */ 
/*    */ import org.springframework.util.StringUtils;
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
/*    */ public class ManagedNotification
/*    */ {
/*    */   private String[] notificationTypes;
/*    */   private String name;
/*    */   private String description;
/*    */   
/*    */   public void setNotificationType(String notificationType) {
/* 41 */     this.notificationTypes = StringUtils.commaDelimitedListToStringArray(notificationType);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setNotificationTypes(String... notificationTypes) {
/* 48 */     this.notificationTypes = notificationTypes;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String[] getNotificationTypes() {
/* 55 */     return this.notificationTypes;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setName(String name) {
/* 62 */     this.name = name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 69 */     return this.name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setDescription(String description) {
/* 76 */     this.description = description;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getDescription() {
/* 83 */     return this.description;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jmx\export\metadata\ManagedNotification.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */