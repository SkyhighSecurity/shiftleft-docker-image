/*    */ package org.apache.commons.collections.functors;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.io.Serializable;
/*    */ import org.apache.commons.collections.Transformer;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CloneTransformer
/*    */   implements Transformer, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -8188742709499652567L;
/* 52 */   public static final Transformer INSTANCE = new CloneTransformer();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Transformer getInstance() {
/* 61 */     return INSTANCE;
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
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object transform(Object input) {
/* 78 */     if (input == null) {
/* 79 */       return null;
/*    */     }
/* 81 */     return PrototypeFactory.getInstance(input).create();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void writeObject(ObjectOutputStream os) throws IOException {
/* 89 */     FunctorUtils.checkUnsafeSerialization(CloneTransformer.class);
/* 90 */     os.defaultWriteObject();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void readObject(ObjectInputStream is) throws ClassNotFoundException, IOException {
/* 98 */     FunctorUtils.checkUnsafeSerialization(CloneTransformer.class);
/* 99 */     is.defaultReadObject();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\functors\CloneTransformer.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */