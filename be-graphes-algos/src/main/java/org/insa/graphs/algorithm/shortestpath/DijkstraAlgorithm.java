package org.insa.graphs.algorithm.shortestpath;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.*;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.GraphStatistics;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

	public DijkstraAlgorithm(ShortestPathData data) {
		super(data);
	}

	@Override
	protected ShortestPathSolution doRun() {
		final ShortestPathData data = getInputData();
		ShortestPathSolution solution = null;
		// Retrieve the graph
		Graph graph = data.getGraph();

		final int nbNodes = graph.size();

		/* Initialize array of marks.
		 * nbNodes: number of the nodes in the graph
		 */
		Label[] marks = this.setLabels(nbNodes);
		//associate the labels with the nodes and initialize the infinite cost
		for (Node node : graph.getNodes()) {
			marks[node.getId()] = this.setMark(node);
		}
		// give the origin a cost 0
		marks[data.getOrigin().getId()].setCost(0);

		// Initialize the heap
		BinaryHeap<Label> bh = new BinaryHeap<Label>();
		bh.insert(marks[data.getOrigin().getId()]);

		// Notify observers about the first event (origin processed).
		notifyOriginProcessed(data.getOrigin());
		
		//stop point when destination is arrived
		boolean destMark = false;
		
		//the successors counter
		int numbPoint=0;
		//the size maximum of the bineary heap
		int bhSize=0;
		//the size maximum of the bineary heap
		int bhIn=0;
		//the size maximum of the bineary heap
		int bhMark=0;
		//iteration
		while (!bh.isEmpty() && !destMark) {
			//System.out.println("Size of the heap : "+bh.size());
			// get the minimum from the heap
			Label label = bh.findMin();
			Node current = label.getCurrent();

			notifyNodeMarked(current);
			
			//remove the current node from the heap and mark this node
			bh.remove(label);
			bhMark++;
			marks[current.getId()].setMark(true);

			
			//search all the successors of this node
			for (Arc arc : label.getCurrent().getSuccessors()) {
				
				// Small test to check allowed roads...
				if (!data.isAllowed(arc)) {
					continue;
				}
				
				// get destination
				Node suc = arc.getDestination();
				
				//find the successors who aren't marked
				if (!marks[suc.getId()].isMark()) {
					
					//verify if the cost of the new path to this successor is short than his original cost or not
					if (marks[suc.getId()].getCost() > (marks[current.getId()].getCost() + data.getCost(arc))) {
						
						
						//stock the original label 
						Label labelSucBefore = marks[suc.getId()];
						
						//make a new label
						double costBefore = labelSucBefore.getCost();
						marks[suc.getId()].setCost(marks[current.getId()].getCost() + data.getCost(arc));
						marks[suc.getId()].setFather(current);
						Label labelSucAfter = marks[suc.getId()];
						
						//verify if the label have existed in the heap
						if (!bh.isExist(labelSucBefore)) {
							if (Double.isInfinite(costBefore) && Double.isFinite(labelSucAfter.getCost())) {
								notifyNodeReached(suc);
								numbPoint++;
							}
							//add a new one to heap 
							bh.insert(labelSucAfter);
							bhIn++;
						}else {
							bh.remove(labelSucBefore);
							bh.insert(labelSucAfter);
						}
					}
				}
				
			}
			//assertTrue(bh.isValid(0));
			//condition to stop : heap is empty or destination is arrived
			if (!bh.isEmpty()) {
				
				//the current head of the heap
				current = bh.findMin().getCurrent();
				
				//destination is arrived
				if (current.equals(data.getDestination())) {
					destMark = true;
				}
			} else {
				
				//heap is empty and destination can't be found
				destMark = true;
				solution = new ShortestPathSolution(data, Status.INFEASIBLE);
			}
			//size of the heap
			if(bh.size()>bhSize) {
				bhSize=bh.size();
			}
		}
		System.out.println("Label goes into BH in total : "+bhIn);
		System.out.println("Label goes out of BH in total : "+bhMark);
		System.out.println("BH size maximum : "+bhSize);
		System.out.println("Number of points touched in total : "+numbPoint);
		// Destination has no predecessor, the solution is infeasible...
		if ((marks[data.getDestination().getId()].getFather() == null)&&
				(data.getDestination().getId()!=data.getOrigin().getId())) {

			solution = new ShortestPathSolution(data, Status.INFEASIBLE);

		} else {

			// The destination has been found, notify the observers.
			notifyDestinationReached(data.getDestination());

			// Create the path from the array of predecessors...
			//ArrayList<Node> nodes = new ArrayList<>();
			ArrayList<Arc> arcs =new ArrayList<Arc>();
			
			Node current = marks[data.getDestination().getId()].getCurrent();
			
			
			Node last;
			
			while (current != null) {
				last=current;
				current = marks[current.getId()].getFather();
				if(current!=null) {
					int index=arcs.size();
					for(Arc arc : current.getSuccessors()) {
						if(arc.getDestination().equals(last)) {
							if(arcs.size()==index) {
								arcs.add(arc);
		        			}else {
		        				if(data.getCost(arc)<data.getCost(arcs.get(index))) {
		        					arcs.set(index,arc);
		        				}
		        			}
						}
					}
				}
			}
			// Reverse the path...
			Collections.reverse(arcs);
			Path pathSolution=new Path(graph, arcs);
			
			Mode m=data.getMode();
			//verify the result with the method of class Path
			if(m.compareTo(Mode.LENGTH)==0) {
				//in length when mode is LENGTH
				assertTrue(Math.abs(marks[data.getDestination().getId()].getCost()-pathSolution.getLength())<1d);
				//verification lower bound
				assertTrue(marks[data.getOrigin().getId()].getCost()<=pathSolution.getLength());
			}else {
				//in time when mode is TIME
				if(data.getMaximumSpeed()==GraphStatistics.NO_MAXIMUM_SPEED) {
					assertTrue(Math.abs(marks[data.getDestination().getId()].getCost()-pathSolution.getMinimumTravelTime())<0.001d);
					//verification lower bound
					assertTrue(marks[data.getOrigin().getId()].getCost()<=pathSolution.getMinimumTravelTime());
				}else {
//					System.out.println(marks[data.getDestination().getId()].getCost());
//					System.out.println(pathSolution.getTravelTime(data.getMaximumSpeed()));
					assertTrue(Math.abs(marks[data.getDestination().getId()].getCost()-pathSolution.getTravelTime(data.getMaximumSpeed()))<10d);
					//verification lower bound
					assertTrue(marks[data.getOrigin().getId()].getCost()<=pathSolution.getTravelTime(data.getMaximumSpeed()));
				}
				
			}
			//verify the result is valid with the method of class Path
			assertTrue(pathSolution.isValid());
			
			//List<Arc> arcs = Path.createShortestPathFromNodes(graph, nodes).getArcs();
			//System.out.println(Path.createShortestPathFromNodes(graph, nodes).getLength());
			
			// Create the final solution.
			solution = new ShortestPathSolution(data, Status.OPTIMAL, pathSolution);
		}

		return solution;
	}

	public Label[] setLabels(int nbNodes) {
		return new Label[nbNodes];
	}

	public Label setMark(Node node) {
		return new Label(node, false, Double.POSITIVE_INFINITY, null);
	}

}
