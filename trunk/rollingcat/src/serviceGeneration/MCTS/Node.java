package serviceGeneration.MCTS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Node 
{
	private Map<Node,Float> neighboors;
	private String name;
	private ActionType actionType;
	public Node(String name,ActionType actionType)
	{
		neighboors = new HashMap<Node,Float>();
		this.actionType = actionType;
		this.name = name;		
	}
	
	public int hashCode()
	{
		int ret = this.name.hashCode();
		return ret;
	}
	
	public boolean equals(Object t)
	{
		if(t instanceof Node)
		{			
			Node p = (Node)(t);
			return p.getName().equals(name) && p.getActionType() == actionType;
		}
		return false;
	}
	
	public String getName()
	{
		return name;
	}

	public String toString()
	{
		String str="";
		str+= this.name + " : ( " + this.getActionType() +" ) ";
		if(neighboors.size() > 0)
		{
			str += "\n";
			str+="Neighboors : ";
	
			Set<Node> list = neighboors.keySet();
			for(Node v : list)
			{
				str += "\n\t" + v.toString() + " " + neighboors.get(v).toString() ;
			}
		}
		return str;
	}

	public ActionType getActionType()
	{
		return actionType;
	}
	
	public Node clone()
	{
		Node n = new Node(name,this.actionType);
		
		Set<Node> list = neighboors.keySet();
		for(Node v : list)
		{
			n.addNeighboor(v.clone(), (Float)(neighboors.get(v)));
		}
		return n;
	}
	
	public void addNeighboor(Node v, Float weight)
	{
		if(!neighboors.containsKey(v))
		{
			neighboors.put(v.clone(),weight);
		}
	}
	
	public Map<Node,Float> getNeighboors()
	{
		return neighboors;
	}
	
	public ArrayList<Node> getNeighboorsList()
	{
		ArrayList<Node> ret = new ArrayList<Node>();
		Set<Node> list = neighboors.keySet();
		for(Node v : list)
		{
			ret.add(v);
		}
		return ret;
	}
	
	public ArrayList<Ridge> getRidge()
	{
		ArrayList<Ridge> ret = new ArrayList<Ridge>();
		Set<Node> list = neighboors.keySet();
		for(Node v : list)
		{
			ret.add(new Ridge(this.clone(),v.clone(),neighboors.get(v)));
		}
		return ret;
	}
}
