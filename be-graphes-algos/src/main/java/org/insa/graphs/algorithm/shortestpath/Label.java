/**
 * 
 */
package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Node;

/**
 * @author 
 *
 */
public class Label implements Comparable<Label>{
	/*sommet associé à ce label (sommet ou numéro de sommet).*/
	private Node current;
	/*booléen, vrai lorsque le coût min de ce sommet est définitivement connu par l'algorithme.*/
	private boolean mark;
	/*valeur courante du plus court chemin depuis l'origine vers le sommet.*/
	private double cost;
	/*sommet précédent sur le chemin correspondant au plus court chemin courant*/
	private Node father;
	
	
	public Label(Node current, boolean mark, double cost, Node father) {
		this.current = current;
		this.mark = mark;
		this.cost = cost;
		this.father = father;
	}


	public Node getFather() {
		return father;
	}


	public Node getCurrent() {
		return current;
	}


	public void setCurrent(Node current) {
		this.current = current;
	}


	public void setFather(Node father) {
		this.father = father;
	}


	public void setCost(double cost) {
		this.cost = cost;
	}


	public double getCost() {
		return cost;
	}

	public boolean isMark() {
		return mark;
	}


	public void setMark(boolean mark) {
		this.mark = mark;
		
	}
	public double getTotalCost() {
		return this.getCost();
	}
	@Override
	public int compareTo(Label other) {
		if(this.getTotalCost()==other.getTotalCost()) {
			return this.current.compareTo(other.getCurrent());
		}else {
			return Double.compare(this.getTotalCost(), other.getTotalCost());
		}
    }
	public String toString(){
		return "Label : "+this.cost;
	}
}
