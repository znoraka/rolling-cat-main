package fr.lirmm.smile.rollingcat.utils;


public class StringUtils {
	
	public static String addEnters(String str, int nbChar){
		String s;
		String temp[] = str.split(" ");
		s = "";
		int length = 0;
		
		for (String string : temp) {
			s += " " + string;
			length += string.length();
			if(length > nbChar){
				s += "\n";
				length = 0;
			}
		}
		return s;
	}
}
