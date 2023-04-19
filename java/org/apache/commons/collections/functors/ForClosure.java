/*     */ package org.apache.commons.collections.functors;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import org.apache.commons.collections.Closure;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ForClosure
/*     */   implements Closure, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -1190120533393621674L;
/*     */   private final int iCount;
/*     */   private final Closure iClosure;
/*     */   
/*     */   public static Closure getInstance(int count, Closure closure) {
/*  65 */     if (count <= 0 || closure == null) {
/*  66 */       return NOPClosure.INSTANCE;
/*     */     }
/*  68 */     if (count == 1) {
/*  69 */       return closure;
/*     */     }
/*  71 */     return new ForClosure(count, closure);
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
/*     */   public ForClosure(int count, Closure closure) {
/*  83 */     this.iCount = count;
/*  84 */     this.iClosure = closure;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(Object input) {
/*  93 */     for (int i = 0; i < this.iCount; i++) {
/*  94 */       this.iClosure.execute(input);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Closure getClosure() {
/* 105 */     return this.iClosure;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCount() {
/* 115 */     return this.iCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream os) throws IOException {
/* 123 */     FunctorUtils.checkUnsafeSerialization(ForClosure.class);
/* 124 */     os.defaultWriteObject();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream is) throws ClassNotFoundException, IOException {
/* 132 */     FunctorUtils.checkUnsafeSerialization(ForClosure.class);
/* 133 */     is.defaultReadObject();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\functors\ForClosure.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */