package automata;

import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.lang.StringBuffer;
import java.util.regex;

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
		//TODO ASK
			if(q.contains(o)){
				return o;
			}
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

    public DAFPila from_dot(String fileName){
      BufferedReader br = new BufferedReader(new FileReader(fileName + ".txt"));
      try {
        String line = br.readLine();

        while (line != null) {
					String [] aux;
					String nt;
					Character tc, tp;
					State from, to;
					Quintuple<State,Character,Character,String,State> auxTransition;

					Pattern initialState = Pattern.compile("^(inic->).*");
					Matcher matInitalState = initialState.matcher(line);

					Pattern finalState = Pattern.compile("^.*(\[).*(doublecircle).*");
					Matcher matFinalState = finalState.matcher(line);

					Pattern transition = Pattern.compile("^(q).*(->).*");
					Matcher matTransition = transition.matcher(line);

     			if(matInitalState.matches()){
						aux = line.split("inic-> | ;");
						this.initial.add(new State(aux[0].trim()));
						this.states.add(this.initial);
					}

					if(matFinalState.matches()){
						aux = line.split("\[");
						this.finalStates.add(new State(aux[0].trim()));
						this.states.add(new State(aux[0].trim()));
					}

					if(matTransition.matches()){
						aux = line.split("-> | \s |  \" | /");

						from = new State(aux[0].trim());
						to = new State(aux[1].trim());
						tc = aux[3].trim();
						tp = aux[4].trim();
						nt = aux[5].trim();

						this.states.add(from);
						this.states.add(to);

						auxTransition = new Quintuple(from,tc,tp,nt,to);
						this.transitions.add(auxTransition);

					}

					line = br.readLine();
				}

        } finally {
          br.close();
        }
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
