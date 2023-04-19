/*     */ package org.springframework.web.bind;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UnsatisfiedServletRequestParameterException
/*     */   extends ServletRequestBindingException
/*     */ {
/*     */   private final List<String[]> paramConditions;
/*     */   private final Map<String, String[]> actualParams;
/*     */   
/*     */   public UnsatisfiedServletRequestParameterException(String[] paramConditions, Map<String, String[]> actualParams) {
/*  52 */     super("");
/*  53 */     this.paramConditions = Arrays.asList(new String[][] { paramConditions });
/*  54 */     this.actualParams = actualParams;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnsatisfiedServletRequestParameterException(List<String[]> paramConditions, Map<String, String[]> actualParams) {
/*  66 */     super("");
/*  67 */     Assert.notEmpty(paramConditions, "Parameter conditions must not be empty");
/*  68 */     this.paramConditions = paramConditions;
/*  69 */     this.actualParams = actualParams;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage() {
/*  75 */     StringBuilder sb = new StringBuilder("Parameter conditions ");
/*  76 */     int i = 0;
/*  77 */     for (String[] conditions : this.paramConditions) {
/*  78 */       if (i > 0) {
/*  79 */         sb.append(" OR ");
/*     */       }
/*  81 */       sb.append("\"");
/*  82 */       sb.append(StringUtils.arrayToDelimitedString((Object[])conditions, ", "));
/*  83 */       sb.append("\"");
/*  84 */       i++;
/*     */     } 
/*  86 */     sb.append(" not met for actual request parameters: ");
/*  87 */     sb.append(requestParameterMapToString(this.actualParams));
/*  88 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String[] getParamConditions() {
/*  97 */     return this.paramConditions.get(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final List<String[]> getParamConditionGroups() {
/* 106 */     return this.paramConditions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Map<String, String[]> getActualParams() {
/* 114 */     return this.actualParams;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String requestParameterMapToString(Map<String, String[]> actualParams) {
/* 119 */     StringBuilder result = new StringBuilder();
/* 120 */     for (Iterator<Map.Entry<String, String[]>> it = actualParams.entrySet().iterator(); it.hasNext(); ) {
/* 121 */       Map.Entry<String, String[]> entry = it.next();
/* 122 */       result.append(entry.getKey()).append('=').append(ObjectUtils.nullSafeToString((Object[])entry.getValue()));
/* 123 */       if (it.hasNext()) {
/* 124 */         result.append(", ");
/*     */       }
/*     */     } 
/* 127 */     return result.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\bind\UnsatisfiedServletRequestParameterException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */