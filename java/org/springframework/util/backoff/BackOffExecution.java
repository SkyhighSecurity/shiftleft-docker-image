package org.springframework.util.backoff;

public interface BackOffExecution {
  public static final long STOP = -1L;
  
  long nextBackOff();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframewor\\util\backoff\BackOffExecution.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */