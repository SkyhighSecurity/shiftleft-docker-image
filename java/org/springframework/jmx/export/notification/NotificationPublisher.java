package org.springframework.jmx.export.notification;

import javax.management.Notification;

public interface NotificationPublisher {
  void sendNotification(Notification paramNotification) throws UnableToSendNotificationException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\jmx\export\notification\NotificationPublisher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */