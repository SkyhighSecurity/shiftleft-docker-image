/*     */ package org.springframework.jmx.support;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import javax.management.MalformedObjectNameException;
/*     */ import javax.management.NotificationFilter;
/*     */ import javax.management.NotificationListener;
/*     */ import javax.management.ObjectName;
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
/*     */ public class NotificationListenerHolder
/*     */ {
/*     */   private NotificationListener notificationListener;
/*     */   private NotificationFilter notificationFilter;
/*     */   private Object handback;
/*     */   protected Set<Object> mappedObjectNames;
/*     */   
/*     */   public void setNotificationListener(NotificationListener notificationListener) {
/*  55 */     this.notificationListener = notificationListener;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NotificationListener getNotificationListener() {
/*  62 */     return this.notificationListener;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNotificationFilter(NotificationFilter notificationFilter) {
/*  71 */     this.notificationFilter = notificationFilter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NotificationFilter getNotificationFilter() {
/*  80 */     return this.notificationFilter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHandback(Object handback) {
/*  91 */     this.handback = handback;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getHandback() {
/* 102 */     return this.handback;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMappedObjectName(Object mappedObjectName) {
/* 113 */     (new Object[1])[0] = mappedObjectName; setMappedObjectNames((mappedObjectName != null) ? new Object[1] : null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMappedObjectNames(Object[] mappedObjectNames) {
/* 124 */     this
/* 125 */       .mappedObjectNames = (mappedObjectNames != null) ? new LinkedHashSet(Arrays.asList(mappedObjectNames)) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectName[] getResolvedObjectNames() throws MalformedObjectNameException {
/* 135 */     if (this.mappedObjectNames == null) {
/* 136 */       return null;
/*     */     }
/* 138 */     ObjectName[] resolved = new ObjectName[this.mappedObjectNames.size()];
/* 139 */     int i = 0;
/* 140 */     for (Object objectName : this.mappedObjectNames) {
/* 141 */       resolved[i] = ObjectNameManager.getInstance(objectName);
/* 142 */       i++;
/*     */     } 
/* 144 */     return resolved;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 150 */     if (this == other) {
/* 151 */       return true;
/*     */     }
/* 153 */     if (!(other instanceof NotificationListenerHolder)) {
/* 154 */       return false;
/*     */     }
/* 156 */     NotificationListenerHolder otherNlh = (NotificationListenerHolder)other;
/* 157 */     return (ObjectUtils.nullSafeEquals(this.notificationListener, otherNlh.notificationListener) && 
/* 158 */       ObjectUtils.nullSafeEquals(this.notificationFilter, otherNlh.notificationFilter) && 
/* 159 */       ObjectUtils.nullSafeEquals(this.handback, otherNlh.handback) && 
/* 160 */       ObjectUtils.nullSafeEquals(this.mappedObjectNames, otherNlh.mappedObjectNames));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 165 */     int hashCode = ObjectUtils.nullSafeHashCode(this.notificationListener);
/* 166 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.notificationFilter);
/* 167 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.handback);
/* 168 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.mappedObjectNames);
/* 169 */     return hashCode;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jmx\support\NotificationListenerHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */