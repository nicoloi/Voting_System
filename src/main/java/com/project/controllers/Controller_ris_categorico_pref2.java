package com.project.controllers;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import com.project.model.Partito;
import com.project.model.*;
import com.project.dao.*;
public class Controller_ris_categorico_pref2 {
	
	Partito vincitore;

    @FXML
    private Label lbl_info;

    @FXML
    private Pane pannello;

    @FXML
    void premi_bottone(ActionEvent event) throws IOException {
    	SessioneDAOImpl.getInstance().delete_sessione();
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menu_admin.fxml"));
    	Parent root = loader.load();
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Menu admin");
		stage.show();
    }

    void inizializza() {
    	lbl_info.setText("Numero di voti ricevuti da ciascun candidato del partito " + vincitore.getNome());
    	VotoDAOImpl daov = VotoDAOImpl.getInstance();
    	double y = 180;
    	for (Candidato c : vincitore) {
    		Label l = new Label();
    		l.setText("- " + c + ":  " + daov.getNumVotiCategorico(c));
    		l.relocate(47, y);
    		y += 25;
    		pannello.getChildren().addAll(l);
    	}
    }
    
    public void setVincitore(Partito p) {
    	vincitore = p;
    }
}
