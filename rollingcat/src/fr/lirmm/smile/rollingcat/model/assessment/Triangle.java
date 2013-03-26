package fr.lirmm.smile.rollingcat.model.assessment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import fr.lirmm.smile.rollingcat.GameConstants;
import fr.lirmm.smile.rollingcat.screen.AssessmentScreen;

public class Triangle {
	private Vector2 direct;
	private Vector2 progression;
	private float degree;
	private int id;
	private float color;
	private int percentage;
	private double max;

	
	/**
	 * peut être changer le viewport est-il plus simple que de retrancher la moitié de la largeur de l'écran à chaque fois
	 * @param angle l'angle du vecteur par rapport à l'horizontale
	 * @param degree l'angle entre chaque vecteur
	 */
	public Triangle (float angle, float degree, int id){
		direct = new Vector2(0, 100000);
		progression = new Vector2(0, 1);
		direct.setAngle(angle);
		progression.rotate(angle);
		this.degree = degree;
		this.id = id;
		this.percentage = 0;
		this.max = (angle == 90)?GameConstants.DISPLAY_HEIGHT:(GameConstants.DISPLAY_WIDTH / 2) / Math.cos((double)(angle * 0.0174532925f));
	}
	
	/**
	 * dessine la zone en rouge avec ses limites en noir et son avancement en vert
	 * @param sr le shaperender utilisé dans le screen
	 */
	public void render(ShapeRenderer sr, AssessmentScreen as){
		if(as.getSelected() == id)
			sr.setColor(0, color - 1f, 1f, 1);
		
		else
			sr.setColor(0, color, 1, 1);
		
		sr.begin(ShapeType.FilledTriangle);
		sr.filledTriangle(
				GameConstants.DISPLAY_WIDTH / 2, 0,
				GameConstants.DISPLAY_WIDTH / 2 + direct.cpy().rotate(degree / 2).x, direct.cpy().rotate(degree / 2).y,
				GameConstants.DISPLAY_WIDTH / 2 + direct.cpy().rotate(-degree / 2).x, direct.cpy().rotate(-degree / 2).y);
		
		sr.setColor(Color.GREEN);
		sr.filledTriangle(
				GameConstants.DISPLAY_WIDTH / 2, 0,
				GameConstants.DISPLAY_WIDTH / 2 + progression.cpy().rotate(degree / 2).x, progression.cpy().rotate(degree / 2).y,
				GameConstants.DISPLAY_WIDTH / 2 + progression.cpy().rotate(-degree / 2).x, progression.cpy().rotate(-degree / 2).y);
		
		sr.end();
		
		sr.setColor(Color.BLACK);
		sr.begin(ShapeType.Triangle);
		sr.triangle(
				GameConstants.DISPLAY_WIDTH / 2, 0,
				GameConstants.DISPLAY_WIDTH / 2 + direct.cpy().rotate(degree / 2).x, direct.cpy().rotate(degree / 2).y,
				GameConstants.DISPLAY_WIDTH / 2 + direct.cpy().rotate(-degree / 2).x, direct.cpy().rotate(-degree / 2).y);
		sr.end();
		
	}
	
	/**
	 * met à jour la progression du patient sur une zone
	 * @param x la position du curseur en x
	 * @param y la position du curseur en y
	 */
	public void setProgression(float x, float y, AssessmentScreen as){
		Vector2 a = new Vector2(x - GameConstants.DISPLAY_WIDTH / 2, y);
		
		if(a.len() < 100)
			as.setSelected(-1);
	
		if(a.angle() <= direct.cpy().rotate(degree / 2).angle() & (a.angle() >= direct.cpy().rotate(-degree / 2).angle() | direct.angle() < degree)){
			
			color = 0.4f;
			
			if(as.getSelected() == -1 && a.len() > 100)
				as.setSelected(this.id);
			
			if(a.len() > progression.len() && as.getSelected() == id){
				progression.set(a);
				progression.setAngle(direct.angle());
			}
			percentage = (int) ((Math.abs(progression.len() / max) * 100 < 100f)?(Math.abs(progression.len()) / max) * 100:100);

		}
		else
			color = 0.78f;
	
	}

	public int getAngle() {
		return (int) (this.degree * id + this.degree / 2);
	}
	
	public int getID() {
		return this.id;
	}

	public int getProgression() {
		return ((percentage > 1)?percentage:0);
	}
		
}
