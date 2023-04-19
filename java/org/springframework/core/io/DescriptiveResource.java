/*    */ package org.springframework.core.io;
/*    */ 
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
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
/*    */ public class DescriptiveResource
/*    */   extends AbstractResource
/*    */ {
/*    */   private final String description;
/*    */   
/*    */   public DescriptiveResource(String description) {
/* 43 */     this.description = (description != null) ? description : "";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean exists() {
/* 49 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isReadable() {
/* 54 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public InputStream getInputStream() throws IOException {
/* 59 */     throw new FileNotFoundException(
/* 60 */         getDescription() + " cannot be opened because it does not point to a readable resource");
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDescription() {
/* 65 */     return this.description;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 74 */     return (obj == this || (obj instanceof DescriptiveResource && ((DescriptiveResource)obj).description
/* 75 */       .equals(this.description)));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 83 */     return this.description.hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\io\DescriptiveResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */