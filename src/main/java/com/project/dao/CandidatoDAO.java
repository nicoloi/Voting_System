package com.project.dao;
import java.util.List;

import com.project.model.Candidato;
import com.project.model.SessioneVoto;

public interface CandidatoDAO {
	public List<Candidato> getAllCandidati();
	public Candidato getCandidato(String nome, String cognome);
	public List<Candidato> getCandidatiSessione(SessioneVoto sessione);
}
