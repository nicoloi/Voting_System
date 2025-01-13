package com.project.controllers;
import java.io.IOException;

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

public class Controller_vittoria_referendum {
	
	private SessioneVoto sessione;

    @FXML
    private RadioButton btn_noQuorum;

    @FXML
    private RadioButton btn_quorum;

    @FXML
    private ToggleGroup vittoria;

    @FXML
    void premi_conferma(ActionEvent event) throws IOException {
    	if (btn_quorum.isSelected()) {
    		sessione.setTipo_vittoria("con quorum");
    		
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/imposta_quorum.fxml"));
        	Parent root = loader.load();
        	Controller_quorum cq = loader.getController();
        	cq.setSessione(sessione);
        	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		Scene scene = new Scene(root);
    		stage.setScene(scene);
    		stage.setTitle("Imposta valore del quorum");
    		stage.show();
    	} else if (btn_noQuorum.isSelected()) {
    		sessione.setTipo_vittoria("senza quorum");
    		
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/quesito_referendum.fxml"));
        	Parent root = loader.load();
        	Controller_quesito_referendum cqr = loader.getController();
        	cqr.setSessione(sessione);
        	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		Scene scene = new Scene(root);
    		stage.setScene(scene);
    		stage.setTitle("Imposta quesito referendum");
    		stage.show();
    	}
    }

    public void setSessione(SessioneVoto sessione) {
    	this.sessione = sessione;
    }
}
