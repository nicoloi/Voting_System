package com.project.controllers;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import com.project.model.SessioneVoto;
//import model.*;
import com.project.dao.*;

public class Controller_ris_referendum {
	
	private SessioneVoto sessione;

    @FXML
    private Button bottone;

    @FXML
    private Label lbl_infoVincitore;

    @FXML
    private Label lbl_num_contrari;

    @FXML
    private Label lbl_num_favorevoli;

    @FXML
    private Label lbl_numeroVotanti;

    @FXML
    private Label lbl_tipoVittoria;

    @FXML
    void premi_bottone(ActionEvent event) throws IOException {
    	if (bottone.getText().equals("Fine")) {
    		SessioneDAOImpl.getInstance().delete_sessione();
    	} else if (bottone.getText().equals("Riapri sessione")) {
    		SessioneDAOImpl.getInstance().cancella_voti_sessione();
    	}
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menu_admin.fxml"));
    	Parent root = loader.load();
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Menu admin");
		stage.show();
    }

    @FXML
    void initialize() {
    	sessione = SessioneDAOImpl.getInstance().get_sessione();
    	int numVotanti = sessione.getVotanti().size();
    	if (sessione.getTipo_vittoria().equals("con quorum"))
    		lbl_tipoVittoria.setText("Tipo vittoria: Referendum con quorum pari a " + sessione.getQuorum());
    	else {
    		lbl_tipoVittoria.setText("Tipo vittoria: Referendum senza quorum");
    	}
    	lbl_numeroVotanti.setText("Numero di elettori che hanno votato:  " + numVotanti);
    	
    	VotoDAOImpl daov = VotoDAOImpl.getInstance();
    	int favorevoli = daov.getNumeroSi();
    	int contrari = daov.getNumeroNo();
    	lbl_num_favorevoli.setText("" + favorevoli);
    	lbl_num_contrari.setText("" + contrari);
    	
    	if (sessione.getTipo_vittoria().equals("con quorum") && numVotanti < sessione.getQuorum()) {
    		//situazione di quorum non raggiunto
    		lbl_infoVincitore.setText("Il quorum non � stato raggiunto. La sessione dovr� essere riaperta per ripetere le votazioni");
    		bottone.setText("Riapri sessione");
    		return;
    	}
    	
    	if (favorevoli > contrari) {
    		lbl_infoVincitore.setText("Vincono i voti favorevoli");
    		bottone.setText("Fine");
    	} else if (favorevoli < contrari) {
    		lbl_infoVincitore.setText("Vincono i voti contrari");
    		bottone.setText("Fine");
    	} else {
    		//situazione di pareggio
    		lbl_infoVincitore.setText("Situazione di pareggio. La sessione dovr� essere riaperta per ripetere le votazioni");
    		bottone.setText("Riapri sessione");
    	}
    }

}
