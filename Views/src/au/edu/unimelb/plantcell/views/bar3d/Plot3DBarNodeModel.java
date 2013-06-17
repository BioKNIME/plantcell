package au.edu.unimelb.plantcell.views.bar3d;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.lang.ArrayUtils;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Statistics;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;


/**
 * This is the model implementation of Plot3DBar.
 * Using jzy3d, this node produces a 3d bar plot using the supplied input columns.
 *
 * @author http://www.plantcell.unimelb.edu.au/bioinformatics
 */
public class Plot3DBarNodeModel extends NodeModel {
    
    // the logger instance
    private static final NodeLogger logger = NodeLogger.getLogger("3D Bar Plot");
       
    static final String CFGKEY_X = "x";
    static final String CFGKEY_Y = "y";
    static final String CFGKEY_Z = "z";
    static final String CFGKEY_QUALITY = "plot-quality";
    
    private final SettingsModelString m_x = new SettingsModelString(CFGKEY_X, "");
    private final SettingsModelString m_y = new SettingsModelString(CFGKEY_Y, "");
    private final SettingsModelString m_z = new SettingsModelString(CFGKEY_Z, "");
    private final SettingsModelString m_quality = new SettingsModelString(CFGKEY_QUALITY, "Nicest");
    
    // these are all parallel vectors ie. must be the same size
    private double[] m_xvec = null;
    private double[] m_yvec = null;
    private double[] m_zvec = null;
    private String[] m_rows = null;		// row ID's used for highlighting
    private Color[] m_colours = null;	// row colours used for the graph
    
    /**
     * Constructor for the node model.
     */
    protected Plot3DBarNodeModel() {
        super(1, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {

    	logger.info(m_x.getStringValue());
    	int x_idx = inData[0].getSpec().findColumnIndex(m_x.getStringValue());
    	int y_idx = inData[0].getSpec().findColumnIndex(m_y.getStringValue());
    	int z_idx = inData[0].getSpec().findColumnIndex(m_z.getStringValue());
    	if (x_idx < 0 || y_idx < 0 || z_idx < 0)
    		throw new InvalidSettingsException("Cannot find input columns - re-configure?");
    	
    	logger.info("Loading "+inData[0].getRowCount()+" data points.");
    	int missing = 0;
    	int bad = 0;
    	int n_rows = inData[0].getRowCount();
    	ArrayList<Double> xvec = new ArrayList<Double>(n_rows);
    	ArrayList<Double> yvec = new ArrayList<Double>(n_rows);
    	ArrayList<Double> zvec = new ArrayList<Double>(n_rows);
    	ArrayList<String> rowids = new ArrayList<String>(n_rows);
    	ArrayList<Color> colours = new ArrayList<Color>(n_rows);
    	for (DataRow r : inData[0]) {
    		DataCell x_cell = r.getCell(x_idx);
    		DataCell y_cell = r.getCell(y_idx);
    		DataCell z_cell = r.getCell(z_idx);
    		if (x_cell == null || y_cell == null || z_cell == null || x_cell.isMissing() || y_cell.isMissing() || z_cell.isMissing()) {
    			missing++;
    			continue;
    		}
    		
    		try {
    			xvec.add(Double.valueOf(x_cell.toString()));
    			yvec.add(Double.valueOf(y_cell.toString()));
    			zvec.add(Double.valueOf(z_cell.toString()));
    			rowids.add(r.getKey().getString());
    			java.awt.Color c_awt = inData[0].getSpec().getRowColor(r).getColor();
    			colours.add(c_awt != null ? new Color(c_awt.getRed(), c_awt.getGreen(), c_awt.getBlue()) : Color.BLACK);
    		} catch (NumberFormatException nfe) {
    			bad++;
    		}
    	}
    	m_xvec = ArrayUtils.toPrimitive(xvec.toArray(new Double[0]));
    	m_yvec = ArrayUtils.toPrimitive(yvec.toArray(new Double[0]));
    	m_zvec = ArrayUtils.toPrimitive(zvec.toArray(new Double[0]));
    	m_rows = rowids.toArray(new String[0]);
    	m_colours = colours.toArray(new Color[0]);
    	
    	double min = Statistics.min(m_xvec);
    	double max = Statistics.max(m_xvec);
    	logger.info("X min="+min+" max="+max);
    	min = Statistics.min(m_yvec);
    	max = Statistics.max(m_yvec);
    	logger.info("Y min="+min+" max="+max);
    	min = Statistics.min(m_zvec);
    	max = Statistics.max(m_zvec);
    	logger.info("Z min="+min+" max="+max);
    	
    	if (missing > 0) 
    		logger.warn("Some "+missing+" datapoints (rows) contain missing values, they will not be considered.");
    	if (bad > 0) 
    		logger.warn("Some "+bad+" datapoints (rows) contain invalid numeric data - they will not be considered.");
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        m_rows = null;
        m_xvec = null;
        m_yvec = null;
        m_zvec = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
            throws InvalidSettingsException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
    	m_x.saveSettingsTo(settings);
    	m_y.saveSettingsTo(settings);
    	m_z.saveSettingsTo(settings);
    	m_quality.saveSettingsTo(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	m_x.loadSettingsFrom(settings);
    	m_y.loadSettingsFrom(settings);
    	m_z.loadSettingsFrom(settings);
    	m_quality.loadSettingsFrom(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        m_x.validateSettings(settings);
        m_y.validateSettings(settings);
        m_z.validateSettings(settings);
        m_quality.validateSettings(settings);
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
     * Returns true if there is something to plot and the internal state looks valid
     * @return
     */
	public boolean hasDataPoints() {
		if (m_xvec == null)
			return false;
		return (m_xvec.length > 0 && m_xvec.length == m_yvec.length && 
				m_xvec.length == m_zvec.length && m_xvec.length == m_rows.length);
	}

	/**
	 * Return the set of 3d points (which must be pre-sized to at least countDataPoints() in length)
	 * @param x
	 * @param y
	 * @param z
	 */
	public void getDataPoints(double[] x, double[] y, double[] z, String[] rowids, Color[] colours) {
		assert(x != null && y != null && z != null);
		
		for (int i=0; i<x.length; i++) {
			x[i] = m_xvec[i];
			y[i] = m_yvec[i];
			z[i] = m_zvec[i];
			rowids[i] = m_rows[i];
			colours[i]= m_colours[i];
		}
	}
	
	public int countDataPoints() {
		return m_xvec.length;
	}

	public String getQuality() {
		return m_quality.getStringValue();
	}

}
