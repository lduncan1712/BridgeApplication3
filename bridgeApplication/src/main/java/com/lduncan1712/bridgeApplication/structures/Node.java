package com.lduncan1712.bridgeApplication.structures;


public class Node {
	
	public double strength;
	public NodeData root;
	
	public Node(NodeData root, double strength) {
		this.root = root;
		this.strength = Math.round(strength*1000.0)/10;
	}
	@Override
	public String toString() {
		return root.name + "(" + strength + ")";
	}

}