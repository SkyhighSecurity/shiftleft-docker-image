/*    */ package org.springframework.core.io.support;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.springframework.core.env.PropertySource;
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
/*    */ public class DefaultPropertySourceFactory
/*    */   implements PropertySourceFactory
/*    */ {
/*    */   public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
/* 36 */     return (name != null) ? (PropertySource<?>)new ResourcePropertySource(name, resource) : (PropertySource<?>)new ResourcePropertySource(resource);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\io\support\DefaultPropertySourceFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */