package com.lduncan1712.bridgeApplication.threads;

import com.lduncan1712.bridgeApplication.controllers.ConfigController;
import com.lduncan1712.bridgeApplication.structures.Edge;
import com.lduncan1712.bridgeApplication.structures.EdgeTier;

public class Phase2Thread extends Thread{
	
	@Override
	public void run() {
		//Attempts to automatch all edges
		for(EdgeTier et: ConfigController.getPhase2Surveyee().sublist) {
			for(Edge e: et.sublist) {
				if(e.attemptAutoMatch(false)) {
					e.incrementTotalProgress(1);
					
				}
				
			}
			
		}
	}

}

