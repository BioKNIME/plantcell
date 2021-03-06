package au.edu.unimelb.plantcell.io.ws.mascot.datfile;

import java.io.File;
import java.io.FileOutputStream;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.activation.DataHandler;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.ws.Service;

import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;

import au.edu.unimelb.plantcell.io.read.mascot.MascotReaderNodeModel;
import au.edu.unimelb.plantcell.servers.mascotee.endpoints.DatFileService;

/**
 * This is the model implementation of DatFileDownload.
 * Permits downloading of Mascot DAT files via a JAX-WS web service and will load each dat file into the output table as per the mascot reader. The node also saves the DAT files to the user-specified folder.
 *
 * @author http://www.plantcell.unimelb.edu.au/bioinformatics
 */
public class DatFileDownloadNodeModel extends MascotReaderNodeModel {
	private static final NodeLogger logger = NodeLogger.getLogger("Mascot Dat Downloader");
	
    // configuration parameters which the dialog also uses (superclass also has state)
	public final static String CFGKEY_MASCOTEE_URL = "mascot-service-url";
	public final static String CFGKEY_DAT_FILES_SINCE    = "since-when";
	public final static String CFGKEY_DAT_FILES          = "available-dat-files";
	public final static String CFGKEY_SAVETO_FOLDER      = "save-dat-files-to";
	public static final String CFGKEY_USERNAME           = "username";
	public static final String CFGKEY_PASSWORD           = "password";

	
	// default values for the dialog
	public final static String DEFAULT_MASCOTEE_URL = "http://mascot.plantcell.unimelb.edu.au:8080/mascotee/";
	private final static QName MASCOT_SERVICE_NAMESPACE = 
			new QName("http://www.plantcell.unimelb.edu.au/bioinformatics/wsdl", "DatFileService");
	public final static String[] SINCE_METHODS = {
		"Last 24 hours",
		"Last 7 days",
		"Current month",
		"Current year",
		"Since the dawn of time (will take a long time)"
	};
	
	// persisted state within this class (note that superclass state is also persisted!)
	private final SettingsModelString            m_url = new SettingsModelString(CFGKEY_MASCOTEE_URL, DEFAULT_MASCOTEE_URL);
	private final SettingsModelString       m_strategy = new SettingsModelString(CFGKEY_DAT_FILES_SINCE, SINCE_METHODS[0]);
	private final SettingsModelStringArray m_dat_files = new SettingsModelStringArray(CFGKEY_DAT_FILES, new String[0]);
	private final SettingsModelString         m_saveto = new SettingsModelString(CFGKEY_SAVETO_FOLDER, "");
	private final SettingsModelString       m_username = new SettingsModelString(CFGKEY_USERNAME, "");
	private final SettingsModelString       m_password = new SettingsModelString(CFGKEY_PASSWORD, "");
	
    /**
     * Constructor for the node model.
     */
    protected DatFileDownloadNodeModel() {
    	// this node has the same outputs and inputs as the superclass, so...
        super();
        m_username.setEnabled(false);
        m_password.setEnabled(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {
    	String u = m_url.getStringValue();
    	if (u.endsWith("/")) {
    		u += "DatFileService?wsdl";
    	}
    	logger.info("Contacting MascotEE at: "+u);
    	Service srv = Service.create(new URL(u), MASCOT_SERVICE_NAMESPACE);

       	if (srv == null)
       		throw new InvalidSettingsException("Unable to connect to "+u);
       	DatFileService dfs = srv.getPort(DatFileService.class);
        
        // first retrieve all the desired dat files into the chosen folder
        String[] wanted_dat_files = m_dat_files.getStringArrayValue();
        if (wanted_dat_files.length < 1) {
        	throw new InvalidSettingsException("No dat files chosen for download! Reconfigure...");
        }
        File out = new File(m_saveto.getStringValue());
        if (!out.exists() && out.isDirectory()) {
        	throw new InvalidSettingsException("Output folder, "+out.getAbsolutePath()+" is not an accessible directory!");
        }
        ArrayList<File> downloaded_files = new ArrayList<File>();
        try {
	        for (String s : wanted_dat_files) {
	        	logger.info("Saving mascot dat file: "+s);
	        	File   dat_out = new File(out, s.replaceAll("[^A-Z0-9a-z\\.]", "_"));
	        	String url     = dfs.getDatFileURL(s);
	        	logger.info("Getting "+s+" using URL: "+url);
	        	
	        	DataHandler       dh = new DataHandler(new URL(url));
	        	FileOutputStream fos = new FileOutputStream(dat_out);
	        	dh.writeTo(fos);
	        	fos.close();
	        	
	        	if (dat_out.length() < 1) {
	        		logger.warn("Zero-sized file for downloaded DAT file: "+dat_out.getAbsolutePath()+": file ignored!");
	        	} else {
	        		downloaded_files.add(dat_out);
	        	}
	        }
        } catch (Exception e) {
        	e.printStackTrace();
        	throw e;
        }
        
        if (downloaded_files.size() < 1) {
        	throw new InvalidSettingsException("No downloaded files available! Nothing to load.");
        }
        
        // now that the files are downloaded we need to initialise the superclass with the chosen files...
        super.setFiles(downloaded_files);
        
        // now process the downloaded dat files as per the mascot reader node
        return super.execute(inData, exec);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
    	super.saveSettingsTo(settings);
    	m_url.saveSettingsTo(settings);
    	m_strategy.saveSettingsTo(settings);
    	m_dat_files.saveSettingsTo(settings);
    	m_saveto.saveSettingsTo(settings);
    	m_username.saveSettingsTo(settings);
    	m_password.saveSettingsTo(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	super.loadValidatedSettingsFrom(settings);
    	m_url.loadSettingsFrom(settings);
    	m_strategy.loadSettingsFrom(settings);
    	m_dat_files.loadSettingsFrom(settings);
    	m_saveto.loadSettingsFrom(settings);
    	m_username.loadSettingsFrom(settings);
    	m_password.loadSettingsFrom(settings);
    }
   
    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
       super.validateSettings(settings);
       m_url.validateSettings(settings);
       m_strategy.validateSettings(settings);
       m_dat_files.validateSettings(settings);
       m_saveto.validateSettings(settings);
       m_username.validateSettings(settings);
       m_password.validateSettings(settings);
    }

    public static Service getMascotService(final String url, final Authenticator auth) throws MalformedURLException {
    	if (auth != null) {
    		Authenticator.setDefault(auth);
    	}
    	return getMascotService(url);
    }
    
    public static Service getMascotService(final String url) throws MalformedURLException {
    	String u = url;
    	if (u.endsWith("/")) {
    		u += "DatFileService?wsdl";
    	}
    	logger.info("Connecting to "+u);
    	return Service.create(new URL(u), MASCOT_SERVICE_NAMESPACE);
    }
    
	public static List<String> getDatFilesSince(final Calendar since, final String url, final Authenticator auth) 
					throws MalformedURLException, InvalidSettingsException, SOAPException {
		assert(url != null && since != null);
		Service srv = getMascotService(url, auth);
       	if (srv == null)
       		throw new InvalidSettingsException("Unable to connect to "+url);
        DatFileService datFileService = srv.getPort(DatFileService.class);
         
        StringBuilder str = new StringBuilder();
        str.append(since.get(Calendar.YEAR));
        int month = since.get(Calendar.MONTH)+1;
        if (month < 10)
        	str.append("0"+month);
        else
        	str.append(month);
        int day_of_month = since.get(Calendar.DAY_OF_MONTH);
        if (day_of_month < 10)
        	str.append("0"+day_of_month);
        else
        	str.append(day_of_month);
        String[] s = datFileService.getDatFilesSince(str.toString());
        ArrayList<String> ret = new ArrayList<String>(s.length);
        for (String dat_file : s) {
        	ret.add(dat_file);
        }
        return ret;
	}

}

