/*     */ package lombok.launch;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.Set;
/*     */ import javax.annotation.processing.AbstractProcessor;
/*     */ import javax.annotation.processing.Completion;
/*     */ import javax.annotation.processing.ProcessingEnvironment;
/*     */ import javax.annotation.processing.RoundEnvironment;
/*     */ import javax.annotation.processing.SupportedAnnotationTypes;
/*     */ import javax.lang.model.SourceVersion;
/*     */ import javax.lang.model.element.AnnotationMirror;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import org.mapstruct.ap.spi.AstModifyingAnnotationProcessor;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class AnnotationProcessorHider
/*     */ {
/*     */   public static class AstModificationNotifier
/*     */     implements AstModifyingAnnotationProcessor
/*     */   {
/*     */     public boolean isTypeComplete(TypeMirror type) {
/*  46 */       if (System.getProperty("lombok.disable") != null) return true; 
/*  47 */       return AnnotationProcessorHider.AstModificationNotifierData.lombokInvoked;
/*     */     }
/*     */   }
/*     */   
/*     */   static class AstModificationNotifierData {
/*     */     static volatile boolean lombokInvoked = false;
/*     */   }
/*     */   
/*     */   public static class AnnotationProcessor extends AbstractProcessor {
/*  56 */     private final AbstractProcessor instance = createWrappedInstance();
/*     */     
/*     */     public Set<String> getSupportedOptions() {
/*  59 */       return this.instance.getSupportedOptions();
/*     */     }
/*     */     
/*     */     public Set<String> getSupportedAnnotationTypes() {
/*  63 */       return this.instance.getSupportedAnnotationTypes();
/*     */     }
/*     */     
/*     */     public SourceVersion getSupportedSourceVersion() {
/*  67 */       return this.instance.getSupportedSourceVersion();
/*     */     }
/*     */     
/*     */     public void init(ProcessingEnvironment processingEnv) {
/*  71 */       disableJava9SillyWarning();
/*  72 */       AnnotationProcessorHider.AstModificationNotifierData.lombokInvoked = true;
/*  73 */       this.instance.init(processingEnv);
/*  74 */       super.init(processingEnv);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void disableJava9SillyWarning() {
/*     */       try {
/*  86 */         Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
/*  87 */         theUnsafe.setAccessible(true);
/*  88 */         Unsafe u = (Unsafe)theUnsafe.get((Object)null);
/*     */         
/*  90 */         Class<?> cls = Class.forName("jdk.internal.module.IllegalAccessLogger");
/*  91 */         Field logger = cls.getDeclaredField("logger");
/*  92 */         u.putObjectVolatile(cls, u.staticFieldOffset(logger), null);
/*  93 */       } catch (Throwable throwable) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
/*  99 */       return this.instance.process(annotations, roundEnv);
/*     */     }
/*     */     
/*     */     public Iterable<? extends Completion> getCompletions(Element element, AnnotationMirror annotation, ExecutableElement member, String userText) {
/* 103 */       return this.instance.getCompletions(element, annotation, member, userText);
/*     */     }
/*     */     
/*     */     private static AbstractProcessor createWrappedInstance() {
/* 107 */       ClassLoader cl = Main.getShadowClassLoader();
/*     */       try {
/* 109 */         Class<?> mc = cl.loadClass("lombok.core.AnnotationProcessor");
/* 110 */         return mc.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
/* 111 */       } catch (Throwable t) {
/* 112 */         if (t instanceof Error) throw (Error)t; 
/* 113 */         if (t instanceof RuntimeException) throw (RuntimeException)t; 
/* 114 */         throw new RuntimeException(t);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   @SupportedAnnotationTypes({"lombok.*"})
/*     */   public static class ClaimingProcessor extends AbstractProcessor {
/*     */     public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
/* 122 */       return true;
/*     */     }
/*     */     
/*     */     public SourceVersion getSupportedSourceVersion() {
/* 126 */       return SourceVersion.latest();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\lombok\launch\AnnotationProcessorHider.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */