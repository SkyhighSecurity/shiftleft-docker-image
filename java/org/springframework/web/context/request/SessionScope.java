/*     */ package org.springframework.web.context.request;
/*     */ 
/*     */ import org.springframework.beans.factory.ObjectFactory;
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
/*     */ public class SessionScope
/*     */   extends AbstractRequestAttributesScope
/*     */ {
/*     */   private final int scope;
/*     */   
/*     */   public SessionScope() {
/*  58 */     this.scope = 1;
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
/*     */ 
/*     */   
/*     */   public SessionScope(boolean globalSession) {
/*  75 */     this.scope = globalSession ? 2 : 1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getScope() {
/*  81 */     return this.scope;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getConversationId() {
/*  86 */     return RequestContextHolder.currentRequestAttributes().getSessionId();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object get(String name, ObjectFactory<?> objectFactory) {
/*  91 */     Object mutex = RequestContextHolder.currentRequestAttributes().getSessionMutex();
/*  92 */     synchronized (mutex) {
/*  93 */       return super.get(name, objectFactory);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Object remove(String name) {
/*  99 */     Object mutex = RequestContextHolder.currentRequestAttributes().getSessionMutex();
/* 100 */     synchronized (mutex) {
/* 101 */       return super.remove(name);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\context\request\SessionScope.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */