/*     */ package org.springframework.beans.factory.parsing;
/*     */ 
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Problem
/*     */ {
/*     */   private final String message;
/*     */   private final Location location;
/*     */   private final ParseState parseState;
/*     */   private final Throwable rootCause;
/*     */   
/*     */   public Problem(String message, Location location) {
/*  49 */     this(message, location, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Problem(String message, Location location, ParseState parseState) {
/*  59 */     this(message, location, parseState, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Problem(String message, Location location, ParseState parseState, Throwable rootCause) {
/*  70 */     Assert.notNull(message, "Message must not be null");
/*  71 */     Assert.notNull(location, "Location must not be null");
/*  72 */     this.message = message;
/*  73 */     this.location = location;
/*  74 */     this.parseState = parseState;
/*  75 */     this.rootCause = rootCause;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage() {
/*  83 */     return this.message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Location getLocation() {
/*  90 */     return this.location;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getResourceDescription() {
/*  99 */     return getLocation().getResource().getDescription();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParseState getParseState() {
/* 106 */     return this.parseState;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getRootCause() {
/* 113 */     return this.rootCause;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 119 */     StringBuilder sb = new StringBuilder();
/* 120 */     sb.append("Configuration problem: ");
/* 121 */     sb.append(getMessage());
/* 122 */     sb.append("\nOffending resource: ").append(getResourceDescription());
/* 123 */     if (getParseState() != null) {
/* 124 */       sb.append('\n').append(getParseState());
/*     */     }
/* 126 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\parsing\Problem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */