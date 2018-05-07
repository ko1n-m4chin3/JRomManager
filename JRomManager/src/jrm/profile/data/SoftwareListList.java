package jrm.profile.data;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.io.IOUtils;

import jrm.profile.Export;
import jrm.profile.Export.EnhancedXMLStreamWriter;
import jrm.ui.AnywareListListRenderer;
import jrm.ui.ProgressHandler;

@SuppressWarnings("serial")
public final class SoftwareListList extends AnywareListList<SoftwareList> implements Serializable
{
	private ArrayList<SoftwareList> sl_list = new ArrayList<>();
	public HashMap<String, SoftwareList> sl_byname = new HashMap<>();

	public SoftwareListList()
	{
		initTransient();
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		initTransient();
	}

	protected void initTransient()
	{
		super.initTransient();
	}

	@Override
	public int getColumnCount()
	{
		return AnywareListListRenderer.columns.length;
	}

	@Override
	public String getColumnName(int columnIndex)
	{
		return AnywareListListRenderer.columns[columnIndex];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		return AnywareListListRenderer.columnsTypes[columnIndex];
	}

	public TableCellRenderer getColumnRenderer(int columnIndex)
	{
		return AnywareListListRenderer.columnsRenderers[columnIndex] != null ? AnywareListListRenderer.columnsRenderers[columnIndex] : new DefaultTableCellRenderer();
	}

	public int getColumnWidth(int columnIndex)
	{
		return AnywareListListRenderer.columnsWidths[columnIndex];
	}

	@Override
	public int getRowCount()
	{
		return getFilteredList().size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		switch(columnIndex)
		{
			case 0:
				return getFilteredList().get(rowIndex);
			case 1:
				return getFilteredList().get(rowIndex).description.toString();
			case 2:
				return String.format("%d/%d", getFilteredList().get(rowIndex).countHave(), getFilteredList().get(rowIndex).countAll());
		}
		return null;
	}

	@Override
	public List<SoftwareList> getList()
	{
		return sl_list;
	}

	public Stream<SoftwareList> getFilteredStream()
	{
		return getList().stream().filter(t -> {
			if(!t.getSystem().isSelected())
				return false;
			return true;
		});
	}

	@Override
	protected List<SoftwareList> getFilteredList()
	{
		if(filtered_list == null)
			filtered_list = getFilteredStream().filter(t -> filter.contains(t.getStatus())).sorted().collect(Collectors.toList());
		return filtered_list;
	}

	public void export(EnhancedXMLStreamWriter writer, ProgressHandler progress, boolean filtered, SoftwareList selection) throws XMLStreamException, IOException
	{
		List<SoftwareList> lists = selection!=null?Collections.singletonList(selection):(filtered?getFilteredStream().collect(Collectors.toList()):getList());
		if(lists.size() > 0)
		{
			writer.writeStartDocument("UTF-8","1.0");
			if(lists.size() > 1)
			{
				writer.writeDTD("<!DOCTYPE softwarelists [\n" + IOUtils.toString(Export.class.getResourceAsStream("/jrm/resources/dtd/softwarelists.dtd"), Charset.forName("UTF-8")) + "\n]>\n");
				writer.writeStartElement("softwarelists");
			}
			else
				writer.writeDTD("<!DOCTYPE softwarelist [\n" + IOUtils.toString(Export.class.getResourceAsStream("/jrm/resources/dtd/softwarelist.dtd"), Charset.forName("UTF-8")) + "\n]>\n");
			progress.setProgress("Exporting", 0, lists.stream().flatMapToInt(sl -> IntStream.of(sl.size())).sum()); //$NON-NLS-1$
			progress.setProgress2(String.format("%d/%d", 0, lists.size()), 0, lists.size()); //$NON-NLS-1$
			for(SoftwareList list : lists)
			{
				list.export(writer, progress);
				progress.setProgress2(String.format("%d/%d", progress.getValue2()+1, lists.size()), progress.getValue2()+1); //$NON-NLS-1$
			}
			writer.writeEndDocument();
		}
	}

}
