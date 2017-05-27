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

    Scanner scan = new Scanner(System.in);

    State initial = null;
    Character stackInitial = null;
    Set<State> states = new HashSet<State>();
    Set<Character> alphabet = new HashSet<Character>();
    Set<Character> stackAlphabet = new HashSet<Character>();
    Set<Quintuple<State, Character, Character, String, State>> transitions = new HashSet<Quintuple<State, Character, Character, String, State>>();
    Set<State> finalStates = new HashSet<State>();

    // System.out.println("Ingrese el nombre del archivo del automata");
    // String fileName = scan.nextLine();
    // File file = new File("/tmp/" + fileName + ".txt");
    // while (!file.exists()) {
    // System.out.println("Ingrese un nombre de archivo correcto");
    // fileName = scan.nextLine();
    // }

    DFAPila atm = new DFAPila(states, alphabet, stackAlphabet, transitions, stackInitial, initial,
        finalStates);
    // atm.from_dot(file);
    atm.from_dot(new File("/tmp/prueba.txt"));

    // atm.report();

    // System.out.println("Ingrese la cadena a probar");
    // String cad = scan.nextLine();
    // while (cad.isEmpty()) {
    // System.out.println("Ingrese una cadena valida");
    // cad = scan.nextLine();
    // }

    String cad = "acb";
    Boolean emptyStackEnd = true;

    Boolean result = atm.accepts(cad, emptyStackEnd);

    if (result) {
      System.out.println("\n La cadena \"" + cad + "\" fue aceptada por el automata.");
    } else {
      System.out.println("\n La cadena \"" + cad + "\" no fue aceptada por el automata.");
    }
  }
}