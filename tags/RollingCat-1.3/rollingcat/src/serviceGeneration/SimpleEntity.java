package serviceGeneration;

import fr.lirmm.smile.rollingcat.utils.EntityModel;

public class SimpleEntity implements EntityModel{

	private float x;
	private float y;
	private String name;
	public SimpleEntity(float x, float y, String name) {
		super();
		this.x = x;
		this.y = y;
		this.name = name;
	}
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public String getName() {
		return name;
	}

	
	
}
