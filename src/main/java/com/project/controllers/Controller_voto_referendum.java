package com.project.controllers;
import java.io.IOException;
import java.sql.SQLException;

import com.project.dao.SessioneDAOImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import com.project.model.Elettore;
import com.project.model.SessioneVoto;
import com.project.model.*;
import com.project.dao.*;

public class Controller_voto_referendum {

	private SessioneVoto sessione = SessioneDAOImpl.getInstance().get_sessione();
	private Elettore elettore;
	
    @FXML
    private Label lbl_quesito;

    @FXML
    void premi_no(ActionEvent event) throws IOException {
    	Referendum r = new Referendum(false, sessione);
    	VotoDAO daov = VotoDAOImpl.getInstance();
    	daov.putVoto(r);
    	
    	//metti l'elettore che ha votato nel db, cos� non potr� pi� votare
    	try {
			UtenteDAO daou = UtenteDAOImpl.getInstance();
			daou.put_elettore(elettore, SessioneDAOImpl.getInstance().get_sessione());
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	//cambia scena
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menu_elettore.fxml"));
    	Parent root = loader.load();
    	Controller_menu_elettore cme = loader.getController();
    	cme.setElettore(elettore);
    	cme.setSessione(sessione);
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Menu elettore");
		stage.show();
    }

    @FXML
    void premi_si(ActionEvent event) throws IOException {
    	Referendum r = new Referendum(true, sessione);
    	VotoDAO daov = VotoDAOImpl.getInstance();
    	daov.putVoto(r);
    	
    	//metti l'elettore che ha votato nel db, cos� non potr� pi� votare
    	try {
			UtenteDAO daou = UtenteDAOImpl.getInstance();
			daou.put_elettore(elettore, SessioneDAOImpl.getInstance().get_sessione());
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	//cambia scena
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menu_elettore.fxml"));
    	Parent root = loader.load();
    	Controller_menu_elettore cme = loader.getController();
    	cme.setElettore(elettore);
    	cme.setSessione(sessione);
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Menu elettore");
		stage.show();
    }
    
    @FXML
    void premi_scheda_bianca(ActionEvent event) throws IOException {
    	//metti l'elettore che ha votato nel db, cos� non potr� pi� votare
    	try {
			UtenteDAO daou = UtenteDAOImpl.getInstance();
			daou.put_elettore(elettore, SessioneDAOImpl.getInstance().get_sessione());
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	//cambia scena
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menu_elettore.fxml"));
    	Parent root = loader.load();
    	Controller_menu_elettore cme = loader.getController();
    	cme.setElettore(elettore);
    	cme.setSessione(sessione);
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Menu elettore");
		stage.show();
    }

    public void setLabel(String s) {
    	lbl_quesito.setText(s);
    }
    
    
    public void setElettore(Elettore elettore) {
		this.elettore = elettore;
	}
}
