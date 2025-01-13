package com.project.dao;
import java.util.List;
import java.util.Set;

import com.project.model.Candidato;
import com.project.model.ElementoDaVotare;
import com.project.model.Elettore;
import com.project.model.Voto;

public interface VotoDAO {
	//restituisce tutti i voti del tipo tipo_voto, andandoli a prendere dal db
	public List<Voto> getAllVoti(String tipo_voto);
	
	//mette il voto v nel database
	public void putVoto(Voto v);
	
	//prende dal db tutti gli elettori che hanno votato nella sessione
	public Set<Elettore> getVotanti();
	
	//restituisce il numero di voti che ha ricevuto l'elemento da votare e, nel caso di voto categorico e categorico con preferenze.
	public int getNumeroVoti(ElementoDaVotare e);
	
	// restituisce il numero di voti che ha ricevuto il candidato c all'interno del partito p, nel caso di voto
	// categorico con preferenze. se la sessione attuale non � un voto categorico con preferenze, solleva un eccezione.
	public int getNumVotiCategorico(Candidato c);
	
	// restituisce il numero di voti favorevoli alla sessione di referendum. se la sessione attuale non � un referendum,
	// solleva un eccezione.
	public int getNumeroSi();
	
	// restituisce il numero di voti non favorevoli alla sessione di referendum. se la sessione attuale non � un referendum,
	// solleva un eccezione.
	public int getNumeroNo();
	
	//restituisce il numero di voti in cui l'elemento "e" si trova in posizione "pos", nel caso di voto ordinale. se la sessione attuale 
	//non � un voto ordinale, solleva un eccezione.
	public int getNumeroByPosizione(ElementoDaVotare e, int pos);

}
