package com.project.controllers;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.project.model.*;
import com.project.dao.*;

public class Controller_iniziale {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    
    @FXML
    private PasswordField campo_pswrd_admin;

    @FXML
    private TextField campo_user_admin;

    @FXML
    private PasswordField campo_pswrd_elettore;

    @FXML
    private TextField campo_cf_elettore; 
    
    @FXML
    private Label lbl_errore;
    
    @FXML
    private Label lbl_errore_elettore;

    @FXML
    void premi_admin(ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login_admin.fxml"));
    	Parent root = loader.load();
    	
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Login amministratore");
		stage.show();
    }

    @FXML
    void premi_elettore(ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login_elettore.fxml"));
    	Parent root = loader.load();
    	
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Login elettore");
		stage.show();
    }
    
    @FXML
    void premi_indietro(ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/schermata_iniziale.fxml"));
    	Parent root = loader.load();
    	
    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    	Scene scene = new Scene(root);
    	stage.setScene(scene);
    	stage.setTitle("Sistema elettorale");
    	stage.show();
    }
    
    @FXML
    void premi_accedi_admin(ActionEvent event) throws IOException {
    	try {
    		UtenteDAO ad = UtenteDAOImpl.getInstance();
    		if (ad.check_login_admin(campo_user_admin.getText(), campo_pswrd_admin.getText())) { //caso credenziali corrette
    			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menu_admin.fxml"));
    	    	Parent root = loader.load();
    	    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    			Scene scene = new Scene(root);
    			stage.setScene(scene);
    			stage.setTitle("Menu admin");
    			stage.show();
    		} else { //caso credenziali errate
    			lbl_errore.setText("Le credenziali inserite non sono valide. Riprova");
    		}
    	} catch (SQLException e) {
			lbl_errore.setText("Errore connessione al database. Riprova pi� tardi.");
		}
    }
    
    @FXML
    void premi_accedi_elettore(ActionEvent event) throws IOException {
    	try {
    		UtenteDAO ad = UtenteDAOImpl.getInstance();
    		if (ad.check_login_elettore(campo_cf_elettore.getText(), campo_pswrd_elettore.getText())) { //caso credenziali corrette
    			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menu_elettore.fxml"));
    	    	Parent root = loader.load();
    	    	
    	    	Elettore elettore = new Elettore(campo_cf_elettore.getText(), campo_pswrd_elettore.getText());
    	    	Controller_menu_elettore cme = loader.getController();
    	    	cme.setElettore(elettore);
    	    	
    	    	Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    			Scene scene = new Scene(root);
    			stage.setScene(scene);
    			stage.setTitle("Menu elettore");
    			stage.show();
    		} else { //caso credenziali errate
    			lbl_errore_elettore.setText("Le credenziali inserite non sono valide. Riprova");
    		}
    	} catch (SQLException e) {
			lbl_errore_elettore.setText("Errore connessione al database. Riprova pi� tardi.");
		}
    }
}
