package au.edu.unimelb.plantcell.io.jemboss.simple;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;

import au.edu.unimelb.plantcell.core.CorePlugin;
import au.edu.unimelb.plantcell.core.Preferences;



public class ACDApplication {
	/**
	 * subfolders within the emboss root to try to locate the acd folder...
	 */
	public final static String[] DEFAULT_EMBOSS_ACD_PATHS = new String[] { "/share/EMBOSS/acd", "/acd", "/shared/acd" };

	
	private String m_name;
	private final HashMap<String,String> m_props = new HashMap<String,String>();
	private List<ACDSection> m_sections = new ArrayList<ACDSection>();
	
	public ACDApplication(ACDStreamReader rdr) throws IOException, ParseException {
		String keyword = rdr.next_token();
		if (!keyword.equals("application")) {
			throw new ParseException("Expected application: got "+keyword, 0);
		}
		String colon = rdr.next_token();
		if (!colon.equals(":")) 
			throw new ParseException("Expected :, got "+colon, 0);
		m_name = rdr.next_token();
		
		rdr.next_properties(m_props);
		
		rdr.read_section_list(m_sections);
	}

	public boolean hasSection(String str) {
		for (ACDSection s : m_sections) {
			if (s.hasName(str))
				return true;
		}
		return false;
	}

	public ACDSection getSection(String str) {
		for (ACDSection s : m_sections) {
			if (s.hasName(str))
				return s;
		}
		return null;
	}

	public boolean hasGFFoutput() {
		ACDSection output_section = getSection("output");
		if (output_section == null)
			return false;
		return (output_section.hasReport());
	}

	public String getName() {
		return m_name;
	}
	
	public List<ACDField> getMandatoryFields(boolean exclude_first_in_out) {
		ArrayList<ACDField> ret = new ArrayList<ACDField>();
		for (ACDSection s : m_sections) {
			ret.addAll(s.getMandatoryFields(exclude_first_in_out));
		}
		return ret;
	}

	public String getOneLineSummary() {
		String descr = m_props.get("documentation");
		if (descr == null)
			descr = "";
		descr = descr.replaceAll("\\s+", " ");
		if (descr.length() > 80)
			descr = descr.substring(0, 80) + "...";
		return getName() + ": " + descr;
	}

	public boolean hasGraphOutput() {
		ACDSection output_section = getSection("output");
		if (output_section == null)
			return false;
	
		return output_section.hasPlot();
	}

	public static String getEmbossDir() {
    	IPreferenceStore prefs = CorePlugin.getDefault().getPreferenceStore();
    	String emboss_dir = prefs.getString(Preferences.PREFS_JEMBOSS_FOLDER);
    	if (emboss_dir == null || emboss_dir.length() < 1) {
    		emboss_dir = prefs.getDefaultString(Preferences.PREFS_JEMBOSS_FOLDER);
    	}
    	return emboss_dir;
    }
	
	public static ACDApplication find(String program) throws IOException, ParseException {
		if (program.length() < 1)
			return null;
		for (String s : DEFAULT_EMBOSS_ACD_PATHS) {
			File root = new File(getEmbossDir(), s);
			if (!root.exists()) {
				continue;
			}
			File                  f = new File(root, program + ".acd");
			BufferedReader      rdr = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			ACDStreamReader acd_rdr = new ACDStreamReader(rdr);
			ACDApplication     appl = new ACDApplication(acd_rdr);
			rdr.close();
			return appl;
		}
		throw new IOException("Unable to locate ACD for program: "+program);
	}

	public String findInputSequenceParameter() {
		ACDSection input = getSection("input");
		
		for (ACDField f : input.getFields()) {
			if (f.isSequence() && f.isMandatory()) {
				return f.getName();
			}
		}
		return "sequence";
	}

	public boolean hasFieldType(String field, String type) {
		for (ACDSection s : m_sections) {
			for (ACDField f : s.getFields()) {
				if (f.hasName(field) && f.hasType(type))
					return true;
			}
		}
		return false;
	}

	public boolean hasSequenceOutput() {
		return (getFirstOutput() != null);
	}

	public ACDField getFirstOutput() {
		ACDSection output_section = getSection("output");
		for (ACDField f : output_section.getFields()) {
			if (f.hasType("seqout") || f.hasType("seqoutall"))
				return f;
		}
		return null;
	}
	
	public boolean hasProperty(String prop) {
		return (getProperty(prop) != null);
	}
	
	public String getProperty(String prop) {
		return m_props.get(prop);
	}

	public String[] getFields(boolean exclude_first_in_out) {
		ArrayList<ACDField> all = new ArrayList<ACDField>();
		for (ACDSection s : m_sections) {
			all.addAll(s.getFields(exclude_first_in_out));
		}
		ArrayList<String> ret = new ArrayList<String>(all.size());
		for (ACDField f : all) {
			ret.add(f.getName());
		}
		return ret.toArray(new String[0]);
	}

	public ACDField getField(String name) {
		for (ACDSection s : m_sections) {
			for (ACDField f : s.getFields()) {
				if (f.hasName(name))
					return f;
			}
		}
		return null;
	}
}
