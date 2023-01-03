package com.lduncan1712.bridgeApplication.structures;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EdgeTier extends _ObjectProgress{
    public Surveyee superObject = null;
    public int id;
    public ObservableList<Edge> sublist = FXCollections.observableArrayList();

    
    public EdgeTier(int id, Surveyee superObject, String appendedNames) {
    	this.id = id;
    	this.superObject = superObject;
    	this.createEdges(appendedNames);
    }
    
    @Override
    public void shareRequirement(int length) {
    	this.stageRequirement+=length;
    	superObject.shareRequirement(length);
    }

    @Override
    public void incrementTotalProgress(int toIncrease) {
    	this.incrementThisProgress(toIncrease);
    	superObject.incrementTotalProgress(toIncrease);
    }
   
    @Override
    public String toString() {
    	return "Tier " + this.id + "(" + this.stageRequirement + ")  " + this.superObject.row + "-" + (char)(77 - this.id);
    }
    
    //Creates Edges From List of Names
    public void createEdges(String appendedNames){
    	for(String singleName: appendedNames.split("[\n,]")) {
    		if(singleName == null || singleName.trim().length() == 0) {
    			continue;
    		}else {
    			this.sublist.add(new Edge(singleName.trim(), this));
    		}
    	}
    	shareRequirement(this.sublist.size());
    }
}

