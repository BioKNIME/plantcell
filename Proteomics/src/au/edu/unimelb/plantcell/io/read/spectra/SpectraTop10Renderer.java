package au.edu.unimelb.plantcell.io.read.spectra;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.Comparator;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.renderer.DefaultDataValueRenderer;

/**
 * Reports the top-ten m/z values based on intensity in the peak list.
 * m/z values are rounded to 3 decimal places.
 * 
 * @author andrew.cassin
 *
 */
public class SpectraTop10Renderer extends DefaultDataValueRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4769347522541135416L;

	@Override
	public boolean accepts(DataColumnSpec spec) {
		return (spec != null && spec.getType().isCompatible(SpectraValue.class));
	}
	
	@Override 
	public String getDescription() {
		return "Top 10 Peaks";
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(200, 20);
	}
	
	@Override
	protected void setValue(final Object value) {
		if (value instanceof SpectraValue) {
			// 1. compute the tenth-biggest intensity (dont worry about ties)
			SpectraValue si = (SpectraValue) value;
			StringBuilder sb = new StringBuilder();

			if (si.getNumPeaks() > 0) {
				double[] intensities = si.getIntensity();
				double[] mz = si.getMZ();
				SortablePeak[] big_peaks = new SortablePeak[mz.length];
				for (int i=0; i<mz.length; i++) {
					big_peaks[i] = new SortablePeak(mz[i], intensities[i], true); // true == sort by intensity
				}
				Arrays.sort(big_peaks);
				// 3. sort peaks by ascending mz (permit spectra to have less than ten peaks to begin with)
				int min = big_peaks.length-10;
				if (min < 0)
					min = 0;
				for (int i=min; i<big_peaks.length; i++) {
					sb.append(big_peaks[i].getMZ());
					sb.append(' ');
					sb.append(big_peaks[i].getIntensity());
					sb.append('\n');
				}
			} else {
				sb.append("No peaks present.");
			}
			
			super.setValue("<html><pre>"+sb.toString());
		} else {
			super.setValue(value);
		}
	}
	
	/**
	 * Sorts solely on the basis of mz and not intensity
	 * @author andrew.cassin
	 *
	 */
	private class SortablePeak implements Comparator<SortablePeak>,Comparable<SortablePeak> {
		private double m_mz, m_intensity;
		private boolean m_sort_by_intensity;
		
		@Override
		public int compare(SortablePeak oa, SortablePeak ob) {
			if (oa == null || ob == null || oa == ob ) 
				return 0;
			assert(oa.m_sort_by_intensity == ob.m_sort_by_intensity);
			
			if (m_sort_by_intensity) {
				if (oa.m_intensity == ob.m_intensity)
					return 0;
				else if (oa.m_intensity < ob.m_intensity) 
					return -1;
				else 
					return 1;
			} else {
				if (oa.m_mz == ob.m_mz)
					return 0;
				else if (oa.m_mz < ob.m_mz)
					return -1;
				else
					return 1;
			}
		}
		
		public SortablePeak(double mz, double intensity, boolean sort_by_intensity) {
			m_mz = mz;
			m_intensity = intensity;
			m_sort_by_intensity = sort_by_intensity;
		}
		
		public double getMZ() { return m_mz; }
		public double getIntensity() { return m_intensity; }

		@Override
		public int compareTo(SortablePeak arg0) {
			return compare(this, arg0);
		}
	}
}
