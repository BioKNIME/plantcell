package au.edu.unimelb.plantcell.io.ws.tmhmm;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import au.edu.unimelb.plantcell.core.UniqueID;
import au.edu.unimelb.plantcell.core.cells.CoordinateSystem;
import au.edu.unimelb.plantcell.core.cells.SequenceValue;
import au.edu.unimelb.plantcell.core.cells.Track;
import au.edu.unimelb.plantcell.core.cells.TrackCreator;
import au.edu.unimelb.plantcore.core.regions.RegionsAnnotation;

public abstract class AbstractWebServiceNodeModel extends NodeModel {
	private static final int INITIAL_CAPACITY = 100 * 1024;

	protected AbstractWebServiceNodeModel(int nrInDataPorts, int nrOutDataPorts) {
		super(nrInDataPorts, nrOutDataPorts);
	}
	
	protected TrackCreator getTrackCreator() {
		return new TrackCreator() {

			@Override
			public Track createTrack(String name)
					throws InvalidSettingsException {
				return new Track(name, new RegionsAnnotation(), CoordinateSystem.OFFSET_FROM_START);
			}
			
		};
	}

	/**
	 * Converts a small table of data to FASTA format using the uniqueID as the sequence ID
	 * 
	 * @param batch
	 * @return
	 * @throws InvalidSettingsException
	 */
	public String toFasta(Map<UniqueID, SequenceValue> batch) throws InvalidSettingsException {
		StringBuffer sb = new StringBuffer(INITIAL_CAPACITY);
		HashSet<String> done = new HashSet<String>();
		for (UniqueID uid : batch.keySet()) {
			if (done.contains(uid.toString())) 
				throw new InvalidSettingsException("duplicate key not permitted: "+uid.toString());
			sb.append(">"+uid.toString()+"\n");
			sb.append(batch.get(uid).getStringValue());
			sb.append("\n");
			done.add(uid.toString());
		}
		
		return sb.toString();
	}
	
	public abstract String getStatus(String jobID) throws Exception;
	
	protected void wait_for_completion(NodeLogger logger, ExecutionContext exec, String jobid)
						throws Exception {
    	String status = "QUEUED";

    	int incomplete_delay = 0;
		while (status.equals("QUEUED") || status.equals("WAITING") || status.equals("RUNNING")) {
			Thread.sleep(5 * 1000);	// mandatory 5s waiting for submission
			exec.checkCanceled();
			
			status = getStatus(jobid);
			logger.info("Got status "+status+" for job "+jobid);
			if (!status.startsWith("COMPLETE")) {
				incomplete_delay += 5;
				if (incomplete_delay > 500)
					throw new Exception("Job "+jobid+" not completed after excessive delay!");
				for (int i=0; i<(incomplete_delay / 5); i++) {
					exec.checkCanceled();
					Thread.sleep(5 * 1000);
				}
			}
		}
		
		if (!status.startsWith("COMPLETE")) {
			throw new Exception("Job "+jobid+" failed, message: "+status);
		}
	}

	
	public static String getDefaultEndpoint() {
		return "";
	}
	
	/**
     * {@inheritDoc}
     */
	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// TODO Auto-generated method stub

	}

	/**
     * {@inheritDoc}
     */
	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {
		// TODO Auto-generated method stub
		int x = 0;
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings)
			throws InvalidSettingsException {
		// TODO Auto-generated method stub
		int x=0;
	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
			throws InvalidSettingsException {
		// TODO Auto-generated method stub
		int x=0;
	}


	/**
     * {@inheritDoc}
     */
	@Override
	protected void reset() {
		// no-op
	}

	/**
	 * Searches the specified input table spec to find a SequenceValue compatible column
	 */
	protected boolean hasSequenceColumn(DataTableSpec inSpec) {
		return (useSequenceColumnIndex(inSpec, null) >= 0);
	}

	/**
	 * Returns the index of the right-most column with a suitable
	 * @param inSpec input table spec to search
	 * @param logger may be null
	 * @return negative if not suitable column can be found, otherwise the column index is returned
	 */
	protected int useSequenceColumnIndex(DataTableSpec inSpec, NodeLogger logger) {
		for (int i=inSpec.getNumColumns()-1; i >= 0; i--) {
			DataColumnSpec cs = inSpec.getColumnSpec(i);
			if (cs.getType().isCompatible(SequenceValue.class)) {
				if (logger != null) {
					logger.warn("Using '"+cs.getName()+"' column for biological sequences.");
				}
				return i;
			}
		}
		return -1;
	}
}
