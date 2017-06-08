package automata;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import utils.Quintuple;
import utils.Triplet;

/**
 * @author Elisa Boselli
 *
 */
public final class NFAPila extends AP {

  private Boolean emptyStackEnd;
  private Boolean isDeterministic;
  private State currentState;
  private Integer v = 0;
  private final Integer DEPTH = 30;

  /**
   * Constructor of the class - returns a NFAPila object
   * 
   * @param states
   *          - states of the NFAPila
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
  public NFAPila(Set<State> states, Set<Character> alphabet, Set<Character> stackAlphabet,
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
    System.out.println("Is a NFA Pila");
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
   * Implement abstract method delta. Find all possible transitions from a state with a certain
   * current character, if any.
   * 
   * @param from
   *          - Current state.
   * @param c
   *          - Current character.
   * @return (State's set) Transition applications.
   */
  @Override
  public Set<Quintuple<State, Character, Character, String, State>> delta(State from, Character c) {
    if (stack.isEmpty()) {
      return null;
    }
    Character top = stack.peek();
    Set<Quintuple<State, Character, Character, String, State>> result = new HashSet<Quintuple<State, Character, Character, String, State>>();

    for (Quintuple<State, Character, Character, String, State> transition : transitions) {
      if (transition.first().equals(from)
          && (transition.second().equals(c) || transition.second().equals(Lambda))
          && ((transition.third().equals(top)) || transition.third().equals(Joker))) {
        result.add(transition);
      }
    }
    return result;
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
    System.out.println("Processing ...");
    return depthAccept(string, 0);
  }

  /**
   * Redefine the accept method with a depth
   * 
   * @param string
   *          - Word to be tried in the automaton.
   * @param int - Current depth.
   * @return (Boolean) True if the word was accepted, otherwise false.
   */
  private boolean depthAccept(String string, int d) {
    if (d == DEPTH) {
      return false;
    }
    int strLength = string.length();

    if (strLength == 0) {
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
      } else {
        return false;
      }
    }
    currentState = this.initial;
    Character currentCharacter;
    Set<Quintuple<State, Character, Character, String, State>> possibleTransitions;
    List<Triplet<State, String, Stack<Character>>> futureSteps = new LinkedList<Triplet<State, String, Stack<Character>>>();

    currentCharacter = string.charAt(0);
    possibleTransitions = delta(currentState, currentCharacter);
    if (possibleTransitions.isEmpty()) {
      return false;
    }

    Character t = stack.pop();
    for (Quintuple<State, Character, Character, String, State> transition : possibleTransitions) {
      if (!transition.second().equals(Lambda)) {
        futureSteps.add(new Triplet<State, String, Stack<Character>>(transition.fifth(), string
            .substring(1, strLength), applyTransitionToStack(stack, transition.fourth(), t)));
      } else {
        futureSteps.add(new Triplet<State, String, Stack<Character>>(transition.fifth(), string,
            applyTransitionToStack(stack, transition.fourth(), t)));
      }
    }

    boolean accpt = false;
    for (Triplet<State, String, Stack<Character>> step : futureSteps) {
      initial = step.first();
      stack = step.third();
      accpt = depthAccept(step.second(), d + 1);
      if (accpt) {
        return accpt;
      }
    }
    return accpt;
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
    System.out.println("\n\nNFA Pila:");
    System.out.println("\nInitial state: " + initial.toString());
    if (!finalStates.isEmpty()) {
      System.out.print("\nFinal states: ");
      for (State s : finalStates) {
        System.out.print(s.toString() + ',');
      }
    }
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
   * Apply a transition to a certain stack.
   * 
   * @param stk
   *          - Current stack.
   * @param str
   *          - Current string (word).
   * @param top
   *          - Stack's top
   * @return (Stack) New stack, copied from the first one, but with the transition applied.
   */
  private Stack<Character> applyTransitionToStack(Stack<Character> stk, String str, Character top) {
    Stack<Character> result = (Stack<Character>) stk.clone();
    Character c;
    if (!str.equals(Lambda.toString())) {
      for (int i = str.length() - 1; i >= 0; i--) {
        c = str.charAt(i);
        if (c.equals(Joker)) {
          result.push(top);
        } else {
          result.push(str.charAt(i));
        }
      }
    }
    return result;
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
      if (transition.first().equals(state) && transition.second().equals(Lambda.toString())
          && transition.fourth().equals(Lambda.toString()) && !stack.isEmpty()) {
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