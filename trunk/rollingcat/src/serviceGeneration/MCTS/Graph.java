package serviceGeneration.MCTS;

import java.util.ArrayList;

public class Graph
{		
	ArrayList<Node> noeuds;
	
	public Graph()
	{
		noeuds = new ArrayList<Node>();
	}
	
	public void put(Node a, Node b, Float weight)
	{
		if(!noeuds.contains(a))
		{
			noeuds.add((Node)a.clone());
		}
		
		noeuds.get( noeuds.indexOf(a) ).addNeighboor(b.clone(),weight);
		
		if(!noeuds.contains(b))
		{	
			noeuds.add((Node)(b.clone()));
		}
		
		noeuds.get( noeuds.indexOf(b) ).addNeighboor(a.clone(),weight);
		
	}

	public void removeNode(Node a)
	{
		this.getNodes().remove(a);
		for(int i = 0 ; i < noeuds.size() ; i++)
		{
			for(int j = 0 ; j < noeuds.get(i).getNeighboors().size(); j++)
			{
				noeuds.get(i).getNeighboors().remove(a);				
			}
		}
		
	}
	
	public String toString()
	{
		String str = "";
		for(Node n : noeuds)
		{
			str+=n.toString() + "\n";
		}
		
		return str;
	}
	
	
	public ArrayList<Node> getNodes()
	{
		return noeuds;
	}
	
	public int countArretes()
	{
		int count = 0;
		for(Node v1 : noeuds)
		{
			for(Node v2 : noeuds)
			{
				if(v2.getNeighboors().containsKey(v1))
				{
					count++;
				}
			}
		}
		return count/2;
	}
	
	public ArrayList<Ridge> getArretes()
	{
		ArrayList<Ridge> ret = new ArrayList<Ridge>();
		for(Node n : noeuds)
		{
			for(Node v  : n.getNeighboorsList())
			{
				ret.add(new Ridge(n,v, n.getNeighboors().get(v)));
			}
		}
		return ret;
	}
}
