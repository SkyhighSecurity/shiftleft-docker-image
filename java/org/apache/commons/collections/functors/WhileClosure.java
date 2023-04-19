/*     */ package org.apache.commons.collections.functors;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import org.apache.commons.collections.Closure;
/*     */ import org.apache.commons.collections.Predicate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WhileClosure
/*     */   implements Closure, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -3110538116913760108L;
/*     */   private final Predicate iPredicate;
/*     */   private final Closure iClosure;
/*     */   private final boolean iDoLoop;
/*     */   
/*     */   public static Closure getInstance(Predicate predicate, Closure closure, boolean doLoop) {
/*  68 */     if (predicate == null) {
/*  69 */       throw new IllegalArgumentException("Predicate must not be null");
/*     */     }
/*  71 */     if (closure == null) {
/*  72 */       throw new IllegalArgumentException("Closure must not be null");
/*     */     }
/*  74 */     return new WhileClosure(predicate, closure, doLoop);
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
/*     */   public WhileClosure(Predicate predicate, Closure closure, boolean doLoop) {
/*  87 */     this.iPredicate = predicate;
/*  88 */     this.iClosure = closure;
/*  89 */     this.iDoLoop = doLoop;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(Object input) {
/*  98 */     if (this.iDoLoop) {
/*  99 */       this.iClosure.execute(input);
/*     */     }
/* 101 */     while (this.iPredicate.evaluate(input)) {
/* 102 */       this.iClosure.execute(input);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Predicate getPredicate() {
/* 113 */     return this.iPredicate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Closure getClosure() {
/* 123 */     return this.iClosure;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDoLoop() {
/* 133 */     return this.iDoLoop;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream os) throws IOException {
/* 141 */     FunctorUtils.checkUnsafeSerialization(WhileClosure.class);
/* 142 */     os.defaultWriteObject();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream is) throws ClassNotFoundException, IOException {
/* 150 */     FunctorUtils.checkUnsafeSerialization(WhileClosure.class);
/* 151 */     is.defaultReadObject();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\functors\WhileClosure.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */