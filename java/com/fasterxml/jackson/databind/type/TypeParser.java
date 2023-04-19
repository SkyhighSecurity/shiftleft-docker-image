/*     */ package com.fasterxml.jackson.databind.type;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TypeParser
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final TypeFactory _factory;
/*     */   
/*     */   public TypeParser(TypeFactory f) {
/*  20 */     this._factory = f;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeParser withFactory(TypeFactory f) {
/*  27 */     return (f == this._factory) ? this : new TypeParser(f);
/*     */   }
/*     */ 
/*     */   
/*     */   public JavaType parse(String canonical) throws IllegalArgumentException {
/*  32 */     MyTokenizer tokens = new MyTokenizer(canonical.trim());
/*  33 */     JavaType type = parseType(tokens);
/*     */     
/*  35 */     if (tokens.hasMoreTokens()) {
/*  36 */       throw _problem(tokens, "Unexpected tokens after complete type");
/*     */     }
/*  38 */     return type;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected JavaType parseType(MyTokenizer tokens) throws IllegalArgumentException {
/*  44 */     if (!tokens.hasMoreTokens()) {
/*  45 */       throw _problem(tokens, "Unexpected end-of-string");
/*     */     }
/*  47 */     Class<?> base = findClass(tokens.nextToken(), tokens);
/*     */ 
/*     */     
/*  50 */     if (tokens.hasMoreTokens()) {
/*  51 */       String token = tokens.nextToken();
/*  52 */       if ("<".equals(token)) {
/*  53 */         List<JavaType> parameterTypes = parseTypes(tokens);
/*  54 */         TypeBindings b = TypeBindings.create(base, parameterTypes);
/*  55 */         return this._factory._fromClass(null, base, b);
/*     */       } 
/*     */       
/*  58 */       tokens.pushBack(token);
/*     */     } 
/*  60 */     return this._factory._fromClass(null, base, TypeBindings.emptyBindings());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<JavaType> parseTypes(MyTokenizer tokens) throws IllegalArgumentException {
/*  66 */     ArrayList<JavaType> types = new ArrayList<>();
/*  67 */     while (tokens.hasMoreTokens()) {
/*  68 */       types.add(parseType(tokens));
/*  69 */       if (!tokens.hasMoreTokens())
/*  70 */         break;  String token = tokens.nextToken();
/*  71 */       if (">".equals(token)) return types; 
/*  72 */       if (!",".equals(token)) {
/*  73 */         throw _problem(tokens, "Unexpected token '" + token + "', expected ',' or '>')");
/*     */       }
/*     */     } 
/*  76 */     throw _problem(tokens, "Unexpected end-of-string");
/*     */   }
/*     */ 
/*     */   
/*     */   protected Class<?> findClass(String className, MyTokenizer tokens) {
/*     */     try {
/*  82 */       return this._factory.findClass(className);
/*  83 */     } catch (Exception e) {
/*  84 */       ClassUtil.throwIfRTE(e);
/*  85 */       throw _problem(tokens, "Cannot locate class '" + className + "', problem: " + e.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected IllegalArgumentException _problem(MyTokenizer tokens, String msg) {
/*  91 */     return new IllegalArgumentException(String.format("Failed to parse type '%s' (remaining: '%s'): %s", new Object[] { tokens
/*  92 */             .getAllInput(), tokens.getRemainingInput(), msg }));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class MyTokenizer
/*     */     extends StringTokenizer
/*     */   {
/*     */     protected final String _input;
/*     */     protected int _index;
/*     */     protected String _pushbackToken;
/*     */     
/*     */     public MyTokenizer(String str) {
/* 104 */       super(str, "<,>", true);
/* 105 */       this._input = str;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasMoreTokens() {
/* 110 */       return (this._pushbackToken != null || super.hasMoreTokens());
/*     */     }
/*     */ 
/*     */     
/*     */     public String nextToken() {
/*     */       String token;
/* 116 */       if (this._pushbackToken != null) {
/* 117 */         token = this._pushbackToken;
/* 118 */         this._pushbackToken = null;
/*     */       } else {
/* 120 */         token = super.nextToken();
/* 121 */         this._index += token.length();
/* 122 */         token = token.trim();
/*     */       } 
/* 124 */       return token;
/*     */     }
/*     */     
/*     */     public void pushBack(String token) {
/* 128 */       this._pushbackToken = token;
/*     */     }
/*     */     
/*     */     public String getAllInput() {
/* 132 */       return this._input;
/*     */     } public String getRemainingInput() {
/* 134 */       return this._input.substring(this._index);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\type\TypeParser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */