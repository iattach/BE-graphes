package org.insa.graphs.algorithm.shortestpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.time.Duration;

import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.RoadInformation;
import org.insa.graphs.model.RoadInformation.RoadType;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;
import org.junit.BeforeClass;
import org.junit.Test;

public class AlgorithmCompareTest {

	// Small graph use for tests
	private static Graph carreGraph,emptyGraph, singleNodeGraph, shortGraph, longGraph, loopGraph, longLoopGraph, invalidGraph;;

	// List of nodes
	private static Node[] nodes;

	private static List<Node> nodesCarreGraph;

	// List of arcs in the graph, a2b is the arc from node A (0) to B (1).
	@SuppressWarnings("unused")
	private static Arc a2b, a2c, a2e, b2c, c2d_1, c2d_2, c2d_3, c2a, d2a, d2e, e2d;

	// Some paths solutions...
	private static ShortestPathSolution emptyPathSolution, singleNodePathSolution, shortPathSolution, longPathSolution,
			loopPathSolution, longLoopPathSolution, invalidPathSolution;
	private ShortestPathSolution dijkstraSolution,aStarSolution,bellmanFordSolution;
	
	// Data use for tests
	private ShortestPathData shortestPathData;

	// Algoritm use for tests
	private DijkstraAlgorithm dijkstraAlgorithm;
	private AStarAlgorithm aStarAlgorithm;
	private BellmanFordAlgorithm bellmanFordAlgorithm;
	// ArcInspector use for tests
	private static List<ArcInspector> listArcInspector;
	
	// Graph reader use for tests
	private static GraphReader reader;

	@BeforeClass
	public static void initAll() throws IOException {
		
		listArcInspector = ArcInspectorFactory.getAllFilters();
		
	}
	/*
	 * Compare the solutions for carre map
	 */
	@Test
	public void testCarreGraph() throws IOException {
		String mapName = "./carre.mapgr";
		
		reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
		carreGraph=reader.read();
		nodesCarreGraph=carreGraph.getNodes();
		
		shortestPathData = new ShortestPathData(carreGraph, nodesCarreGraph.get(0), nodesCarreGraph.get(10), listArcInspector.get(0));
		System.out.println(nodesCarreGraph.size());
		System.out.println(listArcInspector.get(0));
		System.out.println(nodesCarreGraph.get(0));
		System.out.println(nodesCarreGraph.get(10));
		
		dijkstraAlgorithm = new DijkstraAlgorithm(shortestPathData);
		dijkstraSolution=dijkstraAlgorithm.doRun();
				
		aStarAlgorithm = new AStarAlgorithm(shortestPathData);
		aStarSolution = aStarAlgorithm.doRun();
		
		bellmanFordAlgorithm = new BellmanFordAlgorithm(shortestPathData);
		bellmanFordSolution=bellmanFordAlgorithm.doRun();
		
		System.out.println(dijkstraAlgorithm);
		System.out.println(dijkstraSolution.getPath().getMinimumTravelTime());
		
		double durDijkstra=dijkstraSolution.getPath().getMinimumTravelTime();
		double durAStar=aStarSolution.getPath().getMinimumTravelTime();
		double durBellmanFord=bellmanFordSolution.getPath().getMinimumTravelTime();
		
		System.out.println(durDijkstra);
		
		
		assertTrue(Math.abs(durDijkstra - durAStar) < 0.001d);
		assertTrue(durDijkstra==durBellmanFord);

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
		dijkstraAlgorithm = new DijkstraAlgorithm(shortestPathData);
		invalidPathSolution = dijkstraAlgorithm.doRun();
		
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
		dijkstraAlgorithm = new DijkstraAlgorithm(shortestPathData);
		invalidPathSolution = dijkstraAlgorithm.doRun();
		
		assertTrue(!invalidPathSolution.isFeasible());

	}
	

}
