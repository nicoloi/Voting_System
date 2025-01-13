package com.project.model;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.project.dao.UtenteDAO;
import com.project.dao.UtenteDAOImpl;
import com.project.dao.VotoDAO;
import com.project.dao.VotoDAOImpl;

public class SessioneVoto {
	
	//attributi
	private String tipo_voto;
	private String tipo_vittoria;
	private String descrizione;
	private boolean vota_partiti;
	private boolean is_open;
	private Set<ElementoDaVotare> lista_candidati; // e' vuota nel caso di referendum
	private int valore_quorum = -1; //se il voto non � referendum con quorum, il quorum vale -1
	private UtenteDAO daoUt;
	private VotoDAO daoVoto;
	
	//costruttori
	public SessioneVoto() {
		lista_candidati = new HashSet<>();
		
		try {
			daoUt = UtenteDAOImpl.getInstance();
			daoVoto = VotoDAOImpl.getInstance();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//metodi
	
	public void apri_sessione() {
		is_open = true;
	}
	
	public void chiudi_sessione() {
		is_open = false;
	}
	
	public void setApertura(boolean apertura) {
		is_open = apertura;
	}
	
	public boolean is_open() {
		return is_open;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getTipoVoto() {
		return tipo_voto;
	}

	public void setTipoVoto(String tipo_voto) {
		this.tipo_voto = tipo_voto;
	}

	public String getTipo_vittoria() {
		return tipo_vittoria;
	}

	public void setTipo_vittoria(String tipo_vittoria) {
		this.tipo_vittoria = tipo_vittoria;
	}
	
	public void setVotaPartiti(boolean vota_partiti) {
		this.vota_partiti = vota_partiti;
	}
	
	//true se nella sessione ci sono partiti
	public boolean sessione_con_partiti() {
		return vota_partiti;
	}
	
	public void add_votante(Elettore e) {
		daoUt.put_elettore(e, this);
	}
	
	public Set<Elettore> getVotanti() {
		return daoVoto.getVotanti();
	}
	
	public void add_candidato(ElementoDaVotare e) {
		lista_candidati.add(e);
	}
	
	public Set<ElementoDaVotare> getCandidati() {
		return lista_candidati;
	}
	
	public void add_voto(Voto v) {
		daoVoto.putVoto(v);
	}
	
	public void imposta_quorum(int quorum) {
		valore_quorum = quorum;
	}
	
	public int getQuorum() {
		return valore_quorum;
	}
	
	public List<Voto> getVoti() {
		return daoVoto.getAllVoti(tipo_voto);
	}
}
