package com.project.model;

public class VotoCategPref extends Voto {
	private Partito partito_votato;
	private Candidato candidato_nel_partito;
	
	public VotoCategPref(Partito p, SessioneVoto sessione) {
		partito_votato = p;
		candidato_nel_partito = null;
		this.sessione = sessione;
	}
	
	public void add_candidato(Candidato c) {
		candidato_nel_partito = c;
	}

	public Partito getPartito_votato() {
		return partito_votato;
	}
	
	public Candidato getCandidato_votato() {
		return candidato_nel_partito;
	}
}
