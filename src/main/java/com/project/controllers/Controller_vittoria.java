package com.project.controllers;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import com.project.model.SessioneVoto;

public class Controller_vittoria {
	
	//attributi
	
	private SessioneVoto sessione;

    @FXML
    private RadioButton btn_maggAssol;

    @FXML
    private RadioButton btn_maggioranza;

    @FXML
    private ToggleGroup vittoria;
    
    //metodi
    
    public void setSessione(SessioneVoto sessione) {
    	this.sessione = sessione;
    }

    @FXML
    void premi_conferma(ActionEvent event) throws IOException {	
    	if (btn_maggioranza.isSelected()) {
    		sessione.setTipo_vittoria("maggioranza");
    	} else if (btn_maggAssol.isSelected()) {
    		sessione.setTipo_vittoria("maggioranza assoluta");
    	}
    
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/descrizione_voto.fxml"));
    	Parent root = loader.load();
    	Controller_descrizione cd = loader.getController();
    	cd.setSessione(sessione);
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Imposta descrizione");
		stage.show();
    }
}
