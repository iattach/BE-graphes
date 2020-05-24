/**
 * 
 */
package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Node;


public class LabelStar extends Label implements Comparable<Label>{
	
	private double costDest;
	/**
	 * @param current
	 * @param mark
	 * @param cost
	 * @param father
	 */
	public LabelStar(Node current, boolean mark, double cost, Node father) {
		super(current, mark, cost, father);
		// TODO Auto-generated constructor stub
	}
	public double getCostDest() {
		return this.costDest;
	}
	public void setCostDest(double costDest) {
		this.costDest = costDest;
	}
	public double getTotalCost() {
		return this.getCost()+this.getCostDest();
	}
	@Override
	public String toString() {
		return "Label dest : "+(this.costDest);
	}
	
}
