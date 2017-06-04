package main;

import java.io.File;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import utils.Quintuple;
import automata.DFAPila;
import automata.NFAPila;
import automata.State;

public class Main {

  static boolean deterministic;
  static boolean emptyStackEnd = true;

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
    while (op != 5) {
      switch (op) {
      case 1:
        deterministic = true;
        dfa = loadDFAPila();
      case 2:
        deterministic = false;
        nfa = loadNFAPila();
      case 3:
        if (deterministic) {
          dfa.report();
        } else {
          nfa.report();
        }
      case 4:
        str = getWord();
        if (deterministic) {
          tryDFAPila(dfa, str);
        } else {
          tryNFAPila(nfa, str);
        }
      }
      op = displayMenu();
    }
  }

  private static int displayMenu() {
    Scanner scan = new Scanner(System.in);
    System.out.println("Enter the desired option");
    System.out.println("1 - Load finite deterministic stack automaton from dot");
    System.out.println("2 - Load finite non-deterministic stack automaton from grammar");
    System.out.println("3 - Show current automaton");
    System.out.println("4 - Try word in the automaton");
    System.out.println("5 - Exit");
    System.out.println("\n");
    int aux = scan.nextInt();
    while (aux < 0 || aux > 4) {
      System.out.println("Please enter a valid option");
      aux = scan.nextInt();
    }
    scan.close();
    return aux;
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

    DFAPila atm = new DFAPila(states, alphabet, stackAlphabet, transitions, stackInitial, initial,
        finalStates, deterministic, emptyStackEnd);
    atm.from_dot(file);
    if (!atm.rep_ok()) {
      throw new IllegalArgumentException(
          "The built automaton does not meet the requested conditions.");
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

    NFAPila atm = new NFAPila(states, alphabet, stackAlphabet, transitions, stackInitial, initial,
        finalStates, deterministic, emptyStackEnd);
    atm.from_dot(file);
    if (!atm.rep_ok()) {
      throw new IllegalArgumentException(
          "The built automaton does not meet the requested conditions.");
    }
    return atm;
  }

  private static File loadFile() {
    Scanner scan = new Scanner(System.in);
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
    scan.close();
    return file;
  }

  private static String getWord() {
    Scanner scan = new Scanner(System.in);
    System.out.println("Enter the string to test");
    String str = scan.nextLine();
    while (str.isEmpty()) {
      System.out.println("Please enter a valid string");
      str = scan.nextLine();
    }
    scan.close();
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