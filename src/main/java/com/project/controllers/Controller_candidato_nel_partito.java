package com.project.controllers;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import com.project.model.Candidato;
import com.project.model.Elettore;
import com.project.model.Partito;
import com.project.model.SessioneVoto;
import com.project.model.*;
import com.project.dao.*;


public class Controller_candidato_nel_partito {

	private Partito partito;
	private SessioneVoto sessione;
	private Elettore elettore;
	private Map<RadioButton, Candidato> mappa;

    @FXML
    private Pane pannello;

    @FXML
    void premi_conferma(ActionEvent event) throws IOException {
    	//crea l'istanza di voto categorico preferenze e metti tutto nel db
    	
    	Candidato votato = null;
    	for (Node n : pannello.getChildren()) {
    		if (n instanceof RadioButton) {
    			RadioButton rb = (RadioButton)n;
    			if (rb.isSelected()) {
    				votato = mappa.get(rb);
    			}
    		}
    	}
    	
    	VotoCategPref vcp = new VotoCategPref(partito, sessione);
    	vcp.add_candidato(votato);
    	VotoDAO daov = VotoDAOImpl.getInstance();
    	daov.putVoto(vcp);
    	
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

    public void setPartito(Partito partito) {
    	this.partito = partito;
    }
    
    public void setElettore(Elettore elettore) {
		this.elettore = elettore;
	}
    
    public void setElementi() {
    	sessione = SessioneDAOImpl.getInstance().get_sessione();
    	mappa = new HashMap<RadioButton, Candidato>();
    	ToggleGroup tg = new ToggleGroup();
    	double coordy = 100;
		for (Candidato c : partito) {
			RadioButton rb = new RadioButton();
			rb.setToggleGroup(tg);
			rb.relocate(100, coordy);
			coordy += 30;
			rb.setText(c.getNome() + " " + c.getCognome());
			pannello.getChildren().addAll(rb);
			mappa.put(rb, c);
		}
    }
}
