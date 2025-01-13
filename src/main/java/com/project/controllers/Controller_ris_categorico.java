package com.project.controllers;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import com.project.model.ElementoDaVotare;
import com.project.model.SessioneVoto;
import com.project.model.*;
import com.project.dao.*;

public class Controller_ris_categorico {
	
	private SessioneVoto sessione;
	Map<ElementoDaVotare, Integer> mappa;

    @FXML
    private Button bottone;

    @FXML
    private Label lbl_infoVincitore;

    @FXML
    private Label lbl_numeroVotanti;

    @FXML
    private Label lbl_tipoVittoria;

    @FXML
    private Pane pannello;

    @FXML
    void premi_bottone(ActionEvent event) throws IOException {
    	if (bottone.getText().equals("Fine")) {
    		//caso in cui c'� un vincitore
    		SessioneDAOImpl.getInstance().delete_sessione();
    	}
    	else if (bottone.getText().equals("Riapri sessione")) {
    		//caso in cui c'� un pareggio
    		SessioneVoto nuovaSessione = new SessioneVoto();
    		nuovaSessione.setTipoVoto(sessione.getTipoVoto());
    		nuovaSessione.setTipo_vittoria(sessione.getTipo_vittoria());
    		nuovaSessione.setDescrizione(sessione.getDescrizione());
    		nuovaSessione.setVotaPartiti(sessione.sessione_con_partiti());
    		nuovaSessione.imposta_quorum(-1);
    		
        	Object[] arr = mappa.values().toArray();
        	Arrays.sort(arr);
        	
        	for (ElementoDaVotare e : mappa.keySet()) {
	    		if (mappa.get(e) == (Integer)arr[arr.length - 1]) {
	    			nuovaSessione.add_candidato(e);
	    		}
	    	}
    		nuovaSessione.apri_sessione();
    		SessioneDAOImpl.getInstance().delete_sessione();
    		SessioneDAOImpl.getInstance().aggiungi_sessione(nuovaSessione);
    	} else {
    		//caso in cui non si raggiunge la maggioranza assoluta.
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
    	lbl_tipoVittoria.setText("Tipo vittoria:  " + sessione.getTipo_vittoria());
    	lbl_numeroVotanti.setText("Numero di elettori che hanno votato:  " + sessione.getVotanti().size());
    	VotoDAOImpl daov = VotoDAOImpl.getInstance();
    	
    	mappa = new HashMap<>();
    	
    	if (sessione.sessione_con_partiti()) {
	    	List<Partito> partiti = PartitoDAOImpl.getInstance().getPartitiSessione(sessione);
	    	double y = 180;
	    	for (Partito p : partiti) {
	    		Label l = new Label();
	    		l.setText("- " + p + ":  " + daov.getNumeroVoti(p));
	    		l.relocate(47, y);
	    		y += 25;
	    		pannello.getChildren().addAll(l);
	    		mappa.put(p, daov.getNumeroVoti(p));
	    	}
    	} else {
    		List<Candidato> candidati = CandidatoDAOImpl.getInstance().getCandidatiSessione(sessione);
    		double y = 180;
	    	for (Candidato c : candidati) {
	    		Label l = new Label();
	    		l.setText("- " + c + ":  " + daov.getNumeroVoti(c));
	    		l.relocate(47, y);
	    		y += 25;
	    		pannello.getChildren().addAll(l);
	    		mappa.put(c, daov.getNumeroVoti(c));
	    	}
    	}    	
    	Collection<Integer> coll = mappa.values();
    	Object[] arr = coll.toArray();
    	Arrays.sort(arr);
    	
    	if (sessione.getTipo_vittoria().equals("maggioranza")) {
	    	if ((Integer)arr[arr.length - 1] == (Integer)arr[arr.length - 2]) {
	    		//situazione di pareggio tra due o pi� candidati. setta il label info e il testo del button
	    		lbl_infoVincitore.setText("Situazione di pareggio. La sessione dovr� essere riaperta per ripetere le votazioni");
	    		bottone.setText("Riapri sessione");
	    		return;
	    	}
	    	
	    	//bisogna prendere il vincitore
	    	int punteggioMax = (Integer)arr[arr.length - 1];
	    	
	    	for (ElementoDaVotare e : mappa.keySet()) {
	    		if (mappa.get(e) == punteggioMax) {
	    			lbl_infoVincitore.setText("Il vincitore �:  " + e);
	    			break;
	    		}
	    	}
	    	bottone.setText("Fine");
    	} else { 
    		//caso maggioranza assoluta
    		int numVotiTot = sessione.getVotanti().size();
    		int maggAssoluta = (numVotiTot / 2) + 1;
    		int punteggioMax = (Integer)arr[arr.length - 1];
    		
    		if (punteggioMax < maggAssoluta) {
    			//caso in cui nessun candidato ha raggiunto la maggioranza assoluta
    			lbl_infoVincitore.setText("Nessun elemento votato ha raggiunto la maggioranza assoluta. La sessione dovr� essere riaperta");
	    		bottone.setText("Riapri sessione.");
	    		return;
    		}
    		
    		for (ElementoDaVotare e : mappa.keySet()) {
	    		if (mappa.get(e) == punteggioMax) {
	    			lbl_infoVincitore.setText("Il vincitore �:  " + e);
	    			break;
	    		}
	    	}
    		bottone.setText("Fine");
    	}
    }
}
