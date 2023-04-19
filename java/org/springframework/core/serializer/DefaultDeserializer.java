/*    */ package org.springframework.core.serializer;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import org.springframework.core.ConfigurableObjectInputStream;
/*    */ import org.springframework.core.NestedIOException;
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
/*    */ public class DefaultDeserializer
/*    */   implements Deserializer<Object>
/*    */ {
/*    */   private final ClassLoader classLoader;
/*    */   
/*    */   public DefaultDeserializer() {
/* 46 */     this.classLoader = null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultDeserializer(ClassLoader classLoader) {
/* 56 */     this.classLoader = classLoader;
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
/*    */   public Object deserialize(InputStream inputStream) throws IOException {
/* 68 */     ConfigurableObjectInputStream configurableObjectInputStream = new ConfigurableObjectInputStream(inputStream, this.classLoader);
/*    */     try {
/* 70 */       return configurableObjectInputStream.readObject();
/*    */     }
/* 72 */     catch (ClassNotFoundException ex) {
/* 73 */       throw new NestedIOException("Failed to deserialize object type", ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\serializer\DefaultDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */