package com.project.model;

public class VotoCategorico extends Voto {
	
	private ElementoDaVotare elemento_votato;
	
	public VotoCategorico(SessioneVoto sessione, ElementoDaVotare votato) {
		this.sessione = sessione;
		elemento_votato = votato;
	}

	public ElementoDaVotare getElemento_votato() {
		return elemento_votato;
	}
	
}
