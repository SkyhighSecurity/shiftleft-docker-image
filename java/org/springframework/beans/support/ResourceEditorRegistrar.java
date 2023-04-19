/*     */ package org.springframework.beans.support;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import java.io.File;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import org.springframework.beans.PropertyEditorRegistrar;
/*     */ import org.springframework.beans.PropertyEditorRegistry;
/*     */ import org.springframework.beans.PropertyEditorRegistrySupport;
/*     */ import org.springframework.beans.propertyeditors.ClassArrayEditor;
/*     */ import org.springframework.beans.propertyeditors.ClassEditor;
/*     */ import org.springframework.beans.propertyeditors.FileEditor;
/*     */ import org.springframework.beans.propertyeditors.InputSourceEditor;
/*     */ import org.springframework.beans.propertyeditors.InputStreamEditor;
/*     */ import org.springframework.beans.propertyeditors.PathEditor;
/*     */ import org.springframework.beans.propertyeditors.ReaderEditor;
/*     */ import org.springframework.beans.propertyeditors.URIEditor;
/*     */ import org.springframework.beans.propertyeditors.URLEditor;
/*     */ import org.springframework.core.env.PropertyResolver;
/*     */ import org.springframework.core.io.ContextResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceEditor;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.io.support.ResourceArrayPropertyEditor;
/*     */ import org.springframework.core.io.support.ResourcePatternResolver;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.xml.sax.InputSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResourceEditorRegistrar
/*     */   implements PropertyEditorRegistrar
/*     */ {
/*     */   private static Class<?> pathClass;
/*     */   private final PropertyResolver propertyResolver;
/*     */   private final ResourceLoader resourceLoader;
/*     */   
/*     */   static {
/*     */     try {
/*  67 */       pathClass = ClassUtils.forName("java.nio.file.Path", ResourceEditorRegistrar.class.getClassLoader());
/*     */     }
/*  69 */     catch (ClassNotFoundException ex) {
/*     */       
/*  71 */       pathClass = null;
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
/*     */   public ResourceEditorRegistrar(ResourceLoader resourceLoader, PropertyResolver propertyResolver) {
/*  92 */     this.resourceLoader = resourceLoader;
/*  93 */     this.propertyResolver = propertyResolver;
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
/*     */   public void registerCustomEditors(PropertyEditorRegistry registry) {
/* 115 */     ResourceEditor baseEditor = new ResourceEditor(this.resourceLoader, this.propertyResolver);
/* 116 */     doRegisterEditor(registry, Resource.class, (PropertyEditor)baseEditor);
/* 117 */     doRegisterEditor(registry, ContextResource.class, (PropertyEditor)baseEditor);
/* 118 */     doRegisterEditor(registry, InputStream.class, (PropertyEditor)new InputStreamEditor(baseEditor));
/* 119 */     doRegisterEditor(registry, InputSource.class, (PropertyEditor)new InputSourceEditor(baseEditor));
/* 120 */     doRegisterEditor(registry, File.class, (PropertyEditor)new FileEditor(baseEditor));
/* 121 */     if (pathClass != null) {
/* 122 */       doRegisterEditor(registry, pathClass, (PropertyEditor)new PathEditor(baseEditor));
/*     */     }
/* 124 */     doRegisterEditor(registry, Reader.class, (PropertyEditor)new ReaderEditor(baseEditor));
/* 125 */     doRegisterEditor(registry, URL.class, (PropertyEditor)new URLEditor(baseEditor));
/*     */     
/* 127 */     ClassLoader classLoader = this.resourceLoader.getClassLoader();
/* 128 */     doRegisterEditor(registry, URI.class, (PropertyEditor)new URIEditor(classLoader));
/* 129 */     doRegisterEditor(registry, Class.class, (PropertyEditor)new ClassEditor(classLoader));
/* 130 */     doRegisterEditor(registry, Class[].class, (PropertyEditor)new ClassArrayEditor(classLoader));
/*     */     
/* 132 */     if (this.resourceLoader instanceof ResourcePatternResolver) {
/* 133 */       doRegisterEditor(registry, Resource[].class, (PropertyEditor)new ResourceArrayPropertyEditor((ResourcePatternResolver)this.resourceLoader, this.propertyResolver));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void doRegisterEditor(PropertyEditorRegistry registry, Class<?> requiredType, PropertyEditor editor) {
/* 143 */     if (registry instanceof PropertyEditorRegistrySupport) {
/* 144 */       ((PropertyEditorRegistrySupport)registry).overrideDefaultEditor(requiredType, editor);
/*     */     } else {
/*     */       
/* 147 */       registry.registerCustomEditor(requiredType, editor);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\support\ResourceEditorRegistrar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */