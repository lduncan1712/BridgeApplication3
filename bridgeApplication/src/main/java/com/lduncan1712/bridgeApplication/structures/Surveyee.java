package com.lduncan1712.bridgeApplication.structures;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lduncan1712.bridgeApplication.controllers.ConfigController;
import com.lduncan1712.bridgeApplication.controllers.MainSceneController;
import com.lduncan1712.bridgeApplication.controllers.UpperSceneController;
import com.lduncan1712.bridgeApplication.googlesheets.SheetRowFormatting;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;



public class Surveyee extends _ObjectProgress{
	int row;
	String name;
	Node db_match;
    public ObservableList<EdgeTier> sublist = FXCollections.observableArrayList();
    List<Integer> traitList = new ArrayList<>();
    
    
    
    public Surveyee() {}
    
    @Override
    public void shareRequirement(int length) {
    	this.stageRequirement+=length;
    }
    
    @Override
    public void incrementTotalProgress(int toIncrease) {
    	this.incrementThisProgress(toIncrease);
    	this.visualizeUpdatedProgress();
    }
    
    @Override
    public String toString() {
    	return "(gn) " + this.name;
    }
    
    //Core Building Of The Surveyee, (Only Database Connection Being To Match Main Name)
    public void buildSurveyee(List<Object> rowData) throws InterruptedException {
    	//Sets RowNumber
    	this.row = ConfigController.getPreviousRow();
    	//Defines Name
    	this.name = (String) rowData.get(ConfigController.SpreadSheetNR);
    	//Defines Main Surveyee Match
    	this.db_match  = this.mainNameMatching();
    	
    	//CASCADES DOWNWARDS CREATING ALL RELEVANT, EDGE TIERS, EDGES
    	this.createEdgeTiers(rowData.subList(ConfigController.SpreadSheetSER, ConfigController.SpreadSheetEER + 1));
    	
    	//Converts Static Data to DB id's
    	for(int z = ConfigController.SpreadSheetSSI; z <= ConfigController.SpreadSheetESI; z++) {
    		traitList.add(convertToId(rowData.get(z)));
    	}
    }
    
    //Converts Given Static Data To Database Accepted Id
    public static int convertToId(Object o) {
    	return Integer.valueOf(((String)o).substring(0, 1));
    }
   
    
    //Matches Main Name (due to constant proper spelling in this column, no matching ambiguiuty exists)
    public Node mainNameMatching() {
    	Edge mainName = new Edge(this.getName(), null);
		//Obtains list of possible matches
			try {
				mainName.createPossibleMatches();
			} catch (ClassNotFoundException | SQLException e) {
				System.out.println("Obtaining Surveyee Identity Failed");
				e.printStackTrace();
			}
			//Forcefully Matches highest weighted
			mainName.attemptAutoMatch(true);
		return mainName.getConfirmedMatch();
    }
    
   
    public void createEdgeTiers(List<Object> rowData) {
    	for(int tier = rowData.size(); tier>= 1; tier--) {
    		int index = rowData.size() - tier;
    		//If No Values In Tier
			if(rowData.get(index) == null || ((String)rowData.get(index)).isEmpty()) {
				continue;
			//Tier Contains Names
			}else {
				//CREATES AND FORMATS IN TURN, AN EDGE TIER
				this.sublist.add(new EdgeTier(tier, this, (String)rowData.get(index)));
			}
		}
    }
    
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Node getDb_match() {
		return db_match;
	}

	public void setDb_match(Node db_match) {
		this.db_match = db_match;
	}


	public UpperSceneController getPaneController() {
		return paneController;
	}

	public void setPaneController(UpperSceneController paneController) {
		this.paneController = paneController;
	}

	public ObservableList<EdgeTier> getSublist() {
		return sublist;
	}

	public void setSublist(ObservableList<EdgeTier> sublist) {
		this.sublist = sublist;
	}

	public List<Integer> getTraitList() {
		return traitList;
	}

	public void setTraitList(List<Integer> traitList) {
		this.traitList = traitList;
	}
    
    
    





}
