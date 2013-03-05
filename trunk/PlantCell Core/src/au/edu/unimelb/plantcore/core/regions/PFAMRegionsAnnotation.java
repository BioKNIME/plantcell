package au.edu.unimelb.plantcore.core.regions;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;
import java.util.Map;

import org.knime.core.data.DataColumnSpec;

import au.edu.unimelb.plantcell.core.cells.AnnotationType;
import au.edu.unimelb.plantcell.core.cells.SequenceValue;
import au.edu.unimelb.plantcell.core.cells.Track;

/**
 * PFAM models align to the protein sequence in a similar way to BLAST, so we
 * derive from that annotation class. An instance of this expects all regions contained in it
 * to be derived from <code>PFAMHitRegion</code>
 * 
 * @author andrew.cassin
 *
 */
public class PFAMRegionsAnnotation extends AlignedRegionsAnnotation {
	@Override
	public AnnotationType getAnnotationType() {
		return AnnotationType.PFAM_REGIONS;
	}
	
	@Override 
	public List<DataColumnSpec> asColumnSpec(String prefix) {
		List<DataColumnSpec> cols = super.asColumnSpec(prefix);
		DataColumnSpec found1 = null;
		DataColumnSpec found2 = null;
		// PFAM has no notion of a frame or %identity for the HMM match, so we remove them
		for (DataColumnSpec col: cols) {
			String name = col.getName();
			if (name.endsWith(": Frame")) {
				found1 = col;
			} else if (name.endsWith(": % identity")) {
				found2 = col;
			}
		}
		if (found1 != null) {
			cols.remove(found1);
		}
		if (found2 != null) {
			cols.remove(found2);
		}
		// add PFAM-specific columns eg. HMM related match information
		// TODO...
		return cols;
	}
	
	@Override
	public Dimension paintTrack(Map<String, Integer> props, Graphics g,
			SequenceValue sv, Track t) {
		
		// for now we just draw as per BLAST, but this will change soon...
		return super.paintTrack(props, g, sv, t);
	}
}
