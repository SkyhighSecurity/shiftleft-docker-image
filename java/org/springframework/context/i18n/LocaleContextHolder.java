/*     */ package org.springframework.context.i18n;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ import org.springframework.core.NamedInheritableThreadLocal;
/*     */ import org.springframework.core.NamedThreadLocal;
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
/*     */ public abstract class LocaleContextHolder
/*     */ {
/*  46 */   private static final ThreadLocal<LocaleContext> localeContextHolder = (ThreadLocal<LocaleContext>)new NamedThreadLocal("LocaleContext");
/*     */ 
/*     */   
/*  49 */   private static final ThreadLocal<LocaleContext> inheritableLocaleContextHolder = (ThreadLocal<LocaleContext>)new NamedInheritableThreadLocal("LocaleContext");
/*     */ 
/*     */ 
/*     */   
/*     */   private static Locale defaultLocale;
/*     */ 
/*     */ 
/*     */   
/*     */   private static TimeZone defaultTimeZone;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void resetLocaleContext() {
/*  63 */     localeContextHolder.remove();
/*  64 */     inheritableLocaleContextHolder.remove();
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
/*     */   public static void setLocaleContext(LocaleContext localeContext) {
/*  78 */     setLocaleContext(localeContext, false);
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
/*     */   public static void setLocaleContext(LocaleContext localeContext, boolean inheritable) {
/*  93 */     if (localeContext == null) {
/*  94 */       resetLocaleContext();
/*     */     
/*     */     }
/*  97 */     else if (inheritable) {
/*  98 */       inheritableLocaleContextHolder.set(localeContext);
/*  99 */       localeContextHolder.remove();
/*     */     } else {
/*     */       
/* 102 */       localeContextHolder.set(localeContext);
/* 103 */       inheritableLocaleContextHolder.remove();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LocaleContext getLocaleContext() {
/* 113 */     LocaleContext localeContext = localeContextHolder.get();
/* 114 */     if (localeContext == null) {
/* 115 */       localeContext = inheritableLocaleContextHolder.get();
/*     */     }
/* 117 */     return localeContext;
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
/*     */   public static void setLocale(Locale locale) {
/* 131 */     setLocale(locale, false);
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
/*     */   public static void setLocale(Locale locale, boolean inheritable) {
/* 146 */     LocaleContext localeContext = getLocaleContext();
/*     */     
/* 148 */     TimeZone timeZone = (localeContext instanceof TimeZoneAwareLocaleContext) ? ((TimeZoneAwareLocaleContext)localeContext).getTimeZone() : null;
/* 149 */     if (timeZone != null) {
/* 150 */       localeContext = new SimpleTimeZoneAwareLocaleContext(locale, timeZone);
/*     */     }
/* 152 */     else if (locale != null) {
/* 153 */       localeContext = new SimpleLocaleContext(locale);
/*     */     } else {
/*     */       
/* 156 */       localeContext = null;
/*     */     } 
/* 158 */     setLocaleContext(localeContext, inheritable);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setDefaultLocale(Locale locale) {
/* 176 */     defaultLocale = locale;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Locale getLocale() {
/* 196 */     LocaleContext localeContext = getLocaleContext();
/* 197 */     if (localeContext != null) {
/* 198 */       Locale locale = localeContext.getLocale();
/* 199 */       if (locale != null) {
/* 200 */         return locale;
/*     */       }
/*     */     } 
/* 203 */     return (defaultLocale != null) ? defaultLocale : Locale.getDefault();
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
/*     */   public static void setTimeZone(TimeZone timeZone) {
/* 217 */     setTimeZone(timeZone, false);
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
/*     */   public static void setTimeZone(TimeZone timeZone, boolean inheritable) {
/* 232 */     LocaleContext localeContext = getLocaleContext();
/* 233 */     Locale locale = (localeContext != null) ? localeContext.getLocale() : null;
/* 234 */     if (timeZone != null) {
/* 235 */       localeContext = new SimpleTimeZoneAwareLocaleContext(locale, timeZone);
/*     */     }
/* 237 */     else if (locale != null) {
/* 238 */       localeContext = new SimpleLocaleContext(locale);
/*     */     } else {
/*     */       
/* 241 */       localeContext = null;
/*     */     } 
/* 243 */     setLocaleContext(localeContext, inheritable);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setDefaultTimeZone(TimeZone timeZone) {
/* 261 */     defaultTimeZone = timeZone;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TimeZone getTimeZone() {
/* 282 */     LocaleContext localeContext = getLocaleContext();
/* 283 */     if (localeContext instanceof TimeZoneAwareLocaleContext) {
/* 284 */       TimeZone timeZone = ((TimeZoneAwareLocaleContext)localeContext).getTimeZone();
/* 285 */       if (timeZone != null) {
/* 286 */         return timeZone;
/*     */       }
/*     */     } 
/* 289 */     return (defaultTimeZone != null) ? defaultTimeZone : TimeZone.getDefault();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\context\i18n\LocaleContextHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */