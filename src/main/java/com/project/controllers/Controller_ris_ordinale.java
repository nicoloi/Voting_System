package com.project.controllers;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

public class Controller_ris_ordinale {
	
	private SessioneVoto sessione;
	
	//set dove metto gli elementi che sono risultati nella stessa posizione di almeno un altro elemento
	Set<ElementoDaVotare> set_pareggio = new HashSet<ElementoDaVotare>();

    @FXML
    private Button bottone;

    @FXML
    private Label lbl_descrizione;

    @FXML
    private Label lbl_infoVincitore;

    @FXML
    private Label lbl_info_ordine;

    @FXML
    private Label lbl_numeroVotanti;

    @FXML
    private Label lbl_tipoVittoria;

    @FXML
    private Pane pannello;

    @FXML
    void premi_bottone(ActionEvent event) throws IOException {
    	if (bottone.getText().equals("Fine")) {
    		//caso in cui � presente un vincitore
    		SessioneDAOImpl.getInstance().delete_sessione();
    	} else if (bottone.getText().equals("Riapri nuova sessione")) {
    		//caso in cui c'� un pareggio
    		SessioneVoto nuovaSessione = new SessioneVoto();
    		nuovaSessione.setTipoVoto(sessione.getTipoVoto());
    		nuovaSessione.setTipo_vittoria(sessione.getTipo_vittoria());
    		nuovaSessione.setDescrizione(sessione.getDescrizione());
    		nuovaSessione.setVotaPartiti(sessione.sessione_con_partiti());
    		nuovaSessione.imposta_quorum(-1);
    		
    		for (ElementoDaVotare e : set_pareggio) {
    			nuovaSessione.add_candidato(e);
    		}
    		nuovaSessione.apri_sessione();
    		SessioneDAOImpl.getInstance().delete_sessione();
    		SessioneDAOImpl.getInstance().aggiungi_sessione(nuovaSessione);
    	} else if (bottone.getText().equals("Riapri sessione")) {
    		//caso in cui non si raggiunge la maggioranza assoluta
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
    	lbl_descrizione.setText(sessione.getDescrizione());
    	if (sessione.sessione_con_partiti())
    		lbl_info_ordine.setText("La votazione � terminata con i partiti nel seguente ordine:");
    	else
    		lbl_info_ordine.setText("La votazione � terminata con i candidati nel seguente ordine:");
    	VotoDAOImpl daov = VotoDAOImpl.getInstance();
    	
    	Map<ElementoDaVotare, Integer> mappa;
    	List<Integer> lista_voti;
    	
    	if (sessione.sessione_con_partiti()) {
	    	List<Partito> partiti_coinvolti = PartitoDAOImpl.getInstance().getPartitiSessione(sessione);
	    	
	    	double y = 220;
	    	int numPartiti = partiti_coinvolti.size();
	    	for (int i = 1; i <= numPartiti; i++) {
	    		
	    		mappa = new HashMap<>();
	    		lista_voti = new ArrayList<>();
	    		
	    		for (Partito p : partiti_coinvolti) {
	    			int n = daov.getNumeroByPosizione(p, i);
	    			lista_voti.add(n);
	    			mappa.put(p, n);
	    		}
	    		
	    		lista_voti.sort(null);
	    		
	    		for (ElementoDaVotare e : mappa.keySet()) {
		    		if (mappa.get(e) == lista_voti.get(lista_voti.size() - 1)) {
		    			if (mappa.get(e) == lista_voti.get(lista_voti.size() - 2)) {
		    				set_pareggio.add(e);
		    			}
		    			Label l = new Label();
			    		l.setText("- Posizione " + i + ":   " + e);
			    		l.relocate(47, y);
			    		y += 25;
			    		pannello.getChildren().addAll(l);
		    		}
		    	}
	    		
	    		if (i == 1 && sessione.getTipo_vittoria().equals("maggioranza assoluta")) {
	    			int numVotiTot = sessione.getVotanti().size();
	        		int maggAssoluta = (numVotiTot / 2) + 1;
	        		int punteggioMax = lista_voti.get(lista_voti.size() - 1);
	        		if (punteggioMax < maggAssoluta) {
	        			//caso in cui il partito in prima posizione non ha raggiunto la maggioranza assoluta
	        			lbl_infoVincitore.setText("Il partito in prima posizione non ha raggiunto la maggioranza assoluta. La sessione dovr� essere riaperta");
	    	    		bottone.setText("Riapri sessione");
	    	    		return;
	        		}
	    		}
	    	}
	    	
	    	//controllo la presenza di eventuale pareggio tra partiti o candidati
	    	if (!set_pareggio.isEmpty()) {
	    		lbl_infoVincitore.setText("Due o pi� partiti sono risultati nella stessa posizione. La sessione verr� riaperta includendo solo i partiti coinvolti nel pareggio.");
	    		bottone.setText("Riapri nuova sessione");
	    	} else {	
	    		bottone.setText("Fine");
	    	}
    	} else {
    		List<Candidato> candidati_coinvolti = CandidatoDAOImpl.getInstance().getCandidatiSessione(sessione);
	    	
	    	double y = 220;
	    	int numCand = candidati_coinvolti.size();
	    	for (int i = 1; i <= numCand; i++) {
	    		
	    		mappa = new HashMap<>();
	    		lista_voti = new ArrayList<>();
	    		
	    		for (Candidato c : candidati_coinvolti) {
	    			int n = daov.getNumeroByPosizione(c, i);
	    			lista_voti.add(n);
	    			mappa.put(c, n);
	    		}
	    		
	    		lista_voti.sort(null);
	    		
	    		for (ElementoDaVotare e : mappa.keySet()) {
		    		if (mappa.get(e) == lista_voti.get(lista_voti.size() - 1)) {
		    			if (mappa.get(e) == lista_voti.get(lista_voti.size() - 2)) {
		    				set_pareggio.add(e);
		    			}
		    			Label l = new Label();
			    		l.setText("- Posizione " + i + ":   " + e);
			    		l.relocate(47, y);
			    		y += 25;
			    		pannello.getChildren().addAll(l);
		    		}
		    	}
	    		
	    		if (i == 1 && sessione.getTipo_vittoria().equals("maggioranza assoluta")) {
	    			int numVotiTot = sessione.getVotanti().size();
	        		int maggAssoluta = (numVotiTot / 2) + 1;
	        		int punteggioMax = lista_voti.get(lista_voti.size() - 1);
	        		if (punteggioMax < maggAssoluta) {
	        			//caso in cui il partito in prima posizione non ha raggiunto la maggioranza assoluta
	        			lbl_infoVincitore.setText("Il candidato in prima posizione non ha raggiunto la maggioranza assoluta. La sessione dovr� essere riaperta");
	    	    		bottone.setText("Riapri sessione");
	    	    		return;
	        		}
	    		}
	    	}
	    	
	    	//controllo la presenza di eventuale pareggio candidati
	    	if (!set_pareggio.isEmpty()) {
	    		lbl_infoVincitore.setText("Due o pi� candidati sono risultati nella stessa posizione. La sessione verr� riaperta includendo solo i candidati coinvolti nel pareggio.");
	    		bottone.setText("Riapri nuova sessione");
	    	} else {	
	    		bottone.setText("Fine");
	    	}
    	}
    }
}
