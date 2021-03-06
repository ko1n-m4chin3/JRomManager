/* Copyright (C) 2018  optyfr
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package jrm.ui.profile.data;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import jrm.locale.Messages;
import jrm.profile.data.Machine;
import jrm.profile.data.Samples;
import jrm.ui.MainFrame;

// TODO: Auto-generated Javadoc
/**
 * The Class MachineListRenderer.
 */
@SuppressWarnings("serial")
public final class MachineListRenderer
{
	
	/** The Constant folder_closed_green. */
	private final static ImageIcon folder_closed_green = MainFrame.getIcon("/jrm/resicons/folder_closed_green.png"); //$NON-NLS-1$
	
	/** The Constant folder_closed_orange. */
	private final static ImageIcon folder_closed_orange = MainFrame.getIcon("/jrm/resicons/folder_closed_orange.png"); //$NON-NLS-1$
	
	/** The Constant folder_closed_red. */
	private final static ImageIcon folder_closed_red = MainFrame.getIcon("/jrm/resicons/folder_closed_red.png"); //$NON-NLS-1$
	
	/** The Constant folder_closed_gray. */
	private final static ImageIcon folder_closed_gray = MainFrame.getIcon("/jrm/resicons/folder_closed_gray.png"); //$NON-NLS-1$

	/** The columns. */
	public static String[] columns = new String[] { Messages.getString("MachineListRenderer.Status"), Messages.getString("MachineListRenderer.Name"), Messages.getString("MachineListRenderer.Description"), Messages.getString("MachineListRenderer.Have"), Messages.getString("MachineListRenderer.CloneOf"), Messages.getString("MachineListRenderer.RomOf"), Messages.getString("MachineListRenderer.SampleOf"), Messages.getString("MachineListRenderer.Selected") }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
	
	/** The columns types. */
	public final static Class<?>[] columnsTypes = new Class<?>[] { Object.class, Object.class, String.class, String.class, Object.class, Object.class, String.class, Boolean.class };
	
	/** The columns widths. */
	public final static int[] columnsWidths = new int[] { -20, 40, 200, -45, 40, 40, 40, -20 };
	
	/** The columns renderers. */
	public static TableCellRenderer[] columnsRenderers = new TableCellRenderer[] { new DefaultTableCellRenderer()
	{
		@Override
		public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column)
		{
			if(value instanceof Machine)
			{
				super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column); //$NON-NLS-1$
				switch(((Machine) value).getStatus())
				{
					case COMPLETE:
						setIcon(MachineListRenderer.folder_closed_green);
						break;
					case PARTIAL:
						setIcon(MachineListRenderer.folder_closed_orange);
						break;
					case MISSING:
						setIcon(MachineListRenderer.folder_closed_red);
						break;
					case UNKNOWN:
					default:
						setIcon(MachineListRenderer.folder_closed_gray);
						break;
				}
				return this;
			}
			setIcon(null);
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
	}, new DefaultTableCellRenderer()
	{
		ImageIcon application_osx_terminal = MainFrame.getIcon("/jrm/resicons/icons/application_osx_terminal.png"); //$NON-NLS-1$
		ImageIcon computer = MainFrame.getIcon("/jrm/resicons/icons/computer.png"); //$NON-NLS-1$
		ImageIcon wrench = MainFrame.getIcon("/jrm/resicons/icons/wrench.png"); //$NON-NLS-1$
		ImageIcon joystick = MainFrame.getIcon("/jrm/resicons/icons/joystick.png"); //$NON-NLS-1$

		@Override
		public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column)
		{
			if(value instanceof Machine)
			{
				super.getTableCellRendererComponent(table, ((Machine) value).getBaseName(), isSelected, hasFocus, row, column);
				if(((Machine) value).isbios)
					setIcon(application_osx_terminal);
				else if(((Machine) value).isdevice)
					setIcon(computer);
				else if(((Machine) value).ismechanical)
					setIcon(wrench);
				else
					setIcon(joystick);
				setText(((Machine) value).getBaseName());
				return this;
			}
			setIcon(null);
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
	}, new DefaultTableCellRenderer()
	{
		@Override
		public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column)
		{
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			setToolTipText(getText());
			return this;
		};
	}, new DefaultTableCellRenderer()
	{
		{
			setHorizontalAlignment(SwingConstants.CENTER);
		}
	}, new DefaultTableCellRenderer()
	{
		@Override
		public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column)
		{
			if(value instanceof Machine)
			{
				super.getTableCellRendererComponent(table, ((Machine) value).getBaseName(), isSelected, hasFocus, row, column);
				switch(((Machine) value).getStatus())
				{
					case COMPLETE:
						setIcon(MachineListRenderer.folder_closed_green);
						break;
					case PARTIAL:
						setIcon(MachineListRenderer.folder_closed_orange);
						break;
					case MISSING:
						setIcon(MachineListRenderer.folder_closed_red);
						break;
					case UNKNOWN:
					default:
						setIcon(MachineListRenderer.folder_closed_gray);
						break;
				}
				return this;
			}
			setIcon(null);
			if(value!=null)
				setIcon(MachineListRenderer.folder_closed_gray);
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
	}, new DefaultTableCellRenderer()
	{
		@Override
		public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column)
		{
			if(value instanceof Machine)
			{
				super.getTableCellRendererComponent(table, ((Machine) value).getBaseName(), isSelected, hasFocus, row, column);
				switch(((Machine) value).getStatus())
				{
					case COMPLETE:
						setIcon(MachineListRenderer.folder_closed_green);
						break;
					case PARTIAL:
						setIcon(MachineListRenderer.folder_closed_orange);
						break;
					case MISSING:
						setIcon(MachineListRenderer.folder_closed_red);
						break;
					case UNKNOWN:
					default:
						setIcon(MachineListRenderer.folder_closed_gray);
						break;
				}
				return this;
			}
			setIcon(null);
			if(value!=null)
				setIcon(MachineListRenderer.folder_closed_gray);
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
	}, new DefaultTableCellRenderer()
	{
		@Override
		public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column)
		{
			if(value instanceof Samples)
			{
				super.getTableCellRendererComponent(table, ((Samples) value).getBaseName(), isSelected, hasFocus, row, column);
				switch(((Samples) value).getStatus())
				{
					case COMPLETE:
						setIcon(MachineListRenderer.folder_closed_green);
						break;
					case PARTIAL:
						setIcon(MachineListRenderer.folder_closed_orange);
						break;
					case MISSING:
						setIcon(MachineListRenderer.folder_closed_red);
						break;
					case UNKNOWN:
					default:
						setIcon(MachineListRenderer.folder_closed_gray);
						break;
				}
				return this;
			}
			setIcon(null);
			if(value!=null)
				setIcon(MachineListRenderer.folder_closed_gray);
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
	}, null };

	/**
	 * Instantiates a new machine list renderer.
	 */
	private MachineListRenderer()
	{
	}

}
