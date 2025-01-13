package com.project.controllers;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.project.model.SessioneVoto;

public class Controller_quorum {

	private SessioneVoto sessione;
	
    @FXML
    private TextField quorum;
    
    @FXML
    private Label lbl_errore;

    @FXML
    void premi_avanti(ActionEvent event) throws IOException {
    	if (quorum.getText().trim().equals("")) {
    		lbl_errore.setText("Il campo non pu� essere lasciato vuoto");
    		return;
    	}
    	
    	int val_quorum;
    	
    	try {
    		val_quorum = Integer.parseInt(quorum.getText());
    	} catch (NumberFormatException e) {
    		lbl_errore.setText("Il valore del quorum deve essere un numero!");
    		return;
		}
    	
    	sessione.imposta_quorum(val_quorum);
    	
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

	public void setSessione(SessioneVoto sessione) {
		this.sessione = sessione;
	}

}
