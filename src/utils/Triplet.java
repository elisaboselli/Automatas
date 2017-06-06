package utils;

public class Triplet<A, B, C> {

  // Private attributes
  private A first;
  private B second;
  private C third;

  /**
   * Construct of the class - returns a Triplet Object
   * 
   * @param fst
   * @param snd
   * @param trd
   */
  public Triplet(A fst, B snd, C trd) {
    first = fst;
    second = snd;
    third = trd;
  }

  /**
   * Getter method
   * 
   * @return the first element of the Triplet
   */
  public A first() {
    return first;
  }

  /**
   * Getter method
   * 
   * @return the second element of the Triplet
   */
  public B second() {
    return second;
  }

  /**
   * Getter method
   * 
   * @return the third element of the Triplet
   */
  public C third() {
    return third;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((first == null) ? 0 : first.hashCode());
    result = prime * result + ((second == null) ? 0 : second.hashCode());
    result = prime * result + ((third == null) ? 0 : third.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Triplet<A, B, C> other = (Triplet<A, B, C>) obj;
    if (first == null) {
      if (other.first != null) {
        return false;
      }
    } else if (!first.equals(other.first)) {
      return false;
    }
    if (second == null) {
      if (other.second != null) {
        return false;
      } else {
      }
    } else if (!second.equals(other.second)) {
      return false;
    }
    if (third == null) {
      if (other.third != null) {
        return false;
      }
    } else if (!third.equals(other.third)) {
      return false;
    }
    return true;
  }

  public String toString() {
    return "(" + first.toString() + "," + second.toString() + "," + third.toString() + ")";
  }

}
