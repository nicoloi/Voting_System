package com.project.dao;

import com.project.model.SessioneVoto;

public interface SessioneDAO {
	
	//controlla se nel db � presente una sessione
	public boolean checkSessione();
	
	//aggiunge la sessione al database, controllando che non ce ne sia gi� un'altra
	public void aggiungi_sessione(SessioneVoto s);
	
	public SessioneVoto get_sessione();
	
	//cancella la sessione e tutti i dati riferiti dal db
	public void delete_sessione();
	
	//cancella tutti i voti della sessione.
	public void cancella_voti_sessione();
}
