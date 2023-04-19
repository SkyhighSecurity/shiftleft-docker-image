/*     */ package lombok.launch;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.net.URLDecoder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Vector;
/*     */ import java.util.WeakHashMap;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipInputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ShadowClassLoader
/*     */   extends ClassLoader
/*     */ {
/*     */   private static final String SELF_NAME = "lombok/launch/ShadowClassLoader.class";
/*  94 */   private static final ConcurrentMap<String, Class<?>> highlanderMap = new ConcurrentHashMap<String, Class<?>>();
/*     */   
/*     */   private final String SELF_BASE;
/*     */   
/*     */   private final File SELF_BASE_FILE;
/*     */   private final int SELF_BASE_LENGTH;
/* 100 */   private final List<File> override = new ArrayList<File>();
/*     */   private final String sclSuffix;
/* 102 */   private final List<String> parentExclusion = new ArrayList<String>();
/* 103 */   private final List<String> highlanders = new ArrayList<String>(); private final Map<String, Object> mapJarPathToTracker; private static final Map<Object, String> mapTrackerToJarPath = new WeakHashMap<Object, String>(); private static final Map<Object, Set<String>> mapTrackerToJarContents = new WeakHashMap<Object, Set<String>>(); private Map<String, Boolean> fileRootCache; private Map<String, Boolean> jarLocCache; private Set<String> getOrMakeJarListing(String absolutePathToJar) { synchronized (mapTrackerToJarPath) {
/*     */       Object ourTracker = this.mapJarPathToTracker.get(absolutePathToJar); if (ourTracker != null)
/*     */         return mapTrackerToJarContents.get(ourTracker);  for (Map.Entry<Object, String> entry : mapTrackerToJarPath.entrySet()) {
/*     */         if (((String)entry.getValue()).equals(absolutePathToJar)) {
/*     */           Object otherTracker = entry.getKey(); this.mapJarPathToTracker.put(absolutePathToJar, otherTracker); return mapTrackerToJarContents.get(otherTracker);
/*     */         } 
/*     */       }  Object newTracker = new Object(); Set<String> jarMembers = getJarMemberSet(absolutePathToJar); mapTrackerToJarContents.put(newTracker, jarMembers); mapTrackerToJarPath.put(newTracker, absolutePathToJar);
/*     */       this.mapJarPathToTracker.put(absolutePathToJar, newTracker);
/*     */       return jarMembers;
/*     */     }  }
/* 113 */   ShadowClassLoader(ClassLoader source, String sclSuffix, String selfBase, List<String> parentExclusion, List<String> highlanders) { super(source);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 152 */     this.mapJarPathToTracker = new HashMap<String, Object>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 331 */     this.fileRootCache = new HashMap<String, Boolean>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 356 */     this.jarLocCache = new HashMap<String, Boolean>(); this.sclSuffix = sclSuffix; if (parentExclusion != null) for (String pe : parentExclusion) { pe = pe.replace(".", "/"); if (!pe.endsWith("/")) pe = String.valueOf(pe) + "/";  this.parentExclusion.add(pe); }   if (highlanders != null) for (String hl : highlanders) this.highlanders.add(hl);   if (selfBase != null) { this.SELF_BASE = selfBase; this.SELF_BASE_LENGTH = selfBase.length(); } else { URL sclClassUrl = ShadowClassLoader.class.getResource("ShadowClassLoader.class"); String sclClassStr = (sclClassUrl == null) ? null : sclClassUrl.toString(); if (sclClassStr == null || !sclClassStr.endsWith("lombok/launch/ShadowClassLoader.class")) { ClassLoader cl = ShadowClassLoader.class.getClassLoader(); throw new RuntimeException("ShadowLoader can't find itself. SCL loader type: " + ((cl == null) ? "*NULL*" : cl.getClass().toString())); }  this.SELF_BASE_LENGTH = sclClassStr.length() - "lombok/launch/ShadowClassLoader.class".length(); String decoded = urlDecode(sclClassStr.substring(0, this.SELF_BASE_LENGTH)); this.SELF_BASE = decoded; }  if (this.SELF_BASE.startsWith("jar:file:") && this.SELF_BASE.endsWith("!/")) { this.SELF_BASE_FILE = new File(this.SELF_BASE.substring(9, this.SELF_BASE.length() - 2)); } else if (this.SELF_BASE.startsWith("file:")) { this.SELF_BASE_FILE = new File(this.SELF_BASE.substring(5)); } else { this.SELF_BASE_FILE = new File(this.SELF_BASE); }  String scl = System.getProperty("shadow.override." + sclSuffix); if (scl != null && !scl.isEmpty()) { byte b; int i; String[] arrayOfString; for (i = (arrayOfString = scl.split("\\s*" + ((File.pathSeparatorChar == ';') ? ";" : ":") + "\\s*")).length, b = 0; b < i; ) { String part = arrayOfString[b]; if (part.endsWith("/*") || part.endsWith(String.valueOf(File.separator) + "*")) { addOverrideJarDir(part.substring(0, part.length() - 2)); } else { addOverrideClasspathEntry(part); }  b++; }  }  } private Set<String> getJarMemberSet(String absolutePathToJar) { try { int shiftBits = 1; JarFile jar = new JarFile(absolutePathToJar); int jarSizePower2 = Integer.highestOneBit(jar.size()); if (jarSizePower2 != jar.size()) jarSizePower2 <<= 1;  if (jarSizePower2 == 0) jarSizePower2 = 1;  Set<String> jarMembers = new HashSet<String>(jarSizePower2 >> shiftBits, (1 << shiftBits)); try { Enumeration<JarEntry> entries = jar.entries(); while (entries.hasMoreElements()) { JarEntry jarEntry = entries.nextElement(); if (jarEntry.isDirectory())
/*     */             continue;  jarMembers.add(jarEntry.getName()); }  } catch (Exception exception) {  } finally { jar.close(); }  return jarMembers; } catch (Exception exception) { return Collections.emptySet(); }  }
/* 358 */   private boolean isPartOfShadowSuffixJarBased(String jarLoc, String suffix) { String key = String.valueOf(jarLoc) + "::" + suffix;
/* 359 */     Boolean existing = this.jarLocCache.get(key);
/* 360 */     if (existing != null) return existing.booleanValue();
/*     */     
/* 362 */     if (jarLoc.startsWith("file:/")) jarLoc = urlDecode(jarLoc.substring(5)); 
/*     */     
/* 364 */     try { FileInputStream jar = new FileInputStream(jarLoc);
/*     */       try {
/* 366 */         ZipInputStream zip = new ZipInputStream(jar);
/*     */         while (true) {
/* 368 */           ZipEntry entry = zip.getNextEntry();
/* 369 */           if (entry == null) {
/* 370 */             this.jarLocCache.put(key, Boolean.valueOf(false));
/* 371 */             return false;
/*     */           } 
/* 373 */           if (!"META-INF/ShadowClassLoader".equals(entry.getName()))
/* 374 */             continue;  boolean v = sclFileContainsSuffix(zip, suffix);
/* 375 */           this.jarLocCache.put(key, Boolean.valueOf(v));
/* 376 */           return v;
/*     */         } 
/*     */       } finally {
/* 379 */         jar.close();
/*     */       }  }
/* 381 */     catch (FileNotFoundException fileNotFoundException)
/* 382 */     { this.jarLocCache.put(key, Boolean.valueOf(false));
/* 383 */       return false; }
/* 384 */     catch (IOException iOException)
/* 385 */     { this.jarLocCache.put(key, Boolean.valueOf(false));
/* 386 */       return false; }  }
/*     */   private URL getResourceFromLocation(String name, String altName, File location) { File absoluteFile; if (location.isDirectory()) try { if (altName != null) { File file = new File(location, altName); if (file.isFile() && file.canRead()) return file.toURI().toURL();  }  File f = new File(location, name); if (f.isFile() && f.canRead())
/*     */           return f.toURI().toURL();  return null; } catch (MalformedURLException malformedURLException) { return null; }   if (!location.isFile() || !location.canRead())
/*     */       return null;  try { absoluteFile = location.getCanonicalFile(); } catch (Exception exception) { absoluteFile = location.getAbsoluteFile(); }  Set<String> jarContents = getOrMakeJarListing(absoluteFile.getAbsolutePath()); String absoluteUri = absoluteFile.toURI().toString(); try { if (jarContents.contains(altName))
/*     */         return (new URI("jar:" + absoluteUri + "!/" + altName)).toURL();  } catch (Exception exception) {} try { if (jarContents.contains(name))
/*     */         return (new URI("jar:" + absoluteUri + "!/" + name)).toURL();  } catch (Exception exception) {} return null; }
/*     */   private boolean partOfShadow(String item, String name) { return (!name.startsWith("java/") && !name.startsWith("sun/") && (inOwnBase(item, name) || isPartOfShadowSuffix(item, name, this.sclSuffix))); }
/*     */   private boolean inOwnBase(String item, String name) { if (item == null)
/* 394 */       return false;  return (item.length() == this.SELF_BASE_LENGTH + name.length() && this.SELF_BASE.regionMatches(0, item, 0, this.SELF_BASE_LENGTH)); } private boolean isPartOfShadowSuffix(String url, String name, String suffix) { if (url == null) return false; 
/* 395 */     if (url.startsWith("file:/")) {
/* 396 */       url = urlDecode(url.substring(5));
/* 397 */       if (url.length() <= name.length() || !url.endsWith(name) || url.charAt(url.length() - name.length() - 1) != '/') {
/* 398 */         return false;
/*     */       }
/*     */       
/* 401 */       String fileRoot = url.substring(0, url.length() - name.length() - 1);
/* 402 */       return isPartOfShadowSuffixFileBased(fileRoot, suffix);
/* 403 */     }  if (url.startsWith("jar:")) {
/* 404 */       int sep = url.indexOf('!');
/* 405 */       if (sep == -1) {
/* 406 */         return false;
/*     */       }
/* 408 */       String jarLoc = url.substring(4, sep);
/* 409 */       return isPartOfShadowSuffixJarBased(jarLoc, suffix);
/*     */     } 
/*     */     
/* 412 */     return false; }
/*     */   private static boolean sclFileContainsSuffix(InputStream in, String suffix) throws IOException { BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8")); for (String line = br.readLine(); line != null; line = br.readLine()) { line = line.trim(); if (!line.isEmpty() && line.charAt(0) != '#' && line.equals(suffix)) return true;  }  return false; }
/*     */   private static String urlDecode(String in) { try { return URLDecoder.decode(in, "UTF-8"); } catch (UnsupportedEncodingException unsupportedEncodingException) { throw new InternalError("UTF-8 not supported"); }  }
/*     */   private boolean isPartOfShadowSuffixFileBased(String fileRoot, String suffix) { String key = String.valueOf(fileRoot) + "::" + suffix; Boolean existing = this.fileRootCache.get(key); if (existing != null)
/* 416 */       return existing.booleanValue();  File f = new File(String.valueOf(fileRoot) + "/META-INF/ShadowClassLoader"); try { FileInputStream fis = new FileInputStream(f); try { boolean v = sclFileContainsSuffix(fis, suffix); this.fileRootCache.put(key, Boolean.valueOf(v)); return v; } finally { fis.close(); }  } catch (FileNotFoundException fileNotFoundException) { this.fileRootCache.put(key, Boolean.valueOf(false)); return false; } catch (IOException iOException) { this.fileRootCache.put(key, Boolean.valueOf(false)); return false; }  } public Enumeration<URL> getResources(String name) throws IOException { String altName = null;
/* 417 */     if (name.endsWith(".class")) altName = String.valueOf(name.substring(0, name.length() - 6)) + ".SCL." + this.sclSuffix;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 424 */     Vector<URL> vector = new Vector<URL>();
/*     */     
/* 426 */     for (File ce : this.override) {
/* 427 */       URL url = getResourceFromLocation(name, altName, ce);
/* 428 */       if (url != null) vector.add(url);
/*     */     
/*     */     } 
/* 431 */     if (this.override.isEmpty()) {
/* 432 */       URL fromSelf = getResourceFromLocation(name, altName, this.SELF_BASE_FILE);
/* 433 */       if (fromSelf != null) vector.add(fromSelf);
/*     */     
/*     */     } 
/* 436 */     Enumeration<URL> sec = super.getResources(name);
/* 437 */     while (sec.hasMoreElements()) {
/* 438 */       URL item = sec.nextElement();
/* 439 */       if (isPartOfShadowSuffix(item.toString(), name, this.sclSuffix)) vector.add(item);
/*     */     
/*     */     } 
/* 442 */     if (altName != null) {
/* 443 */       Enumeration<URL> tern = super.getResources(altName);
/* 444 */       while (tern.hasMoreElements()) {
/* 445 */         URL item = tern.nextElement();
/* 446 */         if (isPartOfShadowSuffix(item.toString(), altName, this.sclSuffix)) vector.add(item);
/*     */       
/*     */       } 
/*     */     } 
/* 450 */     return vector.elements(); }
/*     */ 
/*     */   
/*     */   public URL getResource(String name) {
/* 454 */     return getResource_(name, false);
/*     */   }
/*     */   
/*     */   private URL getResource_(String name, boolean noSuper) {
/* 458 */     String altName = null;
/* 459 */     if (name.endsWith(".class")) altName = String.valueOf(name.substring(0, name.length() - 6)) + ".SCL." + this.sclSuffix; 
/* 460 */     for (File ce : this.override) {
/* 461 */       URL uRL = getResourceFromLocation(name, altName, ce);
/* 462 */       if (uRL != null) return uRL;
/*     */     
/*     */     } 
/* 465 */     if (!this.override.isEmpty()) {
/* 466 */       if (noSuper) return null; 
/* 467 */       if (altName != null) {
/*     */         try {
/* 469 */           URL uRL = getResourceSkippingSelf(altName);
/* 470 */           if (uRL != null) return uRL; 
/* 471 */         } catch (IOException iOException) {}
/*     */       }
/*     */       
/*     */       try {
/* 475 */         return getResourceSkippingSelf(name);
/* 476 */       } catch (IOException iOException) {
/* 477 */         return null;
/*     */       } 
/*     */     } 
/*     */     
/* 481 */     URL url = getResourceFromLocation(name, altName, this.SELF_BASE_FILE);
/* 482 */     if (url != null) return url;
/*     */     
/* 484 */     if (altName != null) {
/* 485 */       URL uRL = super.getResource(altName);
/* 486 */       if (uRL != null && (!noSuper || partOfShadow(uRL.toString(), altName))) return uRL;
/*     */     
/*     */     } 
/* 489 */     URL res = super.getResource(name);
/* 490 */     if (res != null && (!noSuper || partOfShadow(res.toString(), name))) return res; 
/* 491 */     return null;
/*     */   }
/*     */   
/*     */   private boolean exclusionListMatch(String name) {
/* 495 */     for (String pe : this.parentExclusion) {
/* 496 */       if (name.startsWith(pe)) return true; 
/*     */     } 
/* 498 */     return false;
/*     */   }
/*     */   
/*     */   private URL getResourceSkippingSelf(String name) throws IOException {
/* 502 */     URL candidate = super.getResource(name);
/* 503 */     if (candidate == null) return null; 
/* 504 */     if (!partOfShadow(candidate.toString(), name)) return candidate;
/*     */     
/* 506 */     Enumeration<URL> en = super.getResources(name);
/* 507 */     while (en.hasMoreElements()) {
/* 508 */       candidate = en.nextElement();
/* 509 */       if (!partOfShadow(candidate.toString(), name)) return candidate;
/*     */     
/*     */     } 
/* 512 */     return null;
/*     */   }
/*     */   
/*     */   public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
/*     */     byte[] b;
/* 517 */     Class<?> c, alreadyLoaded = findLoadedClass(name);
/* 518 */     if (alreadyLoaded != null) return alreadyLoaded;
/*     */ 
/*     */     
/* 521 */     if (this.highlanders.contains(name)) {
/* 522 */       Class<?> clazz = highlanderMap.get(name);
/* 523 */       if (clazz != null) return clazz;
/*     */     
/*     */     } 
/* 526 */     String fileNameOfClass = String.valueOf(name.replace(".", "/")) + ".class";
/* 527 */     URL res = getResource_(fileNameOfClass, true);
/* 528 */     if (res == null && 
/* 529 */       !exclusionListMatch(fileNameOfClass))
/* 530 */       try { return super.loadClass(name, resolve); }
/* 531 */       catch (ClassNotFoundException cnfe)
/* 532 */       { res = getResource_("secondaryLoading.SCL." + this.sclSuffix + "/" + name.replace(".", "/") + ".SCL." + this.sclSuffix, true);
/* 533 */         if (res == null) throw cnfe;
/*     */          }
/*     */        
/* 536 */     if (res == null) throw new ClassNotFoundException(name);
/*     */ 
/*     */     
/* 539 */     int p = 0;
/*     */     try {
/* 541 */       InputStream in = res.openStream();
/*     */       
/*     */       try {
/* 544 */         b = new byte[65536];
/*     */         while (true) {
/* 546 */           int r = in.read(b, p, b.length - p);
/* 547 */           if (r == -1)
/* 548 */             break;  p += r;
/* 549 */           if (p == b.length) {
/* 550 */             byte[] nb = new byte[b.length * 2];
/* 551 */             System.arraycopy(b, 0, nb, 0, p);
/* 552 */             b = nb;
/*     */           } 
/*     */         } 
/*     */       } finally {
/* 556 */         in.close();
/*     */       } 
/* 558 */     } catch (IOException e) {
/* 559 */       throw new ClassNotFoundException("I/O exception reading class " + name, e);
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 564 */       c = defineClass(name, b, 0, p);
/* 565 */     } catch (LinkageError e) {
/* 566 */       if (this.highlanders.contains(name)) {
/* 567 */         Class<?> alreadyDefined = highlanderMap.get(name);
/* 568 */         if (alreadyDefined != null) return alreadyDefined; 
/*     */       } 
/*     */       try {
/* 571 */         c = findLoadedClass(name);
/* 572 */       } catch (LinkageError linkageError) {
/* 573 */         throw e;
/*     */       } 
/* 575 */       if (c == null) throw e;
/*     */     
/*     */     } 
/* 578 */     if (this.highlanders.contains(name)) {
/* 579 */       Class<?> alreadyDefined = highlanderMap.putIfAbsent(name, c);
/* 580 */       if (alreadyDefined != null) c = alreadyDefined;
/*     */     
/*     */     } 
/* 583 */     if (resolve) resolveClass(c); 
/* 584 */     return c;
/*     */   }
/*     */   
/*     */   public void addOverrideJarDir(String dir) {
/* 588 */     File f = new File(dir); byte b; int i; File[] arrayOfFile;
/* 589 */     for (i = (arrayOfFile = f.listFiles()).length, b = 0; b < i; ) { File j = arrayOfFile[b];
/* 590 */       if (j.getName().toLowerCase().endsWith(".jar") && j.canRead() && j.isFile()) this.override.add(j); 
/*     */       b++; }
/*     */   
/*     */   }
/*     */   public void addOverrideClasspathEntry(String entry) {
/* 595 */     this.override.add(new File(entry));
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\lombok\launch\ShadowClassLoader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */