package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.*;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
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
        
        // Initialize array of marks.
        Label[] marks=new Label[nbNodes];   
        for (Node node: graph.getNodes()) {
        	marks[node.getId()]=new Label(node,false,Double.POSITIVE_INFINITY,null);
        }
        marks[data.getOrigin().getId()].setCost(0);
        //Initialize the heap
        BinaryHeap<Label> bh=new BinaryHeap<Label>();
        bh.insert(marks[data.getOrigin().getId()]);
        
        // Notify observers about the first event (origin processed).
        notifyOriginProcessed(data.getOrigin());
        
        
        while(!bh.isEmpty()) {
        	//get the minimum from the heap
        	Label label=bh.findMin();
        	Node current=label.getCurrent();
        	
        	bh.remove(label);
        	
        	marks[current.getId()].setMark(true);;
        	
        	for (Arc arc: label.getCurrent().getSuccessors()) {
        		
        		
        		// Small test to check allowed roads...
                if (!data.isAllowed(arc)) {
                    continue;
                }
                //get destination
        		Node suc=arc.getDestination();
        		
        		
        		if(!marks[suc.getId()].isMark()) {
        			if(marks[suc.getId()].getCost()>(marks[current.getId()].getCost()+data.getCost(arc))) {
        				
        				marks[suc.getId()].setCost(marks[current.getId()].getCost()+data.getCost(arc));
        				marks[suc.getId()].setFather(current);
        				
        				Label labelSuc=marks[suc.getId()];
        						
        				if(!bh.isExist(labelSuc)) {
        					bh.insert(labelSuc);
        				}
        			}
        		}
        	}

        	
        }
        
        // Destination has no predecessor, the solution is infeasible...
        if (marks[data.getDestination().getId()].getFather() == null) {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        }else {

            // The destination has been found, notify the observers.
            notifyDestinationReached(data.getDestination());

            // Create the path from the array of predecessors...
            ArrayList<Arc> arcs = new ArrayList<>();
            Arc arc = predecessorArcs[data.getDestination().getId()];
            while (arc != null) {
                arcs.add(arc);
                arc = predecessorArcs[arc.getOrigin().getId()];
            }

            // Reverse the path...
            Collections.reverse(arcs);

            // Create the final solution.
            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
        }
        
        return solution;
    }

}
