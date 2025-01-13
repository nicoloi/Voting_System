package com.project.controllers;
import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.project.model.SessioneVoto;
import com.project.model.*;
import com.project.dao.*;


public class Controller_conferma {
	
	private SessioneVoto sessione;

    @FXML
    void premi_no(ActionEvent event) throws IOException {
    	if (sessione.sessione_con_partiti()) {
    		
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
    		
    		CandidatoDAO cDao = CandidatoDAOImpl.getInstance();
    		List<Candidato> candidati = cDao.getAllCandidati();
    		
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/scegli_lista.fxml"));
    		Parent root = loader.load();
    		Controller_lista cl = loader.getController();
    		cl.setSessione(sessione);
    		cl.setCandidati(candidati);
    		
    		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		Scene scene = new Scene(root);
    		stage.setScene(scene);
    		stage.setTitle("Seleziona candidati coinvolti");
    		stage.show();
    	}
    	
    }

    @FXML
    void premi_si(ActionEvent event) throws IOException {
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
