package au.edu.unimelb.plantcell.networks.cells;

import org.knime.core.data.DataValue;

import edu.uci.ics.jung.graph.Graph;

/**
 * Specification to which all network cells must conform
 * @author andrew.cassin
 *
 */
public interface NetworkValue extends DataValue, Comparable<NetworkValue> {
	
	/**
	 * Returns the graph instance associated with this network
	 * @return
	 */
	public Graph<MyVertex, MyEdge> getGraph();
}
