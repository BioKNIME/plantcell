package au.edu.unimelb.plantcell.algorithms.StringFinder;

import java.util.List;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataType;
import org.knime.core.data.vector.bitvector.DenseBitVector;
import org.knime.core.data.vector.bitvector.DenseBitVectorCellFactory;

public class MatchDensityPositionReporter implements MatchReporter {
	public MatchDensityPositionReporter() {
		
	}
	
	@Override
	public DataCell report(FindGlobalNodeModel m, DataCell str_cell)
			throws Exception {
		List<Extent> match_pos = m.getMatchPos();
		if (match_pos == null)
			return DataType.getMissingCell();
		DenseBitVector bv = new DenseBitVector(str_cell.toString().length());
		for (Extent e : match_pos) {
			bv.set(e.m_start, e.m_end);
		}
		DenseBitVectorCellFactory f = new DenseBitVectorCellFactory(bv);
		return f.createDataCell();
	}

}
