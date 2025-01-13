package com.project.controllers;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
import com.project.model.ElementoDaVotare;
import com.project.model.Elettore;
import com.project.model.SessioneVoto;
import com.project.model.*;
import com.project.dao.*;

public class Controller_voto_cat_pref {

	private Map<RadioButton, ElementoDaVotare> mappa;
	private SessioneVoto sessione;
	private Elettore elettore;
	
    @FXML
    private Pane pannello;

    @FXML
    void premi_conferma(ActionEvent event) throws IOException {
    	//metti il voto del candidato/partito nel db
    	ElementoDaVotare votato = null;
    	for (Node n : pannello.getChildren()) {
    		if (n instanceof RadioButton) {
    			RadioButton rb = (RadioButton)n;
    			if (rb.isSelected()) {
    				votato = mappa.get(rb);
    			}
    		}
    	}   	
    	Partito partito_votato = (Partito) votato;
    	
    	//metti l'elettore che ha votato nel db, cos� non potr� pi� votare
    	try {
			UtenteDAO daou = UtenteDAOImpl.getInstance();
			daou.put_elettore(elettore, sessione);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	if (partito_votato.getNome().equals("scheda bianca")) {
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
    	} else {
	    	//cambia scena
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/scegli_candidato_nel_partito.fxml"));
	    	Parent root = loader.load();
	    	Controller_candidato_nel_partito contr = loader.getController();
	    	contr.setPartito(partito_votato);
	    	contr.setElettore(elettore);
	    	contr.setElementi();
	    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Vota il candidato all'interno del partito selezionato");
			stage.show();
    	}
    }

    public void setElettore(Elettore elettore) {
		this.elettore = elettore;
	}
    
    public void setElementi(Set<ElementoDaVotare> lista) {
    	sessione = SessioneDAOImpl.getInstance().get_sessione();
    	mappa = new HashMap<RadioButton, ElementoDaVotare>();
    	ToggleGroup tg = new ToggleGroup();
    	double coordy = 100;
		for (ElementoDaVotare e : lista) {
			RadioButton rb = new RadioButton();
			rb.setToggleGroup(tg);
			rb.relocate(100, coordy);
			coordy += 30;
			if (e instanceof Partito) {
				Partito p = (Partito)e;
				rb.setText(p.getNome());
				mappa.put(rb, p);
			} else if (e instanceof Candidato) {
				Candidato c = (Candidato)e;
				rb.setText(c.getNome() + " " + c.getCognome());
				mappa.put(rb, c);
			}
			pannello.getChildren().addAll(rb);
		}
		
		//inserimento radiobutton scheda bianca
		RadioButton rb = new RadioButton();
		rb.setToggleGroup(tg);
		rb.relocate(100, coordy);
		rb.setText("Scheda bianca");
		rb.setSelected(true);
		pannello.getChildren().addAll(rb);
		if (sessione.sessione_con_partiti()) {
			mappa.put(rb, new Partito("scheda bianca"));
		} else {
			mappa.put(rb, new Candidato("scheda bianca", "scheda bianca", null));
		}
	}
}
