package au.edu.unimelb.plantcell.networks;

import java.util.Collection;

import au.edu.unimelb.plantcell.networks.cells.MyEdge;
import au.edu.unimelb.plantcell.networks.cells.MyVertex;
import edu.uci.ics.jung.algorithms.shortestpath.UnweightedShortestPath;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;

/**
 * Eliminate vertices based on the currently chosen network filtering options
 * @author andrew.cassin
 *
 */
public class MyDistancePredicate<T extends Context<Graph<MyVertex,MyEdge>, MyVertex>> implements MyPredicate<T> {
	private Graph<MyVertex,MyEdge> m_g = null;
	private UnweightedShortestPath<MyVertex,MyEdge> m_shortest = null;
	private double max_hops = 2.0;
	private Collection<MyVertex> selected_vertices;
	
	public MyDistancePredicate(Collection<MyVertex> target_vertices) {
		assert(target_vertices != null);
		selected_vertices = target_vertices;
	}
	
	@Override
	public boolean evaluate(T c) {
		if (c == null || c.graph == null)
			return false;
		if (m_g != c.graph) {
			m_shortest = new UnweightedShortestPath<MyVertex, MyEdge>(c.graph);
			m_g = c.graph;
		}
		if (m_shortest == null || c.element == null)
			return false;
		
		for (MyVertex v : selected_vertices) {
			Number n = m_shortest.getDistance(c.element, v);
			if (n == null)
				return false;
			double d = n.doubleValue();
			if (d >= 0.0d && d <= max_hops) 
				return true;
		} 
		return false;
	}

	public void setDistance(int hops) {
		assert(hops >= 1);
		max_hops = hops;
	}

	@Override
	public String toString() {
		return "Distance within "+((int)max_hops)+" hops of selected nodes.";
	}
	
	@Override
	public boolean isVertexPredicate() {
		return true;
	}
};