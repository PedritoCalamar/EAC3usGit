package Projecte;

public class Persona {

	int id;
	String nom;
	String cognoms;
	String email;
	String carrec;
	
	public Persona(int id, String nom, String cognoms) {
		super();
		this.id = id;
		this.nom = nom;
		this.cognoms = cognoms;
		
	}
	
	public Persona(int id, String nom, String cognoms,String email, String carrec) {
		super();
		this.id = id;
		this.nom = nom;
		this.cognoms = cognoms;
		this.email = email;
		this.carrec = carrec;
		
	}

}
