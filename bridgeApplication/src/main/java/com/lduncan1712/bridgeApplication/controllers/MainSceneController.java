package com.lduncan1712.bridgeApplication.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import com.lduncan1712.bridgeApplication.App;
import com.lduncan1712.bridgeApplication.structures.Edge;
import com.lduncan1712.bridgeApplication.structures.EdgeTier;
import com.lduncan1712.bridgeApplication.structures.Node;
import com.lduncan1712.bridgeApplication.structures.Surveyee;
import com.lduncan1712.bridgeApplication.threads.Phase1Thread;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class MainSceneController extends ApplicationController implements Initializable{
	
	@FXML Slider match_threshold_s;
	@FXML Label match_threshold_label;
	@FXML Slider long_list_s;
	@FXML Label long_list_label;
	@FXML Slider short_list_s;
	@FXML Label short_list_label;
	@FXML CheckBox continuation_cb;
	@FXML Label end_row_l;
	@FXML Label ending_l;
	@FXML BorderPane upper_pane;
	@FXML BorderPane main_bp;
	
	private static MainSceneController instance;
	
	public MainSceneController() {
		instance = this;
	}
	
	public static MainSceneController getInstance() {
		return instance;
	}

	public static Pane p = new Pane();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//HANDLES MATCH THRESHOLD
		match_threshold_s.setValue(ConfigController.matchThreshhold);
		match_threshold_label.setText(Integer.toString(ConfigController.matchThreshhold));
		match_threshold_s.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				ConfigController.setMatchThreshhold(newValue.intValue());
				match_threshold_label.setText(Integer.toString(newValue.intValue()));	
			}
		});
		//HANDLES LONG LIST THRESHOLD
		long_list_s.setValue(ConfigController.longlistlength);
		long_list_label.setText(Integer.toString(ConfigController.longlistlength));
		long_list_s.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				ConfigController.setMatchThreshhold(newValue.intValue());
				long_list_label.setText(Integer.toString(newValue.intValue()));
			}
		});
		//HANDLED SHORT LIST THRESHOLD
		short_list_s.setValue(ConfigController.shortlistlength);
		short_list_label.setText(Integer.toString(ConfigController.shortlistlength));
		short_list_s.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				ConfigController.setMatchThreshhold(newValue.intValue());
				short_list_label.setText(Integer.toString(newValue.intValue()));
			}
		});
		
		p.getChildren().add(new Label("Removed"));
	}
	
	
	//Triggers Stop To New Phase1
	public void endSession_v(ActionEvent e) {
		ConfigController.setLastDesiredRow(true);
		ending_l.setText("SessionEnding(Vol)");
	}
	
	//Actions When Last Row Reached
	public void endSession_uv() {
		ConfigController.setLastRowFound(true);
		end_row_l.setText("SessionEnding(UnVol)");
	}
	
	//Removes Phase1 Row
	public void removeRow_v() {
		ConfigController.phase1Surveyee = null;
		upper_pane.setLeft(p);
		
	}
	
	
	
	//Create Upper(Progress) Pane For Given Phase
	public void createUpperPane(int phase) throws IOException {
		if(!Objects.isNull(masterList(phase))) {
			FXMLLoader l =  new App().makeLoader("upperScene.fxml");
			Pane pl = l.load();
			masterList(phase).setPaneController(l.getController());
			switch(phase) {
				case 0: upper_pane.setLeft(pl);
						break;
				case 1: upper_pane.setCenter(pl);
						break;
				case 2: upper_pane.setRight(pl);
						break;
			}
			masterList(phase).getPaneController().initialSetUp(masterList(phase));
		}
	}
	
	
	
	//Creates Inner Pane If Phase2 Exists
	public void createInnerIfRequired() throws IOException {
		if(!Objects.isNull(phase2Surveyee)) {
			FXMLLoader l = new App().makeLoader("innerScene.fxml");
			main_bp.setCenter(l.load());
		}
	}
	
	//Facilitates Moving To Next Phase
	public void nextPhase(ActionEvent e) throws InterruptedException, IOException {
		if(readyForNextPhase()) {
			
			//Moves Positions, and Resets Progress
			ApplicationController.ResetBeforeBeginNextPhase();
			
			//Creates New Phase1 Surveyee
			ConfigController.setPhase1Surveyee(new Surveyee());
			
			//Removes Previous Phase Progress Panes
			upper_pane.setLeft(new Pane());
			upper_pane.setCenter(new Pane());
			upper_pane.setRight(new Pane());
			
			//Resets Previous Phase Inner Pane
			main_bp.setCenter(new Pane());
			
			//Creates New Upper Panes If Required (for Phase2, Phase3)
			createUpperPane(1);
			createUpperPane(2);
			
			//Creates New Inner Pane If Required
			createInnerIfRequired();
			
			//Begins Threads
			ApplicationController.beginNextStage();
		}
	}
}

