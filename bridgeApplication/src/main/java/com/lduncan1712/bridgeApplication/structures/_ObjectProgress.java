package com.lduncan1712.bridgeApplication.structures;

import java.util.List;

import com.lduncan1712.bridgeApplication.controllers.UpperSceneController;

import javafx.application.Platform;

public class _ObjectProgress {
	
	public _ObjectProgress() {}

    public int stageProgress = 0;
    public int stageRequirement = 0;
    public UpperSceneController paneController = null;
    
    
    
    public void incrementTotalProgress(int toIncrease) {}
    
    public void shareRequirement(int length) {}
    
    //Updates Relavant Progress Pane
    public void visualizeUpdatedProgress() {
    	UpperSceneController controller = this.paneController;
    	double completedDecimal = this.returnDecimalCompleted();
    	Platform.runLater(new Runnable() {
			@Override
			public void run() {
				controller.updateProgressBar(completedDecimal);
			}	
    	});
    }
    
    //Returns fractional stage completion
    public double returnDecimalCompleted() {
    	return (double)this.stageProgress/this.stageRequirement;
    }
    
    
    //Increases stage completion by given amount
    public void incrementThisProgress(int toIncrease) {
    	this.stageProgress+=toIncrease;
    }
    
    
    //Resets stage progress
    public void resetProgress(){
        stageProgress = 0;
    }
    
    public double getProgress() {
    	return stageProgress/stageRequirement;
    }
    
	public int getStageProgress() {
		return stageProgress;
	}

	public void setStageProgress(int stageProgress) {
		this.stageProgress = stageProgress;
	}

	public int getStageRequirement() {
		return stageRequirement;
	}

	public void setStageRequirement(int stageRequirement) {
		this.stageRequirement = stageRequirement;
	}



	public UpperSceneController getPaneController() {
		return paneController;
	}



	public void setPaneController(UpperSceneController paneController) {
		this.paneController = paneController;
	}


}

