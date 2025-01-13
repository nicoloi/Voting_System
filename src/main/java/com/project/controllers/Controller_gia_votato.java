package com.project.controllers;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.project.model.Elettore;
import com.project.model.SessioneVoto;

public class Controller_gia_votato {
	
	private SessioneVoto sessione;
	private Elettore elettore;

    @FXML
    void premi_indietro(ActionEvent event) throws IOException {
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

    public void setSessione(SessioneVoto sessione) {
    	this.sessione = sessione;
    }
    
    public void setElettore(Elettore elettore) {
		this.elettore = elettore;
	}
}
