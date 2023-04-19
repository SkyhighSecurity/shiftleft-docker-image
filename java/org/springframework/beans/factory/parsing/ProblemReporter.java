package org.springframework.beans.factory.parsing;

public interface ProblemReporter {
  void fatal(Problem paramProblem);
  
  void error(Problem paramProblem);
  
  void warning(Problem paramProblem);
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\springframework\beans\factory\parsing\ProblemReporter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */