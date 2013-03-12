package serviceGeneration.MCTS;

public class GraphFactory {

	public static Graph makeTentativeGraph()
	{
		
		Graph graph = new Graph();

		//Node creation
		Node ground = new Node("Ground",ActionType.GROUND);	
		Node spring = new Node("Spring",ActionType.SPRING);	
		Node jump = new Node("Jump",ActionType.JUMP);	
		Node wast = new Node("Jump",ActionType.WAST);	
		Node fall = new Node("Fall",ActionType.FALL);	
		Node dog = new Node("Dog",ActionType.DOG);	
		Node fan = new Node("Fan",ActionType.FAN);	

		//ground 6 links 
		graph.put(ground, ground, 5.0f);
		graph.put(ground, spring, 5.0f);
		graph.put(ground, dog, 5.0f);
		graph.put(ground, fan, 5.0f);
		graph.put(ground, jump, 5.0f);
		graph.put(ground, fall, 5.0f);

		//spring 1 link
		graph.put(spring, ground, 5.0f);

		//jump 1 link
		graph.put(jump, ground, 5.0f);

		//wast 2 links
		graph.put(wast, wast, 5.0f);
		graph.put(wast, ground, 5.0f);

		//fall 2 links
		graph.put(fall, ground, 5.0f);
		graph.put(fall, fan, 5.0f);

		//dog 4 links
		graph.put(dog, dog, 5.0f);
		graph.put(dog, ground, 5.0f);
		graph.put(dog, fan, 5.0f);
		graph.put(dog, spring, 5.0f);

		//fan links
		graph.put(fan, ground, 5.0f);
		graph.put(fan, wast, 5.0f);
	
		return graph;
	}
}
