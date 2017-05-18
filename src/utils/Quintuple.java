package utils;

public class Quintuple <A,B,C,D,E> {

    // Private attributes
    private A first;
    private B second;
    private C third;
    private D fourth;
    private E fifth;
    
    /**
     * Construct of the class - returns a Quintuple Object
     * @param fst
     * @param snd
     * @param trd
     * @param frt
     * @param fif
     */
    public Quintuple(A fst, B snd, C trd, D frt, E fif) {
        first = fst;
        second = snd;
        third = trd;
        fourth = frt;
        fifth = fif;
    }

    /**
     * Getter method
     * @return the first element of the Quintuple
     */
    public A first() {
        return first;
    }

    /**
     * Getter method
     * @return the second element of the Quintuple
     */
    public B second() {
        return second;
    }

    /**
     * Getter method
     * @return the third element of the Quintuple
     */
    public C third() {
        return third;
    }
    
    /**
     * Getter method
     * @return the fourth element of the Quintuple
     */
    public D fourth() {
        return fourth;
    }
    
    /**
     * Getter method
     * @return the fifth element of the Quintuple
     */
    public E fifth() {
        return fifth;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((first == null) ? 0 : first.hashCode());
        result = prime * result + ((second == null) ? 0 : second.hashCode());
        result = prime * result + ((third == null) ? 0 : third.hashCode());
        result = prime * result + ((fourth == null) ? 0 : fourth.hashCode());
        result = prime * result + ((fifth == null) ? 0 : fifth.hashCode());
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
        Quintuple other = (Quintuple) obj;
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
        }if (fourth == null) {
            if (other.fourth != null) {
                return false;
            }
        } else if (!fourth.equals(other.fourth)) {
            return false;
        }if (fifth == null) {
            if (other.fifth != null) {
                return false;
            }
        } else if (!fifth.equals(other.fifth)) {
            return false;
        }
        return true;
    }
    
    public String toString(){
        return "("+ first.toString() + "," + second.toString() + ","+ third.toString() +")";
    }
	
	
}
