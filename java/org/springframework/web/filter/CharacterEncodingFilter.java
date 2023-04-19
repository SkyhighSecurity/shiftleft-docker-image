/*     */ package org.springframework.web.filter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CharacterEncodingFilter
/*     */   extends OncePerRequestFilter
/*     */ {
/*     */   private String encoding;
/*     */   private boolean forceRequestEncoding = false;
/*     */   private boolean forceResponseEncoding = false;
/*     */   
/*     */   public CharacterEncodingFilter() {}
/*     */   
/*     */   public CharacterEncodingFilter(String encoding) {
/*  69 */     this(encoding, false);
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
/*     */   public CharacterEncodingFilter(String encoding, boolean forceEncoding) {
/*  82 */     this(encoding, forceEncoding, forceEncoding);
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
/*     */   public CharacterEncodingFilter(String encoding, boolean forceRequestEncoding, boolean forceResponseEncoding) {
/*  98 */     Assert.hasLength(encoding, "Encoding must not be empty");
/*  99 */     this.encoding = encoding;
/* 100 */     this.forceRequestEncoding = forceRequestEncoding;
/* 101 */     this.forceResponseEncoding = forceResponseEncoding;
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
/*     */   public void setEncoding(String encoding) {
/* 113 */     this.encoding = encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEncoding() {
/* 121 */     return this.encoding;
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
/*     */   public void setForceEncoding(boolean forceEncoding) {
/* 137 */     this.forceRequestEncoding = forceEncoding;
/* 138 */     this.forceResponseEncoding = forceEncoding;
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
/*     */   public void setForceRequestEncoding(boolean forceRequestEncoding) {
/* 151 */     this.forceRequestEncoding = forceRequestEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isForceRequestEncoding() {
/* 159 */     return this.forceRequestEncoding;
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
/*     */   public void setForceResponseEncoding(boolean forceResponseEncoding) {
/* 171 */     this.forceResponseEncoding = forceResponseEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isForceResponseEncoding() {
/* 179 */     return this.forceResponseEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
/* 188 */     String encoding = getEncoding();
/* 189 */     if (encoding != null) {
/* 190 */       if (isForceRequestEncoding() || request.getCharacterEncoding() == null) {
/* 191 */         request.setCharacterEncoding(encoding);
/*     */       }
/* 193 */       if (isForceResponseEncoding()) {
/* 194 */         response.setCharacterEncoding(encoding);
/*     */       }
/*     */     } 
/* 197 */     filterChain.doFilter((ServletRequest)request, (ServletResponse)response);
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\web\filter\CharacterEncodingFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */