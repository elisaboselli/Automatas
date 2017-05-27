package automata;

import java.util.Set;
import java.util.Stack;

import utils.Quintuple;

public final class DFAPila extends AP {

  private Object nroStates[];
  private Stack<Character> stack; // the stack of the automaton
  private Boolean emptyStackEnd;
  private State currentState;

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
      Character stackInitial, State initial, Set<State> final_states)
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
    nroStates = states.toArray();
    stack = new Stack<Character>();
    // why add?? it should be push, doesn't it?
    stack.add(Joker); // insert the mark in the stack
    if (!rep_ok()) {
      throw new IllegalArgumentException();
    }
    System.out.println("Is a DFA Pila");
  }

  @Override
  public State delta(State from, Character c) {
    Character top = stack.pop();
    for (Quintuple<State, Character, Character, String, State> transition : transitions) {
      if (transition.first().equals(from) && transition.second().equals(c)
          && (transition.third().equals(top) || transition.third().equals('_'))) {
        System.out.print(transition.toString() + " -> Stack:[");
        if (!((transition.fourth().charAt(0)) == ((char) Lambda))) {
          for (int i = (transition.fourth()).length() - 1; i >= 0; i--) {
            stack.push((transition.fourth()).charAt(i));
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

  @Override
  public boolean accepts(String string, boolean end) {
    emptyStackEnd = end;
    currentState = this.initial;
    Character currentCharacter;
    State auxState = null;
    for (int i = 0; i < string.length(); i++) {
      currentCharacter = string.charAt(i);
      auxState = delta(currentState, currentCharacter);
      if (auxState == null) {
        return false;
      }
      currentState = auxState;
    }
    if ((emptyStackEnd && stack.isEmpty()) || (!emptyStackEnd && currentState.inSet(finalStates))) {
      return true;
    }
    return false;
  }

  public boolean rep_ok() {
    // TODO this method have to be implemented
    return true;
  }

  public void report() {
    System.out.println("\n\nDAF Pila:");
    System.out.println("\nInitial state: " + initial.toString());
    System.out.print("\nFinal states: ");
    for (State s : finalStates) {
      System.out.print(s.toString() + ',');
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

    if (finalStates.isEmpty()) {
      emptyStackEnd = true;
      System.out.println("\nSe asume que el autómata terminará por pila vacia.");
    } else {
      emptyStackEnd = false;
      System.out.println("\nSe asume que el autómata terminará por estado final.");
    }
  }
}
