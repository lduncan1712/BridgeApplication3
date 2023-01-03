package com.lduncan1712.bridgeApplication.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.lduncan1712.bridgeApplication.controllers.ApplicationController;
import com.lduncan1712.bridgeApplication.controllers.ConfigController;
import com.lduncan1712.bridgeApplication.structures.Node;
import com.lduncan1712.bridgeApplication.structures.NodeData;
import com.lduncan1712.bridgeApplication.structures.NodeLocalStorage;

import javafx.collections.ObservableList;



public class FromDB extends ConfigController{
	
	public static Connection connectionObject1 = null;
	public static Connection connectionObject2 = null;
	
	//Sets Up Driver
	public static void setUpDriver() {
		try {
			Class.forName(DB_Driver);
		}catch(Exception e) {
			System.out.println(e.toString());
			System.exit(0);
		}
	}
	
	//Creates A Connection Object
	public static Connection makeConnectionObject() throws SQLException, ClassNotFoundException {
		Connection conn = DriverManager.getConnection(DB_Url,DB_Username,DB_Password);
		return conn;
	}
	
	
	//Returns Index of Last Completed Row Stored In Database (0 if no rows completed)
	public static int getLastCompletedRow() throws ClassNotFoundException, SQLException {
		connectionObject1 = makeConnectionObject();
		Statement statement = connectionObject1.createStatement();
		ResultSet rs = statement.executeQuery("SELECT participation_row FROM nodes ORDER BY participation_row DESC LIMIT 1");
		rs.next();
		int lastRow = rs.getInt("participation_row");
		statement.close();
		rs.close();
		return lastRow;
		
	}
	
	//Reset Temporary Tables To Prepare To Store New Potential Matches To Next Edge
	public static void _1of4_ResetTT() throws ClassNotFoundException, SQLException {
		connectionObject1 = makeConnectionObject();
		Statement stmt = connectionObject1.createStatement();
		stmt.execute("DELETE FROM impact_network.temporaryNameTable");
		stmt.execute("DELETE FROM impact_network.temporaryEdgeTable");
	}
	
	
	//Returns List Of Potential ID's (to weigh match)
	//And Places Name To Match In Temporary Tables (in FULLNAME)
	public static List<Integer> _2of4_GetIds(String fullName) throws ClassNotFoundException, SQLException{
		List<Integer> returned = new ArrayList<Integer>();
		CallableStatement cs = connectionObject1.prepareCall("{call getIdsToWeigh(?)}");
		cs.setString(1, fullName);
		ResultSet rs = cs.executeQuery();
		while(rs.next()) {
			returned.add(rs.getInt(1));
		}
		return returned;
	}
	
	//Compares And Weighs The Match Strenth Of Each Given Id
	public static void _3of4_WeighIds(List<Integer> ids, String fullName) throws ClassNotFoundException, SQLException {
		CallableStatement cs = connectionObject1.prepareCall("{ call weighBySC2Score(?)}");
		for(Integer i: ids) {
			cs.setInt(1, i);
			cs.addBatch();
		}
		cs.executeBatch();
		
		
	}
	//Returns The Top n Weighed Row Matches
	public static List<Node> _4of4_obtainFinalList() throws SQLException, ClassNotFoundException {
		List<Node> returned = new ArrayList<>();
		
		Statement stmt = connectionObject1.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * from temporaryEdgeTable ORDER BY strength DESC LIMIT " + ConfigController.getLonglistlength());
		while(rs.next()) {
			
			returned.add(new Node(NodeLocalStorage.conferWithCache(rs.getInt(1), rs.getString(2)), 
						rs.getDouble(3)));
		}
		rs.close();
		stmt.close();
		connectionObject1.close();
		connectionObject1 = null;
		return returned;
	}
	
	//Obtains Potential Match Rows To Given Manual Search String
	public static void setManualSearchResults(String str) throws ClassNotFoundException, SQLException{
		Connection conn = makeConnectionObject();
		ResultSet rs = conn.createStatement().executeQuery("Select f_name, l_name, id from social_network_info.nodes where f_name LIKE '%" + str +"%' OR l_name Like '%" + str + "%' order by f_name");
		while(rs.next()) {
			ConfigController.localManualSearchCache.add(new Node(new NodeData(rs.getString(1) + " " + rs.getString(2), rs.getInt(3)),0));
		}
		rs.close();
		conn.close();
	}
	
	//Determines Whether An (undirectional) Edge (connecting 2 id's) already exists in the database
	public static boolean checkIfEdgeExists(int min, int max) throws SQLException {
		ResultSet rs = connectionObject2.prepareCall("SELECT node1 from edges WHERE node1 = " + min + " AND node2 = " + max).executeQuery();
		boolean rowExists = rs.next();
		rs.close();
		return rowExists;
	}
	

}
