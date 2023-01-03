package com.lduncan1712.bridgeApplication.threads;

import java.sql.SQLException;

import com.lduncan1712.bridgeApplication.controllers.ApplicationController;
import com.lduncan1712.bridgeApplication.database.ToDB;

public class Phase3Thread extends Thread{
	
	@Override
	public void run() {
		
		try {
			//Adds Edge Data (and Node) to database
			ToDB.addEdgeDataBatch();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		//Updates localNodeCache accordingly (removing all nodes saved previously to facilitate above)
		ApplicationController.removeFromLocalCache();
	}

}
