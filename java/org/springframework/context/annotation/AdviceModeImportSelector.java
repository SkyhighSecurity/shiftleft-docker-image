/*    */ package org.springframework.context.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import org.springframework.core.GenericTypeResolver;
/*    */ import org.springframework.core.annotation.AnnotationAttributes;
/*    */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*    */ import org.springframework.core.type.AnnotationMetadata;
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
/*    */ public abstract class AdviceModeImportSelector<A extends Annotation>
/*    */   implements ImportSelector
/*    */ {
/*    */   public static final String DEFAULT_ADVICE_MODE_ATTRIBUTE_NAME = "mode";
/*    */   
/*    */   protected String getAdviceModeAttributeName() {
/* 48 */     return "mode";
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
/*    */   public final String[] selectImports(AnnotationMetadata importingClassMetadata) {
/* 65 */     Class<?> annType = GenericTypeResolver.resolveTypeArgument(getClass(), AdviceModeImportSelector.class);
/* 66 */     AnnotationAttributes attributes = AnnotationConfigUtils.attributesFor((AnnotatedTypeMetadata)importingClassMetadata, annType);
/* 67 */     if (attributes == null) {
/* 68 */       throw new IllegalArgumentException(String.format("@%s is not present on importing class '%s' as expected", new Object[] { annType
/*    */               
/* 70 */               .getSimpleName(), importingClassMetadata.getClassName() }));
/*    */     }
/*    */     
/* 73 */     AdviceMode adviceMode = (AdviceMode)attributes.getEnum(getAdviceModeAttributeName());
/* 74 */     String[] imports = selectImports(adviceMode);
/* 75 */     if (imports == null) {
/* 76 */       throw new IllegalArgumentException("Unknown AdviceMode: " + adviceMode);
/*    */     }
/* 78 */     return imports;
/*    */   }
/*    */   
/*    */   protected abstract String[] selectImports(AdviceMode paramAdviceMode);
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\annotation\AdviceModeImportSelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */