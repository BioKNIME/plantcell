package au.edu.unimelb.plantcell.misc.biojava;

import org.biojava.bio.seq.DNATools;
import org.biojava.bio.seq.RNATools;
import org.biojava.bio.symbol.SymbolList;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.InvalidSettingsException;

import au.edu.unimelb.plantcell.core.cells.SequenceValue;


public class SequenceTranslationProcessor extends BioJavaProcessorTask {
	private boolean m_convert_dna2prot;
	private boolean m_convert_rna2prot;
	private boolean m_convert_dna2rna;
	private int m_col;
	
	public SequenceTranslationProcessor() {
	}
	
	@Override
	public String getCategory() {
		return "Translation";
	}
	
	public static BioJavaProcessorTask getInstance() {
		return new SequenceTranslationProcessor();
	}
	
	public void init(BioJavaProcessorNodeModel m, String task, int col) {
		m_convert_dna2prot = false;
		m_convert_rna2prot = false;
		m_convert_dna2rna  = false;
		m_col = col;
		task = task.toLowerCase().trim();
		if (task.endsWith("dna to protein sequence")) {
			m_convert_dna2prot = true;
		} else if (task.endsWith("rna to protein sequence")) {
			m_convert_rna2prot = true;
		} else {
			m_convert_dna2rna = true;
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public String[] getNames() {
		 return new String[] { "Convert DNA to RNA (Universal translation only)", 
	        "Convert RNA to Protein Sequence", 
	        "Convert DNA to Protein Sequence" };
	}
	
	/** {@inheritDoc} */
	@Override
	public String getHTMLDescription(String task) {
		return "<html>Computes a translation of sequence from DNA->RNA, RNA->AA or DNA->AA based "+
		"based on the node configuration. Currently, back translation from AA to DNA is not supported by this node.";
	}
	
	public DataCell[] getCells(DataRow row) {
		DataCell c = row.getCell(m_col);
		if (c == null || c.isMissing() || !(c instanceof SequenceValue))
			return missing_cells(getColumnSpecs().length);
		SequenceValue sv = (SequenceValue) c;
		
		// skip missing sequences -- TODO: should we put into output table?
		if (sv.getLength() < 1)
			return missing_cells(getColumnSpecs().length);
		SymbolList sl;
		try {
			sl = asBioJava(sv);
		
			String seq;
			
			if (m_convert_dna2rna) {
				// convert DNA sequence to RNA
				// ensure multiple of 3 (trim excess)
				if (sl.length() % 3 != 0) {
					sl = sl.subList(1, sl.length() - (sl.length() % 3));
				}
				SymbolList rna = DNATools.toRNA(sl);
				seq = rna.seqString();
			} else if (m_convert_rna2prot){
				// convert RNA to protein
				// ensure multiple of 3 (trim excess)
				if (sl.length() % 3 != 0) {
					sl = sl.subList(1, sl.length() - (sl.length() % 3));
				}
				seq = RNATools.translate(sl).seqString();
			} else if (m_convert_dna2prot) {
				sl = DNATools.toRNA(sl);
				// ensure multiple of 3 (trim excess)
				if (sl.length() % 3 != 0) {
					sl = sl.subList(1, sl.length() - (sl.length() % 3));
				}
				SymbolList prot = RNATools.translate(sl);
				seq = prot.seqString();
			} else {
				throw new InvalidSettingsException("Unknown conversion -- implementation bug!");
			}
			DataCell[] cells = new DataCell[1];
			cells[0] = new StringCell(seq);
			return cells;
		} catch (Exception e) {
			e.printStackTrace();
			return missing_cells(getColumnSpecs().length);
		}
	}

	public DataColumnSpec[] getColumnSpecs() {
		DataColumnSpec[] allColSpecs = new DataColumnSpec[1];
        allColSpecs[0] = 
            new DataColumnSpecCreator("Converted Sequence", StringCell.TYPE).createSpec();
        return allColSpecs;
	}

}