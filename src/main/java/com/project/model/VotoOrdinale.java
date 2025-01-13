package com.project.model;

import java.util.HashMap;
import java.util.Map;

import com.project.dao.CandidatoDAO;
import com.project.dao.CandidatoDAOImpl;
import com.project.dao.PartitoDAO;
import com.project.dao.PartitoDAOImpl;

public class VotoOrdinale extends Voto {
	private Map<Integer, ElementoDaVotare> lista_ordinata;
	private CandidatoDAO daoc;
	private PartitoDAO daop;
	
	public VotoOrdinale(SessioneVoto sessione) {
		this.sessione = sessione;
		lista_ordinata = new HashMap<>();
		
		daoc = CandidatoDAOImpl.getInstance();
		daop = PartitoDAOImpl.getInstance();
	}
	
	//la posizione deve essere > 0 e l'elemento "e" non deve essere null
	public void add_elemento(int posizione, ElementoDaVotare e) {
		lista_ordinata.put(posizione, e);
	}
	
	//la posizione deve essere > 0 e la stringa "e" non deve essere null
	public void add_elemento(int posizione, String e) {
		String[] arr = e.split(" ");
		if (arr.length == 2) {
			//il parametro e � un candidato
			Candidato c = daoc.getCandidato(arr[0], arr[1]);
			
			if (c == null) throw new IllegalArgumentException();
			lista_ordinata.put(posizione, c);
		} else {
			//e � un partito
			Partito p = daop.getPartito(e);
			
			if (p == null) throw new IllegalArgumentException();
			lista_ordinata.put(posizione, p);
		}
	}
	
	public ElementoDaVotare getElemento(int posizione) {
		if (lista_ordinata.get(posizione) == null) throw new IllegalArgumentException("posizione non valida!");
		return lista_ordinata.get(posizione);
	}
	
	public int numero_elementi() {
		return lista_ordinata.size();
	}
}
