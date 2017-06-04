package automata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.Quintuple;

public abstract class AP {

  public static final Character Lambda = '_';
  public static final Character Joker = '@';
  public static final Character Initial = 'Z';

  protected State initial;
  protected Character stackInitial;
  protected Stack<Character> stack; // stack of the automaton
  protected Set<State> states;
  protected Set<Character> alphabet;
  protected Set<Character> stackAlphabet; // Alphabet of the stack
  protected Set<Quintuple<State, Character, Character, String, State>> transitions; // delta
                                                                                    // function
  protected Set<State> finalStates;

  /*
   * A static constructor should be implemented depending on the final design of the automaton
   */

  public static State getElemFromSet(Set<State> q, State o) {
    // TODO ASK
    if (q.contains(o)) {
      return o;
    }
    return null;
  }

  public Set<State> final_states() {
    return finalStates;
  }

  public State initial_state() {
    return initial;
  }

  public Set<Character> alphabet() {
    return alphabet;
  }

  public Set<State> states() {
    return states;
  }

  public void from_dot(File file) {
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(file));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    try {
      String line = br.readLine();

      String[] aux;
      String nt;
      Character tc, tp;
      Boolean found;
      State in, fin, from, to;
      Quintuple<State, Character, Character, String, State> auxTransition;

      Pattern initialState = Pattern.compile("^(inic->).*");
      Matcher matInitalState;

      Pattern finalState = Pattern.compile("^.*(\\[).*(doublecircle).*");
      Matcher matFinalState;

      Pattern transition = Pattern.compile("^(q).*(->).*");
      Matcher matTransition;

      while (line != null) {

        found = false;

        matInitalState = initialState.matcher(line);

        if (matInitalState.matches()) {
          System.out.print("Initial State found: ");
          aux = line.split("inic->|;");
          in = new State(aux[1].trim());
          if (initial == null) {
            initial = in;
          }
          System.out.println(in);
          if (!in.inSet(states)) {
            states.add(in);
          }
        }

        matFinalState = finalState.matcher(line);

        if (matFinalState.matches()) {
          System.out.print("Final State found: ");
          aux = line.split("\\[");
          fin = new State(aux[0].trim());
          System.out.println(fin);

          if (!fin.inSet(finalStates)) {
            finalStates.add(fin);
          }
          if (!fin.inSet(states)) {
            states.add(new State(aux[0].trim()));
          }
        }

        matTransition = transition.matcher(line);

        if (matTransition.matches()) {

          System.out.print("Transition found: ");
          aux = line.split("->|\\s|\"|/");

          from = new State(aux[0].trim());
          to = new State(aux[1].trim());
          tc = aux[3].trim().charAt(0);
          tp = aux[4].trim().charAt(0);
          nt = aux[5].trim();

          if (!from.inSet(states)) {
            states.add(from);
          }
          if (!to.inSet(states)) {
            states.add(to);
          }

          alphabet.add(tc);
          stackAlphabet.add(tp);

          auxTransition = new Quintuple<State, Character, Character, String, State>(from, tc, tp,
              nt, to);
          System.out.println(auxTransition.toString());
          transitions.add(auxTransition);

        }

        line = br.readLine();
      }
      br.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
    stackInitial = Initial;
    System.out.println("\n");
  }

  public final String to_dot() {
    // assert rep_ok();
    char quotes = '"';
    Iterator i;
    String aux;
    aux = "digraph{\n";
    aux = aux + "inic[shape=point];\n" + "inic->" + this.initial.name() + ";\n";
    i = this.transitions.iterator();
    while (i.hasNext()) {
      Quintuple<State, Character, Character, String, State> quintuple = (Quintuple) i.next();
      aux = aux + quintuple.first().toString() + "->" + quintuple.fifth().toString() + " [label="
          + quotes + quintuple.second().toString() + "/" + quintuple.third() + "/"
          + quintuple.fourth() + quotes + "];\n";
    }
    aux = aux + "\n";
    i = this.finalStates.iterator();
    while (i.hasNext()) {
      State estado = (State) i.next();
      aux = aux + estado.name() + "[shape=doublecircle];\n";
    }
    aux = aux + "}";
    return aux;
  }

  public void from_gram(File file) {
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(file));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    try {
      String line = br.readLine();

      String[] aux;
      Quintuple<State, Character, Character, String, State> auxTransition;

      Pattern grammarDefinition = Pattern.compile("^[A-Z](\\s)*:(\\s)*(\\<.*\\>).*");
      Matcher matGrammatDefinition;

      Pattern production = Pattern.compile("^[A-Z]+(\\s)*(->).*");
      Matcher matProduction;

      while (line != null) {

        matGrammatDefinition = grammarDefinition.matcher(line);
        if (matGrammatDefinition.matches()) {
          System.out.print("Grammar definition found: \n");
          aux = line.split(":|<|>|,|\\{|\\}");

          String[] noTerminals = aux[3].split("\\s");
          for (int i = 0; i < noTerminals.length; i++) {
            stackAlphabet.add(noTerminals[i].trim().charAt(0));
          }

          String[] terminals = aux[6].split("\\s");
          for (int i = 0; i < terminals.length; i++) {
            alphabet.add(terminals[i].trim().charAt(0));
            stackAlphabet.add(terminals[i].trim().charAt(0));
          }

          stackInitial = aux[9].trim().charAt(0);
          stackAlphabet.add(stackInitial);

          initial = new State("Q");
          states.add(initial);
          auxTransition = new Quintuple<State, Character, Character, String, State>(initial,
              Lambda, stackInitial, initial.toString() + stackInitial, initial);

          System.out.print("    Alphabet: ");
          for (Character c : alphabet) {
            System.out.print(c + " ");
          }

          System.out.print("\n    Stack Alphabet: ");
          for (Character c : stackAlphabet) {
            System.out.print(c + " ");
          }

          System.out.println("");

          for (Character c : alphabet) {
            auxTransition = new Quintuple<State, Character, Character, String, State>(initial, c,
                c, Lambda.toString(), initial);
            transitions.add(auxTransition);
            System.out.println("Transition generated: " + auxTransition.toString());
          }
        }

        matProduction = production.matcher(line);

        if (matProduction.matches()) {

          System.out.print("Production found: \n");
          aux = line.split("->|\\|");

          for (int i = 1; i < aux.length; i++) {

            auxTransition = new Quintuple<State, Character, Character, String, State>(initial,
                Lambda, aux[0].trim().charAt(0), aux[i], initial);
            transitions.add(auxTransition);
            System.out.println("        " + auxTransition.toString());

          }

        }

        line = br.readLine();
      }
      br.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
    stackInitial = Initial;
    System.out.println("\n");
  }

  /**
   * this methods should be implemented in DFAPila
   */
  public abstract boolean accepts(String string, boolean end);

  public abstract Object delta(State from, Character c);

}
