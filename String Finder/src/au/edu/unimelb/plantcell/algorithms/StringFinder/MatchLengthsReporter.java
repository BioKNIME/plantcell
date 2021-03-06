package au.edu.unimelb.plantcell.algorithms.StringFinder;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataType;
import org.knime.core.data.collection.CollectionCellFactory;
import org.knime.core.data.def.IntCell;

public class MatchLengthsReporter implements MatchReporter {

	public MatchLengthsReporter() {
	}
	
	@Override
	public DataCell report(FindGlobalNodeModel m, DataCell str_cell)
			throws Exception {
		List<Extent> match_pos = m.getMatchPos();
		if (match_pos == null)
			return DataType.getMissingCell();
		ArrayList<IntCell> vec  = new ArrayList<IntCell>();
		for (Extent e : match_pos) {
			vec.add(new IntCell(e.m_end - e.m_start));
		}
		return CollectionCellFactory.createListCell(vec);
	}

}
