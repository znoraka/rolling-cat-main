package fr.lirmm.smile.rollingcat.utils;

import java.util.ArrayList;


public class Polynome {


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
			}
			if(v2 != null)
			{
				racines.add(v2);
			}
			exdiff = diff;
		}
		return racines.get(0);
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

}