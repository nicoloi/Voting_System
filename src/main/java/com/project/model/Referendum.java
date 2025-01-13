package com.project.model;

public class Referendum extends Voto {
	private boolean favorevole;
	
	public Referendum(boolean favorevole, SessioneVoto sessione) {
		this.favorevole = favorevole;
		this.sessione = sessione;
	}
	
	public boolean isFavorevole() {
		return favorevole;
	}
}
