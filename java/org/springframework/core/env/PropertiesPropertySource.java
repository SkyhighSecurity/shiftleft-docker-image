/*    */ package org.springframework.core.env;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.Properties;
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
/*    */ public class PropertiesPropertySource
/*    */   extends MapPropertySource
/*    */ {
/*    */   public PropertiesPropertySource(String name, Properties source) {
/* 40 */     super(name, source);
/*    */   }
/*    */   
/*    */   protected PropertiesPropertySource(String name, Map<String, Object> source) {
/* 44 */     super(name, source);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\env\PropertiesPropertySource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */