package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
		Label[] marks = this.setLabels(nbNodes);

		for (Node node : graph.getNodes()) {
			marks[node.getId()] = setMark(node);
		}

		marks[data.getOrigin().getId()].setCost(0);

		// Initialize the heap
		BinaryHeap<Label> bh = new BinaryHeap<Label>();
		bh.insert(marks[data.getOrigin().getId()]);

		// Notify observers about the first event (origin processed).
		notifyOriginProcessed(data.getOrigin());

		boolean destMark = false;
		while (!bh.isEmpty() && !destMark) {
			// get the minimum from the heap

			Label label = bh.findMin();
			Node current = label.getCurrent();

			notifyNodeMarked(current);

			bh.remove(label);

			marks[current.getId()].setMark(true);
			;

			for (Arc arc : label.getCurrent().getSuccessors()) {

				// Small test to check allowed roads...
				if (!data.isAllowed(arc)) {
					continue;
				}
				// get destination
				Node suc = arc.getDestination();

				if (!marks[suc.getId()].isMark()) {
					if (marks[suc.getId()].getCost() > (marks[current.getId()].getCost() + data.getCost(arc))) {

						Label labelSucBefore = marks[suc.getId()];
						double costBefore = labelSucBefore.getCost();

						marks[suc.getId()].setCost(marks[current.getId()].getCost() + data.getCost(arc));
						marks[suc.getId()].setFather(current);

						Label labelSucAfter = marks[suc.getId()];

						if (!bh.isExist(labelSucBefore)) {
							if (Double.isInfinite(costBefore) && Double.isFinite(labelSucAfter.getCost())) {
								notifyNodeReached(suc);
							}

							bh.insert(labelSucAfter);
						} else {
							bh.remove(labelSucBefore);
							bh.insert(labelSucAfter);
						}
					}
				}
			}
			if (!bh.isEmpty()) {
				label = bh.findMin();
				current = label.getCurrent();
				if (current.equals(data.getDestination())) {
					// float / double ? = >ici get cost by label => arc return getcost double
					// System.out.println("Getcost "+label.getCost());
					destMark = true;
				}
				// System.out.println(bh.toString());
			} else {
				/*
				 * le chemin vers la destination ne peut pas etre trouvé sur la carte
				 */
				destMark = true;
				solution = new ShortestPathSolution(data, Status.INFEASIBLE);
			}

		}

		// Destination has no predecessor, the solution is infeasible...
		if (marks[data.getDestination().getId()].getFather() == null) {

			solution = new ShortestPathSolution(data, Status.INFEASIBLE);

		} else {

			// The destination has been found, notify the observers.
			notifyDestinationReached(data.getDestination());

			// Create the path from the array of predecessors...
			ArrayList<Node> nodes = new ArrayList<>();

			Node current = marks[data.getDestination().getId()].getCurrent();

			while (current != null) {
				nodes.add(current);
				current = marks[current.getId()].getFather();
			}

			// Reverse the path...
			Collections.reverse(nodes);

			List<Arc> arcs = Path.createShortestPathFromNodes(graph, nodes).getArcs();
			// float / double ? = >ici getlength by path return float
			// System.out.println(Path.createShortestPathFromNodes(graph,
			// nodes).getLength());
			// Create the final solution.
			solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
		}

		return solution;
	}

	public Label[] setLabels(int nbNodes) {
		return new Label[nbNodes];
	}

	public Label setMark(Node node) {
		return new Label(node, false, Double.POSITIVE_INFINITY, null);
	}

	public boolean getCompareCost() {
		return true;
	}
}
