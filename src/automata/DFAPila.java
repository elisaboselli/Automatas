package automata;

import java.util.Set;
import java.util.Stack;

import utils.Quintuple;

public final class DFAPila extends AP{

	private   Object nroStates[] ;
    private Stack<Character> stack; //the stack of the automaton


    /**
     * Constructor of the class - returns a DFAPila object
     * @param states - states of the DFAPila
     * @param alphabet - the alphabet of the automaton
     * @param stackAlphabet - the alphabet of the stack
     * @param transitions - transitions of the automaton
     * @param stackInitial - a Character which represents the initial element of the stack
     * @param initial - initial State of the automaton
     * @param final_states - acceptance states of the automaton
     * @throws IllegalArgumentException
     */
    public DFAPila(
            Set<State> states,
            Set<Character> alphabet,
            Set<Character> stackAlphabet,
            Set<Quintuple<State, Character,Character,String, State>> transitions,
            Character stackInitial,
            State initial,
            Set<State> final_states)
            throws IllegalArgumentException
    {
        this.states = states;
        this.alphabet = alphabet;
        this.stackAlphabet = stackAlphabet;
        stackAlphabet.add(Lambda); //the character lambda is used in the stack to know when do a pop
        stackAlphabet.add(Joker); //the mark of the stack
        this.transitions = transitions;
        this.stackInitial = stackInitial;
        this.initial = initial;
        this.finalStates = final_states;
        nroStates =  states.toArray();
        stack = new Stack<Character>();
				//why add?? it should be push, doesn't it?
        stack.add(Joker); //insert the mark in the stack
        if (!rep_ok()){
            throw new  IllegalArgumentException();
        }
        System.out.println("Is a DFA Pila");
    }


    @Override
    public State delta(State from, Character c){
		//TODO this method have to be implemented7
			Character top = stack.pop();
			for (Quintuple transition : transitions){
					if(transition.first().equals(from) && transition.second().equals(c) && transition.third().equals(top)){
						System.out.println(transition.toString());
						for(int i = transition.fourth().length(); i >= 0; i--){
   						stack.push(transition.fourth().charAt(i));
						}
						stack.push(transition.fourth());
						delta(transition.fifth(),somethingElse);
					}
			}
    	return null;
    }

    @Override
    public boolean accepts(String string) {
        //TODO this method have to be implemented
    	return false;
    }

    public boolean rep_ok() {
        //TODO this method have to be implemented
    	return false;
    }


}
