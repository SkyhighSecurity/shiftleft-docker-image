/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ public abstract class PropertyMatches
/*     */ {
/*     */   public static final int DEFAULT_MAX_DISTANCE = 2;
/*     */   private final String propertyName;
/*     */   private String[] possibleMatches;
/*     */   
/*     */   public static PropertyMatches forProperty(String propertyName, Class<?> beanClass) {
/*  58 */     return forProperty(propertyName, beanClass, 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PropertyMatches forProperty(String propertyName, Class<?> beanClass, int maxDistance) {
/*  68 */     return new BeanPropertyMatches(propertyName, beanClass, maxDistance);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PropertyMatches forField(String propertyName, Class<?> beanClass) {
/*  77 */     return forField(propertyName, beanClass, 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PropertyMatches forField(String propertyName, Class<?> beanClass, int maxDistance) {
/*  87 */     return new FieldPropertyMatches(propertyName, beanClass, maxDistance);
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
/*     */   
/*     */   private PropertyMatches(String propertyName, String[] possibleMatches) {
/* 102 */     this.propertyName = propertyName;
/* 103 */     this.possibleMatches = possibleMatches;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPropertyName() {
/* 111 */     return this.propertyName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getPossibleMatches() {
/* 118 */     return this.possibleMatches;
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
/*     */   protected void appendHintMessage(StringBuilder msg) {
/* 131 */     msg.append("Did you mean ");
/* 132 */     for (int i = 0; i < this.possibleMatches.length; i++) {
/* 133 */       msg.append('\'');
/* 134 */       msg.append(this.possibleMatches[i]);
/* 135 */       if (i < this.possibleMatches.length - 2) {
/* 136 */         msg.append("', ");
/*     */       }
/* 138 */       else if (i == this.possibleMatches.length - 2) {
/* 139 */         msg.append("', or ");
/*     */       } 
/*     */     } 
/* 142 */     msg.append("'?");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int calculateStringDistance(String s1, String s2) {
/* 153 */     if (s1.isEmpty()) {
/* 154 */       return s2.length();
/*     */     }
/* 156 */     if (s2.isEmpty()) {
/* 157 */       return s1.length();
/*     */     }
/*     */     
/* 160 */     int[][] d = new int[s1.length() + 1][s2.length() + 1];
/* 161 */     for (int k = 0; k <= s1.length(); k++) {
/* 162 */       d[k][0] = k;
/*     */     }
/* 164 */     for (int j = 0; j <= s2.length(); j++) {
/* 165 */       d[0][j] = j;
/*     */     }
/*     */     
/* 168 */     for (int i = 1; i <= s1.length(); i++) {
/* 169 */       char c1 = s1.charAt(i - 1);
/* 170 */       for (int m = 1; m <= s2.length(); m++) {
/*     */         int cost;
/* 172 */         char c2 = s2.charAt(m - 1);
/* 173 */         if (c1 == c2) {
/* 174 */           cost = 0;
/*     */         } else {
/*     */           
/* 177 */           cost = 1;
/*     */         } 
/* 179 */         d[i][m] = Math.min(Math.min(d[i - 1][m] + 1, d[i][m - 1] + 1), d[i - 1][m - 1] + cost);
/*     */       } 
/*     */     } 
/*     */     
/* 183 */     return d[s1.length()][s2.length()];
/*     */   }
/*     */   
/*     */   public abstract String buildErrorMessage();
/*     */   
/*     */   private static class BeanPropertyMatches
/*     */     extends PropertyMatches
/*     */   {
/*     */     public BeanPropertyMatches(String propertyName, Class<?> beanClass, int maxDistance) {
/* 192 */       super(propertyName, 
/* 193 */           calculateMatches(propertyName, BeanUtils.getPropertyDescriptors(beanClass), maxDistance));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static String[] calculateMatches(String name, PropertyDescriptor[] descriptors, int maxDistance) {
/* 204 */       List<String> candidates = new ArrayList<String>();
/* 205 */       for (PropertyDescriptor pd : descriptors) {
/* 206 */         if (pd.getWriteMethod() != null) {
/* 207 */           String possibleAlternative = pd.getName();
/* 208 */           if (PropertyMatches.calculateStringDistance(name, possibleAlternative) <= maxDistance) {
/* 209 */             candidates.add(possibleAlternative);
/*     */           }
/*     */         } 
/*     */       } 
/* 213 */       Collections.sort(candidates);
/* 214 */       return StringUtils.toStringArray(candidates);
/*     */     }
/*     */ 
/*     */     
/*     */     public String buildErrorMessage() {
/* 219 */       StringBuilder msg = new StringBuilder(160);
/* 220 */       msg.append("Bean property '").append(getPropertyName()).append("' is not writable or has an invalid setter method. ");
/*     */       
/* 222 */       if (!ObjectUtils.isEmpty((Object[])getPossibleMatches())) {
/* 223 */         appendHintMessage(msg);
/*     */       } else {
/*     */         
/* 226 */         msg.append("Does the parameter type of the setter match the return type of the getter?");
/*     */       } 
/* 228 */       return msg.toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class FieldPropertyMatches
/*     */     extends PropertyMatches
/*     */   {
/*     */     public FieldPropertyMatches(String propertyName, Class<?> beanClass, int maxDistance) {
/* 236 */       super(propertyName, calculateMatches(propertyName, beanClass, maxDistance));
/*     */     }
/*     */     
/*     */     private static String[] calculateMatches(final String name, Class<?> clazz, final int maxDistance) {
/* 240 */       final List<String> candidates = new ArrayList<String>();
/* 241 */       ReflectionUtils.doWithFields(clazz, new ReflectionUtils.FieldCallback()
/*     */           {
/*     */             public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
/* 244 */               String possibleAlternative = field.getName();
/* 245 */               if (PropertyMatches.calculateStringDistance(name, possibleAlternative) <= maxDistance) {
/* 246 */                 candidates.add(possibleAlternative);
/*     */               }
/*     */             }
/*     */           });
/* 250 */       Collections.sort(candidates);
/* 251 */       return StringUtils.toStringArray(candidates);
/*     */     }
/*     */ 
/*     */     
/*     */     public String buildErrorMessage() {
/* 256 */       StringBuilder msg = new StringBuilder(80);
/* 257 */       msg.append("Bean property '").append(getPropertyName()).append("' has no matching field.");
/* 258 */       if (!ObjectUtils.isEmpty((Object[])getPossibleMatches())) {
/* 259 */         msg.append(' ');
/* 260 */         appendHintMessage(msg);
/*     */       } 
/* 262 */       return msg.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\PropertyMatches.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */