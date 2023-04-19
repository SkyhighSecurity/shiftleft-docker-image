/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.NoSuchElementException;
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
/*     */ @GwtCompatible
/*     */ public abstract class AbstractIterator<T>
/*     */   extends UnmodifiableIterator<T>
/*     */ {
/*     */   private T next;
/*     */   
/*     */   protected abstract T computeNext();
/*     */   
/*  61 */   private State state = State.NOT_READY;
/*     */   
/*     */   private enum State
/*     */   {
/*  65 */     READY,
/*     */ 
/*     */     
/*  68 */     NOT_READY,
/*     */ 
/*     */     
/*  71 */     DONE,
/*     */ 
/*     */     
/*  74 */     FAILED;
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
/*     */   protected final T endOfData() {
/* 117 */     this.state = State.DONE;
/* 118 */     return null;
/*     */   }
/*     */   
/*     */   public final boolean hasNext() {
/* 122 */     Preconditions.checkState((this.state != State.FAILED));
/* 123 */     switch (this.state) {
/*     */       case DONE:
/* 125 */         return false;
/*     */       case READY:
/* 127 */         return true;
/*     */     } 
/*     */     
/* 130 */     return tryToComputeNext();
/*     */   }
/*     */   
/*     */   private boolean tryToComputeNext() {
/* 134 */     this.state = State.FAILED;
/* 135 */     this.next = computeNext();
/* 136 */     if (this.state != State.DONE) {
/* 137 */       this.state = State.READY;
/* 138 */       return true;
/*     */     } 
/* 140 */     return false;
/*     */   }
/*     */   
/*     */   public final T next() {
/* 144 */     if (!hasNext()) {
/* 145 */       throw new NoSuchElementException();
/*     */     }
/* 147 */     this.state = State.NOT_READY;
/* 148 */     return this.next;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final T peek() {
/* 159 */     if (!hasNext()) {
/* 160 */       throw new NoSuchElementException();
/*     */     }
/* 162 */     return this.next;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\AbstractIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */