package com.project.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.project.model.*;

//classe singleton
public class UtenteDAOImpl implements UtenteDAO{
	
	private static UtenteDAOImpl instance = null;
	private Connection conn;
	
	private UtenteDAOImpl() throws SQLException {
		getConnessione();
	}
	
	public static UtenteDAOImpl getInstance() throws SQLException {
		if (instance == null) instance = new UtenteDAOImpl();
		return instance;
	}
	
	private void getConnessione() throws SQLException {
		conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:postgresql://localhost/sistema_di_voto?user=postgres&password=pippo");
		} catch (SQLException e) {
			throw e;
		}
	}

	@Override
	public boolean check_login_admin(String username, String pswrd) {
		try {
			PreparedStatement stat = conn.prepareStatement("SELECT COUNT(*) FROM admin WHERE username=? AND password=md5(?)");
			stat.setString(1, username);
			stat.setString(2, pswrd);
			ResultSet rs = stat.executeQuery();
			rs.next();
			if (rs.getInt(1) == 1) return true;
			else if (rs.getInt(1) == 0) return false;
			return false;
		} catch (SQLException e) {
			return false;
		}
	}

	@Override
	public boolean check_login_elettore(String cf, String pswrd) {
		try {
			PreparedStatement stat = conn.prepareStatement("SELECT COUNT(*) FROM elettore WHERE cf = ? AND password=md5(?)");
			stat.setString(1, cf);
			stat.setString(2, pswrd);
			ResultSet rs = stat.executeQuery();
			rs.next();
			if (rs.getInt(1) == 1) return true;
			else if (rs.getInt(1) == 0) return false;
			return false;
		} catch (SQLException e) {
			return false;
		}
	}

	@Override
	public void put_elettore(Elettore elettore, SessioneVoto sessione) {
		try {
			PreparedStatement stat = conn.prepareStatement("INSERT INTO elettore_sessione VALUES(?, ?)");
			stat.setString(1, sessione.getDescrizione());
			stat.setString(2, elettore.getCf());
			int affected = stat.executeUpdate();
			if (affected == 0) throw new SQLException();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean check_elettore_sessione(Elettore elettore, SessioneVoto sessione) {
		try {
			PreparedStatement stat = conn.prepareStatement("SELECT COUNT(*) FROM elettore_sessione WHERE sessione = ? AND elettore = ?");
			stat.setString(1, sessione.getDescrizione());
			stat.setString(2, elettore.getCf());
			ResultSet rs = stat.executeQuery();
			rs.next();
			if (rs.getInt(1) == 1) return true;
			else return false;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Elettore getElettore(String cf) {
		try {
			PreparedStatement stat = conn.prepareStatement("SELECT * FROM elettore WHERE cf = ?");
			stat.setString(1, cf);
			ResultSet rs = stat.executeQuery();
			rs.next();
			return new Elettore(rs.getString("nome"), rs.getString("cognome"), rs.getString("cf"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
