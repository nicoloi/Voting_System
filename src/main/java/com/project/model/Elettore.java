package com.project.model;
public class Elettore{

	//attributi
	private String name;
	private String cognome;
	private String cf;
	private String password;
	
	//costruttori
	
	public Elettore(String name, String cognome, String cf) {
		setName(name);
		setCognome(cognome);
		setCf(cf);
	}
	
	
	public Elettore(String cf, String password) {
		this.cf = cf;
		this.password = password;
	}
	
	public Elettore() {
		
	}
	
	//metodi
	
	//@ requires name != "null";
	public void setName (String name) {
		this.name=name;
	}
	//@ ensures \result != null;
	public String getName() {
		return this.name;
	}
	
	public void setCf(String cf) {
		this.cf = cf;
	}
	
	public String getCf() {	
		return this.cf;
	}

	public String getCognome() {
		return cognome;
	}
	
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return password;
	}
}
