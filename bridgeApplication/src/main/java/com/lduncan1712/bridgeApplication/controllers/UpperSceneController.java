package com.lduncan1712.bridgeApplication.controllers;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


import com.lduncan1712.bridgeApplication.structures.Surveyee;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class UpperSceneController{
	
	@FXML ProgressBar upper_pb;
	@FXML Label upper_num;
	@FXML Label upper_wn;
	@FXML Label upper_mn;
	
	static UpperSceneController instance1;

	public UpperSceneController() {
		instance1 = this;
	}
		
	public static UpperSceneController getInstance() {
		return instance1;
	}
		
	public void initialSetUp(Surveyee s) {
		
		upper_num.setText(s.getDb_match().root.id  + "           " + s.getRow());
		
		upper_wn.setText(s.getName());
		
		if(!Objects.isNull(s.getDb_match())) {
			upper_mn.setText(s.getDb_match().toString());
		}
	}
	
	public void updateProgressBar(Double c) {
		upper_pb.setProgress(c);
	}
}
