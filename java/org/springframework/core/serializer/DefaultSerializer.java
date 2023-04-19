/*    */ package org.springframework.core.serializer;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.io.OutputStream;
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
/*    */ public class DefaultSerializer
/*    */   implements Serializer<Object>
/*    */ {
/*    */   public void serialize(Object object, OutputStream outputStream) throws IOException {
/* 41 */     if (!(object instanceof java.io.Serializable)) {
/* 42 */       throw new IllegalArgumentException(getClass().getSimpleName() + " requires a Serializable payload but received an object of type [" + object
/* 43 */           .getClass().getName() + "]");
/*    */     }
/* 45 */     ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
/* 46 */     objectOutputStream.writeObject(object);
/* 47 */     objectOutputStream.flush();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\serializer\DefaultSerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */