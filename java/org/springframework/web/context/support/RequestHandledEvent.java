/*     */ package org.springframework.web.context.support;
/*     */ 
/*     */ import org.springframework.context.ApplicationEvent;
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
/*     */ public class RequestHandledEvent
/*     */   extends ApplicationEvent
/*     */ {
/*     */   private String sessionId;
/*     */   private String userName;
/*     */   private final long processingTimeMillis;
/*     */   private Throwable failureCause;
/*     */   
/*     */   public RequestHandledEvent(Object source, String sessionId, String userName, long processingTimeMillis) {
/*  61 */     super(source);
/*  62 */     this.sessionId = sessionId;
/*  63 */     this.userName = userName;
/*  64 */     this.processingTimeMillis = processingTimeMillis;
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
/*     */ 
/*     */   
/*     */   public RequestHandledEvent(Object source, String sessionId, String userName, long processingTimeMillis, Throwable failureCause) {
/*  79 */     this(source, sessionId, userName, processingTimeMillis);
/*  80 */     this.failureCause = failureCause;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getProcessingTimeMillis() {
/*  88 */     return this.processingTimeMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSessionId() {
/*  95 */     return this.sessionId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUserName() {
/* 104 */     return this.userName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean wasFailure() {
/* 111 */     return (this.failureCause != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getFailureCause() {
/* 118 */     return this.failureCause;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getShortDescription() {
/* 127 */     StringBuilder sb = new StringBuilder();
/* 128 */     sb.append("session=[").append(this.sessionId).append("]; ");
/* 129 */     sb.append("user=[").append(this.userName).append("]; ");
/* 130 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 138 */     StringBuilder sb = new StringBuilder();
/* 139 */     sb.append("session=[").append(this.sessionId).append("]; ");
/* 140 */     sb.append("user=[").append(this.userName).append("]; ");
/* 141 */     sb.append("time=[").append(this.processingTimeMillis).append("ms]; ");
/* 142 */     sb.append("status=[");
/* 143 */     if (!wasFailure()) {
/* 144 */       sb.append("OK");
/*     */     } else {
/*     */       
/* 147 */       sb.append("failed: ").append(this.failureCause);
/*     */     } 
/* 149 */     sb.append(']');
/* 150 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 155 */     return "RequestHandledEvent: " + getDescription();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\support\RequestHandledEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */