package org.insa.graphs.algorithm.shortestpath;

import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;
import org.junit.BeforeClass;
import org.junit.Test;

public class DijkstraAlgorithmTest {

	// Small graph use for tests
	private static Graph mapGraph;


	private static List<Node> nodesCarreGraph;

	// Some paths solutions...
	private static ShortestPathSolution singleNodePathSolution, invalidPathSolution;
	private ShortestPathSolution dijkstraSolution,bellmanFordSolution;
	
	// Data use for tests
	private ShortestPathData shortestPathData;

	// Algoritm use for tests
	private DijkstraAlgorithm dijkstraAlgorithm;
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
	 * Chemin inexistant
	 */
	@Test
	public void testCheminInvalid_1() throws IOException {
		//System.out.println(System.getProperty("user.dir"));
		String mapName = "../hawaii.mapgr";
		
		reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
		mapGraph=reader.read();
		nodesCarreGraph=mapGraph.getNodes();
		
		shortestPathData = new ShortestPathData(mapGraph, nodesCarreGraph.get(41733), nodesCarreGraph.get(65424), listArcInspector.get(0));
		
		dijkstraAlgorithm = new DijkstraAlgorithm(shortestPathData);
		invalidPathSolution=dijkstraAlgorithm.doRun();
		
		assertTrue(!invalidPathSolution.isFeasible());

	}
	/*
	 * Chemin inexistant
	 */
	@Test
	public void testCheminInvalid_2() throws IOException {
		//System.out.println(System.getProperty("user.dir"));
		String mapName = "../new-zealand.mapgr";
		
		reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
		mapGraph=reader.read();
		nodesCarreGraph=mapGraph.getNodes();
		
		shortestPathData = new ShortestPathData(mapGraph, nodesCarreGraph.get(107873), nodesCarreGraph.get(49212), listArcInspector.get(3));
		
		dijkstraAlgorithm = new DijkstraAlgorithm(shortestPathData);
		invalidPathSolution=dijkstraAlgorithm.doRun();
		
		assertTrue(!invalidPathSolution.isFeasible());

	}
	/*
	 * Chemin de longeur nulle
	 */
	@Test
	public void testSingleNodeChemin_1()throws IOException {
		String mapName = "../carre.mapgr";
		
		reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
		mapGraph=reader.read();
		nodesCarreGraph=mapGraph.getNodes();
		
		shortestPathData = new ShortestPathData(mapGraph, nodesCarreGraph.get(0), nodesCarreGraph.get(0), listArcInspector.get(0));
		
		dijkstraAlgorithm = new DijkstraAlgorithm(shortestPathData);
		singleNodePathSolution = dijkstraAlgorithm.doRun();
		
		double durDijkstra=singleNodePathSolution.getPath().getMinimumTravelTime();
		
		assertTrue(durDijkstra==0);

	}
	
	/*
	 * Chemin de longeur nulle
	 */
	@Test
	public void testSingleNodeChemin_2()throws IOException {
		String mapName = "../hawaii.mapgr";
		
		reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
		mapGraph=reader.read();
		nodesCarreGraph=mapGraph.getNodes();
		
		shortestPathData = new ShortestPathData(mapGraph, nodesCarreGraph.get(37859), nodesCarreGraph.get(37859), listArcInspector.get(0));
		
		dijkstraAlgorithm = new DijkstraAlgorithm(shortestPathData);
		singleNodePathSolution = dijkstraAlgorithm.doRun();

		double durDijkstra=singleNodePathSolution.getPath().getMinimumTravelTime();
		
		assertTrue(durDijkstra==0);

	}
	/*
	 * Shortest path, all roads allowed
	 */
	@Test
	public void testShortestAllAllowed_1()throws IOException {
		String mapName = "../toulouse.mapgr";
		
		reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
		mapGraph=reader.read();
		nodesCarreGraph=mapGraph.getNodes();
		
		shortestPathData = new ShortestPathData(mapGraph, nodesCarreGraph.get(12625), nodesCarreGraph.get(9610), listArcInspector.get(0));
		
		dijkstraAlgorithm = new DijkstraAlgorithm(shortestPathData);
		dijkstraSolution = dijkstraAlgorithm.doRun();

		bellmanFordAlgorithm = new BellmanFordAlgorithm(shortestPathData);
		bellmanFordSolution=bellmanFordAlgorithm.doRun();
		
		double durDijkstra=dijkstraSolution.getPath().getLength();
		double durBellmanFord=bellmanFordSolution.getPath().getLength();
		
		assertTrue(Math.abs(durDijkstra - durBellmanFord) < 0.001d);

	}
	/*
	 * Shortest path, all roads allowed
	 */
	@Test
	public void testShortestAllAllowed_2()throws IOException {
		String mapName = "../washington.mapgr";
		
		reader = new BinaryGraphReader(
               new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
		mapGraph=reader.read();
		nodesCarreGraph=mapGraph.getNodes();
		
		shortestPathData = new ShortestPathData(mapGraph, nodesCarreGraph.get(147639), nodesCarreGraph.get(132165), listArcInspector.get(0));
		
		dijkstraAlgorithm = new DijkstraAlgorithm(shortestPathData);
		dijkstraSolution = dijkstraAlgorithm.doRun();

//		bellmanFordAlgorithm = new BellmanFordAlgorithm(shortestPathData);
//		bellmanFordSolution=bellmanFordAlgorithm.doRun();
//		
//		double durDijkstra=dijkstraSolution.getPath().getLength();
//		double durBellmanFord=bellmanFordSolution.getPath().getLength();
//		
//		assertTrue(Math.abs(durDijkstra - durBellmanFord) < 0.001d);

	}
	/*
	 * Shortest path, only roads open for cars
	 */
	@Test
	public void testShortestOnlyCar_1()throws IOException {
		String mapName = "../toulouse.mapgr";
		
		reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
		mapGraph=reader.read();
		nodesCarreGraph=mapGraph.getNodes();
		
		shortestPathData = new ShortestPathData(mapGraph, nodesCarreGraph.get(12625), nodesCarreGraph.get(9610), listArcInspector.get(1));
		
		dijkstraAlgorithm = new DijkstraAlgorithm(shortestPathData);
		dijkstraSolution = dijkstraAlgorithm.doRun();

		bellmanFordAlgorithm = new BellmanFordAlgorithm(shortestPathData);
		bellmanFordSolution=bellmanFordAlgorithm.doRun();
		
		double durDijkstra=dijkstraSolution.getPath().getLength();
		double durBellmanFord=bellmanFordSolution.getPath().getLength();
		
		assertTrue(Math.abs(durDijkstra - durBellmanFord) < 0.001d);

	}
	/*
	 * Shortest path, only roads open for cars
	 */
	@Test
	public void testShortestOnlyCar_2()throws IOException {
		String mapName = "../midi-pyrenees.mapgr";
		
		reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
		mapGraph=reader.read();
		nodesCarreGraph=mapGraph.getNodes();
		
		shortestPathData = new ShortestPathData(mapGraph, nodesCarreGraph.get(341560), nodesCarreGraph.get(548753), listArcInspector.get(1));
		
		dijkstraAlgorithm = new DijkstraAlgorithm(shortestPathData);
		dijkstraSolution = dijkstraAlgorithm.doRun();

//		bellmanFordAlgorithm = new BellmanFordAlgorithm(shortestPathData);
//		bellmanFordSolution=bellmanFordAlgorithm.doRun();
//		
//		double durDijkstra=dijkstraSolution.getPath().getLength();
//		double durBellmanFord=bellmanFordSolution.getPath().getLength();
//		
//		assertTrue(Math.abs(durDijkstra - durBellmanFord) < 0.001d);

	}
	/*
	 * Fastest path, all roads allowed
	 */
	@Test
	public void testFastestAllAllowed_1() throws IOException {
		String mapName = "../toulouse.mapgr";
		
		reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
		mapGraph=reader.read();
		nodesCarreGraph=mapGraph.getNodes();
		
		shortestPathData = new ShortestPathData(mapGraph, nodesCarreGraph.get(12625), nodesCarreGraph.get(9610), listArcInspector.get(2));
		
		dijkstraAlgorithm = new DijkstraAlgorithm(shortestPathData);
		dijkstraSolution = dijkstraAlgorithm.doRun();

		bellmanFordAlgorithm = new BellmanFordAlgorithm(shortestPathData);
		bellmanFordSolution=bellmanFordAlgorithm.doRun();
		
		double durDijkstra=dijkstraSolution.getPath().getLength();
		double durBellmanFord=bellmanFordSolution.getPath().getLength();
		
		assertTrue(Math.abs(durDijkstra - durBellmanFord) < 0.001d);

	}
	/*
	 * Fastest path, all roads allowed
	 */
	@Test
	public void testFastestAllAllowed_2() throws IOException {
		String mapName = "../insa.mapgr";
		
		reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
		mapGraph=reader.read();
		nodesCarreGraph=mapGraph.getNodes();
		
		shortestPathData = new ShortestPathData(mapGraph, nodesCarreGraph.get(5), nodesCarreGraph.get(636), listArcInspector.get(2));
		
		dijkstraAlgorithm = new DijkstraAlgorithm(shortestPathData);
		dijkstraSolution = dijkstraAlgorithm.doRun();

		bellmanFordAlgorithm = new BellmanFordAlgorithm(shortestPathData);
		bellmanFordSolution=bellmanFordAlgorithm.doRun();
		
		double durDijkstra=dijkstraSolution.getPath().getLength();
		double durBellmanFord=bellmanFordSolution.getPath().getLength();
		
		assertTrue(Math.abs(durDijkstra - durBellmanFord) < 0.001d);

	}
	/*
	 * Fastest path, only roads open for cars
	 */
	@Test
	public void testFastestOnlyCar_1() throws IOException {
		String mapName = "../toulouse.mapgr";
		
		reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
		mapGraph=reader.read();
		nodesCarreGraph=mapGraph.getNodes();
		
		shortestPathData = new ShortestPathData(mapGraph, nodesCarreGraph.get(12625), nodesCarreGraph.get(9610), listArcInspector.get(3));
		
		dijkstraAlgorithm = new DijkstraAlgorithm(shortestPathData);
		dijkstraSolution = dijkstraAlgorithm.doRun();

		bellmanFordAlgorithm = new BellmanFordAlgorithm(shortestPathData);
		bellmanFordSolution=bellmanFordAlgorithm.doRun();
		
		double durDijkstra=dijkstraSolution.getPath().getLength();
		double durBellmanFord=bellmanFordSolution.getPath().getLength();
		
		assertTrue(Math.abs(durDijkstra - durBellmanFord) < 0.001d);

	}
	/*
	 * Fastest path, only roads open for cars
	 */
	@Test
	public void testFastestOnlyCar_2() throws IOException {
		String mapName = "../paris.mapgr";
		
		reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
		mapGraph=reader.read();
		nodesCarreGraph=mapGraph.getNodes();
		
		shortestPathData = new ShortestPathData(mapGraph, nodesCarreGraph.get(7317), nodesCarreGraph.get(5603), listArcInspector.get(3));
		
		dijkstraAlgorithm = new DijkstraAlgorithm(shortestPathData);
		dijkstraSolution = dijkstraAlgorithm.doRun();

		bellmanFordAlgorithm = new BellmanFordAlgorithm(shortestPathData);
		bellmanFordSolution=bellmanFordAlgorithm.doRun();
		
		double durDijkstra=dijkstraSolution.getPath().getLength();
		double durBellmanFord=bellmanFordSolution.getPath().getLength();
		
		assertTrue(Math.abs(durDijkstra - durBellmanFord) < 0.001d);

	}
	/*
	 * Fastest path for pedestrian
	 */
	@Test
	public void testFastestPedestrian_1() throws IOException {
		String mapName = "../toulouse.mapgr";
		
		reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
		mapGraph=reader.read();
		nodesCarreGraph=mapGraph.getNodes();
		
		shortestPathData = new ShortestPathData(mapGraph, nodesCarreGraph.get(12625), nodesCarreGraph.get(9610), listArcInspector.get(4));
		
		dijkstraAlgorithm = new DijkstraAlgorithm(shortestPathData);
		dijkstraSolution = dijkstraAlgorithm.doRun();

		bellmanFordAlgorithm = new BellmanFordAlgorithm(shortestPathData);
		bellmanFordSolution=bellmanFordAlgorithm.doRun();
		
		double durDijkstra=dijkstraSolution.getPath().getLength();
		double durBellmanFord=bellmanFordSolution.getPath().getLength();
		
		assertTrue(Math.abs(durDijkstra - durBellmanFord) < 0.001d);

	}
	/*
	 * Fastest path for pedestrian
	 */
	@Test
	public void testFastestPedestrian_2() throws IOException {
		String mapName = "../bordeaux.mapgr";
		
		reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
		mapGraph=reader.read();
		nodesCarreGraph=mapGraph.getNodes();
		
		shortestPathData = new ShortestPathData(mapGraph, nodesCarreGraph.get(5737), nodesCarreGraph.get(7974), listArcInspector.get(4));
		
		dijkstraAlgorithm = new DijkstraAlgorithm(shortestPathData);
		dijkstraSolution = dijkstraAlgorithm.doRun();

		bellmanFordAlgorithm = new BellmanFordAlgorithm(shortestPathData);
		bellmanFordSolution=bellmanFordAlgorithm.doRun();
		
		double durDijkstra=dijkstraSolution.getPath().getLength();
		double durBellmanFord=bellmanFordSolution.getPath().getLength();
		
		assertTrue(Math.abs(durDijkstra - durBellmanFord) < 0.001d);

	}
	

}
