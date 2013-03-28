package fr.lirmm.smile.rollingcat.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;

import fr.lirmm.smile.rollingcat.GameConstants;

public class GameProgressionMenu {

	private Point items[];
	private Color colors[];
	private int size;
	private int centerX,centerY,rayon;
	public GameProgressionMenu(int centerX, int centerY, int rayon)
	{
		this.centerX = centerX;
		this.centerY = centerY;
		this.rayon = rayon;
		items = new Point[GameConstants.NB_OF_LEVELS_IN_GAME];
		colors = new Color[items.length];
		size = (int) ((int) (2*Math.PI * rayon / items.length) * 1f); 
		
		initCircleLevels();

		final JFrame frame = new JFrame("AffichageMenu");
		frame.add(new JPanel()
		{
			@Override
			public void paintComponent(Graphics g)
			{
				final float coeff = (float) (1.0f / Math.sqrt(2.0f));
				super.paintComponent(g);

				int i = 0 ;
				for(Point t : items)
				{
					g.setColor(colors[i]);
					g.drawOval(t.x, t.y, size, size);
					g.drawRect(t.x + (int)(size*coeff)/4, t.y+(int)(size*coeff)/4, (int)(size*coeff), (int)(size*coeff));
					i++;
				}
			}
		});
		frame.setSize(GameConstants.DISPLAY_WIDTH,GameConstants.DISPLAY_HEIGHT);
		
		frame.setVisible(true);

		Thread th = new Thread(new Runnable()
		{
			int angle = 0;
			@Override
			public void run() {
				while(angle < 360)
				{
					rotateEffect();
					angle++;
					System.out.println("p");
					frame.repaint();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		});
		th.start();
		
	}
	
	public void initCircleLevels()
	{
		float step = 360.0f/items.length;
		for(int i = 0 ; i < items.length ; i++)
		{
			int x = (int) (Math.cos(Math.toRadians(i*step)) * rayon) + centerX;
			int y = (int) (Math.sin(Math.toRadians(i*step)) * rayon)+ centerY;
			items[i] = new Point(x,y);
			colors[i] = new Color(0,0,i*255/items.length); 
		}
	}
	
	public void rotateEffect()
	{
		float step = 360.0f/items.length;
		Point tmp = null;
		for(int i = 1 ; i < items.length; i++)
		{
			int a = i;
			int b = i+1 < items.length ? i+1 : 0;
			tmp = items[a];
			items[a] = items[b];
			items[b] = tmp;
		}	
	}
	
	public static void main(String args[])
	{
		final GameProgressionMenu menu = new GameProgressionMenu(
				GameConstants.DISPLAY_WIDTH/2, 
				GameConstants.DISPLAY_HEIGHT/2, 
				GameConstants.DISPLAY_WIDTH/4 );
		
	}
	
	
}
