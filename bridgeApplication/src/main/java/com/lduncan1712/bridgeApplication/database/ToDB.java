package com.lduncan1712.bridgeApplication.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lduncan1712.bridgeApplication.controllers.ConfigController;
import com.lduncan1712.bridgeApplication.structures.Edge;
import com.lduncan1712.bridgeApplication.structures.EdgeTier;
import com.lduncan1712.bridgeApplication.structures.Surveyee;



public class ToDB extends FromDB {
	
	public static void addNodeData(int total, int sum) throws SQLException {
		String query = "UPDATE NODES SET participation_row = ?, trait0 = ?, trait1 = ?, trait2 = ?, trait3 = ?,"
				+ "trait4 = ?, edgeOut = ?, sumEdgeOut = ? WHERE ID = ?";
		Surveyee main = ConfigController.getPhase3Surveyee();
		CallableStatement stmt = FromDB.connectionObject2.prepareCall(query);
		//Sets Id Of Row To Be Changed
		stmt.setInt(9, main.getDb_match().root.id);
		//Participation Row (Sheet Row)
		stmt.setInt(1, main.getRow());
		//Static Tiers
		for(int x = 0; x < 5; x++) {
			stmt.setInt(x+2, main.getTraitList().get(x));
		}
		//Setting Given Sum, and Total Calculations
		stmt.setInt(7, total);
		stmt.setInt(8, sum);
		stmt.execute();
		stmt.close();													
		FromDB.connectionObject2.close();
		//Marks Completion Of Task
		main.incrementTotalProgress(main.stageRequirement);
	}
	
	public static void addEdgeDataBatch() throws ClassNotFoundException, SQLException {
		//Variables marking usage of following CallableStatements
		boolean m1; boolean m2; boolean u1;  boolean u2; boolean site;
		m1 = m2 = u1 = u2 = site = false;
		int mainId = ConfigController.getPhase3Surveyee().getDb_match().root.id;
		int numValidEdges = 0;
		int sumValidEdges = 0;
		//Makes a connection object
		FromDB.connectionObject2 = FromDB.makeConnectionObject();
		
		//Array containing unique matches, to avoid duplicates
		List<Integer> uniquenessGuarantee = new ArrayList<>();
		
		CallableStatement makeEdge_1 = connectionObject2.prepareCall("INSERT INTO EDGES(node1, node2, node1rank) VALUES (?,?,?)");
		CallableStatement makeEdge_2 = connectionObject2.prepareCall("INSERT INTO EDGES(node1, node2, node2rank) VALUES (?,?,?)");
		CallableStatement updateEdge_1 = connectionObject2.prepareCall("UPDATE EDGES SET node1rank = ? WHERE node1 = ? AND node2 = ?");
		CallableStatement updateEdge_2 = connectionObject2.prepareCall("UPDATE EDGES SET node2rank = ? WHERE node1 = ? and node2 = ?");
		
		CallableStatement setInclusionToEdges = connectionObject2.prepareCall("Update Nodes SET inclusion_row = ? WHERE id = ? AND inclusion_row = 0");
		
		CallableStatement setInToEdges = connectionObject2.prepareCall("Update Nodes SET edgeIn = edgeIn + 1 WHERE id = ?");
		
		//BEGINNING BLACKLIST VALUES
		uniquenessGuarantee.add(0);
		uniquenessGuarantee.add(mainId);
		
		
		for(EdgeTier et: ConfigController.getPhase3Surveyee().sublist) {
			int tierStrength = et.id;
			for(Edge e: et.sublist) {
				
				//If valid edge
				int edgeId = e.getConfirmedMatch().root.id;
				if(!uniquenessGuarantee.contains(edgeId)) {
					numValidEdges++;
					sumValidEdges+=(tierStrength + 1);
					uniquenessGuarantee.add(edgeId);
					
					//IF EDGE ALREADY EXISTS (FROM OTHER PERSON)
					if(FromDB.checkIfEdgeExists(Math.min(edgeId, mainId), Math.max(edgeId, mainId))) {
						if(mainId > edgeId) {
							updateEdge_2.setInt(1, tierStrength);
							updateEdge_2.setInt(2, edgeId);
							updateEdge_2.setInt(3, mainId);
							updateEdge_2.addBatch();
							u2 = true;
						}else {
							updateEdge_1.setInt(1, tierStrength);
							updateEdge_1.setInt(2, mainId);
							updateEdge_1.setInt(3, edgeId);
							updateEdge_1.addBatch();
							u1 = true;
						}
					//IF NEED TO CREATE EDGE FROM SCRATCH	
					}else {
						if(mainId > edgeId) {
							makeEdge_2.setInt(1, edgeId);
							makeEdge_2.setInt(2, mainId);
							makeEdge_2.setInt(3, tierStrength);
							makeEdge_2.addBatch();
							m2 = true;
						}else {
							makeEdge_1.setInt(1, mainId);	
							makeEdge_1.setInt(2, edgeId);
							makeEdge_1.setInt(3, tierStrength);
							makeEdge_1.addBatch();
							m1 = true;
						}		
					}
					//Will update Inclusion Row If Not Already Set (not 0)
					setInclusionToEdges.setInt(1, ConfigController.getPhase3Surveyee().getRow());
					setInclusionToEdges.setInt(2, edgeId);
					setInclusionToEdges.addBatch();
					site = true;
					
					//Will increase In Edge count to given edge reciepient
					setInToEdges.setInt(1, edgeId);
					setInToEdges.addBatch();
				}
			}
		}
		//Executing Required Statements
		if(m1) {
			makeEdge_1.executeBatch();
		}
		makeEdge_1.close();
		if(m2) {
			makeEdge_2.executeBatch();  
		}
		makeEdge_2.close();
		if(u1) {
			updateEdge_1.executeBatch();
		}
		updateEdge_1.close();
		if(u2) {
			updateEdge_2.executeBatch();
		}
		updateEdge_2.close();
		if(site) {
			setInclusionToEdges.executeBatch();
			setInToEdges.executeBatch();
		}
		setInclusionToEdges.close();
		setInToEdges.close();
		
		//Begins Surveyee Static Updation
		addNodeData(numValidEdges, sumValidEdges);
	}
}
