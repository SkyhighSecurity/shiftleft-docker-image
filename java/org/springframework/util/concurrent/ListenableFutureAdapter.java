/*    */ package org.springframework.util.concurrent;
/*    */ 
/*    */ import java.util.concurrent.ExecutionException;
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
/*    */ public abstract class ListenableFutureAdapter<T, S>
/*    */   extends FutureAdapter<T, S>
/*    */   implements ListenableFuture<T>
/*    */ {
/*    */   protected ListenableFutureAdapter(ListenableFuture<S> adaptee) {
/* 40 */     super(adaptee);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void addCallback(ListenableFutureCallback<? super T> callback) {
/* 46 */     addCallback(callback, callback);
/*    */   }
/*    */ 
/*    */   
/*    */   public void addCallback(final SuccessCallback<? super T> successCallback, final FailureCallback failureCallback) {
/* 51 */     ListenableFuture<S> listenableAdaptee = (ListenableFuture<S>)getAdaptee();
/* 52 */     listenableAdaptee.addCallback(new ListenableFutureCallback<S>()
/*    */         {
/*    */           public void onSuccess(S result) {
/*    */             T adapted;
/*    */             try {
/* 57 */               adapted = ListenableFutureAdapter.this.adaptInternal(result);
/*    */             }
/* 59 */             catch (ExecutionException ex) {
/* 60 */               Throwable cause = ex.getCause();
/* 61 */               onFailure((cause != null) ? cause : ex);
/*    */               
/*    */               return;
/* 64 */             } catch (Throwable ex) {
/* 65 */               onFailure(ex);
/*    */               return;
/*    */             } 
/* 68 */             successCallback.onSuccess(adapted);
/*    */           }
/*    */           
/*    */           public void onFailure(Throwable ex) {
/* 72 */             failureCallback.onFailure(ex);
/*    */           }
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\concurrent\ListenableFutureAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */