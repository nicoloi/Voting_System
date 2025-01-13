package com.project.model;
import java.util.Objects;

public class Candidato extends ElementoDaVotare {
	private String cognome;
	private Partito partito; //pu� essere null
	
	public Candidato(String nome, String cognome, Partito partito) {
		this.nome = Objects.requireNonNull(nome);
		this.cognome = Objects.requireNonNull(cognome);
		this.partito = partito;
	}

	public String getCognome() {
		return cognome;
	}

	public Partito getPartito() {
		return partito;
	}
	
	@Override
	public String toString() {
		return nome + " " + cognome;
	}
	
	@Override
	public boolean equals(Object obj) {
		Objects.requireNonNull(obj);
		if (obj instanceof Candidato) {
			String s = ((Candidato)obj).toString();
			return toString().equals(s);
		}
		return false;
	}
}










































