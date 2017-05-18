package automata;

public class State {

	
	private String name;

    public State(String name) {
        name = name;
    }

    public String name() {
        return name;
    }
   
    
    public boolean equals(Object obj) {
        if (!(obj instanceof State))
            return false;	
	if (obj == this)
            return true;
	return this.name.equals(((State) obj).name);
    }
    
    public String toString(){
        return name;
    }
    
    public void rename(String Newname){
        name= Newname;
    }
	
}
