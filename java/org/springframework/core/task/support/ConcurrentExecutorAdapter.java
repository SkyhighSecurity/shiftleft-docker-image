/*    */ package org.springframework.core.task.support;
/*    */ 
/*    */ import java.util.concurrent.Executor;
/*    */ import org.springframework.core.task.TaskExecutor;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class ConcurrentExecutorAdapter
/*    */   implements Executor
/*    */ {
/*    */   private final TaskExecutor taskExecutor;
/*    */   
/*    */   public ConcurrentExecutorAdapter(TaskExecutor taskExecutor) {
/* 48 */     Assert.notNull(taskExecutor, "TaskExecutor must not be null");
/* 49 */     this.taskExecutor = taskExecutor;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(Runnable command) {
/* 55 */     this.taskExecutor.execute(command);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\core\task\support\ConcurrentExecutorAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */