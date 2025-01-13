package com.project.dao;
import java.util.List;

import com.project.model.Partito;
import com.project.model.SessioneVoto;

public interface PartitoDAO {
	public List<Partito> getAllPartiti();
	public Partito getPartito(String nome);
	public List<Partito> getPartitiSessione(SessioneVoto sessione);
}
