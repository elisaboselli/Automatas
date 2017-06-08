package automata;

import java.util.Set;

/**
 * @author ELisa Boselli
 *
 */

public class State {

  private String name;

  /**
   * Constructor of the class - returns a State object
   * 
   * @param nm
   *          - state name
   */
  public State(String nm) {
    name = nm;
  }

  /**
   * Get the state name
   * 
   * @return (String) State name
   */
  public String name() {
    return name;
  }

  /**
   * Compare two states
   * 
   * @return (Boolean) True if both states are equal, otherwise false
   */
  public boolean equals(Object obj) {
    if (!(obj instanceof State))
      return false;
    if (obj == this)
      return true;
    return this.name.equals(((State) obj).name);
  }

  /**
   * Printable representation of the state
   * 
   * @return (String) String that represents the state
   */
  public String toString() {
    return name;
  }

  /**
   * Rename the state
   * 
   * @param Newname
   *          - State new name
   */
  public void rename(String Newname) {
    name = Newname;
  }

  /**
   * Check if the state is in a states set.
   * 
   * @param stateSet
   *          - States set
   * @return (Boolean) True if the state is in the set, otherwise false
   */
  public boolean inSet(Set<State> stateSet) {
    for (State s : stateSet) {
      if (s.equals(this)) {
        return true;
      }
    }
    return false;
  }
}
