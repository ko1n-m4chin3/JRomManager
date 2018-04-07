package jrm.ui;

import java.awt.Component;
import java.io.File;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class FileTableCellRenderer extends DefaultTableCellRenderer
{

	public FileTableCellRenderer()
	{
		super();
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		return super.getTableCellRendererComponent(table, value!=null?((File)value).getName():null, isSelected, hasFocus, row, column);
	}
}