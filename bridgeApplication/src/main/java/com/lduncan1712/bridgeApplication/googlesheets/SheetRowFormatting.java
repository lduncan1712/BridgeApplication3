package com.lduncan1712.bridgeApplication.googlesheets;

import java.sql.SQLException;
import java.util.List;

import com.lduncan1712.bridgeApplication.controllers.ConfigController;
import com.lduncan1712.bridgeApplication.database.FromDB;

import javafx.application.Platform;


public class SheetRowFormatting {
	public static int getNextRowNum() throws ClassNotFoundException, SQLException{
        //IF APPLICATION AT START
		if(ConfigController.previousRow == 0){
            int lastRow = FromDB.getLastCompletedRow();
            //IF ROWS HAVE BEEN COMPLETED
            if(lastRow >= 2){
            	//THEN MOVE TO NEXT ONE
            		int nextRow = ++lastRow;
            		ConfigController.setPreviousRow(nextRow);
            		return nextRow;
            //Prep First To Be
            }else {
            	ConfigController.setPreviousRow(2);
            	return 2;
            		}
        //IF APPLICATION ALREADY UNDERWAY, THEN KNOWN TO BE VALID AND cAN JUST INCREASE    
        }else{
            ConfigController.setPreviousRow(ConfigController.getPreviousRow() + 1);
            return ConfigController.getPreviousRow();
        }

    }

	//RETURNS THE DESIRED ValueRange To be read from the sheet in the form  "Sheet1!A5-A10
    public static String getNextValueRange() throws ClassNotFoundException, SQLException{
    	int rowToObtain = getNextRowNum();
    	return 
    			//alternate this tag with "FormsResponse1!" IF SPREADSHEET IS RESULT OF GOOGLE FORMS
    			"Sheet1!" + 
    			ConfigController.getSpreadsheetsr() + 
    			rowToObtain + 
    			":" +
    			ConfigController.getSpreadsheeter() +
    			rowToObtain;
    }
    
    //Returns Whether Row IS Valid
    public static boolean CheckIfValid(List<Object> valueRange) {
    	if (valueRange == null || valueRange.isEmpty()) {
    		ConfigController.setLastRowFound(true);
    		return false;
    	}else {
    		return true;
    	}
    	
    }
    
    
    
    
    
    
    
    
    
    
}
