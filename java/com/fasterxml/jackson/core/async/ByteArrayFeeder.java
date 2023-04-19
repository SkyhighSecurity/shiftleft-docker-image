package com.fasterxml.jackson.core.async;

import java.io.IOException;

public interface ByteArrayFeeder extends NonBlockingInputFeeder {
  void feedInput(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\core\async\ByteArrayFeeder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */