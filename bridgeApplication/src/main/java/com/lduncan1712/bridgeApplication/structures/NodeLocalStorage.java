package com.lduncan1712.bridgeApplication.structures;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.lduncan1712.bridgeApplication.controllers.ConfigController;

public class NodeLocalStorage extends ConfigController{
	
	//NEEEDS TO HOLD STRENGTH SOMEHOW TO HAVE THE COMPARE TO DECIDE IF AUTOMATCH TO WORK
	
	public NodeData reference;   //holds the actual node
	public Set<Integer> referenceList = new HashSet<>();
	
	public NodeLocalStorage(NodeData reference, int row) {
		this.reference = reference;
		this.referenceList.add(row);
		
	}
	
	public boolean nowEmpty() {
		return this.referenceList.isEmpty();
	}
	
	//Checks if a node with matching Id, is already within cache, otherwise makes and adds one
	public static NodeData conferWithCache(int id, String name) {
    	//IF NODE MATCHING IS ALREADY MADE
    	if(localNodeCache.containsKey(id)) {
    		localNodeCache.get(id).referenceList.add(ConfigController.getPreviousRow());
    		return localNodeCache.get(id).reference;
    	}else {
    		localNodeCache.put(id, new NodeLocalStorage(new NodeData(name, id), 0));
    		return localNodeCache.get(id).reference;
    	}
    }
}
