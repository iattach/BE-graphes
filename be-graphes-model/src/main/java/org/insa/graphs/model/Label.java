/**
 * 
 */
package org.insa.graphs.model;

/**
 * @author 
 *
 */
public class Label {
	/*sommet associé à ce label (sommet ou numéro de sommet).*/
	private Node current;
	/*booléen, vrai lorsque le coût min de ce sommet est définitivement connu par l'algorithme.*/
	private boolean mark;
	/*valeur courante du plus court chemin depuis l'origine vers le sommet.*/
	private float cost;
	/*sommet précédent sur le chemin correspondant au plus court chemin courant*/
	private Node father;
	
	
	public Label(Node current, boolean mark, float cost, Node father) {
		this.current = current;
		this.mark = mark;
		this.cost = cost;
		this.father = father;
	}


	public float getCost() {
		return cost;
	}
	
	
}
