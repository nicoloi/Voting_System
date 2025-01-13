package com.project.dao;

import com.project.model.Elettore;
import com.project.model.SessioneVoto;

public interface UtenteDAO {
	//controlla le credenziali dell'admin sul database
	public boolean check_login_admin(String username, String pswrd);
	
	//controlla le credenziali dell'elettore sul database
	public boolean check_login_elettore(String cf, String pswrd);
	
	//mette l'elettore che ha votato nella sessione, all'interno del database  
	public void put_elettore(Elettore elettore, SessioneVoto sessione);
	
	//restituisce true se l'elettore ha gi� votato per quella sessione
	public boolean check_elettore_sessione(Elettore elettore, SessioneVoto sessione);
	
	//restituisce l'elettore col codice fiscale cf
	public Elettore getElettore(String cf);
}
