package au.edu.unimelb.plantcell.io.ws.biomart;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellEditor;

/**
 * 
 * @author pcbrc.admin
 *
 */
public class MyTextEditor extends AbstractCellEditor implements TableCellEditor,ActionListener {

	/**
	 * not used internally
	 */
	private static final long serialVersionUID = 2733396628759674474L;
	private static final String EDIT = "edit";
	private JButton button;
	private Object new_user_input;		// may be a collection of items (if multi select)
	private Object old_user_input;	
	
	public MyTextEditor(Object val) {
		button = new JButton();
		button.addActionListener(this);
		button.setActionCommand(EDIT);
		button.setBorderPainted(false);
		old_user_input = val;
	}
	
	/*********************** TableCellEditor methods ******************************/
	@Override
	public Object getCellEditorValue() {
		return new_user_input;
	}

	@Override
	public Component getTableCellEditorComponent(final JTable table, final Object value, boolean isSelected, int r, int c) {
		new_user_input = value;
		old_user_input = value;
		return button;
	}
	
	
	/********************** ActionListener methods **********************/
	@Override
	public void actionPerformed(ActionEvent ev) {
		if (EDIT.equals(ev.getActionCommand())) {
			JTextArea input = new JTextArea();
            input.setRows(20);
            input.setColumns(60);
            input.setEditable(true);
            input.setWrapStyleWord(true);
            if (old_user_input != null)
                    input.setText(old_user_input.toString());
           
            // cancel not chosen?
            if (JOptionPane.showInputDialog(null, new JScrollPane(input), null, JOptionPane.OK_CANCEL_OPTION) != null) {
                new_user_input = input.getText();
            } else {
            	new_user_input = old_user_input;
            }

			fireEditingStopped();
		}
	}
}
