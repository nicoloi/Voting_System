package com.project.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.project.model.Candidato;
import com.project.model.*;

//classe singleton
public class CandidatoDAOImpl implements CandidatoDAO {
	
	//attributi
	private static CandidatoDAOImpl instance = null;
	private List<Candidato> candidati;
	
	//costruttore
	
	private CandidatoDAOImpl() {
		candidati = new ArrayList<>();
		
		try {
			Statement stat = getConnessione().createStatement();
			ResultSet rs = stat.executeQuery("select * from candidato");
			while (rs.next()) {
				String nome = rs.getString("nome");
				String cognome = rs.getString("cognome");
				String nomePartito = rs.getString("partito");
				PartitoDAOImpl pdi = PartitoDAOImpl.getInstance();
				Partito p = pdi.getPartito(nomePartito);
				Candidato c = new Candidato(nome, cognome, p);
				candidati.add(c);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//metodi
	
	public static CandidatoDAOImpl getInstance() {
		if (instance == null) instance = new CandidatoDAOImpl();
		return instance;
	}
	
	private Connection getConnessione() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:postgresql://localhost/sistema_di_voto?user=postgres&password=pippo");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	@Override
	public List<Candidato> getAllCandidati() {
		return candidati;
	}

	@Override
	public Candidato getCandidato(String nome, String cognome) {
		for (Candidato c : candidati) {
			if (c.getNome().equals(nome) && c.getCognome().equals(cognome))
				return c;
		}
		return null;
	}

	@Override
	public List<Candidato> getCandidatiSessione(SessioneVoto sessione) {
		List<Candidato> lista = new ArrayList<>();
		try {
			PreparedStatement ps = getConnessione().prepareStatement("SELECT nome, cognome, partito FROM candidato_sessione JOIN candidato ON candidato = cognome WHERE sessione = ?");
			ps.setString(1, sessione.getDescrizione());
			ResultSet rs = ps.executeQuery();
			
			PartitoDAO daop = PartitoDAOImpl.getInstance();
			while (rs.next()) {
				Partito p = daop.getPartito(rs.getString("partito"));
				Candidato c = new Candidato(rs.getString("nome"), rs.getString("cognome"), p);
				lista.add(c);
			}
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lista;
	}

}
