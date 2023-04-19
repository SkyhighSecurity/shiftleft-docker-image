/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.springframework.context.MessageSourceResolvable;
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
/*     */ public class DefaultMessageSourceResolvable
/*     */   implements MessageSourceResolvable, Serializable
/*     */ {
/*     */   private final String[] codes;
/*     */   private final Object[] arguments;
/*     */   private final String defaultMessage;
/*     */   
/*     */   public DefaultMessageSourceResolvable(String code) {
/*  49 */     this(new String[] { code }, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultMessageSourceResolvable(String[] codes) {
/*  57 */     this(codes, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultMessageSourceResolvable(String[] codes, String defaultMessage) {
/*  66 */     this(codes, null, defaultMessage);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultMessageSourceResolvable(String[] codes, Object[] arguments) {
/*  75 */     this(codes, arguments, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultMessageSourceResolvable(String[] codes, Object[] arguments, String defaultMessage) {
/*  85 */     this.codes = codes;
/*  86 */     this.arguments = arguments;
/*  87 */     this.defaultMessage = defaultMessage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultMessageSourceResolvable(MessageSourceResolvable resolvable) {
/*  95 */     this(resolvable.getCodes(), resolvable.getArguments(), resolvable.getDefaultMessage());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCode() {
/* 104 */     return (this.codes != null && this.codes.length > 0) ? this.codes[this.codes.length - 1] : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getCodes() {
/* 109 */     return this.codes;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] getArguments() {
/* 114 */     return this.arguments;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDefaultMessage() {
/* 119 */     return this.defaultMessage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final String resolvableToString() {
/* 128 */     StringBuilder result = new StringBuilder();
/* 129 */     result.append("codes [").append(StringUtils.arrayToDelimitedString((Object[])this.codes, ","));
/* 130 */     result.append("]; arguments [").append(StringUtils.arrayToDelimitedString(this.arguments, ","));
/* 131 */     result.append("]; default message [").append(this.defaultMessage).append(']');
/* 132 */     return result.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 143 */     return getClass().getName() + ": " + resolvableToString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 149 */     if (this == other) {
/* 150 */       return true;
/*     */     }
/* 152 */     if (!(other instanceof MessageSourceResolvable)) {
/* 153 */       return false;
/*     */     }
/* 155 */     MessageSourceResolvable otherResolvable = (MessageSourceResolvable)other;
/* 156 */     return (ObjectUtils.nullSafeEquals(getCodes(), otherResolvable.getCodes()) && 
/* 157 */       ObjectUtils.nullSafeEquals(getArguments(), otherResolvable.getArguments()) && 
/* 158 */       ObjectUtils.nullSafeEquals(getDefaultMessage(), otherResolvable.getDefaultMessage()));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 163 */     int hashCode = ObjectUtils.nullSafeHashCode((Object[])getCodes());
/* 164 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(getArguments());
/* 165 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(getDefaultMessage());
/* 166 */     return hashCode;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\support\DefaultMessageSourceResolvable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */