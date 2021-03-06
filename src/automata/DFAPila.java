package automata;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import utils.Quintuple;

/**
 * @author Elisa Boselli
 *
 */
public final class DFAPila extends AP {

  private Boolean emptyStackEnd;
  private Boolean isDeterministic;
  private State currentState;
  private Integer v = 0;

  /**
   * Constructor of the class - returns a DFAPila object
   * 
   * @param states
   *          - states of the DFAPila
   * @param alphabet
   *          - the alphabet of the automaton
   * @param stackAlphabet
   *          - the alphabet of the stack
   * @param transitions
   *          - transitions of the automaton
   * @param stackInitial
   *          - a Character which represents the initial element of the stack
   * @param initial
   *          - initial State of the automaton
   * @param final_states
   *          - acceptance states of the automaton
   * @throws IllegalArgumentException
   */
  public DFAPila(Set<State> states, Set<Character> alphabet, Set<Character> stackAlphabet,
      Set<Quintuple<State, Character, Character, String, State>> transitions,
      Character stackInitial, State initial, Set<State> final_states, boolean deterministic)
      throws IllegalArgumentException {
    this.states = states;
    this.alphabet = alphabet;
    this.stackAlphabet = stackAlphabet;
    stackAlphabet.add(Lambda); // the character lambda is used in the stack to know when do a pop
    stackAlphabet.add(Joker); // the mark of the stack
    this.transitions = transitions;
    this.stackInitial = stackInitial;
    this.initial = initial;
    this.finalStates = final_states;
    stack = new Stack<Character>();
    stack.push(Initial); // insert the mark in the stack
    isDeterministic = deterministic;
    if (!rep_ok()) {
      throw new IllegalArgumentException();
    }
    System.out.println("Is a DFA Pila");
  }

  /**
   * Set the automaton end.
   * 
   * @param ese
   *          - Represents the two possible terminations, true by empty stack and false by final
   *          state.
   */
  public void setEnd(boolean ese) {
    emptyStackEnd = ese;
  }

  /**
   * Implement abstract method delta. Find the possible transition from a state with a certain
   * current character, if any.
   * 
   * @param from
   *          - Current state.
   * @param c
   *          - Current character.
   * @return (State) Transition application.
   */
  @Override
  public State delta(State from, Character c) {
    if (stack.isEmpty()) {
      return null;
    }
    Character top = stack.pop();
    Character aux;
    for (Quintuple<State, Character, Character, String, State> transition : transitions) {
      if (transition.first().equals(from) && transition.second().equals(c)
          && ((transition.third().equals(top)) || transition.third().equals(Joker))) {
        System.out.print(transition.toString() + " -> Stack:[");
        if (!((transition.fourth().charAt(0)) == ((char) Lambda))) {
          for (int i = (transition.fourth()).length() - 1; i >= 0; i--) {
            aux = transition.fourth().charAt(i);
            if (Joker.equals(aux)) {
              stack.push(top);
            } else {
              stack.push((transition.fourth()).charAt(i));
            }
          }
        }
        Object[] auxArray = stack.toArray();
        for (int i = 0; i < auxArray.length; i++) {
          System.out.print(auxArray[i]);
          if (i < auxArray.length - 1) {
            System.out.print(",");
          }
        }
        System.out.println("]");
        return transition.fifth();
      }
    }
    return null;
  }

  /**
   * Implement abstract method accept. Determine if the automaton accepts a certain String.
   * 
   * @param string
   *          - Word to be tried in the automaton.
   * @return (Boolean) True if the word was accepted, otherwise false.
   */
  @Override
  public boolean accepts(String string) {
    currentState = this.initial;
    Character currentCharacter;
    State auxState = null;
    System.out.println("Processing ...");
    for (int i = 0; i < string.length(); i++) {
      currentCharacter = string.charAt(i);
      auxState = delta(currentState, currentCharacter);
      if (auxState == null) {
        return false;
      }
      currentState = auxState;
    }

    if (!stack.peek().equals(Initial)) {
      System.out.println("Emptying the stack");
      Quintuple<State, Character, Character, String, State> est = emptyStackTransition(currentState);
      while (est != null && !stack.peek().equals(Initial)) {
        System.out.print(est.toString() + " -> Stack:[");
        stack.pop();
        currentState = est.fifth();
        Object[] auxArray = stack.toArray();
        for (int i = 0; i < auxArray.length; i++) {
          System.out.print(auxArray[i]);
          if (i < auxArray.length - 1) {
            System.out.print(",");
          }
        }
        System.out.println("]");
        est = emptyStackTransition(currentState);
      }
    }

    if ((emptyStackEnd && stack.peek().equals(Initial))
        || (!emptyStackEnd && currentState.inSet(finalStates))) {
      return true;
    }
    return false;
  }

  /**
   * Change the automaton termination. From empty stack to final state and from final state to empty
   * stack.
   */
  public void switchEnd() {
    State f = new State("FF" + v.toString());
    v++;
    states.add(f);
    if (emptyStackEnd) {
      // switch to end by final state
      finalStates.add(f);
      Quintuple<State, Character, Character, String, State> newTrans;
      for (State s : states) {
        if (!s.equals(f)) {
          newTrans = new Quintuple<State, Character, Character, String, State>(s, Lambda, Initial,
              Lambda.toString(), f);
          transitions.add(newTrans);
        }
      }
      emptyStackEnd = false;
    } else {
      // switch to end by empty stack
      Quintuple<State, Character, Character, String, State> newTrans;
      for (State s : finalStates) {
        newTrans = new Quintuple<State, Character, Character, String, State>(s, Lambda, Joker,
            Lambda.toString(), f);
        transitions.add(newTrans);
      }
      finalStates = new HashSet<State>();
      newTrans = new Quintuple<State, Character, Character, String, State>(f, Lambda, Joker,
          Lambda.toString(), f);
      transitions.add(newTrans);
      emptyStackEnd = false;
    }
  }

  /**
   * RepOK method.
   * 
   * @return (Boolean) True if the automata was built correctly, otherwise false.
   */
  public boolean rep_ok() {

    // If it comes from dot it is supposed to be deterministic.
    if (isDeterministic) {
      for (Quintuple<State, Character, Character, String, State> trans : transitions) {

        // Check that, if it is deterministic and ends by final state, has not spontaneous
        // transitions
        if (!emptyStackEnd && trans.second().equals(Lambda)) {
          System.out
              .println("A deterministic automaton that ends by final state should not have spontaneous transitions.");
          System.out.println("Transition found: " + trans.toString());
          return false;
        }

        // Check that there are not two transitions that start from the same state, and with the
        // same current character and top of stack arrive to different states.
        for (Quintuple<State, Character, Character, String, State> trans2 : transitions) {
          if ((!trans.equals(trans2)) && trans.firstThreeEquals(trans2)) {
            System.out.println("The automaton is not deterministic, similar transitions found: ");
            System.out.println("        " + trans.toString());
            System.out.println("        " + trans2.toString());
            return false;
          }
        }
      }
    }

    // Check that all the states are reachable
    boolean isReachable;
    for (State s : states) {
      if (!s.equals(initial)) {
        isReachable = false;
        for (Quintuple<State, Character, Character, String, State> transition : transitions) {
          if (transition.fifth().equals(s)) {
            isReachable = true;
            break;
          }
        }
        if (isReachable == false) {
          System.out.println("State " + s.toString() + " is not reachable.");
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Show printable representation of the automaton.
   */
  public void report() {
    System.out.println("\n\nDAF Pila:");
    System.out.println("\nInitial state: " + initial.toString());
    if (!finalStates.isEmpty()) {
      System.out.print("\nFinal states: ");
      for (State s : finalStates) {
        System.out.print(s.toString() + ',');
      }
    }
    System.out.println("\n");
    System.out.print("All states: ");
    for (State s : states) {
      System.out.print(s.toString() + ',');
    }
    System.out.println("\n");
    System.out.print("Alphabet: ");
    for (Character c : alphabet) {
      System.out.print(c.toString() + ',');
    }
    System.out.println("\n");
    System.out.print("Stack Alphabet: ");
    for (Character c : stackAlphabet) {
      System.out.print(c.toString() + ',');
    }
    System.out.println("\n");
    System.out.println("Transitions: ");
    for (Quintuple<State, Character, Character, String, State> t : transitions) {
      System.out.println("        " + t.toString());
    }

    if (emptyStackEnd) {
      System.out.println("\nIt is assumed that the automaton will end by empty stack.");
    } else {
      System.out.println("\nIt is assumed that the automaton will end by final state.");
    }
  }

  /**
   * Look up for empty stack transitions.
   * 
   * @param state
   *          - Current state.
   * @return (Transition) The corresponding transition, if any.
   */
  private Quintuple<State, Character, Character, String, State> emptyStackTransition(State state) {
    for (Quintuple<State, Character, Character, String, State> transition : transitions) {
      if (transition.first().equals(state) && transition.fourth().equals(Lambda.toString())
          && !stack.isEmpty()) {
        return transition;
      }
    }
    return null;
  }

  /**
   * Get the automaton current termination.
   *
   * @return (Boolean) True if it is by empty stack, false if it is by final state.
   */
  public boolean getAutomatonEnd() {
    return emptyStackEnd;
  }

  /**
   * Get the automaton current determinism.
   *
   * @return (Boolean) True if it is deterministic, otherwise false.
   */
  public boolean getDeterministic() {
    return isDeterministic;
  }
}