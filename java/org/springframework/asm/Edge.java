package org.springframework.asm;

class Edge {
  static final int NORMAL = 0;
  
  static final int EXCEPTION = 2147483647;
  
  int info;
  
  Label successor;
  
  Edge next;
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\asm\Edge.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */