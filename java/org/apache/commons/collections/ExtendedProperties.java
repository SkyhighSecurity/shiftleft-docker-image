/*      */ package org.apache.commons.collections;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.LineNumberReader;
/*      */ import java.io.OutputStream;
/*      */ import java.io.PrintWriter;
/*      */ import java.io.Reader;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Properties;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.Vector;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ExtendedProperties
/*      */   extends Hashtable
/*      */ {
/*      */   private ExtendedProperties defaults;
/*      */   protected String file;
/*      */   protected String basePath;
/*      */   protected String fileSeparator;
/*      */   protected boolean isInitialized;
/*  192 */   protected static String include = "include";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ArrayList keysAsListed;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static final String START_TOKEN = "${";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static final String END_TOKEN = "}";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String interpolate(String base) {
/*  214 */     return interpolateHelper(base, (List)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String interpolateHelper(String base, List priorVariables) {
/*  233 */     if (base == null) {
/*  234 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  239 */     if (priorVariables == null) {
/*  240 */       priorVariables = new ArrayList();
/*  241 */       priorVariables.add(base);
/*      */     } 
/*      */     
/*  244 */     int begin = -1;
/*  245 */     int end = -1;
/*  246 */     int prec = 0 - "}".length();
/*  247 */     String variable = null;
/*  248 */     StringBuffer result = new StringBuffer();
/*      */ 
/*      */     
/*  251 */     while ((begin = base.indexOf("${", prec + "}".length())) > -1 && (end = base.indexOf("}", begin)) > -1) {
/*      */       
/*  253 */       result.append(base.substring(prec + "}".length(), begin));
/*  254 */       variable = base.substring(begin + "${".length(), end);
/*      */ 
/*      */       
/*  257 */       if (priorVariables.contains(variable)) {
/*  258 */         String initialBase = priorVariables.remove(0).toString();
/*  259 */         priorVariables.add(variable);
/*  260 */         StringBuffer priorVariableSb = new StringBuffer();
/*      */ 
/*      */ 
/*      */         
/*  264 */         for (Iterator it = priorVariables.iterator(); it.hasNext(); ) {
/*  265 */           priorVariableSb.append(it.next());
/*  266 */           if (it.hasNext()) {
/*  267 */             priorVariableSb.append("->");
/*      */           }
/*      */         } 
/*      */         
/*  271 */         throw new IllegalStateException("infinite loop in property interpolation of " + initialBase + ": " + priorVariableSb.toString());
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  276 */       priorVariables.add(variable);
/*      */ 
/*      */ 
/*      */       
/*  280 */       Object value = getProperty(variable);
/*  281 */       if (value != null) {
/*  282 */         result.append(interpolateHelper(value.toString(), priorVariables));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  288 */         priorVariables.remove(priorVariables.size() - 1);
/*  289 */       } else if (this.defaults != null && this.defaults.getString(variable, (String)null) != null) {
/*  290 */         result.append(this.defaults.getString(variable));
/*      */       } else {
/*      */         
/*  293 */         result.append("${").append(variable).append("}");
/*      */       } 
/*  295 */       prec = end;
/*      */     } 
/*  297 */     result.append(base.substring(prec + "}".length(), base.length()));
/*      */     
/*  299 */     return result.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String escape(String s) {
/*  306 */     StringBuffer buf = new StringBuffer(s);
/*  307 */     for (int i = 0; i < buf.length(); i++) {
/*  308 */       char c = buf.charAt(i);
/*  309 */       if (c == ',' || c == '\\') {
/*  310 */         buf.insert(i, '\\');
/*  311 */         i++;
/*      */       } 
/*      */     } 
/*  314 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String unescape(String s) {
/*  321 */     StringBuffer buf = new StringBuffer(s);
/*  322 */     for (int i = 0; i < buf.length() - 1; i++) {
/*  323 */       char c1 = buf.charAt(i);
/*  324 */       char c2 = buf.charAt(i + 1);
/*  325 */       if (c1 == '\\' && c2 == '\\') {
/*  326 */         buf.deleteCharAt(i);
/*      */       }
/*      */     } 
/*  329 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int countPreceding(String line, int index, char ch) {
/*      */     int i;
/*  338 */     for (i = index - 1; i >= 0 && 
/*  339 */       line.charAt(i) == ch; i--);
/*      */ 
/*      */ 
/*      */     
/*  343 */     return index - 1 - i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean endsWithSlash(String line) {
/*  350 */     if (!line.endsWith("\\")) {
/*  351 */       return false;
/*      */     }
/*  353 */     return (countPreceding(line, line.length() - 1, '\\') % 2 == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class PropertiesReader
/*      */     extends LineNumberReader
/*      */   {
/*      */     public PropertiesReader(Reader reader) {
/*  369 */       super(reader);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String readProperty() throws IOException {
/*  379 */       StringBuffer buffer = new StringBuffer();
/*  380 */       String line = readLine();
/*  381 */       while (line != null) {
/*  382 */         line = line.trim();
/*  383 */         if (line.length() != 0 && line.charAt(0) != '#') {
/*  384 */           if (ExtendedProperties.endsWithSlash(line)) {
/*  385 */             line = line.substring(0, line.length() - 1);
/*  386 */             buffer.append(line);
/*      */           } else {
/*  388 */             buffer.append(line);
/*  389 */             return buffer.toString();
/*      */           } 
/*      */         }
/*  392 */         line = readLine();
/*      */       } 
/*  394 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class PropertiesTokenizer
/*      */     extends StringTokenizer
/*      */   {
/*      */     static final String DELIMITER = ",";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public PropertiesTokenizer(String string) {
/*  415 */       super(string, ",");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean hasMoreTokens() {
/*  424 */       return super.hasMoreTokens();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String nextToken() {
/*  433 */       StringBuffer buffer = new StringBuffer();
/*      */       
/*  435 */       while (hasMoreTokens()) {
/*  436 */         String token = super.nextToken();
/*  437 */         if (ExtendedProperties.endsWithSlash(token)) {
/*  438 */           buffer.append(token.substring(0, token.length() - 1));
/*  439 */           buffer.append(","); continue;
/*      */         } 
/*  441 */         buffer.append(token);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  446 */       return buffer.toString().trim();
/*      */     }
/*      */   }
/*      */   public ExtendedProperties() {
/*      */     try {
/*      */       this.fileSeparator = AccessController.<String>doPrivileged(new PrivilegedAction(this) { private final ExtendedProperties this$0;
/*      */             
/*      */             public Object run() {
/*      */               return System.getProperty("file.separator");
/*      */             } }
/*      */         );
/*      */     } catch (SecurityException ex) {
/*      */       this.fileSeparator = File.separator;
/*      */     } 
/*      */     this.isInitialized = false;
/*      */     this.keysAsListed = new ArrayList();
/*      */   }
/*      */   
/*  464 */   public ExtendedProperties(String file) throws IOException { this(file, (String)null); } public ExtendedProperties(String file, String defaultFile) throws IOException {
/*      */     try {
/*      */       this.fileSeparator = AccessController.<String>doPrivileged(new PrivilegedAction(this) { private final ExtendedProperties this$0; public Object run() {
/*      */               return System.getProperty("file.separator");
/*      */             } }
/*      */         );
/*      */     } catch (SecurityException ex) {
/*      */       this.fileSeparator = File.separator;
/*      */     } 
/*      */     this.isInitialized = false;
/*      */     this.keysAsListed = new ArrayList();
/*  475 */     this.file = file;
/*      */     
/*  477 */     this.basePath = (new File(file)).getAbsolutePath();
/*  478 */     this.basePath = this.basePath.substring(0, this.basePath.lastIndexOf(this.fileSeparator) + 1);
/*      */     
/*  480 */     FileInputStream in = null;
/*      */     try {
/*  482 */       in = new FileInputStream(file);
/*  483 */       load(in);
/*      */     } finally {
/*      */       try {
/*  486 */         if (in != null) {
/*  487 */           in.close();
/*      */         }
/*  489 */       } catch (IOException ex) {}
/*      */     } 
/*      */     
/*  492 */     if (defaultFile != null) {
/*  493 */       this.defaults = new ExtendedProperties(defaultFile);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isInitialized() {
/*  502 */     return this.isInitialized;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getInclude() {
/*  512 */     return include;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInclude(String inc) {
/*  522 */     include = inc;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void load(InputStream input) throws IOException {
/*  532 */     load(input, (String)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void load(InputStream input, String enc) throws IOException {
/*      */     // Byte code:
/*      */     //   0: aconst_null
/*      */     //   1: astore_3
/*      */     //   2: aload_2
/*      */     //   3: ifnull -> 28
/*      */     //   6: new org/apache/commons/collections/ExtendedProperties$PropertiesReader
/*      */     //   9: dup
/*      */     //   10: new java/io/InputStreamReader
/*      */     //   13: dup
/*      */     //   14: aload_1
/*      */     //   15: aload_2
/*      */     //   16: invokespecial <init> : (Ljava/io/InputStream;Ljava/lang/String;)V
/*      */     //   19: invokespecial <init> : (Ljava/io/Reader;)V
/*      */     //   22: astore_3
/*      */     //   23: goto -> 28
/*      */     //   26: astore #4
/*      */     //   28: aload_3
/*      */     //   29: ifnonnull -> 71
/*      */     //   32: new org/apache/commons/collections/ExtendedProperties$PropertiesReader
/*      */     //   35: dup
/*      */     //   36: new java/io/InputStreamReader
/*      */     //   39: dup
/*      */     //   40: aload_1
/*      */     //   41: ldc '8859_1'
/*      */     //   43: invokespecial <init> : (Ljava/io/InputStream;Ljava/lang/String;)V
/*      */     //   46: invokespecial <init> : (Ljava/io/Reader;)V
/*      */     //   49: astore_3
/*      */     //   50: goto -> 71
/*      */     //   53: astore #4
/*      */     //   55: new org/apache/commons/collections/ExtendedProperties$PropertiesReader
/*      */     //   58: dup
/*      */     //   59: new java/io/InputStreamReader
/*      */     //   62: dup
/*      */     //   63: aload_1
/*      */     //   64: invokespecial <init> : (Ljava/io/InputStream;)V
/*      */     //   67: invokespecial <init> : (Ljava/io/Reader;)V
/*      */     //   70: astore_3
/*      */     //   71: aload_3
/*      */     //   72: invokevirtual readProperty : ()Ljava/lang/String;
/*      */     //   75: astore #4
/*      */     //   77: aload #4
/*      */     //   79: ifnonnull -> 88
/*      */     //   82: aload_0
/*      */     //   83: iconst_1
/*      */     //   84: putfield isInitialized : Z
/*      */     //   87: return
/*      */     //   88: aload #4
/*      */     //   90: bipush #61
/*      */     //   92: invokevirtual indexOf : (I)I
/*      */     //   95: istore #5
/*      */     //   97: iload #5
/*      */     //   99: ifle -> 304
/*      */     //   102: aload #4
/*      */     //   104: iconst_0
/*      */     //   105: iload #5
/*      */     //   107: invokevirtual substring : (II)Ljava/lang/String;
/*      */     //   110: invokevirtual trim : ()Ljava/lang/String;
/*      */     //   113: astore #6
/*      */     //   115: aload #4
/*      */     //   117: iload #5
/*      */     //   119: iconst_1
/*      */     //   120: iadd
/*      */     //   121: invokevirtual substring : (I)Ljava/lang/String;
/*      */     //   124: invokevirtual trim : ()Ljava/lang/String;
/*      */     //   127: astore #7
/*      */     //   129: ldc ''
/*      */     //   131: aload #7
/*      */     //   133: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */     //   136: ifeq -> 142
/*      */     //   139: goto -> 71
/*      */     //   142: aload_0
/*      */     //   143: invokevirtual getInclude : ()Ljava/lang/String;
/*      */     //   146: ifnull -> 296
/*      */     //   149: aload #6
/*      */     //   151: aload_0
/*      */     //   152: invokevirtual getInclude : ()Ljava/lang/String;
/*      */     //   155: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
/*      */     //   158: ifeq -> 296
/*      */     //   161: aconst_null
/*      */     //   162: astore #8
/*      */     //   164: aload #7
/*      */     //   166: aload_0
/*      */     //   167: getfield fileSeparator : Ljava/lang/String;
/*      */     //   170: invokevirtual startsWith : (Ljava/lang/String;)Z
/*      */     //   173: ifeq -> 190
/*      */     //   176: new java/io/File
/*      */     //   179: dup
/*      */     //   180: aload #7
/*      */     //   182: invokespecial <init> : (Ljava/lang/String;)V
/*      */     //   185: astore #8
/*      */     //   187: goto -> 259
/*      */     //   190: aload #7
/*      */     //   192: new java/lang/StringBuffer
/*      */     //   195: dup
/*      */     //   196: invokespecial <init> : ()V
/*      */     //   199: ldc '.'
/*      */     //   201: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   204: aload_0
/*      */     //   205: getfield fileSeparator : Ljava/lang/String;
/*      */     //   208: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   211: invokevirtual toString : ()Ljava/lang/String;
/*      */     //   214: invokevirtual startsWith : (Ljava/lang/String;)Z
/*      */     //   217: ifeq -> 228
/*      */     //   220: aload #7
/*      */     //   222: iconst_2
/*      */     //   223: invokevirtual substring : (I)Ljava/lang/String;
/*      */     //   226: astore #7
/*      */     //   228: new java/io/File
/*      */     //   231: dup
/*      */     //   232: new java/lang/StringBuffer
/*      */     //   235: dup
/*      */     //   236: invokespecial <init> : ()V
/*      */     //   239: aload_0
/*      */     //   240: getfield basePath : Ljava/lang/String;
/*      */     //   243: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   246: aload #7
/*      */     //   248: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   251: invokevirtual toString : ()Ljava/lang/String;
/*      */     //   254: invokespecial <init> : (Ljava/lang/String;)V
/*      */     //   257: astore #8
/*      */     //   259: aload #8
/*      */     //   261: ifnull -> 293
/*      */     //   264: aload #8
/*      */     //   266: invokevirtual exists : ()Z
/*      */     //   269: ifeq -> 293
/*      */     //   272: aload #8
/*      */     //   274: invokevirtual canRead : ()Z
/*      */     //   277: ifeq -> 293
/*      */     //   280: aload_0
/*      */     //   281: new java/io/FileInputStream
/*      */     //   284: dup
/*      */     //   285: aload #8
/*      */     //   287: invokespecial <init> : (Ljava/io/File;)V
/*      */     //   290: invokevirtual load : (Ljava/io/InputStream;)V
/*      */     //   293: goto -> 304
/*      */     //   296: aload_0
/*      */     //   297: aload #6
/*      */     //   299: aload #7
/*      */     //   301: invokevirtual addProperty : (Ljava/lang/String;Ljava/lang/Object;)V
/*      */     //   304: goto -> 71
/*      */     //   307: astore #9
/*      */     //   309: aload_0
/*      */     //   310: iconst_1
/*      */     //   311: putfield isInitialized : Z
/*      */     //   314: aload #9
/*      */     //   316: athrow
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #544	-> 0
/*      */     //   #545	-> 2
/*      */     //   #547	-> 6
/*      */     //   #551	-> 23
/*      */     //   #549	-> 26
/*      */     //   #554	-> 28
/*      */     //   #556	-> 32
/*      */     //   #562	-> 50
/*      */     //   #558	-> 53
/*      */     //   #561	-> 55
/*      */     //   #567	-> 71
/*      */     //   #568	-> 77
/*      */     //   #611	-> 82
/*      */     //   #571	-> 88
/*      */     //   #573	-> 97
/*      */     //   #574	-> 102
/*      */     //   #575	-> 115
/*      */     //   #578	-> 129
/*      */     //   #579	-> 139
/*      */     //   #582	-> 142
/*      */     //   #584	-> 161
/*      */     //   #586	-> 164
/*      */     //   #588	-> 176
/*      */     //   #594	-> 190
/*      */     //   #595	-> 220
/*      */     //   #598	-> 228
/*      */     //   #601	-> 259
/*      */     //   #602	-> 280
/*      */     //   #604	-> 293
/*      */     //   #605	-> 296
/*      */     //   #608	-> 304
/*      */     //   #611	-> 307
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   28	0	4	ex	Ljava/io/UnsupportedEncodingException;
/*      */     //   55	16	4	ex	Ljava/io/UnsupportedEncodingException;
/*      */     //   164	129	8	file	Ljava/io/File;
/*      */     //   115	189	6	key	Ljava/lang/String;
/*      */     //   129	175	7	value	Ljava/lang/String;
/*      */     //   77	227	4	line	Ljava/lang/String;
/*      */     //   97	207	5	equalSign	I
/*      */     //   0	317	0	this	Lorg/apache/commons/collections/ExtendedProperties;
/*      */     //   0	317	1	input	Ljava/io/InputStream;
/*      */     //   0	317	2	enc	Ljava/lang/String;
/*      */     //   2	315	3	reader	Lorg/apache/commons/collections/ExtendedProperties$PropertiesReader;
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   6	23	26	java/io/UnsupportedEncodingException
/*      */     //   32	50	53	java/io/UnsupportedEncodingException
/*      */     //   71	82	307	finally
/*      */     //   88	309	307	finally
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getProperty(String key) {
/*  624 */     Object obj = get(key);
/*      */     
/*  626 */     if (obj == null)
/*      */     {
/*      */       
/*  629 */       if (this.defaults != null) {
/*  630 */         obj = this.defaults.get(key);
/*      */       }
/*      */     }
/*      */     
/*  634 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addProperty(String key, Object value) {
/*  657 */     if (value instanceof String) {
/*  658 */       String str = (String)value;
/*  659 */       if (str.indexOf(",") > 0) {
/*      */         
/*  661 */         PropertiesTokenizer tokenizer = new PropertiesTokenizer(str);
/*  662 */         while (tokenizer.hasMoreTokens()) {
/*  663 */           String token = tokenizer.nextToken();
/*  664 */           addPropertyInternal(key, unescape(token));
/*      */         } 
/*      */       } else {
/*      */         
/*  668 */         addPropertyInternal(key, unescape(str));
/*      */       } 
/*      */     } else {
/*  671 */       addPropertyInternal(key, value);
/*      */     } 
/*      */ 
/*      */     
/*  675 */     this.isInitialized = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addPropertyDirect(String key, Object value) {
/*  687 */     if (!containsKey(key)) {
/*  688 */       this.keysAsListed.add(key);
/*      */     }
/*  690 */     put((K)key, (V)value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addPropertyInternal(String key, Object value) {
/*  705 */     Object current = get(key);
/*      */     
/*  707 */     if (current instanceof String) {
/*      */       
/*  709 */       List values = new Vector(2);
/*  710 */       values.add(current);
/*  711 */       values.add(value);
/*  712 */       put((K)key, (V)values);
/*      */     }
/*  714 */     else if (current instanceof List) {
/*      */       
/*  716 */       ((List)current).add(value);
/*      */     }
/*      */     else {
/*      */       
/*  720 */       if (!containsKey(key)) {
/*  721 */         this.keysAsListed.add(key);
/*      */       }
/*  723 */       put((K)key, (V)value);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setProperty(String key, Object value) {
/*  736 */     clearProperty(key);
/*  737 */     addProperty(key, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void save(OutputStream output, String header) throws IOException {
/*  750 */     if (output == null) {
/*      */       return;
/*      */     }
/*  753 */     PrintWriter theWrtr = new PrintWriter(output);
/*  754 */     if (header != null) {
/*  755 */       theWrtr.println(header);
/*      */     }
/*      */     
/*  758 */     Enumeration theKeys = keys();
/*  759 */     while (theKeys.hasMoreElements()) {
/*  760 */       String key = (String)theKeys.nextElement();
/*  761 */       Object value = get(key);
/*  762 */       if (value != null) {
/*  763 */         if (value instanceof String) {
/*  764 */           StringBuffer currentOutput = new StringBuffer();
/*  765 */           currentOutput.append(key);
/*  766 */           currentOutput.append("=");
/*  767 */           currentOutput.append(escape((String)value));
/*  768 */           theWrtr.println(currentOutput.toString());
/*      */         }
/*  770 */         else if (value instanceof List) {
/*  771 */           List values = (List)value;
/*  772 */           for (Iterator it = values.iterator(); it.hasNext(); ) {
/*  773 */             String currentElement = it.next();
/*  774 */             StringBuffer currentOutput = new StringBuffer();
/*  775 */             currentOutput.append(key);
/*  776 */             currentOutput.append("=");
/*  777 */             currentOutput.append(escape(currentElement));
/*  778 */             theWrtr.println(currentOutput.toString());
/*      */           } 
/*      */         } 
/*      */       }
/*  782 */       theWrtr.println();
/*  783 */       theWrtr.flush();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void combine(ExtendedProperties props) {
/*  795 */     for (Iterator it = props.getKeys(); it.hasNext(); ) {
/*  796 */       String key = it.next();
/*  797 */       setProperty(key, props.get(key));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clearProperty(String key) {
/*  807 */     if (containsKey(key)) {
/*      */ 
/*      */       
/*  810 */       for (int i = 0; i < this.keysAsListed.size(); i++) {
/*  811 */         if (this.keysAsListed.get(i).equals(key)) {
/*  812 */           this.keysAsListed.remove(i);
/*      */           break;
/*      */         } 
/*      */       } 
/*  816 */       remove(key);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Iterator getKeys() {
/*  827 */     return this.keysAsListed.iterator();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Iterator getKeys(String prefix) {
/*  838 */     Iterator keys = getKeys();
/*  839 */     ArrayList matchingKeys = new ArrayList();
/*      */     
/*  841 */     while (keys.hasNext()) {
/*  842 */       Object key = keys.next();
/*      */       
/*  844 */       if (key instanceof String && ((String)key).startsWith(prefix)) {
/*  845 */         matchingKeys.add(key);
/*      */       }
/*      */     } 
/*  848 */     return matchingKeys.iterator();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ExtendedProperties subset(String prefix) {
/*  860 */     ExtendedProperties c = new ExtendedProperties();
/*  861 */     Iterator keys = getKeys();
/*  862 */     boolean validSubset = false;
/*      */     
/*  864 */     while (keys.hasNext()) {
/*  865 */       Object key = keys.next();
/*      */       
/*  867 */       if (key instanceof String && ((String)key).startsWith(prefix)) {
/*  868 */         if (!validSubset) {
/*  869 */           validSubset = true;
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  878 */         String newKey = null;
/*  879 */         if (((String)key).length() == prefix.length()) {
/*  880 */           newKey = prefix;
/*      */         } else {
/*  882 */           newKey = ((String)key).substring(prefix.length() + 1);
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  890 */         c.addPropertyDirect(newKey, get(key));
/*      */       } 
/*      */     } 
/*      */     
/*  894 */     if (validSubset) {
/*  895 */       return c;
/*      */     }
/*  897 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void display() {
/*  905 */     Iterator i = getKeys();
/*      */     
/*  907 */     while (i.hasNext()) {
/*  908 */       String key = i.next();
/*  909 */       Object value = get(key);
/*  910 */       System.out.println(key + " => " + value);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getString(String key) {
/*  923 */     return getString(key, (String)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getString(String key, String defaultValue) {
/*  937 */     Object value = get(key);
/*      */     
/*  939 */     if (value instanceof String) {
/*  940 */       return interpolate((String)value);
/*      */     }
/*  942 */     if (value == null) {
/*  943 */       if (this.defaults != null) {
/*  944 */         return interpolate(this.defaults.getString(key, defaultValue));
/*      */       }
/*  946 */       return interpolate(defaultValue);
/*      */     } 
/*  948 */     if (value instanceof List) {
/*  949 */       return interpolate(((List)value).get(0));
/*      */     }
/*  951 */     throw new ClassCastException('\'' + key + "' doesn't map to a String object");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Properties getProperties(String key) {
/*  967 */     return getProperties(key, new Properties());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Properties getProperties(String key, Properties defaults) {
/*  985 */     String[] tokens = getStringArray(key);
/*      */ 
/*      */     
/*  988 */     Properties props = new Properties(defaults);
/*  989 */     for (int i = 0; i < tokens.length; i++) {
/*  990 */       String token = tokens[i];
/*  991 */       int equalSign = token.indexOf('=');
/*  992 */       if (equalSign > 0) {
/*  993 */         String pkey = token.substring(0, equalSign).trim();
/*  994 */         String pvalue = token.substring(equalSign + 1).trim();
/*  995 */         props.put(pkey, pvalue);
/*      */       } else {
/*  997 */         throw new IllegalArgumentException('\'' + token + "' does not contain " + "an equals sign");
/*      */       } 
/*      */     } 
/* 1000 */     return props;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getStringArray(String key) {
/*      */     List values;
/* 1013 */     Object value = get(key);
/*      */ 
/*      */     
/* 1016 */     if (value instanceof String) {
/* 1017 */       values = new Vector(1);
/* 1018 */       values.add(value);
/*      */     }
/* 1020 */     else if (value instanceof List) {
/* 1021 */       values = (List)value;
/*      */     } else {
/* 1023 */       if (value == null) {
/* 1024 */         if (this.defaults != null) {
/* 1025 */           return this.defaults.getStringArray(key);
/*      */         }
/* 1027 */         return new String[0];
/*      */       } 
/*      */       
/* 1030 */       throw new ClassCastException('\'' + key + "' doesn't map to a String/List object");
/*      */     } 
/*      */     
/* 1033 */     String[] tokens = new String[values.size()];
/* 1034 */     for (int i = 0; i < tokens.length; i++) {
/* 1035 */       tokens[i] = values.get(i);
/*      */     }
/*      */     
/* 1038 */     return tokens;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector getVector(String key) {
/* 1051 */     return getVector(key, (Vector)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector getVector(String key, Vector defaultValue) {
/* 1067 */     Object value = get(key);
/*      */     
/* 1069 */     if (value instanceof List) {
/* 1070 */       return new Vector((List)value);
/*      */     }
/* 1072 */     if (value instanceof String) {
/* 1073 */       Vector values = new Vector(1);
/* 1074 */       values.add(value);
/* 1075 */       put((K)key, (V)values);
/* 1076 */       return values;
/*      */     } 
/* 1078 */     if (value == null) {
/* 1079 */       if (this.defaults != null) {
/* 1080 */         return this.defaults.getVector(key, defaultValue);
/*      */       }
/* 1082 */       return (defaultValue == null) ? new Vector() : defaultValue;
/*      */     } 
/*      */     
/* 1085 */     throw new ClassCastException('\'' + key + "' doesn't map to a Vector object");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List getList(String key) {
/* 1102 */     return getList(key, (List)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List getList(String key, List defaultValue) {
/* 1119 */     Object value = get(key);
/*      */     
/* 1121 */     if (value instanceof List) {
/* 1122 */       return new ArrayList((List)value);
/*      */     }
/* 1124 */     if (value instanceof String) {
/* 1125 */       List values = new ArrayList(1);
/* 1126 */       values.add(value);
/* 1127 */       put((K)key, (V)values);
/* 1128 */       return values;
/*      */     } 
/* 1130 */     if (value == null) {
/* 1131 */       if (this.defaults != null) {
/* 1132 */         return this.defaults.getList(key, defaultValue);
/*      */       }
/* 1134 */       return (defaultValue == null) ? new ArrayList() : defaultValue;
/*      */     } 
/*      */     
/* 1137 */     throw new ClassCastException('\'' + key + "' doesn't map to a List object");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getBoolean(String key) {
/* 1152 */     Boolean b = getBoolean(key, (Boolean)null);
/* 1153 */     if (b != null) {
/* 1154 */       return b.booleanValue();
/*      */     }
/* 1156 */     throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getBoolean(String key, boolean defaultValue) {
/* 1170 */     return getBoolean(key, new Boolean(defaultValue)).booleanValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Boolean getBoolean(String key, Boolean defaultValue) {
/* 1185 */     Object value = get(key);
/*      */     
/* 1187 */     if (value instanceof Boolean) {
/* 1188 */       return (Boolean)value;
/*      */     }
/* 1190 */     if (value instanceof String) {
/* 1191 */       String s = testBoolean((String)value);
/* 1192 */       Boolean b = new Boolean(s);
/* 1193 */       put((K)key, (V)b);
/* 1194 */       return b;
/*      */     } 
/* 1196 */     if (value == null) {
/* 1197 */       if (this.defaults != null) {
/* 1198 */         return this.defaults.getBoolean(key, defaultValue);
/*      */       }
/* 1200 */       return defaultValue;
/*      */     } 
/*      */     
/* 1203 */     throw new ClassCastException('\'' + key + "' doesn't map to a Boolean object");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String testBoolean(String value) {
/* 1220 */     String s = value.toLowerCase();
/*      */     
/* 1222 */     if (s.equals("true") || s.equals("on") || s.equals("yes"))
/* 1223 */       return "true"; 
/* 1224 */     if (s.equals("false") || s.equals("off") || s.equals("no")) {
/* 1225 */       return "false";
/*      */     }
/* 1227 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getByte(String key) {
/* 1244 */     Byte b = getByte(key, (Byte)null);
/* 1245 */     if (b != null) {
/* 1246 */       return b.byteValue();
/*      */     }
/* 1248 */     throw new NoSuchElementException('\'' + key + " doesn't map to an existing object");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getByte(String key, byte defaultValue) {
/* 1264 */     return getByte(key, new Byte(defaultValue)).byteValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Byte getByte(String key, Byte defaultValue) {
/* 1280 */     Object value = get(key);
/*      */     
/* 1282 */     if (value instanceof Byte) {
/* 1283 */       return (Byte)value;
/*      */     }
/* 1285 */     if (value instanceof String) {
/* 1286 */       Byte b = new Byte((String)value);
/* 1287 */       put((K)key, (V)b);
/* 1288 */       return b;
/*      */     } 
/* 1290 */     if (value == null) {
/* 1291 */       if (this.defaults != null) {
/* 1292 */         return this.defaults.getByte(key, defaultValue);
/*      */       }
/* 1294 */       return defaultValue;
/*      */     } 
/*      */     
/* 1297 */     throw new ClassCastException('\'' + key + "' doesn't map to a Byte object");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short getShort(String key) {
/* 1314 */     Short s = getShort(key, (Short)null);
/* 1315 */     if (s != null) {
/* 1316 */       return s.shortValue();
/*      */     }
/* 1318 */     throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short getShort(String key, short defaultValue) {
/* 1334 */     return getShort(key, new Short(defaultValue)).shortValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Short getShort(String key, Short defaultValue) {
/* 1350 */     Object value = get(key);
/*      */     
/* 1352 */     if (value instanceof Short) {
/* 1353 */       return (Short)value;
/*      */     }
/* 1355 */     if (value instanceof String) {
/* 1356 */       Short s = new Short((String)value);
/* 1357 */       put((K)key, (V)s);
/* 1358 */       return s;
/*      */     } 
/* 1360 */     if (value == null) {
/* 1361 */       if (this.defaults != null) {
/* 1362 */         return this.defaults.getShort(key, defaultValue);
/*      */       }
/* 1364 */       return defaultValue;
/*      */     } 
/*      */     
/* 1367 */     throw new ClassCastException('\'' + key + "' doesn't map to a Short object");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getInt(String name) {
/* 1379 */     return getInteger(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getInt(String name, int def) {
/* 1391 */     return getInteger(name, def);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getInteger(String key) {
/* 1407 */     Integer i = getInteger(key, (Integer)null);
/* 1408 */     if (i != null) {
/* 1409 */       return i.intValue();
/*      */     }
/* 1411 */     throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getInteger(String key, int defaultValue) {
/* 1427 */     Integer i = getInteger(key, (Integer)null);
/*      */     
/* 1429 */     if (i == null) {
/* 1430 */       return defaultValue;
/*      */     }
/* 1432 */     return i.intValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Integer getInteger(String key, Integer defaultValue) {
/* 1448 */     Object value = get(key);
/*      */     
/* 1450 */     if (value instanceof Integer) {
/* 1451 */       return (Integer)value;
/*      */     }
/* 1453 */     if (value instanceof String) {
/* 1454 */       Integer i = new Integer((String)value);
/* 1455 */       put((K)key, (V)i);
/* 1456 */       return i;
/*      */     } 
/* 1458 */     if (value == null) {
/* 1459 */       if (this.defaults != null) {
/* 1460 */         return this.defaults.getInteger(key, defaultValue);
/*      */       }
/* 1462 */       return defaultValue;
/*      */     } 
/*      */     
/* 1465 */     throw new ClassCastException('\'' + key + "' doesn't map to a Integer object");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLong(String key) {
/* 1482 */     Long l = getLong(key, (Long)null);
/* 1483 */     if (l != null) {
/* 1484 */       return l.longValue();
/*      */     }
/* 1486 */     throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLong(String key, long defaultValue) {
/* 1502 */     return getLong(key, new Long(defaultValue)).longValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Long getLong(String key, Long defaultValue) {
/* 1518 */     Object value = get(key);
/*      */     
/* 1520 */     if (value instanceof Long) {
/* 1521 */       return (Long)value;
/*      */     }
/* 1523 */     if (value instanceof String) {
/* 1524 */       Long l = new Long((String)value);
/* 1525 */       put((K)key, (V)l);
/* 1526 */       return l;
/*      */     } 
/* 1528 */     if (value == null) {
/* 1529 */       if (this.defaults != null) {
/* 1530 */         return this.defaults.getLong(key, defaultValue);
/*      */       }
/* 1532 */       return defaultValue;
/*      */     } 
/*      */     
/* 1535 */     throw new ClassCastException('\'' + key + "' doesn't map to a Long object");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getFloat(String key) {
/* 1552 */     Float f = getFloat(key, (Float)null);
/* 1553 */     if (f != null) {
/* 1554 */       return f.floatValue();
/*      */     }
/* 1556 */     throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getFloat(String key, float defaultValue) {
/* 1572 */     return getFloat(key, new Float(defaultValue)).floatValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Float getFloat(String key, Float defaultValue) {
/* 1588 */     Object value = get(key);
/*      */     
/* 1590 */     if (value instanceof Float) {
/* 1591 */       return (Float)value;
/*      */     }
/* 1593 */     if (value instanceof String) {
/* 1594 */       Float f = new Float((String)value);
/* 1595 */       put((K)key, (V)f);
/* 1596 */       return f;
/*      */     } 
/* 1598 */     if (value == null) {
/* 1599 */       if (this.defaults != null) {
/* 1600 */         return this.defaults.getFloat(key, defaultValue);
/*      */       }
/* 1602 */       return defaultValue;
/*      */     } 
/*      */     
/* 1605 */     throw new ClassCastException('\'' + key + "' doesn't map to a Float object");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getDouble(String key) {
/* 1622 */     Double d = getDouble(key, (Double)null);
/* 1623 */     if (d != null) {
/* 1624 */       return d.doubleValue();
/*      */     }
/* 1626 */     throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getDouble(String key, double defaultValue) {
/* 1642 */     return getDouble(key, new Double(defaultValue)).doubleValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Double getDouble(String key, Double defaultValue) {
/* 1658 */     Object value = get(key);
/*      */     
/* 1660 */     if (value instanceof Double) {
/* 1661 */       return (Double)value;
/*      */     }
/* 1663 */     if (value instanceof String) {
/* 1664 */       Double d = new Double((String)value);
/* 1665 */       put((K)key, (V)d);
/* 1666 */       return d;
/*      */     } 
/* 1668 */     if (value == null) {
/* 1669 */       if (this.defaults != null) {
/* 1670 */         return this.defaults.getDouble(key, defaultValue);
/*      */       }
/* 1672 */       return defaultValue;
/*      */     } 
/*      */     
/* 1675 */     throw new ClassCastException('\'' + key + "' doesn't map to a Double object");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ExtendedProperties convertProperties(Properties props) {
/* 1689 */     ExtendedProperties c = new ExtendedProperties();
/*      */     
/* 1691 */     for (Enumeration e = props.propertyNames(); e.hasMoreElements(); ) {
/* 1692 */       String s = (String)e.nextElement();
/* 1693 */       c.setProperty(s, props.getProperty(s));
/*      */     } 
/*      */     
/* 1696 */     return c;
/*      */   }
/*      */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\ExtendedProperties.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */