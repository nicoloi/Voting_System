package com.project.model;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

//design pattern iterator
public class Partito extends ElementoDaVotare implements Iterable<Candidato> {
	//attributi
	private Set<Candidato> candidati;
	
	//costruttori
	public Partito(String nome) {
		this.nome = nome;
		candidati = new HashSet<>();
	}
	
	public Partito(String nome, Set<Candidato> lista) {
		this.nome = nome;
		candidati = new HashSet<>();
		for (Candidato c : lista) {
			candidati.add(c);
		}
	}
	
	//metodi
	public void add_candidato(Candidato c) {
		candidati.add(c);
	}

	//iteratore dei candidati all'interno dei partiti
	@Override
	public Iterator<Candidato> iterator() {
		return candidati.iterator();
	}
	
	@Override
	public String toString() {
		return nome;
	}
}
