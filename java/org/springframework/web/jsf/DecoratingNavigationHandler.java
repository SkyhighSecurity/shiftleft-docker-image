/*     */ package org.springframework.web.jsf;
/*     */ 
/*     */ import javax.faces.application.NavigationHandler;
/*     */ import javax.faces.context.FacesContext;
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
/*     */ public abstract class DecoratingNavigationHandler
/*     */   extends NavigationHandler
/*     */ {
/*     */   private NavigationHandler decoratedNavigationHandler;
/*     */   
/*     */   protected DecoratingNavigationHandler() {}
/*     */   
/*     */   protected DecoratingNavigationHandler(NavigationHandler originalNavigationHandler) {
/*  54 */     this.decoratedNavigationHandler = originalNavigationHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final NavigationHandler getDecoratedNavigationHandler() {
/*  62 */     return this.decoratedNavigationHandler;
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
/*     */   public final void handleNavigation(FacesContext facesContext, String fromAction, String outcome) {
/*  74 */     handleNavigation(facesContext, fromAction, outcome, this.decoratedNavigationHandler);
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
/*     */   public abstract void handleNavigation(FacesContext paramFacesContext, String paramString1, String paramString2, NavigationHandler paramNavigationHandler);
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
/*     */   protected final void callNextHandlerInChain(FacesContext facesContext, String fromAction, String outcome, NavigationHandler originalNavigationHandler) {
/* 132 */     NavigationHandler decoratedNavigationHandler = getDecoratedNavigationHandler();
/*     */     
/* 134 */     if (decoratedNavigationHandler instanceof DecoratingNavigationHandler) {
/*     */ 
/*     */       
/* 137 */       DecoratingNavigationHandler decHandler = (DecoratingNavigationHandler)decoratedNavigationHandler;
/* 138 */       decHandler.handleNavigation(facesContext, fromAction, outcome, originalNavigationHandler);
/*     */     }
/* 140 */     else if (decoratedNavigationHandler != null) {
/*     */ 
/*     */ 
/*     */       
/* 144 */       decoratedNavigationHandler.handleNavigation(facesContext, fromAction, outcome);
/*     */     }
/* 146 */     else if (originalNavigationHandler != null) {
/*     */ 
/*     */       
/* 149 */       originalNavigationHandler.handleNavigation(facesContext, fromAction, outcome);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\jsf\DecoratingNavigationHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */