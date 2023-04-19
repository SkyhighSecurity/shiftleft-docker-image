/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Function;
/*    */ import com.google.common.base.Objects;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.io.Serializable;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible(serializable = true)
/*    */ final class ByFunctionOrdering<F, T>
/*    */   extends Ordering<F>
/*    */   implements Serializable
/*    */ {
/*    */   final Function<F, ? extends T> function;
/*    */   final Ordering<T> ordering;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   ByFunctionOrdering(Function<F, ? extends T> function, Ordering<T> ordering) {
/* 40 */     this.function = (Function<F, ? extends T>)Preconditions.checkNotNull(function);
/* 41 */     this.ordering = (Ordering<T>)Preconditions.checkNotNull(ordering);
/*    */   }
/*    */   
/*    */   public int compare(F left, F right) {
/* 45 */     return this.ordering.compare((T)this.function.apply(left), (T)this.function.apply(right));
/*    */   }
/*    */   
/*    */   public boolean equals(@Nullable Object object) {
/* 49 */     if (object == this) {
/* 50 */       return true;
/*    */     }
/* 52 */     if (object instanceof ByFunctionOrdering) {
/* 53 */       ByFunctionOrdering<?, ?> that = (ByFunctionOrdering<?, ?>)object;
/* 54 */       return (this.function.equals(that.function) && this.ordering.equals(that.ordering));
/*    */     } 
/*    */     
/* 57 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 61 */     return Objects.hashCode(new Object[] { this.function, this.ordering });
/*    */   }
/*    */   
/*    */   public String toString() {
/* 65 */     return this.ordering + ".onResultOf(" + this.function + ")";
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\google\common\collect\ByFunctionOrdering.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */