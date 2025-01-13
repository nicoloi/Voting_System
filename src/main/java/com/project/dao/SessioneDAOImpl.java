package com.project.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Set;
import com.project.model.*;

//classe singleton
public class SessioneDAOImpl implements SessioneDAO {
	
	//attributi
	private static SessioneDAOImpl istanza = null;
	
	
	//costruttori
	private SessioneDAOImpl() {

	}
	
	//metodi
	
	public static SessioneDAOImpl getInstance() {
		if (istanza == null) istanza = new SessioneDAOImpl();
		return istanza;
	}
	
	//restituisce true se nel db c'� salvata la sessione
	@Override
	public boolean checkSessione() {
		try {
			Statement stat = getConnessione().createStatement();
			ResultSet rs = stat.executeQuery("SELECT COUNT(*) FROM sessione_voto");
			rs.next();
			if (rs.getInt(1) == 0) {
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	//aggiunge la sessione al db
	@Override
	public void aggiungi_sessione(SessioneVoto s) {
		
		if (checkSessione()) return; //se c'� gi� un'altra sessione non ne aggiunge un'altra
		
		try {
			PreparedStatement ps = getConnessione().prepareStatement("INSERT INTO sessione_voto VALUES(?,?,?,?,?,?)");
			ps.setString(1, s.getTipoVoto());
			ps.setString(2, s.getTipo_vittoria());
			ps.setString(3, s.getDescrizione());
			ps.setBoolean(4, s.sessione_con_partiti());
			ps.setBoolean(5, s.is_open());
			ps.setInt(6, s.getQuorum());
			int affected = ps.executeUpdate(); //deve essere 1
			//metti istruzioni con affected
			if (affected != 1) throw new SQLException("la sessione non � stata aggiunta al db!");
			
			//aggiungi la lista dei candidati/partiti al db
			if (!(s.getTipoVoto().equals("referendum"))) {
				if (s.sessione_con_partiti()) {
					Set<ElementoDaVotare> lista_part = s.getCandidati(); 
					for (ElementoDaVotare e : lista_part) {
						PreparedStatement ps1 = getConnessione().prepareStatement("INSERT INTO partito_sessione VALUES(?,?)");
						ps1.setString(1, s.getDescrizione());
						ps1.setString(2, e.getNome());
						ps1.executeUpdate();
					}
				} else {
					Set<ElementoDaVotare> lista_cand = s.getCandidati(); 
					for (ElementoDaVotare e : lista_cand) {
						PreparedStatement ps1 = getConnessione().prepareStatement("INSERT INTO candidato_sessione VALUES(?,?)");
						ps1.setString(1, s.getDescrizione());
						ps1.setString(2, ((Candidato)e).getCognome());
						ps1.executeUpdate();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//prende la sessione dal db
	@Override
	public SessioneVoto get_sessione() {
		SessioneVoto ris = null;
		try {
			PreparedStatement ps = getConnessione().prepareStatement("SELECT * FROM sessione_voto");
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {				
				ris = new SessioneVoto();
				ris.setTipoVoto(rs.getString("tipo_voto"));
				ris.setTipo_vittoria(rs.getString("tipo_vittoria"));
				ris.setDescrizione(rs.getString("titolo"));
				ris.setVotaPartiti(rs.getBoolean("vota_partiti"));
				ris.setApertura(rs.getBoolean("is_open"));
				ris.imposta_quorum(rs.getInt("valore_quorum"));
			} else return null;
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (ris.sessione_con_partiti()) {
			PartitoDAO daop = PartitoDAOImpl.getInstance();
			List<Partito> partiti = daop.getPartitiSessione(ris);
			for (Partito p : partiti) {
				ris.add_candidato(p);
			}
		} else {
			CandidatoDAO daoc = CandidatoDAOImpl.getInstance();
			List<Candidato> candidati = daoc.getCandidatiSessione(ris);
			for (Candidato c : candidati) {
				ris.add_candidato(c);
			}
		}
		return ris;
	}

	@Override
	public void delete_sessione() {
		istanza = null;
		try {
			Connection conn = getConnessione();
			PreparedStatement ps = conn.prepareStatement("DELETE FROM elettore_sessione");
			ps.executeUpdate();
			
			ps = conn.prepareStatement("DELETE FROM candidato_sessione");
			ps.executeUpdate();
			
			ps = conn.prepareStatement("DELETE FROM partito_sessione");
			ps.executeUpdate();
			
			ps = conn.prepareStatement("DELETE FROM voto_categorico");
			ps.executeUpdate();
			
			ps = conn.prepareStatement("DELETE FROM voto_categorico_pref");
			ps.executeUpdate();
			
			ps = conn.prepareStatement("DELETE FROM voto_ordinale");
			ps.executeUpdate();
			
			ps = conn.prepareStatement("DELETE FROM voto_referendum");
			ps.executeUpdate();
			
			ps = conn.prepareStatement("DELETE FROM sessione_voto");
			ps.executeUpdate();
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
	public void cancella_voti_sessione() {
		try {
			Connection conn = getConnessione();
			PreparedStatement ps = conn.prepareStatement("DELETE FROM elettore_sessione");
			ps.executeUpdate();
			
			ps = conn.prepareStatement("DELETE FROM voto_categorico");
			ps.executeUpdate();
			
			ps = conn.prepareStatement("DELETE FROM voto_categorico_pref");
			ps.executeUpdate();
			
			ps = conn.prepareStatement("DELETE FROM voto_ordinale");
			ps.executeUpdate();
			
			ps = conn.prepareStatement("DELETE FROM voto_referendum");
			ps.executeUpdate();
			
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
