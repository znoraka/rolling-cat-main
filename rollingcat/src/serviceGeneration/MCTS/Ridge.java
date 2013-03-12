package serviceGeneration.MCTS;

public class Ridge {

	private Node a;
	private Node b;
	private float weight;
	
	public Ridge(Node a, Node b, float weight) {
		super();
		this.a = a;
		this.b = b;
		this.weight = weight;
	}
	
	public Node getA() {
		return a;
	}
	
	public void setA(Node a) {
		this.a = a;
	}
	
	public Node getB() {
		return b;
	}
	
	public void setB(Node b) {
		this.b = b;
	}
	
	public float getWeight() {
		return weight;
	}
	
	public void setWeight(float weight) {
		this.weight = weight;
	}
	
	
}
