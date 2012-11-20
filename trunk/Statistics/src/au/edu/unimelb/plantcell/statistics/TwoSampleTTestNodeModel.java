package au.edu.unimelb.plantcell.statistics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.math3.stat.inference.TestUtils;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DoubleValue;
import org.knime.core.data.RowIterator;
import org.knime.core.data.def.BooleanCell;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelDouble;
import org.knime.core.node.defaultnodesettings.SettingsModelFilterString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;


/**
 * This is the model implementation of Ranker.
 * Basic descriptive and inferential statistics support built using the apache commons math v3 library.
 *
 * @author http://www.plantcell.unimelb.edu.au/bioinformatics
 */
public class TwoSampleTTestNodeModel extends NodeModel {
    
    // the logger instance
    private static final NodeLogger logger = NodeLogger.getLogger("2sample t-Test");
            
    public static final String CFGKEY_ALPHA = "significance-level";
    public static final String CFGKEY_FROM  = "data-from";
    public static final String CFGKEY_COLUMN_CLASSES = "column-class-names";
    public static final String CFGKEY_PAIRED = "paired?";
    public static final String CFGKEY_EQUAL_VARIANCE = "assume-equal-variance?";
    

    private SettingsModelDouble m_alpha         = new SettingsModelDouble(CFGKEY_ALPHA, 0.05);
    private SettingsModelString m_from          = new SettingsModelString(CFGKEY_FROM, "columns");
    private SettingsModelFilterString m_columns = new SettingsModelFilterString(CFGKEY_COLUMN_CLASSES);
    private SettingsModelBoolean m_paired = new SettingsModelBoolean(CFGKEY_PAIRED, Boolean.TRUE);
    private SettingsModelBoolean m_equal_var = new SettingsModelBoolean(CFGKEY_EQUAL_VARIANCE, Boolean.FALSE);

    
    /**
     * Constructor for the node model.
     */
    protected TwoSampleTTestNodeModel() {
        super(1, 1);
    }

    /**
     * {@inheritDoc}
     */
	@Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {

    	Map<Integer, String> wanted_set = new HashMap<Integer,String>();
    	for (String colName : m_columns.getIncludeList()) {
    		int idx = inData[0].getSpec().findColumnIndex(colName);
    		if (idx < 0) {
    			throw new InvalidSettingsException("Unable to locate: "+colName+" - re-configure the node?");
    		}
    		wanted_set.put(new Integer(idx), colName);
    	}
    	
    	BufferedDataContainer c = exec.createDataContainer(make_output_spec(inData[0].getSpec()));
    	Map<Integer,ArrayList<Double>> samples = new HashMap<Integer,ArrayList<Double>>();
    	RowIterator it = inData[0].iterator();
    	Integer[] wanted = wanted_set.keySet().toArray(new Integer[0]);
    	
    	while (it.hasNext()) {
    		DataRow r = it.next();
    		for (Integer w : wanted) {
    			DataCell cell = r.getCell(w.intValue());
    			if (cell == null || cell.isMissing()) {
    				continue;
    			} 
    			DoubleValue dv = (DoubleValue) cell;
    			if (Double.isNaN(dv.getDoubleValue()))
    				continue;
    			ArrayList<Double> values = samples.get(w);
    			if (values == null) {
    				values = new ArrayList<Double>();
    				samples.put(w, values);
    			}
    			values.add(new Double(dv.getDoubleValue()));
    		}
    	}
    	
    	int id = 1;
    	for (Integer idx : wanted_set.keySet()) {
    		List<Double> l = samples.get(idx);
			logger.info("Sample "+wanted_set.get(idx)+" has "+l.size()+" valid data values.");
		}
    	
    	List<Integer> vec = new ArrayList<Integer>();
    	vec.addAll(wanted_set.keySet());
    	
    	for (Integer i : vec) {
    		for (Integer j : vec) {
    			if (!i.equals(j) && j < i) {
    				double[] i_array = ArrayUtils.toPrimitive(samples.get(i).toArray(new Double[0]));
    				double[] j_array = ArrayUtils.toPrimitive(samples.get(j).toArray(new Double[0]));
    				String s1 = wanted_set.get(i);
    				String s2 = wanted_set.get(j);
    				if (i_array.length != j_array.length && m_paired.getBooleanValue()) {
    					logger.warn("Skipping pair: "+s1+"-"+s2+", as lengths not equal");
    					continue;
    				}
    				DataCell[] cells = do_t(s1, s2, i_array, j_array, exec);
    				c.addRowToTable(new DefaultRow("TwoSample"+id++, cells));
    			}
    		}
    	}
        c.close();
        BufferedDataTable out = c.getTable();
        return new BufferedDataTable[]{out};
    }

    
	private DataCell[] do_t(String s1, String s2, double[] i_array, double[] j_array,
			ExecutionContext exec) {
		DataCell[] cells = new DataCell[5];
		cells[0] = new StringCell(s1);
		cells[1] = new StringCell(s2);
		if (m_equal_var.getBooleanValue()) {
			cells[2] = new DoubleCell(TestUtils.homoscedasticT(i_array, j_array));
			cells[3] = new DoubleCell(TestUtils.homoscedasticTTest(i_array, j_array));
			cells[4] = TestUtils.homoscedasticTTest(i_array, j_array, m_alpha.getDoubleValue()) ? BooleanCell.TRUE : BooleanCell.FALSE;
		} else {
			if (m_paired.getBooleanValue()) {
				cells[2] = new DoubleCell(TestUtils.pairedT(i_array, j_array));
				cells[3] = new DoubleCell(TestUtils.pairedTTest(i_array, j_array));
				cells[4] = TestUtils.pairedTTest(i_array, j_array, m_alpha.getDoubleValue()) ? BooleanCell.TRUE : BooleanCell.FALSE;
			} else {
				cells[2] = new DoubleCell(TestUtils.t(i_array, j_array));
				cells[3] = new DoubleCell(TestUtils.tTest(i_array, j_array));
				cells[4] = TestUtils.tTest(i_array, j_array, m_alpha.getDoubleValue()) ? BooleanCell.TRUE : BooleanCell.FALSE;
			}
		}
		return cells;
	}

	private DataTableSpec make_output_spec(DataTableSpec inSpec) {
    	DataColumnSpec[] cols = new DataColumnSpec[5];
    	
    	cols[0] = new DataColumnSpecCreator("TT2: sample 1", StringCell.TYPE).createSpec();
    	cols[1] = new DataColumnSpecCreator("TT2: sample 2", StringCell.TYPE).createSpec();
    	cols[2] = new DataColumnSpecCreator("TT2: T-Statistic", DoubleCell.TYPE).createSpec();
    	cols[3] = new DataColumnSpecCreator("TT2: P-Value (two-sided)", DoubleCell.TYPE).createSpec();
    	cols[4] = new DataColumnSpecCreator("TT2: Null hypothesis (two-sided) rejected at "+m_alpha.getDoubleValue()+"?", BooleanCell.TYPE).createSpec();

		return new DataTableSpec(cols);
	}

	/**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
            throws InvalidSettingsException {
    	return new DataTableSpec[] { make_output_spec(inSpecs[0]) };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
    	m_alpha.saveSettingsTo(settings);
    	m_from.saveSettingsTo(settings);
    	m_columns.saveSettingsTo(settings);
    	m_paired.saveSettingsTo(settings);
    	m_equal_var.saveSettingsTo(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	m_alpha.loadSettingsFrom(settings);
    	m_from.loadSettingsFrom(settings);
    	m_columns.loadSettingsFrom(settings);
    	m_paired.loadSettingsFrom(settings);
    	m_equal_var.loadSettingsFrom(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	m_alpha.validateSettings(settings);
    	m_from.validateSettings(settings);
    	m_columns.validateSettings(settings);
    	m_paired.validateSettings(settings);
    	m_equal_var.validateSettings(settings);
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

}

