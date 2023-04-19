/*     */ package lombok.delombok.ant;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Location;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.FileSet;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.types.Reference;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Tasks
/*     */ {
/*     */   public static class Format
/*     */   {
/*     */     private String value;
/*     */     
/*     */     public int hashCode() {
/*  46 */       int result = 1;
/*  47 */       result = 31 * result + ((this.value == null) ? 0 : this.value.hashCode());
/*  48 */       return result;
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj) {
/*  52 */       if (this == obj) return true; 
/*  53 */       if (obj == null) return false; 
/*  54 */       if (getClass() != obj.getClass()) return false; 
/*  55 */       Format other = (Format)obj;
/*  56 */       if (this.value == null)
/*  57 */       { if (other.value != null) return false;  }
/*  58 */       else if (!this.value.equals(other.value)) { return false; }
/*  59 */        return true;
/*     */     }
/*     */     
/*     */     public String toString() {
/*  63 */       return "FormatOption [value=" + this.value + "]";
/*     */     }
/*     */     
/*     */     public String getValue() {
/*  67 */       return this.value;
/*     */     }
/*     */     
/*     */     public void setValue(String value) {
/*  71 */       this.value = value;
/*     */     } }
/*     */   
/*     */   public static class Delombok extends Task {
/*     */     private File fromDir;
/*     */     private File toDir;
/*     */     private Path classpath;
/*     */     private Path sourcepath;
/*     */     private Path modulepath;
/*     */     private boolean verbose;
/*     */     private String encoding;
/*     */     private Path path;
/*  83 */     private List<Tasks.Format> formatOptions = new ArrayList<Tasks.Format>(); private static ClassLoader shadowLoader;
/*     */     
/*     */     public void setClasspath(Path classpath) {
/*  86 */       if (this.classpath == null) {
/*  87 */         this.classpath = classpath;
/*     */       } else {
/*  89 */         this.classpath.append(classpath);
/*     */       } 
/*     */     }
/*     */     
/*     */     public Path createClasspath() {
/*  94 */       if (this.classpath == null) this.classpath = new Path(getProject()); 
/*  95 */       return this.classpath.createPath();
/*     */     }
/*     */     
/*     */     public void setClasspathRef(Reference r) {
/*  99 */       createClasspath().setRefid(r);
/*     */     }
/*     */     
/*     */     public void setSourcepath(Path sourcepath) {
/* 103 */       if (this.sourcepath == null) {
/* 104 */         this.sourcepath = sourcepath;
/*     */       } else {
/* 106 */         this.sourcepath.append(sourcepath);
/*     */       } 
/*     */     }
/*     */     
/*     */     public Path createSourcepath() {
/* 111 */       if (this.sourcepath == null) this.sourcepath = new Path(getProject()); 
/* 112 */       return this.sourcepath.createPath();
/*     */     }
/*     */     
/*     */     public void setSourcepathRef(Reference r) {
/* 116 */       createSourcepath().setRefid(r);
/*     */     }
/*     */     
/*     */     public void setModulepath(Path modulepath) {
/* 120 */       if (this.modulepath == null) {
/* 121 */         this.modulepath = modulepath;
/*     */       } else {
/* 123 */         this.modulepath.append(modulepath);
/*     */       } 
/*     */     }
/*     */     
/*     */     public Path createModulepath() {
/* 128 */       if (this.modulepath == null) this.modulepath = new Path(getProject()); 
/* 129 */       return this.modulepath.createPath();
/*     */     }
/*     */     
/*     */     public void setModulepathRef(Reference r) {
/* 133 */       createModulepath().setRefid(r);
/*     */     }
/*     */     
/*     */     public void setFrom(File dir) {
/* 137 */       this.fromDir = dir;
/*     */     }
/*     */     
/*     */     public void setTo(File dir) {
/* 141 */       this.toDir = dir;
/*     */     }
/*     */     
/*     */     public void setVerbose(boolean verbose) {
/* 145 */       this.verbose = verbose;
/*     */     }
/*     */     
/*     */     public void setEncoding(String encoding) {
/* 149 */       this.encoding = encoding;
/*     */     }
/*     */     
/*     */     public void addFileset(FileSet set) {
/* 153 */       if (this.path == null) this.path = new Path(getProject()); 
/* 154 */       this.path.add((ResourceCollection)set);
/*     */     }
/*     */     
/*     */     public void addFormat(Tasks.Format format) {
/* 158 */       this.formatOptions.add(format);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public static Class<?> shadowLoadClass(String name) {
/*     */       try {
/* 165 */         if (shadowLoader == null) {
/*     */           try {
/* 167 */             Class.forName("lombok.core.LombokNode");
/*     */             
/* 169 */             shadowLoader = Delombok.class.getClassLoader();
/* 170 */           } catch (ClassNotFoundException classNotFoundException) {
/*     */             
/* 172 */             Class<?> launcherMain = Class.forName("lombok.launch.Main");
/* 173 */             Method m = launcherMain.getDeclaredMethod("getShadowClassLoader", new Class[0]);
/* 174 */             m.setAccessible(true);
/* 175 */             shadowLoader = (ClassLoader)m.invoke((Object)null, new Object[0]);
/*     */           } 
/*     */         }
/*     */         
/* 179 */         return Class.forName(name, true, shadowLoader);
/* 180 */       } catch (Exception e) {
/* 181 */         if (e instanceof RuntimeException) throw (RuntimeException)e; 
/* 182 */         throw new RuntimeException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void execute() throws BuildException {
/* 188 */       Location loc = getLocation();
/*     */       
/*     */       try {
/* 191 */         Object instance = shadowLoadClass("lombok.delombok.ant.DelombokTaskImpl").getConstructor(new Class[0]).newInstance(new Object[0]); byte b; int i; Field[] arrayOfField;
/* 192 */         for (i = (arrayOfField = getClass().getDeclaredFields()).length, b = 0; b < i; ) { Field selfField = arrayOfField[b];
/* 193 */           if (!selfField.isSynthetic() && !Modifier.isStatic(selfField.getModifiers())) {
/* 194 */             Field otherField = instance.getClass().getDeclaredField(selfField.getName());
/* 195 */             otherField.setAccessible(true);
/* 196 */             if (selfField.getName().equals("formatOptions")) {
/* 197 */               List<String> rep = new ArrayList<String>();
/* 198 */               for (Tasks.Format f : this.formatOptions) {
/* 199 */                 if (f.getValue() == null) throw new BuildException("'value' property required for <format>"); 
/* 200 */                 rep.add(f.getValue());
/*     */               } 
/* 202 */               otherField.set(instance, rep);
/*     */             } else {
/* 204 */               otherField.set(instance, selfField.get(this));
/*     */             } 
/*     */           }  b++; }
/*     */         
/* 208 */         Method m = instance.getClass().getMethod("execute", new Class[] { Location.class });
/* 209 */         m.invoke(instance, new Object[] { loc });
/* 210 */       } catch (Exception e) {
/* 211 */         Throwable t = (e instanceof java.lang.reflect.InvocationTargetException) ? e.getCause() : e;
/* 212 */         if (t instanceof Error) throw (Error)t; 
/* 213 */         if (t instanceof RuntimeException) throw (RuntimeException)t; 
/* 214 */         throw new RuntimeException(t);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\lombok\delombok\ant\Tasks.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */