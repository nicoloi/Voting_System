package com.project.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.project.model.SessioneVoto;
import com.project.model.Voto;

import com.project.model.*;
//classe singleton
public class VotoDAOImpl implements VotoDAO {
	
	private static VotoDAOImpl instance;
	
	private VotoDAOImpl() {
		
	}
	
	public static VotoDAOImpl getInstance() {
		if (instance == null) instance = new VotoDAOImpl();
		return instance;
	}

	@Override
	public List<Voto> getAllVoti(String tipo_voto) {
				
		List<Voto> lista_voti = new ArrayList<Voto>();
		SessioneVoto sessione = SessioneDAOImpl.getInstance().get_sessione();
		PartitoDAOImpl daop = PartitoDAOImpl.getInstance();
		CandidatoDAOImpl daoc = CandidatoDAOImpl.getInstance();
		
		try {
			switch (tipo_voto) {
			case "categorico":
				if (sessione.sessione_con_partiti()) {
					PreparedStatement ps = getConnessione().prepareStatement("SELECT partito FROM voto_categorico");
					ResultSet rSet = ps.executeQuery();
					while (rSet.next()) {	
						VotoCategorico vc = new VotoCategorico(sessione, daop.getPartito(rSet.getString("partito")));
						lista_voti.add(vc);
					}
				} else {
					PreparedStatement ps = getConnessione().prepareStatement("SELECT nome, cognome FROM voto_categorico JOIN candidato ON voto_categorico.candidato = candidato.cognome");
					ResultSet rSet = ps.executeQuery();
					while (rSet.next()) {	
						VotoCategorico vc = new VotoCategorico(sessione, daoc.getCandidato(rSet.getString("nome"), rSet.getString("cognome")));
						lista_voti.add(vc);
					}
				}
				break;
			case "categorico preferenze":
				PreparedStatement ps = getConnessione().prepareStatement("SELECT voto_categorico_pref.partito, nome, cognome FROM voto_categorico_pref JOIN candidato ON candidato_nel_partito = cognome");
				ResultSet rSet = ps.executeQuery();
				while (rSet.next()) {	
					VotoCategPref vcp = new VotoCategPref(daop.getPartito(rSet.getString("partito")), sessione);
					vcp.add_candidato(daoc.getCandidato(rSet.getString("nome"), rSet.getString("cognome")));
					lista_voti.add(vcp);
				}
				break;
			case "ordinale":
				for (int i = 1; i <= get_max_id(); i++) {
					VotoOrdinale vo = new VotoOrdinale(sessione);
					if (sessione.sessione_con_partiti()) {
						PreparedStatement ps1 = getConnessione().prepareStatement("SELECT posizione, partito FROM voto_ordinale WHERE id = ?");
						ps1.setInt(1, i);
						ResultSet rSet1 = ps1.executeQuery();
						while (rSet1.next()) {
							vo.add_elemento(rSet1.getInt("posizione"), daop.getPartito(rSet1.getString("partito")));
						}
					} else {
						PreparedStatement ps1 = getConnessione().prepareStatement("SELECT posizione, nome, cognome FROM voto_ordinale JOIN candidato ON candidato = cognome WHERE id = ?");
						ps1.setInt(1, i);
						ResultSet rSet1 = ps1.executeQuery();
						while (rSet1.next()) {
							vo.add_elemento(rSet1.getInt("posizione"), daoc.getCandidato(rSet1.getString("nome"), rSet1.getString("cognome")));
						}
					}
					lista_voti.add(vo);
				}
				break;
			case "referendum":
				PreparedStatement ps2 = getConnessione().prepareStatement("SELECT favorevole FROM voto_referendum");
				ResultSet rSet2 = ps2.executeQuery();
				while (rSet2.next()) {
					Referendum r = new Referendum(rSet2.getBoolean(1), sessione);
					lista_voti.add(r);
				}
				break;
			default:
				throw new IllegalArgumentException("parametro tipo_voto non valido!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return lista_voti;
	}

	@Override
	public void putVoto(Voto v) {
		try {
			if (v instanceof VotoCategorico) {
				if (v.getSessione().sessione_con_partiti()) {
					PreparedStatement ps = getConnessione().prepareStatement("INSERT INTO voto_categorico(sessione, partito) VALUES(?, ?)");
					ps.setString(1, v.getSessione().getDescrizione());
					ElementoDaVotare e = ((VotoCategorico) v).getElemento_votato();
					ps.setString(2, e.getNome());
					ps.executeUpdate();
				} else {
					PreparedStatement ps = getConnessione().prepareStatement("INSERT INTO voto_categorico(sessione, candidato) VALUES(?, ?)");
					ps.setString(1, v.getSessione().getDescrizione());
					ElementoDaVotare e = ((VotoCategorico) v).getElemento_votato();
					ps.setString(2, ((Candidato)e).getCognome());
					ps.executeUpdate();
				}
			} else if (v instanceof VotoCategPref) {
				VotoCategPref vcp = (VotoCategPref) v;
				PreparedStatement ps = getConnessione().prepareStatement("INSERT INTO voto_categorico_pref(candidato_nel_partito, partito, sessione) VALUES(?, ?, ?)");
				ps.setString(1, vcp.getCandidato_votato().getCognome());
				ps.setString(2, vcp.getPartito_votato().getNome());
				ps.setString(3, vcp.getSessione().getDescrizione());
				ps.executeUpdate();
			} else if (v instanceof Referendum) {
				Referendum r = (Referendum)v;
				PreparedStatement ps = getConnessione().prepareStatement("INSERT INTO voto_referendum(sessione, favorevole) VALUES(?, ?)");				
				ps.setString(1, r.getSessione().getDescrizione());
				ps.setBoolean(2, r.isFavorevole());
				ps.executeUpdate();
			} else if (v instanceof VotoOrdinale) {
				//prendo l'id pi� grande dal db
				int id = get_max_id();
				id++;
			
				//metto il voto nel db
				VotoOrdinale vo = (VotoOrdinale)v;
				if (vo.getSessione().sessione_con_partiti()) {
					for (int i = 1; i <= vo.numero_elementi(); i++) {
						PreparedStatement ps = getConnessione().prepareStatement("INSERT INTO voto_ordinale(id, posizione, partito, sessione) VALUES(?, ?, ?, ?)");	
						ps.setInt(1, id);
						ps.setInt(2, i);
						ps.setString(3, vo.getElemento(i).getNome());
						ps.setString(4, vo.getSessione().getDescrizione());
						ps.executeUpdate();
					}
				} else {
					for (int i = 1; i <= vo.numero_elementi(); i++) {
						PreparedStatement ps = getConnessione().prepareStatement("INSERT INTO voto_ordinale(id, posizione, candidato, sessione) VALUES(?, ?, ?, ?)");	
						ps.setInt(1, id);
						ps.setInt(2, i);
						ps.setString(3, ((Candidato)vo.getElemento(i)).getCognome());
						ps.setString(4, vo.getSessione().getDescrizione());
						ps.executeUpdate();
					}
				}
			}
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
	
	//metodo per ottenere il massimo id del voto ordinale nel db
	private int get_max_id() {
		PreparedStatement statement;
		try {
			statement = getConnessione().prepareStatement("SELECT MAX(id) FROM voto_ordinale");
			ResultSet rs = statement.executeQuery();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public Set<Elettore> getVotanti() {
		Set<Elettore> votanti = new HashSet<>();
		try {
			PreparedStatement ps = getConnessione().prepareStatement("SELECT elettore FROM elettore_sessione");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Elettore elettore = UtenteDAOImpl.getInstance().getElettore(rs.getString(1));
				votanti.add(elettore);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return votanti;
	}

	@Override
	public int getNumeroVoti(ElementoDaVotare e) {
		SessioneVoto sessione = SessioneDAOImpl.getInstance().get_sessione();
		
		switch (sessione.getTipoVoto()) {
		case "categorico":
			if (e instanceof Partito) {
				try {
					PreparedStatement ps = getConnessione().prepareStatement("SELECT COUNT(*) FROM voto_categorico WHERE partito = ?");
					ps.setString(1, e.getNome());
					ResultSet rs = ps.executeQuery();
					rs.next();
					return rs.getInt(1);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			} else if (e instanceof Candidato) {
				try {
					PreparedStatement ps = getConnessione().prepareStatement("SELECT COUNT(*) FROM voto_categorico WHERE candidato = ?");
					String cognome = ((Candidato)e).getCognome();
					ps.setString(1, cognome);
					ResultSet rs = ps.executeQuery();
					rs.next();
					return rs.getInt(1);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			break;
			
		case "categorico preferenze":
			try {
				PreparedStatement ps = getConnessione().prepareStatement("SELECT COUNT(*) FROM voto_categorico_pref WHERE partito = ?");
				ps.setString(1, e.getNome());
				ResultSet rs = ps.executeQuery();
				rs.next();
				return rs.getInt(1);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			break;	
		default: throw new IllegalArgumentException();
		}
		return -1;
	}

	@Override
	public int getNumVotiCategorico(Candidato c) {
		SessioneVoto sessione = SessioneDAOImpl.getInstance().get_sessione();
		if (!sessione.getTipoVoto().equals("categorico preferenze")) throw new IllegalArgumentException("Questo metodo deve essere usato soltanto nel caso di voto categorico con preferenze");
		
		try {
			PreparedStatement ps = getConnessione().prepareStatement("SELECT COUNT(*) FROM voto_categorico_pref WHERE candidato_nel_partito = ?");
			ps.setString(1, c.getCognome());
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public int getNumeroSi() {
		SessioneVoto sessione = SessioneDAOImpl.getInstance().get_sessione();
		if (!sessione.getTipoVoto().equals("referendum")) throw new IllegalArgumentException("Questo metodo deve essere usato soltanto nel caso di referendum");
		
		try {
			PreparedStatement ps = getConnessione().prepareStatement("SELECT COUNT(*) FROM voto_referendum WHERE favorevole = true");
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public int getNumeroNo() {
		SessioneVoto sessione = SessioneDAOImpl.getInstance().get_sessione();
		if (!sessione.getTipoVoto().equals("referendum")) throw new IllegalArgumentException("Questo metodo deve essere usato soltanto nel caso di referendum");
		
		try {
			PreparedStatement ps = getConnessione().prepareStatement("SELECT COUNT(*) FROM voto_referendum WHERE favorevole = false");
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public int getNumeroByPosizione(ElementoDaVotare e, int pos) {
		try {
			if (SessioneDAOImpl.getInstance().get_sessione().sessione_con_partiti()) {
				PreparedStatement ps = getConnessione().prepareStatement("SELECT COUNT(*) FROM voto_ordinale WHERE posizione = ? AND partito = ?");
				ps.setInt(1, pos);
				ps.setString(2, e.getNome());
				ResultSet rs = ps.executeQuery();
				rs.next();
				return rs.getInt(1);
			} else {
				PreparedStatement ps = getConnessione().prepareStatement("SELECT COUNT(*) FROM voto_ordinale WHERE posizione = ? AND candidato = ?");	
				ps.setInt(1, pos);
				ps.setString(2, ((Candidato)e).getCognome());
				ResultSet rs = ps.executeQuery();
				rs.next();
				return rs.getInt(1);
			}
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return -1;
	}
	
	
}
