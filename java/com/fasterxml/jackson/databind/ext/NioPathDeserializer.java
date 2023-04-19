/*    */ package com.fasterxml.jackson.databind.ext;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.core.JsonProcessingException;
/*    */ import com.fasterxml.jackson.core.JsonToken;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.net.URI;
/*    */ import java.net.URISyntaxException;
/*    */ import java.nio.file.FileSystemNotFoundException;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.Paths;
/*    */ import java.nio.file.spi.FileSystemProvider;
/*    */ import java.util.ServiceLoader;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NioPathDeserializer
/*    */   extends StdScalarDeserializer<Path>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private static final boolean areWindowsFilePathsSupported;
/*    */   
/*    */   static {
/* 29 */     boolean isWindowsRootFound = false;
/* 30 */     for (File file : File.listRoots()) {
/* 31 */       String path = file.getPath();
/* 32 */       if (path.length() >= 2 && Character.isLetter(path.charAt(0)) && path.charAt(1) == ':') {
/* 33 */         isWindowsRootFound = true;
/*    */         break;
/*    */       } 
/*    */     } 
/* 37 */     areWindowsFilePathsSupported = isWindowsRootFound;
/*    */   }
/*    */   public NioPathDeserializer() {
/* 40 */     super(Path.class);
/*    */   }
/*    */   public Path deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
/*    */     URI uri;
/* 44 */     if (!p.hasToken(JsonToken.VALUE_STRING)) {
/* 45 */       return (Path)ctxt.handleUnexpectedToken(Path.class, p);
/*    */     }
/*    */     
/* 48 */     String value = p.getText();
/*    */ 
/*    */ 
/*    */     
/* 52 */     if (value.indexOf(':') < 0) {
/* 53 */       return Paths.get(value, new String[0]);
/*    */     }
/*    */     
/* 56 */     if (areWindowsFilePathsSupported && 
/* 57 */       value.length() >= 2 && Character.isLetter(value.charAt(0)) && value.charAt(1) == ':') {
/* 58 */       return Paths.get(value, new String[0]);
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     try {
/* 64 */       uri = new URI(value);
/* 65 */     } catch (URISyntaxException e) {
/* 66 */       return (Path)ctxt.handleInstantiationProblem(handledType(), value, e);
/*    */     } 
/*    */     try {
/* 69 */       return Paths.get(uri);
/* 70 */     } catch (FileSystemNotFoundException cause) {
/*    */       try {
/* 72 */         String scheme = uri.getScheme();
/*    */         
/* 74 */         for (FileSystemProvider provider : ServiceLoader.<FileSystemProvider>load(FileSystemProvider.class)) {
/* 75 */           if (provider.getScheme().equalsIgnoreCase(scheme)) {
/* 76 */             return provider.getPath(uri);
/*    */           }
/*    */         } 
/* 79 */         return (Path)ctxt.handleInstantiationProblem(handledType(), value, cause);
/* 80 */       } catch (Throwable e) {
/* 81 */         e.addSuppressed(cause);
/* 82 */         return (Path)ctxt.handleInstantiationProblem(handledType(), value, e);
/*    */       } 
/* 84 */     } catch (Throwable e) {
/* 85 */       return (Path)ctxt.handleInstantiationProblem(handledType(), value, e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\databind\ext\NioPathDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */