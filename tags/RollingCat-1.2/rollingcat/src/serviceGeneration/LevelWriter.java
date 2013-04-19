package serviceGeneration;

import java.util.ArrayList;
import java.util.List;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.utils.EntityModel;

public class LevelWriter {

	
	public static String write(List<EntityModel> entities) {
		String str = "";
		int i =  0 ;
		for(EntityModel e : entities)
		{
			String s = e.getName() + ";" +  e.getX() + ";" + e.getY();
			if(i < entities.size() -1)
			{
				s+="/";
			}
			i++;
			str+=s;
		}
		return str;
	}
	
	public static void main(String args[])
	{
		ArrayList<EntityModel> list = new ArrayList<EntityModel>();
		list.add(new SimpleEntity(0.0f, 2.0f,"cat"));
		list.add(new SimpleEntity(1.0f, 2.0f,"groundblock"));
		list.add(new SimpleEntity(2.0f, 2.0f,"groundblock"));
	
	}
	
}
