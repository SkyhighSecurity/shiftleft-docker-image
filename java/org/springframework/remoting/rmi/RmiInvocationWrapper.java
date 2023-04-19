/*    */ package org.springframework.remoting.rmi;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.rmi.RemoteException;
/*    */ import org.springframework.remoting.support.RemoteInvocation;
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
/*    */ class RmiInvocationWrapper
/*    */   implements RmiInvocationHandler
/*    */ {
/*    */   private final Object wrappedObject;
/*    */   private final RmiBasedExporter rmiExporter;
/*    */   
/*    */   public RmiInvocationWrapper(Object wrappedObject, RmiBasedExporter rmiExporter) {
/* 49 */     Assert.notNull(wrappedObject, "Object to wrap is required");
/* 50 */     Assert.notNull(rmiExporter, "RMI exporter is required");
/* 51 */     this.wrappedObject = wrappedObject;
/* 52 */     this.rmiExporter = rmiExporter;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getTargetInterfaceName() {
/* 62 */     Class<?> ifc = this.rmiExporter.getServiceInterface();
/* 63 */     return (ifc != null) ? ifc.getName() : null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object invoke(RemoteInvocation invocation) throws RemoteException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/* 74 */     return this.rmiExporter.invoke(invocation, this.wrappedObject);
/*    */   }
/*    */ }


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\remoting\rmi\RmiInvocationWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */