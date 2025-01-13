package com.project.controllers;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import com.project.model.Candidato;
import com.project.model.Partito;
import com.project.model.SessioneVoto;
//import model.*;
import com.project.dao.*;

public class Controller_lista {
	
	//attributi
	private SessioneVoto sessione;
	
	private Map<CheckBox, Partito> mappa_partiti;
	private Map<CheckBox, Candidato> mappa_cand;

	@FXML
	private Pane pannello;
	
	@FXML
	private Label lbl_errore;

    @FXML
    void premi_apriSessione(ActionEvent event) throws IOException {
    	//controllo che siano stati selezionati almeno due elementi
    	int numElem = 0;
    	for (Node n : pannello.getChildren()) {
    		if (n instanceof CheckBox) {
    			CheckBox cb = (CheckBox) n;
    			if (cb.isSelected()) {
    				numElem++;
    			}
    		}
    	}
    	
    	if (numElem < 2) {
    		lbl_errore.setText("Seleziona almeno due elementi!");
    		return;
    	}
    	
    	
    	//aggiungo gli elementi selezionati alla sessione
    	for (Node n : pannello.getChildren()) {
    		if (n instanceof CheckBox) {
    			CheckBox cb = (CheckBox) n;
    			if (cb.isSelected()) {
    				if (sessione.sessione_con_partiti()) {
    					sessione.add_candidato(mappa_partiti.get(cb));
    				} else {
    					sessione.add_candidato(mappa_cand.get(cb));
    				}
    			}
    		}
    	}
    	
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
    
    public void setPartiti(List<Partito> elementi) {
    	mappa_partiti = new HashMap<CheckBox, Partito>();
    	double coordY = 100;
    	
    	for (Partito p : elementi) {
    		CheckBox cb = new CheckBox(p.getNome());
    		cb.relocate(100, coordY);
    		coordY += 30;
    		mappa_partiti.put(cb, p);
    		pannello.getChildren().addAll(cb);
    	}
    }
    
    public void setCandidati(List<Candidato> elementi) {
    	mappa_cand = new HashMap<>();
    	double coordY = 100;
    	
    	for (Candidato c : elementi) {
    		CheckBox cb = new CheckBox(c.getNome() + " " + c.getCognome());
    		cb.relocate(100, coordY);
    		coordY += 30;
    		mappa_cand.put(cb, c);
    		pannello.getChildren().addAll(cb);
    	}
    }

}
