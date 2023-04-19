package org.apache.commons.collections.functors;

import org.apache.commons.collections.Predicate;

public interface PredicateDecorator extends Predicate {
  Predicate[] getPredicates();
}


/* Location:              C:\Users\nateb\Desktop\shiftleft-docker-image-1.2.0.jar!\org\apache\commons\collections\functors\PredicateDecorator.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */