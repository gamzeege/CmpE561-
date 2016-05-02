package cmpe561project2;

public class Term {
	String name;
	Double probability;
	String tag;
	public Term previous;
	boolean isknown;
	public Term(String name, Double prob, String tag, Term previous) {
		super();
		this.name = name;
		this.probability = prob;
		this.tag = tag;
		this.previous = previous;
	}
	public Term(String name, String tag) {
		super();
		this.name = name;
		this.tag = tag;
		this.probability= 0.0;
		this.previous= null;
	}
	
}
