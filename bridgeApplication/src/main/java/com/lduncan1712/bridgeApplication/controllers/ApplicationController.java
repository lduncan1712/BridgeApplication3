package com.lduncan1712.bridgeApplication.controllers;

import java.util.Objects;

import com.lduncan1712.bridgeApplication.structures.Edge;
import com.lduncan1712.bridgeApplication.structures.EdgeTier;
import com.lduncan1712.bridgeApplication.structures.Node;
import com.lduncan1712.bridgeApplication.structures.NodeData;
import com.lduncan1712.bridgeApplication.structures.NodeLocalStorage;
import com.lduncan1712.bridgeApplication.structures.Surveyee;
import com.lduncan1712.bridgeApplication.threads.Phase1Thread;
import com.lduncan1712.bridgeApplication.threads.Phase2Thread;
import com.lduncan1712.bridgeApplication.threads.Phase3Thread;

import javafx.fxml.FXMLLoader;




public class ApplicationController extends ConfigController{
	
	//Moves Surveyees Into Next Phase, And Resets Phase Progress
	public static void ResetBeforeBeginNextPhase() {
		resetSurveyeeProgress(phase1Surveyee);
        resetSurveyeeProgress(phase2Surveyee);
        phase3Surveyee = phase2Surveyee;
        phase2Surveyee = phase1Surveyee;
        phase1Surveyee = null; 
	}

	//Begins Relevant Phase Specific Threads
    public static void beginNextStage(){
        if(!lastRowFound && !lastDesiredRow) {
        	Phase1Thread t1 = new Phase1Thread();
        	t1.start();
        }
        if(!Objects.isNull(phase2Surveyee)) {
        	Phase2Thread t2 = new Phase2Thread();
        	t2.start(); 
        }
        if(!Objects.isNull(phase3Surveyee)) {
        	Phase3Thread t3 = new Phase3Thread();
        	t3.start();
        }
    }

    //Resets Phase Progress Of Given Surveyee S
    public static void resetSurveyeeProgress(Surveyee s){
        if(Objects.isNull(s)){
            return;
        }else{
            s.resetProgress();
            for(EdgeTier et: s.sublist) {
                et.resetProgress();
                for (Edge e : et.sublist) {
                    e.resetProgress();
                }
            }
        }
    }

    //Determines Whether All Relevant Phases Are Complete
    public static boolean readyForNextPhase(){
        if(!Objects.isNull(phase1Surveyee)){
            if(phase1Surveyee.stageRequirement != 0 && phase1Surveyee.getProgress() != 1){
                return false;
            }
        }
        if(!Objects.isNull(phase2Surveyee)){
            if(phase2Surveyee.stageRequirement != 0 && phase2Surveyee.getProgress() != 1){
                return false;
            }
        }
        if(!Objects.isNull(phase3Surveyee)){
            if(phase3Surveyee.stageRequirement != 0 && phase3Surveyee.getProgress() != 1){
                return false;
            }
        }
        return true;
    }
    
    //Removes Unnecessary Nodes From LocalCache
    //(when no longer needed)
    public static void removeFromLocalCache() {
    	for(Integer i: ConfigController.getLocalNodeCache().keySet()) {
    		NodeLocalStorage nls = ConfigController.getLocalNodeCache().get(i);
    		nls.referenceList.remove(ConfigController.getPhase3Surveyee().getRow());
    		if(nls.referenceList.isEmpty()) {
    			ConfigController.getLocalNodeCache().remove(i);
    		}
    	}
    	System.out.println("NEW LOCAL LENGTH: " + ConfigController.getLocalNodeCache().size());
    }
    
    
    
    
    

}
