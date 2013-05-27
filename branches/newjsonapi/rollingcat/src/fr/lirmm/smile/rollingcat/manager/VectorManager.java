package fr.lirmm.smile.rollingcat.manager;

import java.util.ArrayList;

import fr.lirmm.smile.rollingcat.model.assessment.Triangle;

public class VectorManager {
	
	/**
	 * 
	 * @param n number of areas
	 * @return ArrayList of rotated vectors
	 */
	public static ArrayList<Triangle> getVectorsFromAreas(int n){
		int a = 180 / n;
		
		ArrayList<Triangle> list = new ArrayList<Triangle>();
		
		for (int i = 0; i < n; i++) {
			list.add(new Triangle(a * i + a / 2, 180 / n, i));
		}
		return list;
	}
	
}
