/*    */ package org.springframework.context.annotation;
/*    */ 
/*    */ import org.springframework.beans.factory.parsing.Location;
/*    */ import org.springframework.beans.factory.parsing.ProblemReporter;
/*    */ import org.springframework.core.type.MethodMetadata;
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
/*    */ abstract class ConfigurationMethod
/*    */ {
/*    */   protected final MethodMetadata metadata;
/*    */   protected final ConfigurationClass configurationClass;
/*    */   
/*    */   public ConfigurationMethod(MethodMetadata metadata, ConfigurationClass configurationClass) {
/* 35 */     this.metadata = metadata;
/* 36 */     this.configurationClass = configurationClass;
/*    */   }
/*    */ 
/*    */   
/*    */   public MethodMetadata getMetadata() {
/* 41 */     return this.metadata;
/*    */   }
/*    */   
/*    */   public ConfigurationClass getConfigurationClass() {
/* 45 */     return this.configurationClass;
/*    */   }
/*    */   
/*    */   public Location getResourceLocation() {
/* 49 */     return new Location(this.configurationClass.getResource(), this.metadata);
/*    */   }
/*    */   
/*    */   String getFullyQualifiedMethodName() {
/* 53 */     return this.metadata.getDeclaringClassName() + "#" + this.metadata.getMethodName();
/*    */   }
/*    */   
/*    */   static String getShortMethodName(String fullyQualifiedMethodName) {
/* 57 */     return fullyQualifiedMethodName.substring(fullyQualifiedMethodName.indexOf('#') + 1);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void validate(ProblemReporter problemReporter) {}
/*    */ 
/*    */   
/*    */   public String toString() {
/* 66 */     return String.format("[%s:name=%s,declaringClass=%s]", new Object[] {
/* 67 */           getClass().getSimpleName(), getMetadata().getMethodName(), getMetadata().getDeclaringClassName()
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\annotation\ConfigurationMethod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */