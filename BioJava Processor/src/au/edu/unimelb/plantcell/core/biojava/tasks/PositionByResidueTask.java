package au.edu.unimelb.plantcell.core.biojava.tasks;

import java.util.ArrayList;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.collection.CollectionCellFactory;
import org.knime.core.data.collection.ListCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.InvalidSettingsException;

import au.edu.unimelb.plantcell.core.cells.SequenceValue;
import au.edu.unimelb.plantcell.misc.biojava.TaskParameter;

/**
 * Adds columns for each position (up to the specified maximum length in the constructor) 
 * with a cell for each residue in each column. Each cells contains the number of residues at that
 * position. Positions are numbered from 1 to correspond to what biologists expect ;-)
 * 
 * @author andrew.cassin
 *
 */
public class PositionByResidueTask extends BioJavaProcessorTask {
	private int m_maxlen;
	
	public PositionByResidueTask() {
	}
	
	@Override
	public String getCategory() {
		return "Common";
	}
	
	public static BioJavaProcessorTask getInstance() {
		return new PositionByResidueTask();
	}
	
	@Override
	public boolean canWindow() {
		return true;
	}
	
	@Override
	public void init(String task, int col) throws Exception {
		super.init(task, col);
		m_maxlen = 75;
		TaskParameter maxlen_tp = getParameter("Maximum Sequence Length", "75");
		Integer i_maxlen = new Integer(maxlen_tp.getValue());
		if (i_maxlen.intValue() < 1) {
			throw new InvalidSettingsException("Invalid maximum sequence length (must be at least one!)");
		}
		m_maxlen = i_maxlen.intValue();
	}
	
	/** {@inheritDoc} */
	@Override
	public String[] getNames() {
		return new String[] { "Residue Frequency by Position" };
	}

	/** {@inheritDoc} */
	@Override
	public String getHTMLDescription(String task) {
		return "<html>Outputs a table of data: residue (either DNA+IUPAC codes or AA) by position up to " +
				"the specified maximum length of sequence data. If not all sequences are the "+
				"same length, you'll need to be careful to interpret the raw counts. " +
				"By default, only the first <b>75</b> residues are counted, but you can change that with the advanced settings tab in the configure dialog.";
	}
	
	@Override
	public DataCell[] getCells(DataRow row) {
		ResidueByPosition[] pos = new ResidueByPosition[m_maxlen];
		for (int i=0; i<m_maxlen; i++) {
			pos[i] = new ResidueByPosition(i+1);
		}
		
		// scan the sequences -- speed is key here
		SequenceValue sv = getSequenceForRow(row);
		if (sv == null) 
			return missing_cells(m_maxlen);
		
		int len = m_maxlen;
		if (sv.getLength() < m_maxlen)
			len = sv.getLength();
		String seq = sv.getStringValue().toUpperCase();
		for (int i=0; i<len; i++ ) {
			pos[i].bump(seq.charAt(i));
		}
		
		// build the output table...
		char[] letters = pos[0].getResidueLetters();
		DataCell[] pos_cells = new DataCell[m_maxlen];
		for (int j=0; j<m_maxlen; j++) {
			ArrayList<StringCell> out = new ArrayList<StringCell>();
			for (int i=0; i<letters.length; i++) {
				int cnt = pos[j].count(letters[i]);
				if (cnt > 0) {
					out.add(new StringCell(letters[i]+"="+cnt));
				}
			}
			pos_cells[j] = CollectionCellFactory.createListCell(out);
		}
		
		return pos_cells;
	}

	@Override
	public DataColumnSpec[] getColumnSpecs() {
		assert(m_maxlen > 0);
		DataColumnSpec[] cols = new DataColumnSpec[m_maxlen];
		for (int i=0; i<m_maxlen; i++) {
			cols[i] = new DataColumnSpecCreator("Position "+new Integer(i+1).toString(), ListCell.getCollectionType(StringCell.TYPE)).createSpec();
		}
		return cols;
	}

	public class ResidueByPosition {
		private final char[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',  
				'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
		private int[]  count;
		
		public ResidueByPosition(int i) {
			count = new int[] {0, 0, 0, 0, 0, 0,
					 0, 0, 0, 0, 0, 0,
					 0, 0, 0, 0, 0, 0,
					 0, 0, 0, 0, 0, 0, 0, 0};	
		}
		
		public char[] getResidueLetters() {
			return letters;
		}
	
		public void bump(char c) {
			assert(c >= 'A' && c <= 'Z');
			int offset = c - 'A';
			count[offset]++;
		}

		public int count(char c) {
			assert(c >= 'A' && c <= 'Z');
			int offset = c - 'A';
			return count[offset];
		}
	}

}
