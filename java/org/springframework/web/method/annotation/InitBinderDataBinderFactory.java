/*    */ package org.springframework.web.method.annotation;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import org.springframework.util.ObjectUtils;
/*    */ import org.springframework.web.bind.WebDataBinder;
/*    */ import org.springframework.web.bind.annotation.InitBinder;
/*    */ import org.springframework.web.bind.support.DefaultDataBinderFactory;
/*    */ import org.springframework.web.bind.support.WebBindingInitializer;
/*    */ import org.springframework.web.context.request.NativeWebRequest;
/*    */ import org.springframework.web.method.HandlerMethod;
/*    */ import org.springframework.web.method.support.InvocableHandlerMethod;
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
/*    */ public class InitBinderDataBinderFactory
/*    */   extends DefaultDataBinderFactory
/*    */ {
/*    */   private final List<InvocableHandlerMethod> binderMethods;
/*    */   
/*    */   public InitBinderDataBinderFactory(List<InvocableHandlerMethod> binderMethods, WebBindingInitializer initializer) {
/* 48 */     super(initializer);
/* 49 */     this.binderMethods = (binderMethods != null) ? binderMethods : Collections.<InvocableHandlerMethod>emptyList();
/*    */   }
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
/*    */   public void initBinder(WebDataBinder dataBinder, NativeWebRequest request) throws Exception {
/* 62 */     for (InvocableHandlerMethod binderMethod : this.binderMethods) {
/* 63 */       if (isBinderMethodApplicable((HandlerMethod)binderMethod, dataBinder)) {
/* 64 */         Object returnValue = binderMethod.invokeForRequest(request, null, new Object[] { dataBinder });
/* 65 */         if (returnValue != null) {
/* 66 */           throw new IllegalStateException("@InitBinder methods must not return a value (should be void): " + binderMethod);
/*    */         }
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean isBinderMethodApplicable(HandlerMethod initBinderMethod, WebDataBinder dataBinder) {
/* 79 */     InitBinder ann = (InitBinder)initBinderMethod.getMethodAnnotation(InitBinder.class);
/* 80 */     String[] names = ann.value();
/* 81 */     return (ObjectUtils.isEmpty((Object[])names) || ObjectUtils.containsElement((Object[])names, dataBinder.getObjectName()));
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\method\annotation\InitBinderDataBinderFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */