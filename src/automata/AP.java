package automata;

import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

import utils.Quintuple;



public abstract class AP {

	public static final Character Lambda = '_';
	public static final Character Joker = '@';
	public static final Character Initial = 'Z';
	
	protected State initial;
    protected Character stackInitial;
    protected Stack<Character> stack; //stack of the automaton
    protected Set<State> states;
    protected Set<Character> alphabet;
    protected Set<Character> stackAlphabet; //Alphabet of the stack
    protected Set<Quintuple<State,Character,Character,String,State>> transitions; //delta function
    protected Set<State> finalStates;
	
	
    /*
     * A static constructor should be implemented depending on the final design of the automaton
     * */
    
    
    public static State getElemFromSet(Set<State> q,State o){
		//TODO this method have to be implemented.
    	return null;
     }
          
    public Set<State> final_states(){
        return finalStates;
    }
     
    public State initial_state(){
        return initial;
    }
    
    public Set<Character> alphabet(){
        return alphabet;
    }
    
    public Set<State> states(){
        return states;
    }
     
    public final String to_dot(){
        //assert rep_ok();
        char quotes= '"';
        Iterator i;
        String aux;
        aux = "digraph{\n";
        aux = aux + "inic[shape=point];\n" + "inic->" + this.initial.name() + ";\n";
        i = this.transitions.iterator();
        while (i.hasNext()) {
           Quintuple quintuple =(Quintuple) i.next();
           aux = aux + quintuple.first().toString() + "->" + quintuple.fifth().toString() + " [label=" +quotes+ quintuple.second().toString() +"/"+ quintuple.third()+"/"+quintuple.fourth()+ quotes+ "];\n";
        }
        aux = aux+ "\n";
        i = this.finalStates.iterator();
        while (i.hasNext()){
            State estado = (State) i.next();
            aux = aux + estado.name() + "[shape=doublecircle];\n";
        }
        aux = aux + "}";
        return aux;
    }
    
    /**
    * this methods should be implemented in DFAPila 
    */
    public abstract boolean accepts(String string);
     
    public abstract Object delta(State from, Character c);
    
    
}
