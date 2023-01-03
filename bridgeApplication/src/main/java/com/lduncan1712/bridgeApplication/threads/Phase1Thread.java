package com.lduncan1712.bridgeApplication.threads;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import com.lduncan1712.bridgeApplication.controllers.ConfigController;
import com.lduncan1712.bridgeApplication.controllers.MainSceneController;
import com.lduncan1712.bridgeApplication.googlesheets.SheetRowFormatting;
import com.lduncan1712.bridgeApplication.googlesheets.SheetsAPI;
import com.lduncan1712.bridgeApplication.structures.Edge;
import com.lduncan1712.bridgeApplication.structures.EdgeTier;
import com.lduncan1712.bridgeApplication.structures.Surveyee;

import javafx.application.Platform;



public class Phase1Thread extends Thread{
	@Override
	public void run() {
		
		//try {
			List<Object> row = null;
			try {
				//reading the next row specified
				row = SheetsAPI.readNextRow(SheetRowFormatting.getNextValueRange());
			} catch (ClassNotFoundException | IOException | GeneralSecurityException | SQLException e) {
				e.printStackTrace();
			}
			
			//Checking the row is valid, and non empty
			if(SheetRowFormatting.CheckIfValid(row)) {
				//Cascades downwards, creating all relevant 
				try {
					ConfigController.getPhase1Surveyee().buildSurveyee(row);
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				
				//CREATES VISUAL IN UPPER BAR
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						try {
							MainSceneController.getInstance().createUpperPane(0);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
				
				//Ensures Previous Task Is Completed Before Continuing
				while(Objects.isNull(ConfigController.getPhase1Surveyee().paneController)){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				
				//Creating List of Possible Match Suggestions For Each Edge
				for(EdgeTier et: ConfigController.getPhase1Surveyee().sublist) {
					for(Edge e: et.sublist) {
						try {
							e.createPossibleMatches();
						} catch (ClassNotFoundException | SQLException e1) {
							System.out.println("Obtaining Edge Identiity List Failed");
							e1.printStackTrace();
						}
					}
					//FOR EVERY COMPLETED TIER, UPDATES OVERALL COMPLETED
					et.incrementTotalProgress(et.stageRequirement);
				}
				
			//Means the row is invalid, thus killing the thread/removing Progress Pane
			}else {
				ConfigController.setPhase1Surveyee(null);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						MainSceneController.getInstance().removeRow_v();
						MainSceneController.getInstance().endSession_uv();
					}
				});
				while(true) {
					try {
						Thread.sleep(100000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
	}

}
