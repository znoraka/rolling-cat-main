package fr.lirmm.smile.rollingcat.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Polynome {

	private JPanel panel = new JPanel()
	{
		@Override
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			g.setColor(Color.white);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(Color.red);
			Float exV =null;
			float recalcul = 0.0000001f;
			Point O = new Point(this.getWidth()/2,this.getHeight()/2);
			for(int i = -this.getWidth() ; i < this.getWidth() ; i++)
			{
				float v = f(i);
				g.drawOval(O.x+(int)(i), O.y+(int)(v*recalcul), 5, 5);
				if(exV != null)
				{
					g.drawLine(O.x+(int)((i-1)), O.y+(int)(exV * recalcul), O.x+(int)(i), O.y+(int)(v * recalcul));
				}
				exV = v;
			}
		}
	};

	private ArrayList<Float> C;
	private ArrayList<Float> racines;

	public Polynome()
	{
		this.C = new ArrayList<Float>();
		this.racines = new ArrayList<Float>();
	}

	public void add(float c)
	{
		this.C.add(c);
	}

	public float f(float x)
	{
		float sum = 0;
		for(int i = 0 ; i < this.C.size() ; i++)
		{
			sum += (this.C.get(i)  * Math.pow(x, i));
		}
		return sum;
	}

	private Float getRacineBeetween(float x1, float x2)
	{
		float v1 = f(x1);
		float v2 = f(x2);
		if(v1 < 0 == v2 < 0)
		{
			return null; 
		}
		else
		{
			return (x1 + x2)/2;
		}
	}

	public float solve(float epsilon)
	{
		float diff = 0;
		float exdiff = 0;
		int nbRacine = 0;
		while(racines.size() == 0)
		{
			diff+=epsilon;

			Float v1 = this.getRacineBeetween(diff, exdiff);
			Float v2 = this.getRacineBeetween(-exdiff, -diff);
			if(v1 != null)
			{
				racines.add(v1);
				System.out.println(racines);
				System.out.println(diff);
			}
			if(v2 != null)
			{
				racines.add(v2);
				System.out.println(racines);
				System.out.println(diff);
			}
			exdiff = diff;
		}
		return racines.get(0);
	}

	public void draw()
	{
		JFrame frame = new JFrame("courbe");
		frame.add(this.panel);
		frame.setSize(800, 600);
		frame.setVisible(true);
	}

	public String toString()
	{
		String s = "p(x) = ";
		for(int i = 0 ; i < this.C.size() ; i++)
		{
			s += (this.C.get(i) + "*x^" + i+ "\t");
		}
		return s;	
	}


	public static void main(String args[])
	{/*
		float k = 5;
		for(float   n = 2 ; n <= k ; n++)
		{
			Polynome p = new Polynome();
			float U0 = 300;
			float Sn = 3000 + U0/2;
			p.add(-Sn/U0 + 1);
			for(int i = 1 ; i < n ; i++)
			{
				p.add(1);
			}
			float q = p.solve(0.00001f);

			float sum = 0;
			for(int i = 0 ; i< n ; i++)
			{
				float size = (float) Math.pow(q, i);
				sum+= (U0 * size); 
			}

			System.out.println(" n : " + n);
			float q1 = 1.0f - (float) (Math.pow(( U0 / Sn),1.0f/(float)n));
			System.out.println("q : " + q);
			System.out.println("q1 : " + q1);
		}
	 */

		JFrame frame = new JFrame("Test");

		frame.add(new JPanel()
		{
			@Override
			public void paintComponent(Graphics g)
			{

				super.paintComponent(g);
				float exQ1 = 0;
				float exQ2 = 0;
				float k = 10;
				int cX = 20;
				for(float   n = 2 ; n <= k ; n++)
				{

					Polynome p = new Polynome();
					float U0 = 300;
					float Sn = 3000 + U0/2;
					p.add(-Sn/U0 + 1);
					for(int i = 1 ; i < n ; i++)
					{
						p.add(1);
					}
					float q = p.solve(0.00001f);

					float sum = 0;
					for(int i = 0 ; i< n ; i++)
					{
						float size = (float) Math.pow(q, i);
						sum+= (U0 * size); 
					}

					float q1 = 1.0f - ( U0 / Sn);

					if( n > 2)
					{
						g.setColor(Color.red);
						g.drawLine((int)(k-1)*cX, (int)(exQ1*10), (int)k*cX, (int)(q*10));
						g.drawOval((int)k*cX, (int)(q*10), 5, 5);
						g.setColor(Color.blue);
						g.drawLine((int)(k-1)*cX, (int)(exQ2*10), (int)k*cX, (int)(q1*10));
						g.drawOval((int)k*cX, (int)(q1*10), 5, 5);
					}
					exQ1 = q;
					exQ2 = q1;
				}

			}
		});
		frame.setSize(500,500);
		frame.setVisible(true);
	}


}