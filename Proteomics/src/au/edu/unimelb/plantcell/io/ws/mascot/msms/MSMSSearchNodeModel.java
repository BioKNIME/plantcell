package au.edu.unimelb.plantcell.io.ws.mascot.msms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.soap.MTOMFeature;
import javax.xml.ws.soap.SOAPBinding;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataRow;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;

import au.edu.unimelb.plantcell.io.read.mascot.MascotReaderNodeModel;
import au.edu.unimelb.plantcell.io.read.spectra.SpectraValue;
import au.edu.unimelb.plantcell.io.write.spectra.EmptyPeakListHandler;
import au.edu.unimelb.plantcell.io.write.spectra.SpectraWriterNodeModel;
import au.edu.unimelb.plantcell.servers.mascotee.endpoints.SearchService;
import au.edu.unimelb.plantcell.servers.mascotee.jaxb.Constraints;
import au.edu.unimelb.plantcell.servers.mascotee.jaxb.Data;
import au.edu.unimelb.plantcell.servers.mascotee.jaxb.Identification;
import au.edu.unimelb.plantcell.servers.mascotee.jaxb.KeyParameters;
import au.edu.unimelb.plantcell.servers.mascotee.jaxb.MSMSTolerance;
import au.edu.unimelb.plantcell.servers.mascotee.jaxb.MsMsIonSearch;
import au.edu.unimelb.plantcell.servers.mascotee.jaxb.ObjectFactory;
import au.edu.unimelb.plantcell.servers.mascotee.jaxb.PeptideTolerance;
import au.edu.unimelb.plantcell.servers.mascotee.jaxb.Quantitation;
import au.edu.unimelb.plantcell.servers.mascotee.jaxb.Reporting;
import au.edu.unimelb.plantcell.servers.mascotee.jaxb.Search;


/**
 * This is the model implementation of DatFileDownload.
 * Permits downloading of Mascot DAT files via a JAX-WS web service and will load each dat file into the output table as per the mascot reader. The node also saves the DAT files to the user-specified folder.
 *
 * @author http://www.plantcell.unimelb.edu.au/bioinformatics
 */
public class MSMSSearchNodeModel extends MascotReaderNodeModel {
	private static final NodeLogger logger = NodeLogger.getLogger("MS/MS Ion Search");
	
	public final static QName SEARCH_NAMESPACE = 
			new QName("http://www.plantcell.unimelb.edu.au/bioinformatics/wsdl", "SearchService");
	public final static QName CONFIG_NAMESPACE = 
			new QName("http://www.plantcell.unimelb.edu.au/bioinformatics/wsdl", "ConfigService");
	
	public final static String CFGKEY_MASCOTEE_URL = "mascotee-url";
	public final static String CFGKEY_DATA_SOURCE  = "data-source";
	public final static String CFGKEY_DATA_COLUMN  = "input-data-column";
	// mascot ms/ms ion search parameters
	public final static String CFGKEY_USERNAME     = "mascot-username";
	public final static String CFGKEY_EMAIL        = "mascot-email";
	public final static String CFGKEY_TITLE        = "mascot-job-title";
	public final static String CFGKEY_DATABASE     = "mascot-database";
	public final static String CFGKEY_FIXED_MODS   = "mascot-fixed-modifications";
	public final static String CFGKEY_VARIABLE_MODS= "mascot-variable-modifications";
	public final static String CFGKEY_MASSTYPE     = "mascot-mass-measurement-type";
	public final static String CFGKEY_TAXONOMY     = "mascot-taxonomy";
	public final static String CFGKEY_ENZYME       = "mascot-enzyme";
	public final static String CFGKEY_MISSED_CLEAVAGES = "mascot-missed-cleavages";
	public final static String CFGKEY_ALLOWED_PROTEIN_MASS = "mascot-protein-mass-allowed";
	public final static String CFGKEY_PEPTIDE_TOL_VALUE = "mascot-peptide-tolerance-value";
	public final static String CFGKEY_PEPTIDE_TOL_UNITS = "mascot-peptide-tolerance-unit";
	public final static String CFGKEY_MSMS_TOL_VALUE = "mascot-msms-tolerance-value";
	public final static String CFGKEY_MSMS_TOL_UNITS = "mascot-msms-tolerance-unit";
	public final static String CFGKEY_PEPTIDE_CHARGE = "mascot-peptide-charge";
	public final static String CFGKEY_REPORT_OVERVIEW= "mascot-report-overview";
	public final static String CFGKEY_REPORT_TOP     = "mascot-report-top";
	public final static String CFGKEY_QUANT_ICAT     = "mascot-quant-icat";
	public final static String CFGKEY_INSTRUMENT     = "mascot-instrument";
	public final static String CFGKEY_PRECURSOR      = "mascot-precursor";
	public static final String CFGKEY_OUT_MGF        = "output-mgf-folder";
	public static final String CFGKEY_OUT_DAT        = "output-mascot-dat-file-folder";
	public final static String CFGKEY_MASCOTEE_USER  = "mascotee-username";
	public final static String CFGKEY_MASCOTEE_PASSWD= "mascotee-passwd";
	
	public final static String   DEFAULT_MASCOTEE_URL = "http://mascot.plantcell.unimelb.edu.au:8080/mascotee/";
	// order for DATA_SOURCES is critical: append new entries to the END so that existing code works (or modify everything ;-)
	public final static String[] DATA_SOURCES = new String[] { "from input file (select column)", "all input files combined (select column)", "from input MS/MS spectra (select column)" };

	
	
	private final SettingsModelString m_url = new SettingsModelString(CFGKEY_MASCOTEE_URL, DEFAULT_MASCOTEE_URL);
	private final SettingsModelString m_mascotee_username = new SettingsModelString(CFGKEY_MASCOTEE_USER, "");
	private final SettingsModelString m_mascotee_password = new SettingsModelString(CFGKEY_MASCOTEE_PASSWD, "");
	private final SettingsModelString m_data_source = new SettingsModelString(CFGKEY_DATA_SOURCE, DATA_SOURCES[0]);
	private final SettingsModelString m_column = new SettingsModelString(CFGKEY_DATA_COLUMN, "");
	private final SettingsModelString m_out_mgf = new SettingsModelString(CFGKEY_OUT_MGF, "");
	private final SettingsModelString m_out_dat = new SettingsModelString(CFGKEY_OUT_DAT, "");
	
	
	// mascot ms/ms ion search persisted state
	private final SettingsModelString m_user        = new SettingsModelString(CFGKEY_USERNAME, "");
	private final SettingsModelString m_email       = new SettingsModelString(CFGKEY_EMAIL, "");
	private final SettingsModelString m_job_title   = new SettingsModelString(CFGKEY_TITLE, "");
	private final SettingsModelString m_database    = new SettingsModelString(CFGKEY_DATABASE, "");
	private final SettingsModelStringArray m_fixed_mods = new SettingsModelStringArray(CFGKEY_FIXED_MODS, new String[] {});
	private final SettingsModelStringArray m_variable_mods = new SettingsModelStringArray(CFGKEY_VARIABLE_MODS, new String[] {});
	private final SettingsModelString m_mass_type = new SettingsModelString(CFGKEY_MASSTYPE, "Monoisotopic");
	private final SettingsModelString m_taxonomy  = new SettingsModelString(CFGKEY_TAXONOMY, "");
	private final SettingsModelString m_enzyme    = new SettingsModelString(CFGKEY_ENZYME, "");
	private final SettingsModelIntegerBounded m_missed_cleavages = new SettingsModelIntegerBounded(CFGKEY_MISSED_CLEAVAGES, 0, 0, 9);
	private final SettingsModelString m_protein_mass = new SettingsModelString(CFGKEY_ALLOWED_PROTEIN_MASS, "");
	private final SettingsModelString m_peptide_tolerance = new SettingsModelString(CFGKEY_PEPTIDE_TOL_VALUE, "");
	private final SettingsModelString m_peptide_tolerance_unit = new SettingsModelString(CFGKEY_PEPTIDE_TOL_UNITS, "Da");
	private final SettingsModelString m_msms_tolerance = new SettingsModelString(CFGKEY_MSMS_TOL_VALUE, "");
	private final SettingsModelString m_msms_tolerance_unit = new SettingsModelString(CFGKEY_MSMS_TOL_UNITS, "Da");
	private final SettingsModelString m_peptide_charge = new SettingsModelString(CFGKEY_PEPTIDE_CHARGE, "");
	private final SettingsModelBoolean m_report_overview = new SettingsModelBoolean(CFGKEY_REPORT_OVERVIEW, true);
	private final SettingsModelString  m_report_top = new SettingsModelString(CFGKEY_REPORT_TOP, "AUTO");
	private final SettingsModelBoolean m_quant_icat = new SettingsModelBoolean(CFGKEY_QUANT_ICAT, false);
	private final SettingsModelString  m_instrument = new SettingsModelString(CFGKEY_INSTRUMENT, "");
	private final SettingsModelString  m_precursor  = new SettingsModelString(CFGKEY_PRECURSOR, "");
			
	public MSMSSearchNodeModel() {
		// same output ports as superclass but this node has an input port to get either a column of files
		// or a column of spectra to search with...
		super(1, 2);
	}
	
	/**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] 
    		execute(final BufferedDataTable[] inData, final ExecutionContext exec) throws Exception {
    	logger.info("Performing ms/ms ion search using MascotEE: "+m_url.getStringValue());
    	
    	// 0. create Search query and validate the parameters using the server
    	Search s = makeSearchQuery();
    	
    	SearchService ss = makeSearchService();
    	if (ss == null)
    		throw new InvalidSettingsException("Cannot connect to MascotEE: "+m_url.getStringValue());
    	
    	// 1. queue the searches with the specified mascotee installation
    	List<File> input_mgf_files = makeInputFiles(inData[0],exec);
    	logger.info("Created "+input_mgf_files.size()+" data files to be searched via MascotEE");
    	for (File f : input_mgf_files) {
    		logger.info("Checking file... "+f.getAbsolutePath());
    		if (!f.exists() || !f.canRead()) {
    			throw new InvalidSettingsException("Cannot read data file (check permissions?): "+f.getAbsolutePath());
    		}
    	}
    	
    	// 2. wait for the jobs to complete
    	List<String> job_ids = new ArrayList<String>();
    	for (File f : input_mgf_files) {
    		logger.info("Running MS/MS ion search for data file: "+f.getAbsolutePath());
    		s.getMsMsIonSearch().getData().setFormat("Mascot generic");
    		s.getMsMsIonSearch().getData().setFile(new DataHandler(f.toURI().toURL()));
    		String job_id = ss.validateAndSearch(s);
    		job_ids.add(job_id);
    	}
    	logger.info("Waiting for "+job_ids.size()+" jobs to complete.");
    	List<String> results_files = new JobCompletionManager(logger, exec).waitForAllJobsCompleted(ss, job_ids);
    	
    	if (results_files.size() != job_ids.size()) {
    		logger.warn("*** Not all jobs completed successfully: downloading results for those which did!");
    	}
    	
    	// 3. download the results
    	logger.info("Got "+results_files.size()+" mascot .dat files");
    	
    	// 4. load the results into the output table by filename
    	List<File> downloaded_files = new DatDownloadManager(logger, m_url.getStringValue(), m_out_dat.getStringValue(), exec).downloadDatFiles(results_files);
    	
		if (downloaded_files.size() < 1) {
		  	throw new Exception("No downloaded files available! Nothing to load.");
		}
		  
		// now that the files are downloaded we need to initialise the superclass with the chosen files...
		logger.info("Successfully downloaded "+downloaded_files.size()+" mascot .DAT files");
		super.setFiles(downloaded_files);
		
		// purge temporary job files from MascotEE server
		logger.info("Removing temporary files from MascotEE server (Mascot results are not removed)");
		for (String jid : job_ids) {
			ss.purgeJob(jid);
		}
		  
		// now process the downloaded dat files as per the mascot reader node
		return super.execute(inData, exec);
    }
  
	private List<File> makeInputFiles(final BufferedDataTable inData, final ExecutionContext exec) throws InvalidSettingsException,IOException {
		assert(inData != null && exec != null);
		
		ArrayList<File> ret = new ArrayList<File>();
		int col_idx = inData.getSpec().findColumnIndex(m_column.getStringValue());
		if (col_idx < 0) {
			throw new InvalidSettingsException("Cannot find data column! Check configuration?");
		}
		DataColumnSpec spec = inData.getSpec().getColumnSpec(col_idx);
		String ds = m_data_source.getStringValue();
		if (ds.equals(DATA_SOURCES[2])) {
			if (!spec.getType().isCompatible(SpectraValue.class)) {
				throw new InvalidSettingsException("Chosen column does not contain m/z peaklists!");
			}
			return makeCombinedMGF(inData, col_idx, new File(m_out_mgf.getStringValue()));
		} else if (ds.equals(DATA_SOURCES[0]) || ds.equals(DATA_SOURCES[1])) {
			// process list of files and save into array
			Set<String> files = new HashSet<String>();
			for (DataRow r : inData) {
				DataCell c = r.getCell(col_idx);
				if (c == null || c.isMissing()) {
					continue;
				}
				files.add(c.toString());
			} 
			if (ds.equals(DATA_SOURCES[0])) {
				for (String s : files) {
					ret.add(new File(s));
				}
			} else {
				return makeCombinedMGF(files, new File(m_out_mgf.getStringValue()));
			}
		} else {
			throw new InvalidSettingsException("Not yet supported!");
		}
		return ret;
	}

	/**
	 * Combine the specified files into a single MGF. A simple-minded concatenate is used, which is not quite
	 * correct: if the mgfs have different default peptide charge states in the preamble, the concatenate may not
	 * correctly ensure that in the combined result. But the vast majority of MGFs in circulation should be ok.
	 * 
	 * @param files files to be combined
	 * @param output_mgf_folder where to save the combined mgf (must exist prior to call)
	 * @return a single element list with the combined MGF in it
	 * @throws InvalidSettingsException
	 * @throws IOException 
	 * HACK TODO FIXME
	 */
	private List<File> makeCombinedMGF(final Set<String> files, final File output_mgf_folder) throws InvalidSettingsException, IOException {
		validateMgfFolder(output_mgf_folder);
		ArrayList<File> ret = new ArrayList<File>();
		File out = new File(output_mgf_folder, "combined_"+files.size()+"_input_files.mgf");
		if (out.exists()) {
			throw new InvalidSettingsException("Will not overwrite existing: "+out.getAbsolutePath());
		}
		ret.add(out);
		PrintWriter out_pw = null;
		BufferedReader rdr = null;
		int saved = 0;
		try {
			out_pw = new PrintWriter(new FileWriter(out));
		
			for (String s : files) {
				File in = new File(s);
				rdr     = new BufferedReader(new FileReader(in));
				String line;
				while ((line = rdr.readLine()) != null) {
					out_pw.println(line);
					if (line.startsWith("BEGIN IONS")) {
						saved++;
					}
				}
				rdr.close();
				rdr = null;
			}
		} finally {
				if (rdr != null) {
					rdr.close();
				}
				if (out_pw != null) {
					out_pw.close();
				}
		}
		logger.info("Saved "+saved+" peaklists into "+out.getAbsolutePath());
		return ret;
	}

	/**
	 * Save the specified spectra column as a single combined 
	 * @param inData		input data table
	 * @param col_idx		column with m/z cells
	 * @param output_mgf_folder folder to save the combined MGF to (the file will be returned). Must exist prior to call.
	 * @return
	 * @throws InvalidSettingsException
	 * @throws IOException 
	 */
	private List<File> makeCombinedMGF(final BufferedDataTable inData, int col_idx, final File output_mgf_folder) throws InvalidSettingsException, IOException {
		assert(col_idx >= 0 && inData != null);
		validateMgfFolder(output_mgf_folder);
		
		ArrayList<File> ret = new ArrayList<File>();
		File out = new File(output_mgf_folder, "input_spectra_combined.mgf");
		if (out.exists()) {
			throw new InvalidSettingsException("Will not overwrite existing: "+out.getAbsolutePath());
		}
		PrintWriter pw = null;
		int saved = 0;
		try {
			pw = new PrintWriter(new FileWriter(out));
			for (DataRow dr : inData) {
				DataCell dc = dr.getCell(col_idx);
				if (dc.isMissing()) {
					continue;
				}
				if (dc instanceof SpectraValue) {
					if (SpectraWriterNodeModel.savePeakListasMGF(pw, (SpectraValue)dc, new EmptyPeakListHandler(false, logger, 0))) {
						saved++;
					}
				}
			}
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
		
		logger.info("Saved "+saved+" peaklists into: "+out.getAbsolutePath());
		return ret;
	}

	private void validateMgfFolder(final File output_mgf_folder) throws InvalidSettingsException {
		if (output_mgf_folder == null || !(output_mgf_folder.exists() && output_mgf_folder.isDirectory())) {
			throw new InvalidSettingsException("Output MGF folder does not exist!");
		}
	}

	private SearchService makeSearchService() throws MalformedURLException {
		String url = m_url.getStringValue();
		if (url.endsWith("/")) {
			url += "SearchService?wsdl";
		}
		Service      srv = Service.create(new URL(url), SEARCH_NAMESPACE);
		// MTOM will make an attachment greater than 1MB otherwise inline
		SearchService ss = srv.getPort(SearchService.class, new MTOMFeature(1024 * 1024));
		BindingProvider bp = (BindingProvider) ss;
        SOAPBinding binding = (SOAPBinding) bp.getBinding();
        if (!binding.isMTOMEnabled()) {
        	logger.warn("MTOM support is unavailable: may run out of java heap space");
        }
       
		return ss;
	}

	/**
     * Construct a MascotEE JAXB-compatible instance from the current user configuration. This method is
     * called within the context of execute() and should probably not be called at other times.
     * 
     * @return
     */
	private Search makeSearchQuery() throws InvalidSettingsException {
		ObjectFactory   of = new ObjectFactory();
		MsMsIonSearch  mss = of.createMsMsIonSearch();
		
		Identification id = of.createIdentification();
		id.setUsername(finaliseUsername(m_user.getStringValue()));
		id.setEmail(finaliseEmail(m_email.getStringValue()));
		id.setTitle(finaliseTitle(m_job_title.getStringValue()));
		mss.setIdentification(id);
		KeyParameters p = of.createKeyParameters();
		p.setDatabase(finaliseDatabase(m_database.getStringValue()));
		addModifications(p.getFixedMod(), m_fixed_mods.getStringArrayValue());
		addModifications(p.getVariableMod(), m_variable_mods.getStringArrayValue());
		
		p.setMassType(finaliseMassType(m_mass_type.getStringValue()));
		mss.setParameters(p);
		
		Constraints c = of.createConstraints();
		c.setAllowedTaxa(finaliseTaxonomy(m_taxonomy.getStringValue()));
		c.setEnzyme(finaliseEnzyme(m_enzyme.getStringValue()));
		c.setAllowXMissedCleavages(finaliseMissedCleavages(m_missed_cleavages.getIntValue()));
		c.setAllowedProteinMass(finaliseAllowedProteinMass(m_protein_mass.getStringValue()));  // all protein masses allowed
		
		PeptideTolerance pt = finalisePeptideTolerance(m_peptide_tolerance.getStringValue(), m_peptide_tolerance_unit.getStringValue());
		MSMSTolerance mt = finaliseMSMSTolerance(m_msms_tolerance.getStringValue(), m_msms_tolerance_unit.getStringValue());
		
		c.setPeptideCharge(finalisePeptideCharge(m_peptide_charge.getStringValue()));
		c.setPeptideTolerance(pt);
		c.setMsmsTolerance(mt);
		mss.setConstraints(c);
		
		Reporting r = of.createReporting();
		r.setOverview(m_report_overview.getBooleanValue());
		r.setTop(m_report_top.getStringValue());
		mss.setReporting(r);
		
		Quantitation q = of.createQuantitation();
		q.setIcat(m_quant_icat.getBooleanValue());
		mss.setQuant(q);
		
		Data d = of.createData();
		d.setFormat("Mascot generic");	// HACK TODO FIXME... currently hardcoded
		d.setInstrument(finaliseInstrument(m_instrument.getStringValue()));
		d.setPrecursor(finalisePrecursor(m_precursor.getStringValue()));
		// the suggested filename and data itself (d.setFile() and d.setSuggestedFileName()) are set for each search much later ...
		// but we set them here so that validation of search parameters does not fail
		d.setFile(new DataHandler(new ByteArrayDataSource(new byte[0], "")));
		d.setSuggestedFileName("temp.mgf");
		mss.setData(d);
		Search s = of.createSearch();
		s.setMsMsIonSearch(mss);
		return s;
	}

	private MSMSTolerance finaliseMSMSTolerance(final String value, final String unit) {
		MSMSTolerance tol = new MSMSTolerance();
		tol.setValue(value);
		tol.setUnit(unit);
		return tol;
	}

	private PeptideTolerance finalisePeptideTolerance(final String value, final String unit) {
		PeptideTolerance pt = new PeptideTolerance();
		pt.setValue(value);
		pt.setUnit(unit);
		return pt;
	}

	private String finaliseAllowedProteinMass(String pm) {
		return pm;
	}

	private String finalisePeptideCharge(String pc) {
		return pc;
	}

	private int finaliseMissedCleavages(int intValue) throws InvalidSettingsException {
		if (intValue < 0 || intValue > 9)
			throw new InvalidSettingsException("Missed cleavages must be a small non-negative value (<10)!");
		return intValue;
	}

	private String finaliseMassType(String mono_or_avg) throws InvalidSettingsException {
		if (!mono_or_avg.equals("Monoisotopic") && !mono_or_avg.equals("Average")) {
			throw new InvalidSettingsException("Unknown mass type: "+mono_or_avg);
		}
		return mono_or_avg;
	}

	private String finaliseEnzyme(final String cle) {
		return cle;
	}

	private String finaliseTaxonomy(final String t) {
		// prune leading dots and spaces from the string
		return t.replaceFirst("^[.\\s]+", "");
	}

	private String finalisePrecursor(String prec) {
		return prec;
	}

	private String finaliseInstrument(String instr) {
		return instr;
	}

	private void addModifications(final List<String> mod, final String[] vec) throws InvalidSettingsException {
		if (mod == null || vec == null)
			throw new InvalidSettingsException("Missing modification list!");
		for (String s : vec) {
			mod.add(s);
		}
	}

	private String finaliseDatabase(final String db) {
		assert(db != null);
		
		return db;
	}

	private String finaliseTitle(String title) throws InvalidSettingsException {
		assert(title != null);
		if (title.length() < 1) {
			title += "uuid="+UUID.randomUUID().toString();
		}
		return title;
	}

	private String finaliseEmail(final String email) {
		assert(email != null);
		return email;
	}

	private String finaliseUsername(final String user) throws InvalidSettingsException {
		assert(user != null);
		Pattern p = Pattern.compile("^\\w*$");
		Matcher m = p.matcher(user);
		if (!m.matches()) {
			throw new InvalidSettingsException("Username must be letters, digits and underscore (_) only!");
		}
		return user;
	}
	
	

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {
		super.saveSettingsTo(settings);
		m_url.saveSettingsTo(settings);
		m_column.saveSettingsTo(settings);
		m_data_source.saveSettingsTo(settings);
		m_mascotee_username.saveSettingsTo(settings);
		m_mascotee_password.saveSettingsTo(settings);
		
		// mascot search state
		m_user.saveSettingsTo(settings);
		m_email.saveSettingsTo(settings);
		m_job_title.saveSettingsTo(settings);
		m_database.saveSettingsTo(settings);
		m_fixed_mods.saveSettingsTo(settings);
		m_variable_mods.saveSettingsTo(settings);
		m_mass_type.saveSettingsTo(settings);
		m_taxonomy.saveSettingsTo(settings);
		m_enzyme.saveSettingsTo(settings);
		m_missed_cleavages.saveSettingsTo(settings);
		m_protein_mass.saveSettingsTo(settings);
		m_peptide_tolerance.saveSettingsTo(settings);
		m_peptide_tolerance_unit.saveSettingsTo(settings);
		m_msms_tolerance.saveSettingsTo(settings);
		m_msms_tolerance_unit.saveSettingsTo(settings);
		m_peptide_charge.saveSettingsTo(settings);
		m_report_overview.saveSettingsTo(settings);
		m_report_top.saveSettingsTo(settings);
		m_quant_icat.saveSettingsTo(settings);
		m_instrument.saveSettingsTo(settings);
		m_precursor.saveSettingsTo(settings);
		
		m_out_mgf.saveSettingsTo(settings);
		m_out_dat.saveSettingsTo(settings);
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings)
			throws InvalidSettingsException {
		super.validateSettings(settings);
		m_url.validateSettings(settings);
		m_column.validateSettings(settings);
		m_data_source.validateSettings(settings);
		m_mascotee_username.validateSettings(settings);
		m_mascotee_password.validateSettings(settings);
		
		
		// mascot search state
		m_user.validateSettings(settings);
		m_email.validateSettings(settings);
		m_job_title.validateSettings(settings);
		m_database.validateSettings(settings);
		m_fixed_mods.validateSettings(settings);
		m_variable_mods.validateSettings(settings);
		m_mass_type.validateSettings(settings);
		m_taxonomy.validateSettings(settings);
		m_enzyme.validateSettings(settings);
		m_missed_cleavages.validateSettings(settings);
		m_protein_mass.validateSettings(settings);
		m_peptide_tolerance.validateSettings(settings);
		m_peptide_tolerance_unit.validateSettings(settings);
		m_msms_tolerance.validateSettings(settings);
		m_msms_tolerance_unit.validateSettings(settings);
		m_peptide_charge.validateSettings(settings);
		m_report_overview.validateSettings(settings);
		m_report_top.validateSettings(settings);
		m_quant_icat.validateSettings(settings);
		m_instrument.validateSettings(settings);
		m_precursor.validateSettings(settings);
		
		m_out_mgf.validateSettings(settings);
		m_out_dat.validateSettings(settings);
	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
			throws InvalidSettingsException {
		super.loadValidatedSettingsFrom(settings);
		m_url.loadSettingsFrom(settings);
		m_column.loadSettingsFrom(settings);
		m_data_source.loadSettingsFrom(settings);
		m_mascotee_username.loadSettingsFrom(settings);
		m_mascotee_password.loadSettingsFrom(settings);
		
		// mascot search state
		m_user.loadSettingsFrom(settings);
		m_email.loadSettingsFrom(settings);
		m_job_title.loadSettingsFrom(settings);
		m_database.loadSettingsFrom(settings);
		m_fixed_mods.loadSettingsFrom(settings);
		m_variable_mods.loadSettingsFrom(settings);
		m_mass_type.loadSettingsFrom(settings);
		m_taxonomy.loadSettingsFrom(settings);
		m_enzyme.loadSettingsFrom(settings);
		m_missed_cleavages.loadSettingsFrom(settings);
		m_protein_mass.loadSettingsFrom(settings);
		m_peptide_tolerance.loadSettingsFrom(settings);
		m_peptide_tolerance_unit.loadSettingsFrom(settings);
		m_msms_tolerance.loadSettingsFrom(settings);
		m_msms_tolerance_unit.loadSettingsFrom(settings);
		m_peptide_charge.loadSettingsFrom(settings);
		m_report_overview.loadSettingsFrom(settings);
		m_report_top.loadSettingsFrom(settings);
		m_quant_icat.loadSettingsFrom(settings);
		m_instrument.loadSettingsFrom(settings);
		m_precursor.loadSettingsFrom(settings);
		
		m_out_mgf.loadSettingsFrom(settings);
		m_out_dat.loadSettingsFrom(settings);
	}

	@Override
	protected void reset() {
	}
	
	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
	}

	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
	}

}