package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Point;
import org.insa.graphs.model.GraphStatistics;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }
    @Override
	public Label[] setLabels(int nbNodes) {
    	return new LabelStar[nbNodes];
    }
	@Override
	public Label setMark(Node node) {
		LabelStar mark=new LabelStar(node,false,Double.POSITIVE_INFINITY,null);
		mark.setCostDest(this.getDestCost(node));
    	return mark;
    }

	public double getDestCost(Node node) {
		final ShortestPathData data = getInputData();
		Point pointNode=node.getPoint();
		Point pointNodeDest=data.getDestination().getPoint();
		
		double distance=Point.distance(pointNode, pointNodeDest);
		
		Mode m=data.getMode();

		if(m.compareTo(Mode.LENGTH)==0) {
			return distance;
		}else {
			if(data.getMaximumSpeed()==GraphStatistics.NO_MAXIMUM_SPEED) {
				return distance/data.getGraph().getGraphInformation().getMaximumSpeed();
			}else {
				return distance/data.getMaximumSpeed();
			}
		}
    }
}
