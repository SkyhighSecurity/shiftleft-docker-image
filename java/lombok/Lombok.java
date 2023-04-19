/*    */ package lombok;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Lombok
/*    */ {
/*    */   public static RuntimeException sneakyThrow(Throwable t) {
/* 50 */     if (t == null) throw new NullPointerException("t"); 
/* 51 */     return sneakyThrow0(t);
/*    */   }
/*    */ 
/*    */   
/*    */   private static <T extends Throwable> T sneakyThrow0(Throwable t) throws T {
/* 56 */     throw (T)t;
/*    */   }
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
/*    */   public static <T> T preventNullAnalysis(T value) {
/* 69 */     return value;
/*    */   }
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
/*    */   public static <T> T checkNotNull(T value, String message) {
/* 82 */     if (value == null) throw new NullPointerException(message); 
/* 83 */     return value;
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\lombok\Lombok.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */