/*     */ package org.springframework.core.io.support;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.JarURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.net.URLConnection;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.zip.ZipException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.io.DefaultResourceLoader;
/*     */ import org.springframework.core.io.FileSystemResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.io.UrlResource;
/*     */ import org.springframework.core.io.VfsResource;
/*     */ import org.springframework.util.AntPathMatcher;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.PathMatcher;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.ResourceUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PathMatchingResourcePatternResolver
/*     */   implements ResourcePatternResolver
/*     */ {
/* 182 */   private static final Log logger = LogFactory.getLog(PathMatchingResourcePatternResolver.class);
/*     */   
/*     */   private static Method equinoxResolveMethod;
/*     */   private final ResourceLoader resourceLoader;
/*     */   
/*     */   static {
/*     */     try {
/* 189 */       Class<?> fileLocatorClass = ClassUtils.forName("org.eclipse.core.runtime.FileLocator", PathMatchingResourcePatternResolver.class
/* 190 */           .getClassLoader());
/* 191 */       equinoxResolveMethod = fileLocatorClass.getMethod("resolve", new Class[] { URL.class });
/* 192 */       logger.debug("Found Equinox FileLocator for OSGi bundle URL resolution");
/*     */     }
/* 194 */     catch (Throwable ex) {
/* 195 */       equinoxResolveMethod = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 202 */   private PathMatcher pathMatcher = (PathMatcher)new AntPathMatcher();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathMatchingResourcePatternResolver() {
/* 211 */     this.resourceLoader = (ResourceLoader)new DefaultResourceLoader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathMatchingResourcePatternResolver(ResourceLoader resourceLoader) {
/* 221 */     Assert.notNull(resourceLoader, "ResourceLoader must not be null");
/* 222 */     this.resourceLoader = resourceLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathMatchingResourcePatternResolver(ClassLoader classLoader) {
/* 233 */     this.resourceLoader = (ResourceLoader)new DefaultResourceLoader(classLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceLoader getResourceLoader() {
/* 241 */     return this.resourceLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassLoader getClassLoader() {
/* 246 */     return getResourceLoader().getClassLoader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPathMatcher(PathMatcher pathMatcher) {
/* 255 */     Assert.notNull(pathMatcher, "PathMatcher must not be null");
/* 256 */     this.pathMatcher = pathMatcher;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathMatcher getPathMatcher() {
/* 263 */     return this.pathMatcher;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Resource getResource(String location) {
/* 269 */     return getResourceLoader().getResource(location);
/*     */   }
/*     */ 
/*     */   
/*     */   public Resource[] getResources(String locationPattern) throws IOException {
/* 274 */     Assert.notNull(locationPattern, "Location pattern must not be null");
/* 275 */     if (locationPattern.startsWith("classpath*:")) {
/*     */       
/* 277 */       if (getPathMatcher().isPattern(locationPattern.substring("classpath*:".length())))
/*     */       {
/* 279 */         return findPathMatchingResources(locationPattern);
/*     */       }
/*     */ 
/*     */       
/* 283 */       return findAllClassPathResources(locationPattern.substring("classpath*:".length()));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 290 */     int prefixEnd = locationPattern.startsWith("war:") ? (locationPattern.indexOf("*/") + 1) : (locationPattern.indexOf(':') + 1);
/* 291 */     if (getPathMatcher().isPattern(locationPattern.substring(prefixEnd)))
/*     */     {
/* 293 */       return findPathMatchingResources(locationPattern);
/*     */     }
/*     */ 
/*     */     
/* 297 */     return new Resource[] { getResourceLoader().getResource(locationPattern) };
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
/*     */   protected Resource[] findAllClassPathResources(String location) throws IOException {
/* 312 */     String path = location;
/* 313 */     if (path.startsWith("/")) {
/* 314 */       path = path.substring(1);
/*     */     }
/* 316 */     Set<Resource> result = doFindAllClassPathResources(path);
/* 317 */     if (logger.isDebugEnabled()) {
/* 318 */       logger.debug("Resolved classpath location [" + location + "] to resources " + result);
/*     */     }
/* 320 */     return result.<Resource>toArray(new Resource[result.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Set<Resource> doFindAllClassPathResources(String path) throws IOException {
/* 331 */     Set<Resource> result = new LinkedHashSet<Resource>(16);
/* 332 */     ClassLoader cl = getClassLoader();
/* 333 */     Enumeration<URL> resourceUrls = (cl != null) ? cl.getResources(path) : ClassLoader.getSystemResources(path);
/* 334 */     while (resourceUrls.hasMoreElements()) {
/* 335 */       URL url = resourceUrls.nextElement();
/* 336 */       result.add(convertClassLoaderURL(url));
/*     */     } 
/* 338 */     if ("".equals(path))
/*     */     {
/*     */       
/* 341 */       addAllClassLoaderJarRoots(cl, result);
/*     */     }
/* 343 */     return result;
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
/*     */   protected Resource convertClassLoaderURL(URL url) {
/* 355 */     return (Resource)new UrlResource(url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAllClassLoaderJarRoots(ClassLoader classLoader, Set<Resource> result) {
/* 366 */     if (classLoader instanceof URLClassLoader) {
/*     */       try {
/* 368 */         for (URL url : ((URLClassLoader)classLoader).getURLs()) {
/*     */           try {
/* 370 */             UrlResource jarResource = new UrlResource("jar:" + url + "!/");
/*     */             
/* 372 */             if (jarResource.exists()) {
/* 373 */               result.add(jarResource);
/*     */             }
/*     */           }
/* 376 */           catch (MalformedURLException ex) {
/* 377 */             if (logger.isDebugEnabled()) {
/* 378 */               logger.debug("Cannot search for matching files underneath [" + url + "] because it cannot be converted to a valid 'jar:' URL: " + ex
/* 379 */                   .getMessage());
/*     */             }
/*     */           }
/*     */         
/*     */         } 
/* 384 */       } catch (Exception ex) {
/* 385 */         if (logger.isDebugEnabled()) {
/* 386 */           logger.debug("Cannot introspect jar files since ClassLoader [" + classLoader + "] does not support 'getURLs()': " + ex);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 392 */     if (classLoader == ClassLoader.getSystemClassLoader())
/*     */     {
/* 394 */       addClassPathManifestEntries(result);
/*     */     }
/*     */     
/* 397 */     if (classLoader != null) {
/*     */       
/*     */       try {
/* 400 */         addAllClassLoaderJarRoots(classLoader.getParent(), result);
/*     */       }
/* 402 */       catch (Exception ex) {
/* 403 */         if (logger.isDebugEnabled()) {
/* 404 */           logger.debug("Cannot introspect jar files in parent ClassLoader since [" + classLoader + "] does not support 'getParent()': " + ex);
/*     */         }
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
/*     */   protected void addClassPathManifestEntries(Set<Resource> result) {
/*     */     try {
/* 419 */       String javaClassPathProperty = System.getProperty("java.class.path");
/* 420 */       for (String path : StringUtils.delimitedListToStringArray(javaClassPathProperty, 
/* 421 */           System.getProperty("path.separator"))) {
/*     */         try {
/* 423 */           String filePath = (new File(path)).getAbsolutePath();
/* 424 */           int prefixIndex = filePath.indexOf(':');
/* 425 */           if (prefixIndex == 1)
/*     */           {
/* 427 */             filePath = StringUtils.capitalize(filePath);
/*     */           }
/* 429 */           UrlResource jarResource = new UrlResource("jar:file:" + filePath + "!/");
/*     */ 
/*     */           
/* 432 */           if (!result.contains(jarResource) && !hasDuplicate(filePath, result) && jarResource.exists()) {
/* 433 */             result.add(jarResource);
/*     */           }
/*     */         }
/* 436 */         catch (MalformedURLException ex) {
/* 437 */           if (logger.isDebugEnabled()) {
/* 438 */             logger.debug("Cannot search for matching files underneath [" + path + "] because it cannot be converted to a valid 'jar:' URL: " + ex
/* 439 */                 .getMessage());
/*     */           }
/*     */         }
/*     */       
/*     */       } 
/* 444 */     } catch (Exception ex) {
/* 445 */       if (logger.isDebugEnabled()) {
/* 446 */         logger.debug("Failed to evaluate 'java.class.path' manifest entries: " + ex);
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
/*     */   private boolean hasDuplicate(String filePath, Set<Resource> result) {
/* 460 */     if (result.isEmpty()) {
/* 461 */       return false;
/*     */     }
/* 463 */     String duplicatePath = filePath.startsWith("/") ? filePath.substring(1) : ("/" + filePath);
/*     */     try {
/* 465 */       return result.contains(new UrlResource("jar:file:" + duplicatePath + "!/"));
/*     */     
/*     */     }
/* 468 */     catch (MalformedURLException ex) {
/*     */       
/* 470 */       return false;
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
/*     */   protected Resource[] findPathMatchingResources(String locationPattern) throws IOException {
/* 486 */     String rootDirPath = determineRootDir(locationPattern);
/* 487 */     String subPattern = locationPattern.substring(rootDirPath.length());
/* 488 */     Resource[] rootDirResources = getResources(rootDirPath);
/* 489 */     Set<Resource> result = new LinkedHashSet<Resource>(16);
/* 490 */     for (Resource rootDirResource : rootDirResources) {
/* 491 */       UrlResource urlResource; rootDirResource = resolveRootDirResource(rootDirResource);
/* 492 */       URL rootDirUrl = rootDirResource.getURL();
/* 493 */       if (equinoxResolveMethod != null && 
/* 494 */         rootDirUrl.getProtocol().startsWith("bundle")) {
/* 495 */         rootDirUrl = (URL)ReflectionUtils.invokeMethod(equinoxResolveMethod, null, new Object[] { rootDirUrl });
/* 496 */         urlResource = new UrlResource(rootDirUrl);
/*     */       } 
/*     */       
/* 499 */       if (rootDirUrl.getProtocol().startsWith("vfs")) {
/* 500 */         result.addAll(VfsResourceMatchingDelegate.findMatchingResources(rootDirUrl, subPattern, getPathMatcher()));
/*     */       }
/* 502 */       else if (ResourceUtils.isJarURL(rootDirUrl) || isJarResource((Resource)urlResource)) {
/* 503 */         result.addAll(doFindPathMatchingJarResources((Resource)urlResource, rootDirUrl, subPattern));
/*     */       } else {
/*     */         
/* 506 */         result.addAll(doFindPathMatchingFileResources((Resource)urlResource, subPattern));
/*     */       } 
/*     */     } 
/* 509 */     if (logger.isDebugEnabled()) {
/* 510 */       logger.debug("Resolved location pattern [" + locationPattern + "] to resources " + result);
/*     */     }
/* 512 */     return result.<Resource>toArray(new Resource[result.size()]);
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
/*     */   protected String determineRootDir(String location) {
/* 528 */     int prefixEnd = location.indexOf(':') + 1;
/* 529 */     int rootDirEnd = location.length();
/* 530 */     while (rootDirEnd > prefixEnd && getPathMatcher().isPattern(location.substring(prefixEnd, rootDirEnd))) {
/* 531 */       rootDirEnd = location.lastIndexOf('/', rootDirEnd - 2) + 1;
/*     */     }
/* 533 */     if (rootDirEnd == 0) {
/* 534 */       rootDirEnd = prefixEnd;
/*     */     }
/* 536 */     return location.substring(0, rootDirEnd);
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
/*     */   protected Resource resolveRootDirResource(Resource original) throws IOException {
/* 550 */     return original;
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
/*     */   protected boolean isJarResource(Resource resource) throws IOException {
/* 566 */     return false;
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
/*     */   protected Set<Resource> doFindPathMatchingJarResources(Resource rootDirResource, URL rootDirURL, String subPattern) throws IOException {
/*     */     JarFile jarFile;
/*     */     String jarFileUrl, rootEntryPath;
/*     */     boolean closeJarFile;
/* 586 */     Set<Resource> result = doFindPathMatchingJarResources(rootDirResource, subPattern);
/* 587 */     if (result != null) {
/* 588 */       return result;
/*     */     }
/*     */     
/* 591 */     URLConnection con = rootDirURL.openConnection();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 597 */     if (con instanceof JarURLConnection) {
/*     */       
/* 599 */       JarURLConnection jarCon = (JarURLConnection)con;
/* 600 */       ResourceUtils.useCachesIfNecessary(jarCon);
/* 601 */       jarFile = jarCon.getJarFile();
/* 602 */       jarFileUrl = jarCon.getJarFileURL().toExternalForm();
/* 603 */       JarEntry jarEntry = jarCon.getJarEntry();
/* 604 */       rootEntryPath = (jarEntry != null) ? jarEntry.getName() : "";
/* 605 */       closeJarFile = !jarCon.getUseCaches();
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 612 */       String urlFile = rootDirURL.getFile();
/*     */       try {
/* 614 */         int separatorIndex = urlFile.indexOf("*/");
/* 615 */         if (separatorIndex == -1) {
/* 616 */           separatorIndex = urlFile.indexOf("!/");
/*     */         }
/* 618 */         if (separatorIndex != -1) {
/* 619 */           jarFileUrl = urlFile.substring(0, separatorIndex);
/* 620 */           rootEntryPath = urlFile.substring(separatorIndex + 2);
/* 621 */           jarFile = getJarFile(jarFileUrl);
/*     */         } else {
/*     */           
/* 624 */           jarFile = new JarFile(urlFile);
/* 625 */           jarFileUrl = urlFile;
/* 626 */           rootEntryPath = "";
/*     */         } 
/* 628 */         closeJarFile = true;
/*     */       }
/* 630 */       catch (ZipException ex) {
/* 631 */         if (logger.isDebugEnabled()) {
/* 632 */           logger.debug("Skipping invalid jar classpath entry [" + urlFile + "]");
/*     */         }
/* 634 */         return Collections.emptySet();
/*     */       } 
/*     */     } 
/*     */     
/*     */     try {
/* 639 */       if (logger.isDebugEnabled()) {
/* 640 */         logger.debug("Looking for matching resources in jar file [" + jarFileUrl + "]");
/*     */       }
/* 642 */       if (!"".equals(rootEntryPath) && !rootEntryPath.endsWith("/"))
/*     */       {
/*     */         
/* 645 */         rootEntryPath = rootEntryPath + "/";
/*     */       }
/* 647 */       result = new LinkedHashSet<Resource>(8);
/* 648 */       for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements(); ) {
/* 649 */         JarEntry entry = entries.nextElement();
/* 650 */         String entryPath = entry.getName();
/* 651 */         if (entryPath.startsWith(rootEntryPath)) {
/* 652 */           String relativePath = entryPath.substring(rootEntryPath.length());
/* 653 */           if (getPathMatcher().match(subPattern, relativePath)) {
/* 654 */             result.add(rootDirResource.createRelative(relativePath));
/*     */           }
/*     */         } 
/*     */       } 
/* 658 */       return result;
/*     */     } finally {
/*     */       
/* 661 */       if (closeJarFile) {
/* 662 */         jarFile.close();
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
/*     */   @Deprecated
/*     */   protected Set<Resource> doFindPathMatchingJarResources(Resource rootDirResource, String subPattern) throws IOException {
/* 681 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JarFile getJarFile(String jarFileUrl) throws IOException {
/* 688 */     if (jarFileUrl.startsWith("file:")) {
/*     */       try {
/* 690 */         return new JarFile(ResourceUtils.toURI(jarFileUrl).getSchemeSpecificPart());
/*     */       }
/* 692 */       catch (URISyntaxException ex) {
/*     */         
/* 694 */         return new JarFile(jarFileUrl.substring("file:".length()));
/*     */       } 
/*     */     }
/*     */     
/* 698 */     return new JarFile(jarFileUrl);
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
/*     */   protected Set<Resource> doFindPathMatchingFileResources(Resource rootDirResource, String subPattern) throws IOException {
/*     */     File rootDir;
/*     */     try {
/* 717 */       rootDir = rootDirResource.getFile().getAbsoluteFile();
/*     */     }
/* 719 */     catch (IOException ex) {
/* 720 */       if (logger.isWarnEnabled()) {
/* 721 */         logger.warn("Cannot search for matching files underneath " + rootDirResource + " because it does not correspond to a directory in the file system", ex);
/*     */       }
/*     */       
/* 724 */       return Collections.emptySet();
/*     */     } 
/* 726 */     return doFindMatchingFileSystemResources(rootDir, subPattern);
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
/*     */   protected Set<Resource> doFindMatchingFileSystemResources(File rootDir, String subPattern) throws IOException {
/* 740 */     if (logger.isDebugEnabled()) {
/* 741 */       logger.debug("Looking for matching resources in directory tree [" + rootDir.getPath() + "]");
/*     */     }
/* 743 */     Set<File> matchingFiles = retrieveMatchingFiles(rootDir, subPattern);
/* 744 */     Set<Resource> result = new LinkedHashSet<Resource>(matchingFiles.size());
/* 745 */     for (File file : matchingFiles) {
/* 746 */       result.add(new FileSystemResource(file));
/*     */     }
/* 748 */     return result;
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
/*     */   protected Set<File> retrieveMatchingFiles(File rootDir, String pattern) throws IOException {
/* 761 */     if (!rootDir.exists()) {
/*     */       
/* 763 */       if (logger.isDebugEnabled()) {
/* 764 */         logger.debug("Skipping [" + rootDir.getAbsolutePath() + "] because it does not exist");
/*     */       }
/* 766 */       return Collections.emptySet();
/*     */     } 
/* 768 */     if (!rootDir.isDirectory()) {
/*     */       
/* 770 */       if (logger.isWarnEnabled()) {
/* 771 */         logger.warn("Skipping [" + rootDir.getAbsolutePath() + "] because it does not denote a directory");
/*     */       }
/* 773 */       return Collections.emptySet();
/*     */     } 
/* 775 */     if (!rootDir.canRead()) {
/* 776 */       if (logger.isWarnEnabled()) {
/* 777 */         logger.warn("Cannot search for matching files underneath directory [" + rootDir.getAbsolutePath() + "] because the application is not allowed to read the directory");
/*     */       }
/*     */       
/* 780 */       return Collections.emptySet();
/*     */     } 
/* 782 */     String fullPattern = StringUtils.replace(rootDir.getAbsolutePath(), File.separator, "/");
/* 783 */     if (!pattern.startsWith("/")) {
/* 784 */       fullPattern = fullPattern + "/";
/*     */     }
/* 786 */     fullPattern = fullPattern + StringUtils.replace(pattern, File.separator, "/");
/* 787 */     Set<File> result = new LinkedHashSet<File>(8);
/* 788 */     doRetrieveMatchingFiles(fullPattern, rootDir, result);
/* 789 */     return result;
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
/*     */   protected void doRetrieveMatchingFiles(String fullPattern, File dir, Set<File> result) throws IOException {
/* 802 */     if (logger.isDebugEnabled()) {
/* 803 */       logger.debug("Searching directory [" + dir.getAbsolutePath() + "] for files matching pattern [" + fullPattern + "]");
/*     */     }
/*     */     
/* 806 */     File[] dirContents = dir.listFiles();
/* 807 */     if (dirContents == null) {
/* 808 */       if (logger.isWarnEnabled()) {
/* 809 */         logger.warn("Could not retrieve contents of directory [" + dir.getAbsolutePath() + "]");
/*     */       }
/*     */       return;
/*     */     } 
/* 813 */     Arrays.sort((Object[])dirContents);
/* 814 */     for (File content : dirContents) {
/* 815 */       String currPath = StringUtils.replace(content.getAbsolutePath(), File.separator, "/");
/* 816 */       if (content.isDirectory() && getPathMatcher().matchStart(fullPattern, currPath + "/")) {
/* 817 */         if (!content.canRead()) {
/* 818 */           if (logger.isDebugEnabled()) {
/* 819 */             logger.debug("Skipping subdirectory [" + dir.getAbsolutePath() + "] because the application is not allowed to read the directory");
/*     */           }
/*     */         }
/*     */         else {
/*     */           
/* 824 */           doRetrieveMatchingFiles(fullPattern, content, result);
/*     */         } 
/*     */       }
/* 827 */       if (getPathMatcher().match(fullPattern, currPath)) {
/* 828 */         result.add(content);
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
/*     */   private static class VfsResourceMatchingDelegate
/*     */   {
/*     */     public static Set<Resource> findMatchingResources(URL rootDirURL, String locationPattern, PathMatcher pathMatcher) throws IOException {
/* 842 */       Object root = VfsPatternUtils.findRoot(rootDirURL);
/*     */       
/* 844 */       PathMatchingResourcePatternResolver.PatternVirtualFileVisitor visitor = new PathMatchingResourcePatternResolver.PatternVirtualFileVisitor(VfsPatternUtils.getPath(root), locationPattern, pathMatcher);
/* 845 */       VfsPatternUtils.visit(root, visitor);
/* 846 */       return visitor.getResources();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class PatternVirtualFileVisitor
/*     */     implements InvocationHandler
/*     */   {
/*     */     private final String subPattern;
/*     */ 
/*     */     
/*     */     private final PathMatcher pathMatcher;
/*     */ 
/*     */     
/*     */     private final String rootPath;
/*     */     
/* 863 */     private final Set<Resource> resources = new LinkedHashSet<Resource>();
/*     */     
/*     */     public PatternVirtualFileVisitor(String rootPath, String subPattern, PathMatcher pathMatcher) {
/* 866 */       this.subPattern = subPattern;
/* 867 */       this.pathMatcher = pathMatcher;
/* 868 */       this.rootPath = (rootPath.isEmpty() || rootPath.endsWith("/")) ? rootPath : (rootPath + "/");
/*     */     }
/*     */ 
/*     */     
/*     */     public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/* 873 */       String methodName = method.getName();
/* 874 */       if (Object.class == method.getDeclaringClass()) {
/* 875 */         if (methodName.equals("equals"))
/*     */         {
/* 877 */           return Boolean.valueOf((proxy == args[0]));
/*     */         }
/* 879 */         if (methodName.equals("hashCode")) {
/* 880 */           return Integer.valueOf(System.identityHashCode(proxy));
/*     */         }
/*     */       } else {
/* 883 */         if ("getAttributes".equals(methodName)) {
/* 884 */           return getAttributes();
/*     */         }
/* 886 */         if ("visit".equals(methodName)) {
/* 887 */           visit(args[0]);
/* 888 */           return null;
/*     */         } 
/* 890 */         if ("toString".equals(methodName)) {
/* 891 */           return toString();
/*     */         }
/*     */       } 
/* 894 */       throw new IllegalStateException("Unexpected method invocation: " + method);
/*     */     }
/*     */     
/*     */     public void visit(Object vfsResource) {
/* 898 */       if (this.pathMatcher.match(this.subPattern, 
/* 899 */           VfsPatternUtils.getPath(vfsResource).substring(this.rootPath.length()))) {
/* 900 */         this.resources.add(new VfsResource(vfsResource));
/*     */       }
/*     */     }
/*     */     
/*     */     public Object getAttributes() {
/* 905 */       return VfsPatternUtils.getVisitorAttribute();
/*     */     }
/*     */     
/*     */     public Set<Resource> getResources() {
/* 909 */       return this.resources;
/*     */     }
/*     */     
/*     */     public int size() {
/* 913 */       return this.resources.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 918 */       return "sub-pattern: " + this.subPattern + ", resources: " + this.resources;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\io\support\PathMatchingResourcePatternResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */