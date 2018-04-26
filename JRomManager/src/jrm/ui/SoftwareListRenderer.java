package jrm.ui;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import jrm.Messages;
import jrm.profile.data.Software;

@SuppressWarnings("serial")
public final class SoftwareListRenderer
{
	private final static ImageIcon folder_closed_green = new ImageIcon(SoftwareListRenderer.class.getResource("/jrm/resources/folder_closed_green.png")); //$NON-NLS-1$
	private final static ImageIcon folder_closed_orange = new ImageIcon(SoftwareListRenderer.class.getResource("/jrm/resources/folder_closed_orange.png")); //$NON-NLS-1$
	private final static ImageIcon folder_closed_red = new ImageIcon(SoftwareListRenderer.class.getResource("/jrm/resources/folder_closed_red.png")); //$NON-NLS-1$
	private final static ImageIcon folder_closed_gray = new ImageIcon(SoftwareListRenderer.class.getResource("/jrm/resources/folder_closed_gray.png")); //$NON-NLS-1$
	
	public final static String[] columns = new String[] { Messages.getString("SoftwareListRenderer.Status"), Messages.getString("SoftwareListRenderer.Name"), Messages.getString("SoftwareListRenderer.Description"), Messages.getString("SoftwareListRenderer.Have"), Messages.getString("SoftwareListRenderer.CloneOf") }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	public final static Class<?>[] columnsTypes = new Class<?>[] { Object.class, Object.class, String.class, String.class, Object.class };
	public final static int[] columnsWidths = new int[] { -20, 40, 200, -45, 40 };
	public final static TableCellRenderer[] columnsRenderers = new TableCellRenderer[] { new DefaultTableCellRenderer()
	{

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			if(value instanceof Software)
			{
				super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column); //$NON-NLS-1$
				switch(((Software) value).getStatus())
				{
					case COMPLETE:
						setIcon(folder_closed_green);
						break;
					case PARTIAL:
						setIcon(folder_closed_orange);
						break;
					case MISSING:
						setIcon(folder_closed_red);
						break;
					case UNKNOWN:
					default:
						setIcon(folder_closed_gray);
						break;
				}
				return this;
			}
			setIcon(null);
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
	}, new DefaultTableCellRenderer()
	{
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			if(value instanceof Software)
			{
				return super.getTableCellRendererComponent(table, ((Software) value).name, isSelected, hasFocus, row, column);
			}
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
	}, null, new DefaultTableCellRenderer()
	{
		{
			setHorizontalAlignment(CENTER);
		}
	}, new DefaultTableCellRenderer()
	{
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			if(value instanceof Software)
			{
				super.getTableCellRendererComponent(table, ((Software) value).name, isSelected, hasFocus, row, column);
				switch(((Software) value).getStatus())
				{
					case COMPLETE:
						setIcon(folder_closed_green);
						break;
					case PARTIAL:
						setIcon(folder_closed_orange);
						break;
					case MISSING:
						setIcon(folder_closed_red);
						break;
					case UNKNOWN:
					default:
						setIcon(folder_closed_gray);
						break;
				}
				return this;
			}
			setIcon(null);
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
	} };

	private SoftwareListRenderer()
	{
	}

}