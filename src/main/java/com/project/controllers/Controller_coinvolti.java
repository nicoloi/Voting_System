package com.project.controllers;
import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import com.project.model.SessioneVoto;

import com.project.model.*;
import com.project.dao.*;

public class Controller_coinvolti {
	
	private SessioneVoto sessione;

    @FXML
    private ToggleGroup group;

    @FXML
    private RadioButton rb_cand;

    @FXML
    private RadioButton rb_partiti;

    @FXML
    void premi_avanti(ActionEvent event) throws IOException {
    	if (rb_partiti.isSelected()) {
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
    		
    		
    	} else if (rb_cand.isSelected()) {
    		sessione.setVotaPartiti(false);
    		
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

    public void setSessione(SessioneVoto sessione) {
    	this.sessione = sessione;
    }
}
