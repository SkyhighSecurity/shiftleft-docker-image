/*    */ package org.springframework.jmx.export.metadata;
/*    */ 
/*    */ import javax.management.modelmbean.ModelMBeanNotificationInfo;
/*    */ import org.springframework.util.ObjectUtils;
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
/*    */ public abstract class JmxMetadataUtils
/*    */ {
/*    */   public static ModelMBeanNotificationInfo convertToModelMBeanNotificationInfo(ManagedNotification notificationInfo) {
/* 38 */     String[] notifTypes = notificationInfo.getNotificationTypes();
/* 39 */     if (ObjectUtils.isEmpty((Object[])notifTypes)) {
/* 40 */       throw new IllegalArgumentException("Must specify at least one notification type");
/*    */     }
/*    */     
/* 43 */     String name = notificationInfo.getName();
/* 44 */     if (!StringUtils.hasText(name)) {
/* 45 */       throw new IllegalArgumentException("Must specify notification name");
/*    */     }
/*    */     
/* 48 */     String description = notificationInfo.getDescription();
/* 49 */     return new ModelMBeanNotificationInfo(notifTypes, name, description);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jmx\export\metadata\JmxMetadataUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */