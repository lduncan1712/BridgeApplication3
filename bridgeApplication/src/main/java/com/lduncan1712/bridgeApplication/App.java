package com.lduncan1712.bridgeApplication;

import javafx.application.Application;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;

import javax.xml.stream.EventFilter;
import javax.xml.stream.events.XMLEvent;
import javafx.event.EventHandler;

import com.lduncan1712.bridgeApplication.controllers.InnerSceneController;
import com.lduncan1712.bridgeApplication.controllers.MainSceneController;
import com.lduncan1712.bridgeApplication.database.FromDB;
import com.lduncan1712.bridgeApplication.googlesheets.SheetRowFormatting;
import com.lduncan1712.bridgeApplication.googlesheets.SheetsAPI;
import com.lduncan1712.bridgeApplication.structures.Edge;
import com.lduncan1712.bridgeApplication.structures.Node;



public class App extends Application {
	
	public static void main(String[] args) {
        launch();
    }
	
    @Override
    public void start(Stage stage) throws IOException, GeneralSecurityException, ClassNotFoundException, SQLException {
        Parent root = FXMLLoader.load(getClass().getResource("mainScene.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
     
        
        FromDB.setUpDriver();
        
        stage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				try {
					switch(event.getCode().getCode()) {
						//Remove Phase1 Row; (Key: 1)
						case 49:
							MainSceneController.getInstance().removeRow_v();
							break;
						//Traverse Upward through Phase2 edge.sublist (S)
						case 87:
							InnerSceneController.getInstance().moveWithinList(false);
							break;
						//Traverse Downward through Phase2 edge.sublist (W)
						case 83: 
							InnerSceneController.getInstance().moveWithinList(true);
							break;
						//Matches Currently Selected edge to Null Person (Q)
						case 81:
							InnerSceneController.getInstance().matchToNull(null);
							break;
						//Attempts Next Stage (R)
						case 82:
							MainSceneController.getInstance().nextPhase(null);
							break;
					}
				}catch(Exception e) {}
			}
        });  
    }
    
    public FXMLLoader makeLoader(String resource) {
    	return new FXMLLoader(getClass().getResource(resource));
    }
    
    
}