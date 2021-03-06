package au.edu.unimelb.plantcell.proteomics.views.multi;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.TableCellEditor;

public class MySliderEditor extends AbstractCellEditor implements TableCellEditor {
	private JSpinner m_slider;
	private int m_min, m_max;
	
	public MySliderEditor() {
		this(1, 100);
	}
	
	public MySliderEditor(int min, int max) {
		assert(min < max);
		m_min = min;
		m_max = max;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4657578671212907738L;

	@Override
	public Component getTableCellEditorComponent(JTable arg0, Object arg1, boolean arg2, int arg3, int arg4) {
		Integer new_value = (Integer) arg1;
		m_slider = new JSpinner(new SpinnerNumberModel(new_value.intValue(), m_min, m_max, 10));
		return m_slider;
	}

	@Override
	public Object getCellEditorValue() {
		return (Integer) m_slider.getValue();
	}

}
