package com.project.controllers;
import java.io.IOException;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import com.project.model.SessioneVoto;
//import model.*;
import com.project.dao.*;


public class Controller_quesito_referendum {
	
	private SessioneVoto sessione;

    @FXML
    private Label lbl_errore;

    @FXML
    private TextArea quesito;

    @FXML
    void premi_apri_sessione(ActionEvent event) throws IOException {
    	if (quesito == null || quesito.getText().trim().equals("")) {
    		lbl_errore.setText("Il campo non pu� essere lasciato vuoto");
    		return;
    	}
    	
    	sessione.setDescrizione(quesito.getText());
    	sessione.setVotaPartiti(false);
    	sessione.setApertura(true);
    	
    	SessioneDAOImpl istanza = SessioneDAOImpl.getInstance();
    	istanza.aggiungi_sessione(sessione);
    	
    	//torna alla pagina menu_admin.fxml
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menu_admin.fxml"));
    	Parent root = loader.load();
    	
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("menu");
		stage.show();
    }

    public void setSessione(SessioneVoto sessione) {
    	this.sessione = sessione;
    }
}
