package au.edu.unimelb.plantcell.io.muscle;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.exec.PumpStreamHandler;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.collection.ListCell;
import org.knime.core.data.collection.SetCell;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;

import au.edu.unimelb.plantcell.core.ErrorLogger;
import au.edu.unimelb.plantcell.core.ExecutorUtils;
import au.edu.unimelb.plantcell.core.UniqueID;
import au.edu.unimelb.plantcell.core.cells.SequenceType;
import au.edu.unimelb.plantcell.core.cells.SequenceValue;
import au.edu.unimelb.plantcell.io.write.fasta.FastaWriter;
import au.edu.unimelb.plantcell.io.ws.multialign.AlignmentViewDataModel;
import au.edu.unimelb.plantcell.io.ws.multialign.MultiAlignmentCell;

public abstract class AbstractAlignerNodeModel extends NodeModel implements AlignmentViewDataModel  {
	protected final static NodeLogger logger = NodeLogger.getLogger("Aligner (local)");
	
	private boolean warned_small_seqs;	// initialised by reset();

	/**
	 * After this constructor call you must subsequently call <code>reset()</code> to ensure all
	 * state is correctly initialised. This class provides a shared, protected, logger instance which all alignment nodes share
	 * 
	 * @param n_in number of input ports
	 * @param n_out number of output ports
	 */
	public AbstractAlignerNodeModel(int n_in, int n_out) {
		super(n_in, n_out);
	}
	  
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
    }

	/**
	 * Create a suitable output table specification for the node. Called during <code>configure()</code>
	 * and <code>execute()</code> this code must handle all aligners and parameters as required by KNIME platform
	 */
	 protected DataTableSpec make_output_spec(DataTableSpec inSpec, final int seq_idx) {
    	boolean has_collection_of_input_sequences = false;
    	if (seq_idx >= 0) {
    		DataColumnSpec seqSpec = inSpec.getColumnSpec(seq_idx);
    		try {
				has_collection_of_input_sequences = isCollectionOfSequencesColumn(seqSpec);
			} catch (InvalidSettingsException e) {
				has_collection_of_input_sequences = false;
				e.printStackTrace();
			}
    		// FALLTHRU
    	}
    	
    	DataColumnSpec[] cols = new DataColumnSpec[1];
		cols[0] = new DataColumnSpecCreator(getAlignmentLogName()+ " aligned sequences", MultiAlignmentCell.TYPE).createSpec();
    	if (seq_idx < 0 || !has_collection_of_input_sequences) {
    		return new DataTableSpec(cols);
    	} else {
    		return new DataTableSpec(inSpec, new DataTableSpec(cols));
    	}	
	    	
	}
	 
	/**
	 * Subclasses are required to return a valid executable when requested. This should be idempotent
	 * @return executable path to alignment program, ready for use
	 */
	protected abstract File getAlignmentProgram();
	
	/**
	 * Subclasses are required to return a valid display name for the alignment program used. Must not be null. This
	 * should, for user convenience, be idempotent.
	 * @return
	 */
	protected abstract String getAlignmentLogName();
	
	/**
	 * Return true if alignment progress made by the executable should be reported to the KNIME console. False otherwise.
	 */
	public boolean shouldLogAlignmentProgress() {
		return true;
	}
	
	/**
	 * Must call this implementation when reset is chosen by the framework/user: otherwise abstract class state
	 * will not be reset
	 */
	 @Override
	 protected void reset() {
		 warned_small_seqs = false;
	 }
	 
	/**
	  * Report an error to the current logger
	  */
	public void error(final Object msg) {
		logger.error(msg);
	}
	
	/**
	 * Checks to see if the current cell (as obtained during the execute() row iteration) is a collection or not.
	 * 
	 * @param seqs_cell
	 * @return true if a collection cell is found, false otherwise
	 */
    public boolean isValidCollectionForAlignment(final DataCell seqs_cell) {
    	if (seqs_cell == null)
    		return false;
    	
    	// list of sequence values eg. groupby list?
    	if ((seqs_cell instanceof ListCell) || (seqs_cell instanceof SetCell)) {
    		return true;
    	}
    	return false;
	}
	    
	/**
     * Returns true if the specified columnSpec is a list-of-sequences (or set-of-sequences) false otherwise.
     * 
     * @param columnSpec
     * @return
     * @throws InvalidSettingsException if bogus input given or not configured yet
     */
    protected boolean isCollectionOfSequencesColumn(final DataColumnSpec columnSpec) throws InvalidSettingsException {
		if (columnSpec == null)
			throw new InvalidSettingsException("No sequences column to check!");
		
		if (columnSpec.getType().isCollectionType() && columnSpec.getType().getCollectionElementType().isCompatible(SequenceValue.class)) {
			return true;
		}
		return false;
	}

	    
   /**
     * Verify that the input executable path is valid on this system and can be run. Will throw an exception if not.
     * 
     * @param exe path to muscle executable (must not be null)
     * @return a File instance to the specified executable
     * @throws IOException thrown if the path does not exist or cannot be executed
     */
	protected File validateAlignmentProgram(final String exe) throws IOException {
		final File f = new File(exe);
    	if (!f.exists() || !f.canExecute()) {
    		throw new IOException("Alignment program not executable: "+exe);
    	}
    	return f;
	}
    
    /**
     * Given the sequences in the specified FASTA format file and the specified sequence type, this
     * method is required to populate a CommandLine object with the appropriate arguments and return it.
     * The method is also responsible for determination of the executable to run
     * 
     * @param fasta_file
     * @param alignment_sequence_type
     * @return
     */
    public abstract CommandLine makeCommandLineArguments(final File fasta_file, SequenceType alignment_sequence_type) throws Exception;
    
    /**
     * Responsible for returning a new DataCell for the KNIME output table from the output of the alignment program. Called
     * during runAlignmentProgram() so if you override that there is no need to do anything with this method. But the local alignment
     * nodes (muscle, mafft) use this to process the stdout of the local process into the alignment cell.
     * 
     * @param tsv the aligned output
     * @param st the type of alignment sequences (AA or NA)
     * @param row_id the row_id of the current row (which may be a little bogus if all sequence rows are being aligned at once)
     */
    public abstract DataCell makeAlignmentCellAndPopulateResultsMap(final LogOutputStream tsv, 
			final SequenceType st, final String row_id) throws IOException;
    	
    /**
     * Called immediately prior to runAlignmentProgram() this method is responsible for ensuring that the sequence map is valid. Must
     * throw an exception if not.
     */
    public void validateSequencesToBeAligned(final Map<UniqueID, SequenceValue> seqs) throws InvalidSettingsException {
    	if (seqs == null || seqs.size() < 1)
    		throw new InvalidSettingsException("No sequences to align!");
    	
    	// ensure no two sequences have the same ID and that all sequences to be aligned are of the same type eg. all protein
        HashSet<String> dup_accsns = new HashSet<String>();
        SequenceType must_be = SequenceType.UNKNOWN;
        boolean is_first = true;
        for (SequenceValue sv : seqs.values()) {
      	  if (dup_accsns.contains(sv.getID())) {
      		  throw new InvalidSettingsException("Refusing to align two sequences with the same ID: "+sv.getID());
      	  }
      	  if (is_first) {
      		  must_be  = sv.getSequenceType();
      		  is_first = false;
      	  }
      	  if (!must_be.equals(sv.getSequenceType())) {
      		  throw new InvalidSettingsException("Refusing to align a mixture of sequences types: must be all AA or NA!");
      	  }
      	  dup_accsns.add(sv.getID());
        }
        dup_accsns = null;
        HashSet<UniqueID> dup_uid = new HashSet<UniqueID>();
        for (UniqueID uid : seqs.keySet()) {
        	if (dup_uid.contains(uid)) {
        		throw new InvalidSettingsException("Encountered duplicate short sequence ID! Programmer error! Should never happen!");
        	}
        }
        
        // log a warning in some cases so the user can fix their input data
        if (seqs.size() < 3) {
    		if (!warned_small_seqs) {
    			logger.warn("Too few sequences (less than three) for multiple sequence alignment: not recommended!");
    			warned_small_seqs = true;
    		}
    	}
    	logger.info("Aligning "+seqs.size()+ " "+must_be+" sequences.");
    }
    
	/**
     * Run muscle on the sequences specified in <code>seq_map</code> (which should be at least 3) using the specified muscle executable.
     * 
     * @param seq_map
     * @param muscle_exe	Existing executable for the muscle program
     * @param rowid			Used to help the user track down problems (by logging the rowid of the problem)
     * @param st			Type of sequences (AA, NA) to be aligned
     * @return
     */
    public DataCell runAlignmentProgram(final Map<UniqueID, SequenceValue> seq_map, final String rowid, final SequenceType st) {
    	File f = null;
		
		// run alignment program and return result if run went ok, else log errors
		try {
			f = File.createTempFile("output_aligner", ".fasta");
			FastaWriter fw = new FastaWriter(f, seq_map);
			fw.write();
			
			final DefaultExecutor exe = new DefaultExecutor();
	    	exe.setExitValues(new int[] {0});
	    	final LogOutputStream tsv = new LogOutputStream() {
	    		private StringBuffer sb = new StringBuffer(1024 * 1024);
	    		
				@Override
				protected void processLine(String arg0, int arg1) {
					if (arg0.startsWith(">")) {
						String id = arg0.substring(1);
						try {
							SequenceValue sv = seq_map.get(new UniqueID(id));
							sb.append(">"+sv.getID());
						} catch (InvalidSettingsException ise) {
							ise.printStackTrace();
							sb.append(arg0);
						}
					} else {
						sb.append(arg0);
					}
					sb.append("\n");
				}
	    		
				@Override
				public String toString() {
					return sb.toString();
				}
	    	};
	    	LogOutputStream errors = shouldLogAlignmentProgress() ? new ErrorLogger(logger, true) : new LogOutputStream() {

				@Override
				protected void processLine(String arg0, int arg1) {
				}
	    		
	    	};
	    	exe.setStreamHandler(new PumpStreamHandler(tsv, errors));
	    	exe.setWorkingDirectory(f.getParentFile());		// must match addQueryDatabase() semantics
	    	exe.setWatchdog(new ExecuteWatchdog(ExecuteWatchdog.INFINITE_TIMEOUT));
	    	
	    	CommandLine cmdLine = makeCommandLineArguments(f, st);
	    	exe.setWorkingDirectory(f.getParentFile());
	    	final String logname = getAlignmentLogName();
	    	
	    	int exitStatus = new ExecutorUtils(exe,logger).run(cmdLine);
	    	if (exe.isFailure(exitStatus)) {
	    		logger.error(logname+" failed to align sequences in row "+rowid+" - check console messages and input data");
	    		return DataType.getMissingCell();
	    	} 
	    	return makeAlignmentCellAndPopulateResultsMap(tsv, st, rowid);
		} catch (Exception e) {
			e.printStackTrace();
			return DataType.getMissingCell();
		} finally {
			if (f != null)
				f.delete();
		}
    }

}
