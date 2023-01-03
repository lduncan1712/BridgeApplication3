package com.lduncan1712.bridgeApplication.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.lduncan1712.bridgeApplication.structures.Node;
import com.lduncan1712.bridgeApplication.structures.NodeData;
import com.lduncan1712.bridgeApplication.structures.NodeLocalStorage;
import com.lduncan1712.bridgeApplication.structures.Surveyee;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ConfigController {

    //DATABASE CONFIGURATION
    public static final String DB_Driver = "com.mysql.cj.jdbc.Driver";
    public static final String DB_Url = "jdbc:mysql://localhost:3306/impact_network";
    public static final String DB_Username = "root";
    public static final String DB_Password = "Punt@b1eP1pp1";
    
    

    //GOOGLE SHEETS CONFIGURATION
    public static final String USID = "1M29zhrGzHkbDf7MxDfPiTZ-b-ur4uXa8oKTbQLSiKPQ";
    public static final String CREDENTIALS_FILE_PATH = "/com/lduncan1712/bridgeApplication/MyCred.json";
    
    //Starting/Ending Range Of SpreadSheet read;
    public static final char SpreadSheetSR = 'B';
    public static final char SpreadSheetER = 'K';
    
    //Starting/Ending Index Range Of 'Edge' Rows
    public static final int SpreadSheetSER = 6;
    public static final int SpreadSheetEER = 9;
    
    //Row Index For Surveyee Name
    public static final int SpreadSheetNR = 0;
    
    //Starting/Ending Index Range Of 'Static Info' Rows
    public static final int SpreadSheetSSI = 1;
    public static final int SpreadSheetESI = 5;
    
    //******************************************************************************
    //Changes Require Additional Changing To ToDB, addNodeData, 'query' Variable and static Tiers
    //*****************************************************************************
    
    
    //Surveyee's at Various Phases
    public static Surveyee phase1Surveyee = null;
    public static Surveyee phase2Surveyee = null;
    public static Surveyee phase3Surveyee = null;
    
    //Variable Holding Last Completed Row Found In Database (Automatically Determined)
    public static int previousRow = 0;
    
    //Percent Requirement Required For Auto-Match
    public static int matchThreshhold = 50;
    
    //End Program Condition
    public static boolean lastRowFound = false;
    public static boolean lastDesiredRow = false;
    
    //Number Of Recommended Possible Matches Given
    public static int longlistlength = 20; //(when below threshhold)
    public static int shortlistlength = 10; //(when above threshold)

    
    //(Medium Term)Local Cache Of Possible Database matches
    public static HashMap<Integer, NodeLocalStorage> localNodeCache = new HashMap<>();
    
   //(Short Term) Local Cache Of Possible Database matches (based on search param)
    public static ObservableList<Node> localManualSearchCache = FXCollections.observableArrayList();
    
    
    public static Node null_match = new Node(new NodeData("Null", 0), 0);
    
    
    public static Surveyee masterList(int index) {
    	switch(index) {
    		case 0: return phase1Surveyee;
    		case 1: return phase2Surveyee;
    		case 2: return phase3Surveyee;
    	}
    	return null;
    	
    }
    
    public static int getShortlistlength() {
		return shortlistlength;
	}
	public static void setShortlistlength(int shortlistlength) {
		ConfigController.shortlistlength = shortlistlength;
	}
    
	public static Surveyee getPhase1Surveyee() {
		return phase1Surveyee;
	}
	public static void setPhase1Surveyee(Surveyee stage1Surveyee) {
		ConfigController.phase1Surveyee = stage1Surveyee;
	}
	public static Surveyee getPhase2Surveyee() {
		return phase2Surveyee;
	}
	public static void setPhase2Surveyee(Surveyee stage2Surveyee) {
		ConfigController.phase2Surveyee = stage2Surveyee;
	}
	public static Surveyee getPhase3Surveyee() {
		return phase3Surveyee;
	}
	public static void setPhase3Surveyee(Surveyee stage3Surveyee) {
		ConfigController.phase3Surveyee = stage3Surveyee;
	}
	public static int getPreviousRow() {
		return previousRow;
	}
	public static void setPreviousRow(int previousRow) {
		ConfigController.previousRow = previousRow;
	}
	public static int getMatchThreshhold() {
		return matchThreshhold;
	}
	public static void setMatchThreshhold(int matchThreshhold) {
		ConfigController.matchThreshhold = matchThreshhold;
	}
	public static boolean isLastRowFound() {
		return lastRowFound;
	}
	public static void setLastRowFound(boolean lastRowFound) {
		ConfigController.lastRowFound = lastRowFound;
	}
	public static int getLonglistlength() {
		return longlistlength;
	}
	public static void setLonglistlength(int longlistlength) {
		ConfigController.longlistlength = longlistlength;
	}
	public static HashMap<Integer, NodeLocalStorage> getLocalNodeCache() {
		return localNodeCache;
	}
	public static void setLocalNodeCache(HashMap<Integer, NodeLocalStorage> localNodeCache) {
		ConfigController.localNodeCache = localNodeCache;
	}
	
	public static char getSpreadsheetsr() {
		return SpreadSheetSR;
	}
	public static char getSpreadsheeter() {
		return SpreadSheetER;
	}
	public static boolean isLastDesiredRow() {
		return lastDesiredRow;
	}
	public static void setLastDesiredRow(boolean lastDesiredRow) {
		ConfigController.lastDesiredRow = lastDesiredRow;
	}







	public static String getCredentialsFilePath() {
		return CREDENTIALS_FILE_PATH;
	}
    
    
    
	




}

