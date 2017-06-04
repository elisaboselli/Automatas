package main;

import java.io.File;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import utils.Quintuple;
import automata.DFAPila;
import automata.State;

public class Main {

  public static void main(String[] args) {

    System.out.println("//--------------------------------------------------//");
    System.out.println("//------------- AUTOMATAS Y LENGUAJES --------------//");
    System.out.println("//--------------------- (1961) ---------------------//");
    System.out.println("//----------------- Elisa Boselli ------------------//");
    System.out.println("//--------------------------------------------------//");

    boolean deterministic;
    boolean emptyStackEnd = true;

    State initial = null;
    Character stackInitial = null;
    Set<State> states = new HashSet<State>();
    Set<Character> alphabet = new HashSet<Character>();
    Set<Character> stackAlphabet = new HashSet<Character>();
    Set<Quintuple<State, Character, Character, String, State>> transitions = new HashSet<Quintuple<State, Character, Character, String, State>>();
    Set<State> finalStates = new HashSet<State>();

    Scanner scan = new Scanner(System.in);

    String option = "2";
    // System.out.println("\n\nPara cargar un autómata pila deterministico de dot ingrese 1.");
    // System.out.println("Para cargar una gramatica ingrese 2.");
    // System.out.println("Para salir ingrese 3.");
    // String option = scan.nextLine();
    // while (!option.equals("1") && !option.equals("2") && !option.equals("3")) {
    // System.out.println("Ingrese una opción válida");
    // option = scan.nextLine();
    // }

    if (option.equals("1")) {

      deterministic = true;

      System.out.println("Ingrese el nombre del archivo del automata");
      String fileName = scan.nextLine();
      File file = new File("/tmp/" + fileName + ".txt");
      while (!file.exists()) {
        System.out.println("Ingrese un nombre de archivo correcto");
        fileName = scan.nextLine();
      }

      DFAPila atm = new DFAPila(states, alphabet, stackAlphabet, transitions, stackInitial,
          initial, finalStates, deterministic, emptyStackEnd);
      atm.from_dot(file);
      // atm.from_dot(new File("/home/koodu/Elisa/AUTOMATAS/workspace/Automata/prueba.txt"));
      if (!atm.rep_ok(deterministic, emptyStackEnd)) {
        throw new IllegalArgumentException(
            "The built automaton does not meet the requested conditions.");
      }

      // atm.report();

      System.out.println("Ingrese la cadena a probar");
      String cad = scan.nextLine();
      while (cad.isEmpty()) {
        System.out.println("Ingrese una cadena valida");
        cad = scan.nextLine();
      }

      // String cad = "aab";
      // String cad = "11+11=1111";

      Boolean result = atm.accepts(cad, emptyStackEnd);

      if (result) {
        System.out.print("\n La cadena \"" + cad + "\" fue aceptada por el automata ");
      } else {
        System.out.print("\n La cadena \"" + cad + "\" no fue aceptada por el automata ");
      }

      if (emptyStackEnd) {
        System.out.println("por pila vacía.");
      } else {
        System.out.println("por estado final.");
      }
    }

    if (option.equals("2")) {

      deterministic = false;

      // System.out.println("Ingrese el nombre del archivo de la gramática");
      // String fileName = scan.nextLine();
      // File file = new File("/tmp/" + fileName + ".txt");
      // while (!file.exists()) {
      // System.out.println("Ingrese un nombre de archivo correcto");
      // fileName = scan.nextLine();
      // }

      File file = new File("/tmp/pruebaGram.txt");
      DFAPila atm = new DFAPila(states, alphabet, stackAlphabet, transitions, stackInitial,
          initial, finalStates, deterministic, emptyStackEnd);
      atm.from_gram(file);
      if (!atm.rep_ok(deterministic, emptyStackEnd)) {
        throw new IllegalArgumentException(
            "The built automaton does not meet the requested conditions.");
      }
      System.out.println(atm.to_dot());

    }

  }
}