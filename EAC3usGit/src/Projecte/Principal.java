package Projecte;

import java.util.ArrayList;

public class Principal {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ArrayList<Persona> llistaP = new ArrayList<Persona>();
		
		Persona persona1 = new Persona(1,"Josep","Canals Ponts");
		Persona persona2 = new Persona(2,"Maria","Lopez Garrido");
		
		llistaP.add(persona1);
		llistaP.add(persona2);
				
		for (Persona p : llistaP) {
			System.out.println(p.id);
		}
		
	}
}
