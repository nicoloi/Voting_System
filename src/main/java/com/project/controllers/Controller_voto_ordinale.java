package com.project.controllers;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.project.dao.SessioneDAOImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import com.project.model.*;
import com.project.dao.*;

public class Controller_voto_ordinale {

	private SessioneVoto sessione = SessioneDAOImpl.getInstance().get_sessione();;
	private Elettore elettore;
	private Map<Integer, ComboBox<String>> mappa;

    @FXML
    private Pane pannello;
    
    @FXML
    private Label lbl_errore;

    @FXML
    void premi_vota(ActionEvent event) throws IOException {
    	//controlla che in ogni posizione ci sia un elemento diverso
    	Set<String> insieme = new HashSet<>();
    	for (int i = 1; i <= mappa.size(); i++) {
    		insieme.add(mappa.get(i).getValue());
    	}
    	
    	if (insieme.size() != mappa.size()) {
    		lbl_errore.setText("ERRORE! � presente lo stesso elemento in posizioni diverse");
    		return;
    	}
    	
    	//prendi gli elementi dalla mappa e metti il voto nel db
    	VotoOrdinale v = new VotoOrdinale(sessione);
    	for (int posizione = 1; posizione <= mappa.size(); posizione++) {
    		v.add_elemento(posizione, mappa.get(posizione).getValue());
    	}
    	VotoDAO daov = VotoDAOImpl.getInstance();
    	daov.putVoto(v);
    	
    	//metti l'elettore che ha votato nel db, cos� non potr� pi� votare
    	try {
			UtenteDAO daou = UtenteDAOImpl.getInstance();
			daou.put_elettore(elettore, sessione);
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
			daou.put_elettore(elettore, sessione);
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

    public void setElettore(Elettore elettore) {
		this.elettore = elettore;
	}
    
    public void setElementi(Set<ElementoDaVotare> lista) {
    	mappa = new HashMap<>();
    	double coordy = 120;
    	int posizione = 1;
    	
    	ObservableList<String> elementi = FXCollections.observableArrayList();
    	double y_label = 150;
    	for (ElementoDaVotare e : lista) {
    		elementi.add(e.toString());
    		Label l = new Label("- " + e.toString());
    		l.relocate(100, y_label);
    		y_label += 20;
    		pannello.getChildren().addAll(l);
    	}
    	
    	for (int i = 0; i < elementi.size(); i++) {
    		ComboBox<String> combo = new ComboBox<>();
    		combo.relocate(335, coordy);
    		combo.setItems(elementi);
    		pannello.getChildren().addAll(combo);
    		mappa.put(posizione, combo);
    		
    		Label l = new Label("posizione " + posizione);
    		l.relocate(550, coordy);
    		pannello.getChildren().addAll(l);
    		
    		coordy += 40;
    		posizione++;
    	}
    }
}
