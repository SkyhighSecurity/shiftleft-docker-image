/*    */ package org.springframework.expression.spel.ast;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.springframework.core.convert.TypeDescriptor;
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ public class FormatHelper
/*    */ {
/*    */   public static String formatMethodForMessage(String name, List<TypeDescriptor> argumentTypes) {
/* 38 */     StringBuilder sb = new StringBuilder(name);
/* 39 */     sb.append("(");
/* 40 */     for (int i = 0; i < argumentTypes.size(); i++) {
/* 41 */       if (i > 0) {
/* 42 */         sb.append(",");
/*    */       }
/* 44 */       TypeDescriptor typeDescriptor = argumentTypes.get(i);
/* 45 */       if (typeDescriptor != null) {
/* 46 */         sb.append(formatClassNameForMessage(typeDescriptor.getType()));
/*    */       } else {
/*    */         
/* 49 */         sb.append(formatClassNameForMessage(null));
/*    */       } 
/*    */     } 
/* 52 */     sb.append(")");
/* 53 */     return sb.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String formatClassNameForMessage(Class<?> clazz) {
/* 64 */     return (clazz != null) ? ClassUtils.getQualifiedName(clazz) : "null";
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\expression\spel\ast\FormatHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */