/*     */ package org.springframework.aop.interceptor;
/*     */ 
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.core.Constants;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StopWatch;
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
/*     */ public class CustomizableTraceInterceptor
/*     */   extends AbstractTraceInterceptor
/*     */ {
/*     */   public static final String PLACEHOLDER_METHOD_NAME = "$[methodName]";
/*     */   public static final String PLACEHOLDER_TARGET_CLASS_NAME = "$[targetClassName]";
/*     */   public static final String PLACEHOLDER_TARGET_CLASS_SHORT_NAME = "$[targetClassShortName]";
/*     */   public static final String PLACEHOLDER_RETURN_VALUE = "$[returnValue]";
/*     */   public static final String PLACEHOLDER_ARGUMENT_TYPES = "$[argumentTypes]";
/*     */   public static final String PLACEHOLDER_ARGUMENTS = "$[arguments]";
/*     */   public static final String PLACEHOLDER_EXCEPTION = "$[exception]";
/*     */   public static final String PLACEHOLDER_INVOCATION_TIME = "$[invocationTime]";
/*     */   private static final String DEFAULT_ENTER_MESSAGE = "Entering method '$[methodName]' of class [$[targetClassName]]";
/*     */   private static final String DEFAULT_EXIT_MESSAGE = "Exiting method '$[methodName]' of class [$[targetClassName]]";
/*     */   private static final String DEFAULT_EXCEPTION_MESSAGE = "Exception thrown in method '$[methodName]' of class [$[targetClassName]]";
/* 149 */   private static final Pattern PATTERN = Pattern.compile("\\$\\[\\p{Alpha}+\\]");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 154 */   private static final Set<Object> ALLOWED_PLACEHOLDERS = (new Constants(CustomizableTraceInterceptor.class))
/* 155 */     .getValues("PLACEHOLDER_");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 161 */   private String enterMessage = "Entering method '$[methodName]' of class [$[targetClassName]]";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 166 */   private String exitMessage = "Exiting method '$[methodName]' of class [$[targetClassName]]";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 171 */   private String exceptionMessage = "Exception thrown in method '$[methodName]' of class [$[targetClassName]]";
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
/*     */   public void setEnterMessage(String enterMessage) throws IllegalArgumentException {
/* 185 */     Assert.hasText(enterMessage, "enterMessage must not be empty");
/* 186 */     checkForInvalidPlaceholders(enterMessage);
/* 187 */     Assert.doesNotContain(enterMessage, "$[returnValue]", "enterMessage cannot contain placeholder $[returnValue]");
/*     */     
/* 189 */     Assert.doesNotContain(enterMessage, "$[exception]", "enterMessage cannot contain placeholder $[exception]");
/*     */     
/* 191 */     Assert.doesNotContain(enterMessage, "$[invocationTime]", "enterMessage cannot contain placeholder $[invocationTime]");
/*     */     
/* 193 */     this.enterMessage = enterMessage;
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
/*     */   public void setExitMessage(String exitMessage) {
/* 209 */     Assert.hasText(exitMessage, "exitMessage must not be empty");
/* 210 */     checkForInvalidPlaceholders(exitMessage);
/* 211 */     Assert.doesNotContain(exitMessage, "$[exception]", "exitMessage cannot contain placeholder$[exception]");
/*     */     
/* 213 */     this.exitMessage = exitMessage;
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
/*     */   public void setExceptionMessage(String exceptionMessage) {
/* 228 */     Assert.hasText(exceptionMessage, "exceptionMessage must not be empty");
/* 229 */     checkForInvalidPlaceholders(exceptionMessage);
/* 230 */     Assert.doesNotContain(exceptionMessage, "$[returnValue]", "exceptionMessage cannot contain placeholder $[returnValue]");
/*     */     
/* 232 */     this.exceptionMessage = exceptionMessage;
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
/*     */   protected Object invokeUnderTrace(MethodInvocation invocation, Log logger) throws Throwable {
/* 247 */     String name = ClassUtils.getQualifiedMethodName(invocation.getMethod());
/* 248 */     StopWatch stopWatch = new StopWatch(name);
/* 249 */     Object returnValue = null;
/* 250 */     boolean exitThroughException = false;
/*     */     try {
/* 252 */       stopWatch.start(name);
/* 253 */       writeToLog(logger, 
/* 254 */           replacePlaceholders(this.enterMessage, invocation, (Object)null, (Throwable)null, -1L));
/* 255 */       returnValue = invocation.proceed();
/* 256 */       return returnValue;
/*     */     }
/* 258 */     catch (Throwable ex) {
/* 259 */       if (stopWatch.isRunning()) {
/* 260 */         stopWatch.stop();
/*     */       }
/* 262 */       exitThroughException = true;
/* 263 */       writeToLog(logger, replacePlaceholders(this.exceptionMessage, invocation, (Object)null, ex, stopWatch
/* 264 */             .getTotalTimeMillis()), ex);
/* 265 */       throw ex;
/*     */     } finally {
/*     */       
/* 268 */       if (!exitThroughException) {
/* 269 */         if (stopWatch.isRunning()) {
/* 270 */           stopWatch.stop();
/*     */         }
/* 272 */         writeToLog(logger, replacePlaceholders(this.exitMessage, invocation, returnValue, (Throwable)null, stopWatch
/* 273 */               .getTotalTimeMillis()));
/*     */       } 
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
/*     */   protected String replacePlaceholders(String message, MethodInvocation methodInvocation, Object returnValue, Throwable throwable, long invocationTime) {
/* 297 */     Matcher matcher = PATTERN.matcher(message);
/*     */     
/* 299 */     StringBuffer output = new StringBuffer();
/* 300 */     while (matcher.find()) {
/* 301 */       String match = matcher.group();
/* 302 */       if ("$[methodName]".equals(match)) {
/* 303 */         matcher.appendReplacement(output, Matcher.quoteReplacement(methodInvocation.getMethod().getName())); continue;
/*     */       } 
/* 305 */       if ("$[targetClassName]".equals(match)) {
/* 306 */         String className = getClassForLogging(methodInvocation.getThis()).getName();
/* 307 */         matcher.appendReplacement(output, Matcher.quoteReplacement(className)); continue;
/*     */       } 
/* 309 */       if ("$[targetClassShortName]".equals(match)) {
/* 310 */         String shortName = ClassUtils.getShortName(getClassForLogging(methodInvocation.getThis()));
/* 311 */         matcher.appendReplacement(output, Matcher.quoteReplacement(shortName)); continue;
/*     */       } 
/* 313 */       if ("$[arguments]".equals(match)) {
/* 314 */         matcher.appendReplacement(output, 
/* 315 */             Matcher.quoteReplacement(StringUtils.arrayToCommaDelimitedString(methodInvocation.getArguments()))); continue;
/*     */       } 
/* 317 */       if ("$[argumentTypes]".equals(match)) {
/* 318 */         appendArgumentTypes(methodInvocation, matcher, output); continue;
/*     */       } 
/* 320 */       if ("$[returnValue]".equals(match)) {
/* 321 */         appendReturnValue(methodInvocation, matcher, output, returnValue); continue;
/*     */       } 
/* 323 */       if (throwable != null && "$[exception]".equals(match)) {
/* 324 */         matcher.appendReplacement(output, Matcher.quoteReplacement(throwable.toString())); continue;
/*     */       } 
/* 326 */       if ("$[invocationTime]".equals(match)) {
/* 327 */         matcher.appendReplacement(output, Long.toString(invocationTime));
/*     */         
/*     */         continue;
/*     */       } 
/* 331 */       throw new IllegalArgumentException("Unknown placeholder [" + match + "]");
/*     */     } 
/*     */     
/* 334 */     matcher.appendTail(output);
/*     */     
/* 336 */     return output.toString();
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
/*     */   private void appendReturnValue(MethodInvocation methodInvocation, Matcher matcher, StringBuffer output, Object returnValue) {
/* 351 */     if (methodInvocation.getMethod().getReturnType() == void.class) {
/* 352 */       matcher.appendReplacement(output, "void");
/*     */     }
/* 354 */     else if (returnValue == null) {
/* 355 */       matcher.appendReplacement(output, "null");
/*     */     } else {
/*     */       
/* 358 */       matcher.appendReplacement(output, Matcher.quoteReplacement(returnValue.toString()));
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
/*     */ 
/*     */   
/*     */   private void appendArgumentTypes(MethodInvocation methodInvocation, Matcher matcher, StringBuffer output) {
/* 373 */     Class<?>[] argumentTypes = methodInvocation.getMethod().getParameterTypes();
/* 374 */     String[] argumentTypeShortNames = new String[argumentTypes.length];
/* 375 */     for (int i = 0; i < argumentTypeShortNames.length; i++) {
/* 376 */       argumentTypeShortNames[i] = ClassUtils.getShortName(argumentTypes[i]);
/*     */     }
/* 378 */     matcher.appendReplacement(output, 
/* 379 */         Matcher.quoteReplacement(StringUtils.arrayToCommaDelimitedString((Object[])argumentTypeShortNames)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkForInvalidPlaceholders(String message) throws IllegalArgumentException {
/* 388 */     Matcher matcher = PATTERN.matcher(message);
/* 389 */     while (matcher.find()) {
/* 390 */       String match = matcher.group();
/* 391 */       if (!ALLOWED_PLACEHOLDERS.contains(match))
/* 392 */         throw new IllegalArgumentException("Placeholder [" + match + "] is not valid"); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\aop\interceptor\CustomizableTraceInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */