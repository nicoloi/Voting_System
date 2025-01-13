package com.project.controllers;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.project.model.SessioneVoto;
import com.project.dao.*;

public class Controller_sessione {

	//attributi
	private SessioneVoto sessione; //questo attributo deve essere acceduto dagli altri controller
    
    //metodi
    
    public void carica_scena(String fileFXML, String nomeScena, ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource(fileFXML));
    	Parent root = loader.load();
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle(nomeScena);
		stage.show();
    }
    
    @FXML
    void premi_logout(ActionEvent event) throws IOException {
    	carica_scena("/view/schermata_iniziale.fxml", "Sistema elettorale", event);
    }

    @FXML
    void premi_nuova_sessione(ActionEvent event) throws IOException {
    	SessioneDAOImpl istanza = SessioneDAOImpl.getInstance();
    	if (istanza.checkSessione()) {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/errore.fxml"));
        	Parent root = loader.load();
        	Controller_errore ce = loader.getController();
        	ce.setLabel("Non puoi creare una nuova sessione perch� ne � gi� presente una aperta");
        	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		Scene scene = new Scene(root);
    		stage.setScene(scene);
    		stage.setTitle("Errore, sessione gi� aperta");
    		stage.show();
    	} else {
    		carica_scena("/view/configurazione_sessione.fxml", "Configura sessione", event);
    	}
    }

    @FXML
    void premi_termina_sessione(ActionEvent event) throws IOException {    	
    	SessioneDAOImpl istanza = SessioneDAOImpl.getInstance();
    	SessioneVoto sessione = istanza.get_sessione();
    	if (sessione == null) {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/errore.fxml"));
        	Parent root = loader.load();
        	Controller_errore ce = loader.getController();
        	ce.setLabel("Non � stata impostata alcuna sessione");
        	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		Scene scene = new Scene(root);
    		stage.setScene(scene);
    		stage.setTitle("errore, sessione non presente");
    		stage.show();
       		return;
    	}
    	
    	sessione.chiudi_sessione();    	    	
    	switch (sessione.getTipoVoto()) {
    	case "categorico":
    		carica_scena("/view/risultati_categorico.fxml", "Esito sessione", event);
    		break;
    	case "categorico preferenze":
			carica_scena("/view/risultati_categorico_pref.fxml", "Esito sessione", event);
			break;
		case "ordinale":
			carica_scena("/view/risultati_ordinale.fxml", "Esito sessione", event);
			break;
		case "referendum":
			carica_scena("/view/risultati_referendum.fxml", "Esito referendum", event);
			break;
    	}
    }

    @FXML
    void premi_indietro(ActionEvent event) throws IOException {
    	carica_scena("/view/menu_admin.fxml", "Menu admin", event);
    }

    @FXML
    void premi_referendum(ActionEvent event) throws IOException {
    	sessione = new SessioneVoto();
    	sessione.setTipoVoto("referendum");
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/schermata_vittoria_referendum.fxml"));
    	Parent root = loader.load();
    	Controller_vittoria_referendum cvf = loader.getController();
    	cvf.setSessione(sessione);
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Seleziona vittoria");
		stage.show();
    }

    @FXML
    //nel voto categorico con preferenze bisogna votare solo i partiti
    void premi_voto_cat_preferenze(ActionEvent event) throws IOException {
    	sessione = new SessioneVoto();
    	sessione.setTipoVoto("categorico preferenze");
    		
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/schermata_vittoria.fxml"));
    	Parent root = loader.load();
    	Controller_vittoria cv = loader.getController();
    	cv.setSessione(sessione);
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Seleziona vittoria");
		stage.show();
    }

    @FXML
    void premi_voto_categorico(ActionEvent event) throws IOException {
    	sessione = new SessioneVoto();
    	sessione.setTipoVoto("categorico");
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/schermata_vittoria.fxml"));
    	Parent root = loader.load();
    	Controller_vittoria cv = loader.getController();
    	cv.setSessione(sessione);
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Seleziona vittoria");
		stage.show();
    }

    @FXML
    void premi_voto_ordinale(ActionEvent event) throws IOException {
    	sessione = new SessioneVoto();
    	sessione.setTipoVoto("ordinale");
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/schermata_vittoria.fxml"));
    	Parent root = loader.load();
    	Controller_vittoria cv = loader.getController();
    	cv.setSessione(sessione);
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Seleziona vittoria");
		stage.show();
    }
    
    
}
