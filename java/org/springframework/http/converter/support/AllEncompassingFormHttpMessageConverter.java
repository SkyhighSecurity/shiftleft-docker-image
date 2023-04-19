/*    */ package org.springframework.http.converter.support;
/*    */ 
/*    */ import org.springframework.http.converter.FormHttpMessageConverter;
/*    */ import org.springframework.http.converter.HttpMessageConverter;
/*    */ import org.springframework.http.converter.json.GsonHttpMessageConverter;
/*    */ import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
/*    */ import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
/*    */ import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
/*    */ import org.springframework.http.converter.xml.SourceHttpMessageConverter;
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
/*    */ public class AllEncompassingFormHttpMessageConverter
/*    */   extends FormHttpMessageConverter
/*    */ {
/* 40 */   private static final boolean jaxb2Present = ClassUtils.isPresent("javax.xml.bind.Binder", AllEncompassingFormHttpMessageConverter.class
/* 41 */       .getClassLoader());
/*    */   
/* 43 */   private static final boolean jackson2Present = (
/* 44 */     ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", AllEncompassingFormHttpMessageConverter.class
/* 45 */       .getClassLoader()) && 
/* 46 */     ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", AllEncompassingFormHttpMessageConverter.class
/* 47 */       .getClassLoader()));
/*    */ 
/*    */   
/* 50 */   private static final boolean jackson2XmlPresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.xml.XmlMapper", AllEncompassingFormHttpMessageConverter.class
/* 51 */       .getClassLoader());
/*    */ 
/*    */   
/* 54 */   private static final boolean gsonPresent = ClassUtils.isPresent("com.google.gson.Gson", AllEncompassingFormHttpMessageConverter.class
/* 55 */       .getClassLoader());
/*    */ 
/*    */   
/*    */   public AllEncompassingFormHttpMessageConverter() {
/* 59 */     addPartConverter((HttpMessageConverter)new SourceHttpMessageConverter());
/*    */     
/* 61 */     if (jaxb2Present && !jackson2XmlPresent) {
/* 62 */       addPartConverter((HttpMessageConverter)new Jaxb2RootElementHttpMessageConverter());
/*    */     }
/*    */     
/* 65 */     if (jackson2Present) {
/* 66 */       addPartConverter((HttpMessageConverter)new MappingJackson2HttpMessageConverter());
/*    */     }
/* 68 */     else if (gsonPresent) {
/* 69 */       addPartConverter((HttpMessageConverter)new GsonHttpMessageConverter());
/*    */     } 
/*    */     
/* 72 */     if (jackson2XmlPresent)
/* 73 */       addPartConverter((HttpMessageConverter)new MappingJackson2XmlHttpMessageConverter()); 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\http\converter\support\AllEncompassingFormHttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */