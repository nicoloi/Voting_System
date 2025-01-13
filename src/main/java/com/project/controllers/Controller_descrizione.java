package com.project.controllers;
import java.io.IOException;
import java.util.List;

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
import com.project.model.*;
import com.project.dao.*;

public class Controller_descrizione {
	
	private SessioneVoto sessione;

    @FXML
    private TextArea descrizione_textarea;

    @FXML
    private Label lbl_errore;

    @FXML
    void premi_avanti(ActionEvent event) throws IOException {
    	if (descrizione_textarea == null || descrizione_textarea.getText().trim().equals("")) {
    		lbl_errore.setText("Il campo non pu� essere lasciato vuoto");
    		return;
    	}
    	
    	sessione.setDescrizione(descrizione_textarea.getText());
    	
    	if (sessione.getTipoVoto().equals("categorico preferenze")) {
    		sessione.setVotaPartiti(true);
    		
    		PartitoDAO pDao = PartitoDAOImpl.getInstance();
    		List<Partito> partiti = pDao.getAllPartiti();
    		
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/scegli_lista.fxml"));
    		Parent root = loader.load();
    		Controller_lista cl = loader.getController();
    		cl.setSessione(sessione);
    		cl.setPartiti(partiti);
    		
    		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		Scene scene = new Scene(root);
    		stage.setScene(scene);
    		stage.setTitle("Seleziona partiti coinvolti");
    		stage.show();
    	} else {
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/schermata_coinvolti.fxml"));
	    	Parent root = loader.load();
	    	Controller_coinvolti cc = loader.getController();
	    	cc.setSessione(sessione);
	    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Imposta presenza di partiti o candidati");
			stage.show();
    	}
    	
    }
    
    public void setSessione(SessioneVoto sessione) {
    	this.sessione = sessione;
    }
}
