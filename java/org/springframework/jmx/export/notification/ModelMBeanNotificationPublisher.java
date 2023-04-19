/*     */ package org.springframework.jmx.export.notification;
/*     */ 
/*     */ import javax.management.AttributeChangeNotification;
/*     */ import javax.management.MBeanException;
/*     */ import javax.management.Notification;
/*     */ import javax.management.ObjectName;
/*     */ import javax.management.modelmbean.ModelMBeanNotificationBroadcaster;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ public class ModelMBeanNotificationPublisher
/*     */   implements NotificationPublisher
/*     */ {
/*     */   private final ModelMBeanNotificationBroadcaster modelMBean;
/*     */   private final ObjectName objectName;
/*     */   private final Object managedResource;
/*     */   
/*     */   public ModelMBeanNotificationPublisher(ModelMBeanNotificationBroadcaster modelMBean, ObjectName objectName, Object managedResource) {
/*  72 */     Assert.notNull(modelMBean, "'modelMBean' must not be null");
/*  73 */     Assert.notNull(objectName, "'objectName' must not be null");
/*  74 */     Assert.notNull(managedResource, "'managedResource' must not be null");
/*  75 */     this.modelMBean = modelMBean;
/*  76 */     this.objectName = objectName;
/*  77 */     this.managedResource = managedResource;
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
/*     */   public void sendNotification(Notification notification) {
/*  90 */     Assert.notNull(notification, "Notification must not be null");
/*  91 */     replaceNotificationSourceIfNecessary(notification);
/*     */     try {
/*  93 */       if (notification instanceof AttributeChangeNotification) {
/*  94 */         this.modelMBean.sendAttributeChangeNotification((AttributeChangeNotification)notification);
/*     */       } else {
/*     */         
/*  97 */         this.modelMBean.sendNotification(notification);
/*     */       }
/*     */     
/* 100 */     } catch (MBeanException ex) {
/* 101 */       throw new UnableToSendNotificationException("Unable to send notification [" + notification + "]", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void replaceNotificationSourceIfNecessary(Notification notification) {
/* 113 */     if (notification.getSource() == null || notification.getSource().equals(this.managedResource))
/* 114 */       notification.setSource(this.objectName); 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jmx\export\notification\ModelMBeanNotificationPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */