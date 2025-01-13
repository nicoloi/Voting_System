package com.project.controllers;
import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import com.project.model.Elettore;
import com.project.model.SessioneVoto;
import com.project.dao.*;

public class Controller_menu_elettore {
	
	private SessioneVoto sessione;
	private Elettore elettore;

    @FXML
    private Label lbl_tipoVoto;

    @FXML
    private Label lbl_titolo;

    @FXML
    void premi_esprimi_voto(ActionEvent event) throws IOException {
    	SessioneDAO sd = SessioneDAOImpl.getInstance();
    	if (sd.checkSessione()) {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/schermata_descrizione_voto_elettore.fxml"));
        	Parent root = loader.load();
        	
    		Controller_menu_elettore cme = loader.getController();
    		
    		this.sessione = sd.get_sessione();
    		switch (sessione.getTipoVoto()) {
			case "categorico":
				cme.setTipoVoto("VOTO CATEGORICO");
				break;
			case "categorico preferenze":
				cme.setTipoVoto("VOTO CATEGORICO CON PREFERENZE");
				break;
			case "ordinale":
				cme.setTipoVoto("VOTO ORDINALE");
				break;
			case "referendum":
				cme.setTipoVoto("REFERENDUM");
				break;
			}
    		
    		cme.setTitolo(sessione.getDescrizione());
    		cme.setSessione(sessione);
    		cme.setElettore(elettore);
    		
    		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		Scene scene = new Scene(root);
    		stage.setScene(scene);
    		stage.setTitle("Descrizione votazione");
    		stage.show();
    	} else {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/errore_sessione_non_presente.fxml"));
        	Parent root = loader.load();
        	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		Scene scene = new Scene(root);
    		stage.setScene(scene);
    		stage.setTitle("Sistema elettorale");
    		stage.show();
    	}
    }

    @FXML
    void premi_logout(ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/schermata_iniziale.fxml"));
    	Parent root = loader.load();
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Sistema elettorale");
		stage.show();
    }

    @FXML
    void premi_vota(ActionEvent event) throws IOException, SQLException { 
    	UtenteDAO daou = UtenteDAOImpl.getInstance();
    	
    	if (daou.check_elettore_sessione(elettore, sessione)) {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/gia_votato.fxml"));
        	Parent root = loader.load();
        	
        	Controller_gia_votato cgv = loader.getController();
        	cgv.setElettore(elettore);
        	cgv.setSessione(sessione);
        	
        	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		Scene scene = new Scene(root);
    		stage.setScene(scene);
    		stage.setTitle("Hai gi� votato per questa sessione");
    		stage.show();
    		return;
    	}
    	
    	switch (sessione.getTipoVoto()) {
		case "categorico":
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/schermata_voto_cat.fxml"));
        	Parent root = loader.load();
        	
        	Controller_voto_cat cvc = loader.getController();
        	if (sessione.sessione_con_partiti())
        		cvc.setLabel("Seleziona il partito che vuoi votare");
        	else
        		cvc.setLabel("Seleziona il candidato che vuoi votare");
        	
        	//metti i partiti o cand nel pannello nei radiobutton
        	cvc.setElementi(sessione.getCandidati());
        	cvc.setElettore(elettore);
        	
        	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		Scene scene = new Scene(root);
    		stage.setScene(scene);
    		stage.setTitle("Esprimi voto");
    		stage.show();
			break;
		case "categorico preferenze":
			
			FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/view/schermata_voto_cat_pref.fxml"));
        	Parent root1 = loader1.load();
        	
        	Controller_voto_cat_pref cvcp = loader1.getController();
        	
        	//metti i partiti o cand nel pannello nei radiobutton
        	cvcp.setElementi(sessione.getCandidati());
        	cvcp.setElettore(elettore);
        	
        	Stage stage1 = (Stage)((Node)event.getSource()).getScene().getWindow();
    		Scene scene1 = new Scene(root1);
    		stage1.setScene(scene1);
    		stage1.setTitle("Esprimi voto");
    		stage1.show();
			break;
		case "referendum":
			FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/view/voto_referendum.fxml"));
        	Parent root2 = loader2.load();
        	Controller_voto_referendum cvr = loader2.getController();
        	cvr.setElettore(elettore);
        	cvr.setLabel(sessione.getDescrizione());
        	Stage stage2 = (Stage)((Node)event.getSource()).getScene().getWindow();
    		Scene scene2 = new Scene(root2);
    		stage2.setScene(scene2);
    		stage2.setTitle("Esprimi voto");
    		stage2.show();
			break;
		case "ordinale":
			FXMLLoader loader3 = new FXMLLoader(getClass().getResource("/view/schermata_voto_ordinale.fxml"));
        	Parent root3 = loader3.load();
        	
        	Controller_voto_ordinale cvo = loader3.getController();
        	cvo.setElettore(elettore);
        	cvo.setElementi(sessione.getCandidati());
        	
        	Stage stage3 = (Stage)((Node)event.getSource()).getScene().getWindow();
    		Scene scene3 = new Scene(root3);
    		stage3.setScene(scene3);
    		stage3.setTitle("Esprimi voto");
    		stage3.show();
			break;
		}
    }
    
    public void setTipoVoto(String s) {
    	lbl_tipoVoto.setText(s);
    }
    
    public void setTitolo(String s) {
    	lbl_titolo.setText(s);
    }
    
    public void setSessione(SessioneVoto sessione) {
    	this.sessione = sessione;
    }
    
    public void setElettore(Elettore elettore) {
		this.elettore = elettore;
	}
}
