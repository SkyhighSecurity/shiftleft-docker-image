package com.fasterxml.jackson.core.async;

public interface NonBlockingInputFeeder {
  boolean needMoreInput();
  
  void endOfInput();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\core\async\NonBlockingInputFeeder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */