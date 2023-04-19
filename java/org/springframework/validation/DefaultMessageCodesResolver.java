/*     */ package org.springframework.validation;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ public class DefaultMessageCodesResolver
/*     */   implements MessageCodesResolver, Serializable
/*     */ {
/*     */   public static final String CODE_SEPARATOR = ".";
/*  98 */   private static final MessageCodeFormatter DEFAULT_FORMATTER = Format.PREFIX_ERROR_CODE;
/*     */ 
/*     */   
/* 101 */   private String prefix = "";
/*     */   
/* 103 */   private MessageCodeFormatter formatter = DEFAULT_FORMATTER;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrefix(String prefix) {
/* 112 */     this.prefix = (prefix != null) ? prefix : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getPrefix() {
/* 120 */     return this.prefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessageCodeFormatter(MessageCodeFormatter formatter) {
/* 130 */     this.formatter = (formatter != null) ? formatter : DEFAULT_FORMATTER;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] resolveMessageCodes(String errorCode, String objectName) {
/* 136 */     return resolveMessageCodes(errorCode, objectName, "", null);
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
/*     */ 
/*     */   
/*     */   public String[] resolveMessageCodes(String errorCode, String objectName, String field, Class<?> fieldType) {
/* 150 */     Set<String> codeList = new LinkedHashSet<String>();
/* 151 */     List<String> fieldList = new ArrayList<String>();
/* 152 */     buildFieldList(field, fieldList);
/* 153 */     addCodes(codeList, errorCode, objectName, fieldList);
/* 154 */     int dotIndex = field.lastIndexOf('.');
/* 155 */     if (dotIndex != -1) {
/* 156 */       buildFieldList(field.substring(dotIndex + 1), fieldList);
/*     */     }
/* 158 */     addCodes(codeList, errorCode, null, fieldList);
/* 159 */     if (fieldType != null) {
/* 160 */       addCode(codeList, errorCode, null, fieldType.getName());
/*     */     }
/* 162 */     addCode(codeList, errorCode, null, null);
/* 163 */     return StringUtils.toStringArray(codeList);
/*     */   }
/*     */   
/*     */   private void addCodes(Collection<String> codeList, String errorCode, String objectName, Iterable<String> fields) {
/* 167 */     for (String field : fields) {
/* 168 */       addCode(codeList, errorCode, objectName, field);
/*     */     }
/*     */   }
/*     */   
/*     */   private void addCode(Collection<String> codeList, String errorCode, String objectName, String field) {
/* 173 */     codeList.add(postProcessMessageCode(this.formatter.format(errorCode, objectName, field)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void buildFieldList(String field, List<String> fieldList) {
/* 181 */     fieldList.add(field);
/* 182 */     String plainField = field;
/* 183 */     int keyIndex = plainField.lastIndexOf('[');
/* 184 */     while (keyIndex != -1) {
/* 185 */       int endKeyIndex = plainField.indexOf(']', keyIndex);
/* 186 */       if (endKeyIndex != -1) {
/* 187 */         plainField = plainField.substring(0, keyIndex) + plainField.substring(endKeyIndex + 1);
/* 188 */         fieldList.add(plainField);
/* 189 */         keyIndex = plainField.lastIndexOf('[');
/*     */         continue;
/*     */       } 
/* 192 */       keyIndex = -1;
/*     */     } 
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
/*     */   protected String postProcessMessageCode(String code) {
/* 205 */     return getPrefix() + code;
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
/*     */   
/*     */   public enum Format
/*     */     implements MessageCodeFormatter
/*     */   {
/* 220 */     PREFIX_ERROR_CODE
/*     */     {
/*     */       public String format(String errorCode, String objectName, String field) {
/* 223 */         return null.toDelimitedString(new String[] { errorCode, objectName, field
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             });
/*     */       }
/*     */     },
/* 231 */     POSTFIX_ERROR_CODE
/*     */     {
/*     */       public String format(String errorCode, String objectName, String field) {
/* 234 */         return null.toDelimitedString(new String[] { objectName, field, errorCode });
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static String toDelimitedString(String... elements) {
/* 244 */       StringBuilder rtn = new StringBuilder();
/* 245 */       for (String element : elements) {
/* 246 */         if (StringUtils.hasLength(element)) {
/* 247 */           rtn.append((rtn.length() == 0) ? "" : ".");
/* 248 */           rtn.append(element);
/*     */         } 
/*     */       } 
/* 251 */       return rtn.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\validation\DefaultMessageCodesResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */