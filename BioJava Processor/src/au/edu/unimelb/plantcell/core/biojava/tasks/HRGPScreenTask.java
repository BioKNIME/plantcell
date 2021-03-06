package au.edu.unimelb.plantcell.core.biojava.tasks;

import java.util.ArrayList;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataType;
import org.knime.core.data.collection.CollectionCellFactory;
import org.knime.core.data.collection.ListCell;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.data.vector.bitvector.DenseBitVector;

import au.edu.unimelb.plantcell.core.cells.SequenceValue;

/**
 * Emits metrics to aid in identification of HRGP proteins. These metrics are aimed at computing
 * the %PAST, %P, %PSYK and %PVK within windows and identification of rich "regions" of these metrics.
 * the task provides highlighted regions and numeric metrics for subsequent use.
 * 
 * The key concept is that windows increase (from 8aa) as justified by %proline and the biased composition dictates.
 * But for a window to be accepted as a starting window it must contain at least 20% proline ie. 2 out of 8. And only then if
 * it satisfies one of %PAST, %PSYK or %PVK.
 * 
 * Acknowledgment: Carolyn Schultz from University of Adelaide, South Australia
 * @author andrew.cassin
 *
 */
public class HRGPScreenTask extends BioJavaProcessorTask {
	private int m_past, m_p, m_pvk, m_psyk;
	private int m_n_regions;
	private boolean want_all;
	
	@Override
	public void init(final String task_name, int input_column_index) throws Exception {
		super.init(task_name, input_column_index);
		want_all = (task_name.endsWith("(all)"));
	}
	
	@Override
	public String getCategory() {
		return "Protein Metrics";
	}
	
	@Override
	public String[] getNames() { 
		return new String[] { "Screen for Proline-rich HRGP-like proteins", "Add Proline rich metrics (all)" };
	}
	
	@Override
	public DataColumnSpec[] getColumnSpecs() {
		DataColumnSpec[] cols = new DataColumnSpec[6];
		cols[0] = new DataColumnSpecCreator("Region of interest (HTML)", StringCell.TYPE).createSpec();
		cols[1] = new DataColumnSpecCreator("Window lengths satisfying threshold", ListCell.getCollectionType(DoubleCell.TYPE)).createSpec();
		cols[2] = new DataColumnSpecCreator("RoI Coverage (%) of predicted protein", DoubleCell.TYPE).createSpec();
		cols[3] = new DataColumnSpecCreator("Number of distinct regions satisfying threshold", IntCell.TYPE).createSpec();
		cols[4] = new DataColumnSpecCreator("Window size (AA)", IntCell.TYPE).createSpec();
		cols[5] = new DataColumnSpecCreator("Windows satisfying threshold (list)", ListCell.getCollectionType(StringCell.TYPE)).createSpec();
		return cols;
	}
	
	@Override
	public String getHTMLDescription(String task) {
		return "<html>Adds metrics to aid in identification of proteins related to HRGP's (incl. Extensins, AGPs etc.)."+
	            "Requires protein sequence. The 'screen' task only reports values if %RoI is at least 10% (otherwise missing), the 'add metrics'" +
	            "task reports values for all input sequences. The screen task is more space efficient for really large numbers of sequences.";
	}
	
	/**
	 * Responsible for computing the results for the given row. Changes to this must be compatible with the KNIME platform ie. must match
	 * the column specification as given by <code>getColumnSpecs()</code>. Performance is important for working with 1kp...
	 */
	@Override
	public DataCell[] getCells(DataRow r) {
		SequenceValue sv = getSequenceForRow(r);
		DataCell[] cells = missing_cells(getColumnSpecs().length);
		if (!sv.getSequenceType().isProtein())
			return cells;
		
		// compute windows
		int len = sv.getLength();
		int window_size = 8;
		if (len < window_size)		// not even a single full window available?
			return cells;
		
		DenseBitVector bv = new DenseBitVector(len);
		boolean got_window_start = false;
		int start_pos = -1;
		
		int n_windows = 0;
		String seq = sv.getStringValue();
		for (int i=0; i<len-window_size+1; i++) {
			if (!got_window_start) {
				String window = seq.substring(i, i+window_size);
				compute_window(window);	// updates m_*
				
				if (accept_window(window_size)) {
					got_window_start = true;
					n_windows++;
					start_pos = i;
					bv.set(i,i+window_size);
				}
				// reject current window and keep going...
			} else {
				String window = seq.substring(start_pos, i+window_size);
				compute_window(window);	// updates m_*
				
				if (accept_window(i+window_size-start_pos)) {
					bv.set(i, i+window_size);
				} else {
					got_window_start = false;
					start_pos = -1;
				}
			}
		}

		// to save disk space and speed calculation we just output results only if at least one window found
		// Update 20130603: following advice from Carolyn, reject all %RoI < 10% to avoid false positives
		double percent_roi = ((double)bv.cardinality()) / len * 100.0d;
		if (n_windows > 0 && (want_all || percent_roi >= 10.0)) {
			cells[0] = getHTMLCell(seq, bv);
			getWindows(seq, bv, cells);		// side effects: m_n_windows and cells[1] and cells[5]
			cells[2] = new DoubleCell(percent_roi);
			cells[3] = new IntCell(m_n_regions);
			cells[4] = new IntCell(window_size);
		} 
		return cells;
	}
	
	/**
	 * Process the set of "rich regions" denoted by set bits in <code>bv</code> and extract the
	 * length and polypeptide sequence from each into the output cells for the plugin.
	 * 
	 * @param seq
	 * @param bv     one bits in this vector denote the proline rich regions. Must have at least <code>seq.length()</code> bits in the vector
	 * @param cells  some elements (which must have a size of at least <code>getColumnSpecs().length/code>) are side-effected by this call
	 */
	private void getWindows(final String seq, final DenseBitVector bv, DataCell[] cells) {
		ArrayList<IntCell>    col = new ArrayList<IntCell>();
		ArrayList<StringCell> ret = new ArrayList<StringCell>();
		long start = 0;
		while ((start = bv.nextSetBit(start)) >= 0) {
			long end = bv.nextClearBit(start+1);
	
			if (end < 0) {
				col.add(new IntCell((int)(bv.length()-start)));
				ret.add(new StringCell(seq.substring((int) start)));
				break;	// last region in input so thats it...
			} else {
				col.add(new IntCell((int)(end-start)));
				ret.add(new StringCell(seq.substring((int) start, (int) end)));
				start = end;
			}
		}
		
		// no windows? cells already contains missing values so just return
		if (col.size() < 1)
			return;
		
		// else...
		m_n_regions = col.size();
		cells[1] = CollectionCellFactory.createListCell(col);
		cells[5] = CollectionCellFactory.createListCell(ret);
	}

	private DataCell getHTMLCell(String seq, DenseBitVector bv) {
		if (seq == null || bv == null || seq.length() != bv.length())
			return DataType.getMissingCell();
		StringBuilder sb = new StringBuilder(seq.length());
		sb.append("<html>");
		for (int i=0; i<seq.length(); i++) {
			char c = seq.charAt(i);
			if (bv.get(i)) {
				sb.append("<font color=\"blue\">");
				sb.append(c);
				sb.append("</font>");
			} else {
				sb.append(c);
			}
		}
		
		return new StringCell(sb.toString());
	}

	/**
	 * The current window will be accepted for exactly one of three reasons:
	 * 1) based on <code>window_size</code> the %PAST is >= 70 with %P at least 20%
	 * 2) based on <code>window_size</code> the %PSYK is >= 70 with %P at least 20%
	 * 3) based on <code>window_size</code> the %PVK is >= 70 with %P at least 20%
	 * Otherwise false is returned which will cause a search for later windows meeting the above criteria.
	 * 
	 * @param window_size the size of the current window in number of AA - used for % calculations
	 * @return
	 */
	private boolean accept_window(int window_size) {
		double percent_p = ((double)m_p)/window_size * 100;
		if (percent_p < 20.0d)
			return false;
		
		double percent_past = ((double)m_past)/window_size * 100.0d;
		if (percent_past >= 70.0d && percent_p >= 20.0d) {
			return true;
		} 
		double percent_psyk = ((double)m_psyk)/window_size * 100.0d;
		if (percent_psyk >= 70.0d && percent_p >= 20.0d) {
			return true;
		}
		double percent_pvk = ((double)m_pvk)/window_size * 100.0d;
		if (percent_pvk >= 70.0d && percent_p >= 20.0d)
			return true;
		return false;
	}

	/**
	 * Dumb implementation as it keeps recalculating the same residues... but for now...
	 * WARNING: this method alters m_{past,p,pvk,psyk} so be careful when you call this.
	 * 
	 * @param window
	 */
	private void compute_window(String window) {
		assert(window != null && window.length() > 0);
		m_past = 0;
		m_p    = 0;
		m_pvk  = 0;
		m_psyk = 0;
		for (int j=0; j<window.length(); j++) {
			char c = window.charAt(j);
			if (c == 'P') {
				m_past++;
				m_p++;
				m_pvk++;
				m_psyk++;
				continue;
			}
			if (c == 'A' || c == 'S' || c == 'T')
				m_past++;
			
			if (c == 'V' || c == 'K')
				m_pvk++;
			if (c == 'S' || c == 'Y' || c == 'K')
				m_psyk++;
		}
	}

}
