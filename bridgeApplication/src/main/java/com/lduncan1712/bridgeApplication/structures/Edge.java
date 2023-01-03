package com.lduncan1712.bridgeApplication.structures;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import com.lduncan1712.bridgeApplication.controllers.ConfigController;
import com.lduncan1712.bridgeApplication.database.FromDB;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Edge extends _ObjectProgress{
    public Node confirmedMatch = null;
    public EdgeTier superObject = null;
    public ObservableList<Node> sublist = FXCollections.observableArrayList();
    public String recipient;
    
    public Edge(String recipient, EdgeTier superObject) {
    	this.recipient = recipient;
    	this.superObject = superObject;
    }
    
    @Override
    public void incrementTotalProgress(int toIncrease) {
    	superObject.incrementTotalProgress(toIncrease);
    }
    
    @Override
    public void shareRequirement(int length) {
    	this.stageRequirement+=length;
    	superObject.shareRequirement(length);
    }
    
    @Override
    public String toString() {
    	return recipient;
    }
    
    
    //Creates Weighted List Of Possible Matches
    public void createPossibleMatches() throws ClassNotFoundException, SQLException {
    	FromDB._1of4_ResetTT();
    	FromDB._3of4_WeighIds(FromDB._2of4_GetIds(this.recipient), recipient);
    	this.sublist.addAll(FromDB._4of4_obtainFinalList());
    }
    
    //Attempts to match to first element in sublist
    public boolean attemptAutoMatch(boolean must) {
    	if(!this.sublist.isEmpty()) {
    		Node firstInList = this.sublist.get(0);
        	if(must == true) {
        		this.confirmedMatch = firstInList;
        		return true;
        	}else if (passesCriteriaForMatch(firstInList)){
        		this.confirmedMatch = firstInList;
        		return true;
        	}else {
        		return false;
        	}
    	}
    	return false;
    }
    
    //Returns whether a given Node is can be automatched
    public boolean passesCriteriaForMatch(Node n) {
    	if(n.strength > ConfigController.matchThreshhold) {
    		return true;
    	}else {
    		return false;
    	}
    }
    
	public Node getConfirmedMatch() {
		return confirmedMatch;
	}

	public void setConfirmedMatch(Node confirmedMatch) {
		this.confirmedMatch = confirmedMatch;
	}

	public EdgeTier getSuperObject() {
		return superObject;
	}

	public void setSuperObject(EdgeTier superObject) {
		this.superObject = superObject;
	}

	
	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
    
    
    

	



}