/*     */ package org.springframework.beans.factory;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import org.springframework.beans.FatalBeanException;
/*     */ import org.springframework.core.NestedRuntimeException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BeanCreationException
/*     */   extends FatalBeanException
/*     */ {
/*     */   private String beanName;
/*     */   private String resourceDescription;
/*     */   private List<Throwable> relatedCauses;
/*     */   
/*     */   public BeanCreationException(String msg) {
/*  48 */     super(msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanCreationException(String msg, Throwable cause) {
/*  57 */     super(msg, cause);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanCreationException(String beanName, String msg) {
/*  66 */     super("Error creating bean" + ((beanName != null) ? (" with name '" + beanName + "'") : "") + ": " + msg);
/*  67 */     this.beanName = beanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanCreationException(String beanName, String msg, Throwable cause) {
/*  77 */     this(beanName, msg);
/*  78 */     initCause(cause);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanCreationException(String resourceDescription, String beanName, String msg) {
/*  89 */     super("Error creating bean" + ((beanName != null) ? (" with name '" + beanName + "'") : "") + ((resourceDescription != null) ? (" defined in " + resourceDescription) : "") + ": " + msg);
/*     */     
/*  91 */     this.resourceDescription = resourceDescription;
/*  92 */     this.beanName = beanName;
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
/*     */   public BeanCreationException(String resourceDescription, String beanName, String msg, Throwable cause) {
/* 104 */     this(resourceDescription, beanName, msg);
/* 105 */     initCause(cause);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBeanName() {
/* 113 */     return this.beanName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getResourceDescription() {
/* 121 */     return this.resourceDescription;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addRelatedCause(Throwable ex) {
/* 131 */     if (this.relatedCauses == null) {
/* 132 */       this.relatedCauses = new LinkedList<Throwable>();
/*     */     }
/* 134 */     this.relatedCauses.add(ex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable[] getRelatedCauses() {
/* 142 */     if (this.relatedCauses == null) {
/* 143 */       return null;
/*     */     }
/* 145 */     return this.relatedCauses.<Throwable>toArray(new Throwable[this.relatedCauses.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 151 */     StringBuilder sb = new StringBuilder(super.toString());
/* 152 */     if (this.relatedCauses != null) {
/* 153 */       for (Throwable relatedCause : this.relatedCauses) {
/* 154 */         sb.append("\nRelated cause: ");
/* 155 */         sb.append(relatedCause);
/*     */       } 
/*     */     }
/* 158 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void printStackTrace(PrintStream ps) {
/* 163 */     synchronized (ps) {
/* 164 */       super.printStackTrace(ps);
/* 165 */       if (this.relatedCauses != null) {
/* 166 */         for (Throwable relatedCause : this.relatedCauses) {
/* 167 */           ps.println("Related cause:");
/* 168 */           relatedCause.printStackTrace(ps);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void printStackTrace(PrintWriter pw) {
/* 176 */     synchronized (pw) {
/* 177 */       super.printStackTrace(pw);
/* 178 */       if (this.relatedCauses != null) {
/* 179 */         for (Throwable relatedCause : this.relatedCauses) {
/* 180 */           pw.println("Related cause:");
/* 181 */           relatedCause.printStackTrace(pw);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Class<?> exClass) {
/* 189 */     if (super.contains(exClass)) {
/* 190 */       return true;
/*     */     }
/* 192 */     if (this.relatedCauses != null) {
/* 193 */       for (Throwable relatedCause : this.relatedCauses) {
/* 194 */         if (relatedCause instanceof NestedRuntimeException && ((NestedRuntimeException)relatedCause)
/* 195 */           .contains(exClass)) {
/* 196 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/* 200 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\BeanCreationException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */