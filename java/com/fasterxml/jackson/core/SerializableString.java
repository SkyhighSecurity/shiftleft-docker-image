package com.fasterxml.jackson.core;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public interface SerializableString {
  String getValue();
  
  int charLength();
  
  char[] asQuotedChars();
  
  byte[] asUnquotedUTF8();
  
  byte[] asQuotedUTF8();
  
  int appendQuotedUTF8(byte[] paramArrayOfbyte, int paramInt);
  
  int appendQuoted(char[] paramArrayOfchar, int paramInt);
  
  int appendUnquotedUTF8(byte[] paramArrayOfbyte, int paramInt);
  
  int appendUnquoted(char[] paramArrayOfchar, int paramInt);
  
  int writeQuotedUTF8(OutputStream paramOutputStream) throws IOException;
  
  int writeUnquotedUTF8(OutputStream paramOutputStream) throws IOException;
  
  int putQuotedUTF8(ByteBuffer paramByteBuffer) throws IOException;
  
  int putUnquotedUTF8(ByteBuffer paramByteBuffer) throws IOException;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\com\fasterxml\jackson\core\SerializableString.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */