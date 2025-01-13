package com.project.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.project.model.Partito;
import com.project.model.*;

//classe singleton
public class PartitoDAOImpl implements PartitoDAO {
	
	//attributi
	private static PartitoDAOImpl istanza = null;
	private List<Partito> partiti;

	//costruttore
	private PartitoDAOImpl() {
		partiti = new ArrayList<Partito>();
		
		try {
			Statement stat = getConnessione().createStatement();
			ResultSet rs = stat.executeQuery("select * from partito");
			while (rs.next()) {
				String nome = rs.getString("nome");
				Partito p = new Partito(nome);
				partiti.add(p);
				
				//metti i candidati nei partiti
				PreparedStatement ps = getConnessione().prepareStatement("SELECT * FROM rel_part_cand JOIN candidato ON candidato.cognome = rel_part_cand.candidato WHERE rel_part_cand.partito = ?");
				ps.setString(1, nome);
				ResultSet rs2 = ps.executeQuery();
				while (rs2.next()) {
					Candidato candidato = new Candidato(rs2.getString("nome"), rs2.getString("cognome"), p);
					p.add_candidato(candidato);
				}
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//metodi
	
	public static PartitoDAOImpl getInstance() {
		if (istanza == null) istanza = new PartitoDAOImpl();
		return istanza;
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
	public List<Partito> getAllPartiti() {
		return partiti;
	}

	@Override
	public Partito getPartito(String nome) {
		for (Partito p : partiti) {
			if (p.getNome().equals(nome))
				return p;
		}
		return null;
	}

	@Override
	public List<Partito> getPartitiSessione(SessioneVoto sessione) {
		List<Partito> lista = new ArrayList<>();
		try {
			PreparedStatement ps = getConnessione().prepareStatement("SELECT partito FROM partito_sessione WHERE sessione = ?");
			ps.setString(1, sessione.getDescrizione());
			ResultSet rs = ps.executeQuery();
			
			 
			while (rs.next()) {
				String nomePart = rs.getString("partito");
				for (Partito part : partiti) {
					if (part.getNome().equals(nomePart)) {
						lista.add(part);
					}
				}
			}
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lista;
	}

}
