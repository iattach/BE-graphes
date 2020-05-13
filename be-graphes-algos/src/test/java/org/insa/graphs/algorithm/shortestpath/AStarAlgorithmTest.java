package org.insa.graphs.algorithm.shortestpath;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.RoadInformation;
import org.insa.graphs.model.RoadInformation.RoadType;
import org.junit.BeforeClass;
import org.junit.Test;

public class AStarAlgorithmTest {

	// Small graph use for tests
	private static Graph emptyGraph, singleNodeGraph, shortGraph, longGraph, loopGraph, longLoopGraph, invalidGraph;;

	// List of nodes
	private static Node[] nodes;

	// List of arcs in the graph, a2b is the arc from node A (0) to B (1).
	@SuppressWarnings("unused")
	private static Arc a2b, a2c, a2e, b2c, c2d_1, c2d_2, c2d_3, c2a, d2a, d2e, e2d;

	// Some paths solutions...
	private static ShortestPathSolution emptyPathSolution, singleNodePathSolution, shortPathSolution, longPathSolution,
			loopPathSolution, longLoopPathSolution, invalidPathSolution;
	// Data use for tests
	private static ShortestPathData shortestPathData;

	// Algoritm use for tests
	private static AStarAlgorithm aStarAlgorithm;

	// ArcInspector use for tests
	private static List<ArcInspector> listArcInspector;

	@BeforeClass
	public static void initAll() throws IOException {
		
		
		listArcInspector = ArcInspectorFactory.getAllFilters();
		
	}
	
	/*
	 * Chemin invalide
	 */
	@Test
	public void testInvalidGraph() {
		// 10 and 20 meters per seconds
		RoadInformation speed10 = new RoadInformation(RoadType.MOTORWAY, null, true, 36, ""),
						speed20 = new RoadInformation(RoadType.MOTORWAY, null, true, 72, "");
		// Create nodes
		nodes = new Node[5];
		for (int i = 0; i < nodes.length; ++i) {
			nodes[i] = new Node(i, null);
		}
		// Add arcs...
		a2b = Node.linkNodes(nodes[0], nodes[1], 10, speed10, null);
		d2e = Node.linkNodes(nodes[3], nodes[4], 22.8f, speed20, null);
		e2d = Node.linkNodes(nodes[4], nodes[0], 10, speed10, null);
		
		invalidGraph = new Graph("ID", "", Arrays.asList(nodes), null);
		
		shortestPathData = new ShortestPathData(invalidGraph, nodes[0], nodes[4], listArcInspector.get(0));
		aStarAlgorithm = new AStarAlgorithm(shortestPathData);
		invalidPathSolution = aStarAlgorithm.doRun();
		
		assertTrue(!invalidPathSolution.isFeasible());

	}
	/*
	 * Chemin de longeur nulle
	 */
	@Test
	public void testCheminNullGraph() {
		// 10 and 20 meters per seconds
		RoadInformation speed10 = new RoadInformation(RoadType.MOTORWAY, null, true, 36, ""),
						speed20 = new RoadInformation(RoadType.MOTORWAY, null, true, 72, "");
		// Create nodes
		nodes = new Node[5];
		for (int i = 0; i < nodes.length; ++i) {
			nodes[i] = new Node(i, null);
		}
		// Add arcs...
		a2b = Node.linkNodes(nodes[0], nodes[1], 10, speed10, null);
		d2e = Node.linkNodes(nodes[3], nodes[4], 22.8f, speed20, null);
		e2d = Node.linkNodes(nodes[4], nodes[0], 10, speed10, null);
		
		invalidGraph = new Graph("ID", "", Arrays.asList(nodes), null);
		
		shortestPathData = new ShortestPathData(invalidGraph, nodes[0], nodes[0], listArcInspector.get(0));
		aStarAlgorithm = new AStarAlgorithm(shortestPathData);
		invalidPathSolution = aStarAlgorithm.doRun();
		
		assertTrue(!invalidPathSolution.isFeasible());

	}
	

}
