package main;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import utils.Quintuple;
import automata.DFAPila;
import automata.NFAPila;
import automata.State;

public class Main {

  static boolean deterministic;
  static boolean emptyStackEnd;
  static Scanner scan = new Scanner(System.in);

  public static void main(String[] args) {

    DFAPila dfa = null;
    NFAPila nfa = null;
    String str = null;

    System.out.println("//--------------------------------------------------//");
    System.out.println("//------------- AUTOMATAS Y LENGUAJES --------------//");
    System.out.println("//--------------------- (1961) ---------------------//");
    System.out.println("//----------------- Elisa Boselli ------------------//");
    System.out.println("//--------------------------------------------------//");

    int op = displayMenu();
    while (op != 8) {
      switch (op) {
      case 1:
        // Load DFA
        dfa = loadDFAPila();
        break;

      case 2:
        // Load NFA
        nfa = loadNFAPila();
        break;

      case 3:
        // Show automaton
        if (deterministic) {
          if (dfa != null) {
            dfa.report();
          } else {
            System.out.println("There is not deterministic automaton loaded.");
          }
        } else {
          if (nfa != null) {
            nfa.report();
          } else {
            System.out.println("There is not non-deterministic automaton loaded.");
          }
        }
        break;

      case 4:
        // Switch automaton
        if (deterministic) {
          if (nfa != null) {
            deterministic = false;
            System.out.println("Switch to non-deterministic automaton.");
          } else {
            System.out.println("There is not non-deterministic automaton loaded.");
          }
        } else {
          if (dfa != null) {
            deterministic = true;
            System.out.println("Switch to deterministic automaton.");
          } else {
            System.out.println("There is not deterministic automaton loaded.");
          }
        }
        break;

      case 5:
        // Switch automaton end
        if (deterministic) {
          if (emptyStackEnd) {
            System.out.println("Switch dfa from end by empty stack to end by final state.");
          } else {
            System.out.println("Switch dfa from end by final to end by empty stack.");
          }
          dfa.switchEnd();
          emptyStackEnd = dfa.getAutomatonEnd();

        } else {
          if (emptyStackEnd) {
            System.out.println("Switch dfa from end by empty stack to end by final state.");
          } else {
            System.out.println("Switch dfa from end by final to end by empty stack.");
          }
          // Implement switch
        }
        break;

      case 6:
        // Export automaton to dot
        if (deterministic) {
          if (dfa != null) {
            System.out.println("Deterministic automaton to dot:");
            System.out.println(dfa.to_dot());
          } else {
            System.out.println("There is not deterministic automaton loaded.");
          }
        } else {
          if (nfa != null) {
            System.out.println("Non-deterministic automaton to dot.");
            System.out.println(nfa.to_dot());
          } else {
            System.out.println("There is not non-deterministic automaton loaded.");
          }
        }
        break;

      case 7:
        // Try word
        if (dfa != null || nfa != null) {
          str = getWord();
          if (deterministic) {
            if (dfa != null) {
              tryDFAPila(dfa, str);
            } else {
              System.out.println("There is not deterministic automaton loaded.");
            }

          } else {
            if (nfa != null) {
              tryNFAPila(nfa, str);
            } else {
              System.out.println("There is not non-deterministic automaton loaded.");
            }
          }
        } else {
          System.out.println("Please, load an automaton before trying to test a word");
        }
        break;
      }
      waitUser();
      op = displayMenu();
    }
    scan.close();
    System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    System.out.println("Bye ...");
  }

  private static int displayMenu() {
    System.out.println("Enter the desired option");
    System.out.println("1 - Load finite deterministic stack automaton from dot");
    System.out.println("2 - Load finite non-deterministic stack automaton from grammar");
    System.out.println("3 - Show current automaton");
    System.out.println("4 - Switch current automaton");
    System.out.println("5 - Switch automaton end");
    System.out.println("6 - Export automaton to dot");
    System.out.println("7 - Try word in the automaton");
    System.out.println("8 - Exit");
    int aux = scan.nextInt();
    scan.nextLine();
    while (aux < 0 || aux > 8) {
      System.out.println("Please enter a valid option");
      aux = scan.nextInt();
      scan.nextLine();
    }
    return aux;
  }

  private static void waitUser() {
    System.out.println("Press enter to continue\n");
    scan.nextLine();
    System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    try {
      Runtime.getRuntime().exec("clear");
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  private static DFAPila loadDFAPila() {
    File file = loadFile();
    State initial = null;
    Character stackInitial = null;
    Set<State> states = new HashSet<State>();
    Set<Character> alphabet = new HashSet<Character>();
    Set<Character> stackAlphabet = new HashSet<Character>();
    Set<Quintuple<State, Character, Character, String, State>> transitions = new HashSet<Quintuple<State, Character, Character, String, State>>();
    Set<State> finalStates = new HashSet<State>();

    deterministic = true;
    DFAPila atm = new DFAPila(states, alphabet, stackAlphabet, transitions, stackInitial, initial,
        finalStates, deterministic);
    emptyStackEnd = atm.from_dot(file);
    atm.setEnd(emptyStackEnd);
    if (!atm.rep_ok()) {
      atm = null;
      System.out.println("Automaton deleted");
    }
    return atm;

  }

  private static NFAPila loadNFAPila() {
    File file = loadFile();
    State initial = null;
    Character stackInitial = null;
    Set<State> states = new HashSet<State>();
    Set<Character> alphabet = new HashSet<Character>();
    Set<Character> stackAlphabet = new HashSet<Character>();
    Set<Quintuple<State, Character, Character, String, State>> transitions = new HashSet<Quintuple<State, Character, Character, String, State>>();
    Set<State> finalStates = new HashSet<State>();

    deterministic = false;
    NFAPila atm = new NFAPila(states, alphabet, stackAlphabet, transitions, stackInitial, initial,
        finalStates, deterministic);
    emptyStackEnd = atm.from_gram(file);
    atm.setEnd(emptyStackEnd);
    if (!atm.rep_ok()) {
      atm = null;
      System.out.println("Automaton deleted");
    }
    return atm;
  }

  private static File loadFile() {
    System.out.print("Enter the name of the ");
    if (deterministic) {
      System.out.println("automaton dot file");
    } else {
      System.out.println("grammar file");
    }
    String fileName = scan.nextLine();
    File file = new File("/tmp/" + fileName + ".txt");
    while (!file.exists()) {
      System.out.println("Please enter a valid file name");
      fileName = scan.nextLine();
    }
    return file;
  }

  private static String getWord() {
    System.out.println("Enter the string to test");
    String str = scan.nextLine();
    while (str.isEmpty()) {
      System.out.println("Please enter a valid string");
      str = scan.nextLine();
    }
    return str;
  }

  private static void tryDFAPila(DFAPila dfa, String str) {
    Boolean result = dfa.accepts(str);

    if (result) {
      System.out.print("\n The string \"" + str + "\" was accepted by the automaton ");
    } else {
      System.out.print("\n The string \"" + str + "\" was not accepted by the automaton ");
    }

    if (emptyStackEnd) {
      System.out.println("by empty stack.");
    } else {
      System.out.println("by final state.");
    }
  }

  private static void tryNFAPila(NFAPila nfa, String str) {
    Boolean result = nfa.accepts(str);

    if (result) {
      System.out.print("\n The string \"" + str + "\" was accepted by the automaton ");
    } else {
      System.out.print("\n The string \"" + str + "\" was not accepted by the automaton ");
    }

    if (emptyStackEnd) {
      System.out.println("by empty stack.");
    } else {
      System.out.println("by final state.");
    }
  }
}