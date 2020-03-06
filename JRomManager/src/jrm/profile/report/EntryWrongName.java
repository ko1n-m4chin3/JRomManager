package jrm.profile.report;

import java.io.Serializable;

import org.apache.commons.text.StringEscapeUtils;

import jrm.locale.Messages;
import jrm.profile.data.Entity;
import jrm.profile.data.Entry;

/**
 * An {@link Entry} has been found for a related {@link Entity}, but is wrongly named
 * @author optyfr
 *
 */
@SuppressWarnings("serial")
public class EntryWrongName extends Note implements Serializable
{
	/**
	 * The related {@link Entity}
	 */
	final Entity entity;
	/**
	 * The entry wrongly named
	 */
	final Entry entry;

	/**
	 * The constructor
	 * @param entity The related {@link Entity}
	 * @param entry The entry wrongly named
	 */
	public EntryWrongName(final Entity entity, final Entry entry)
	{
		this.entity = entity;
		this.entry = entry;
	}

	@Override
	public String toString()
	{
		return String.format(Messages.getString("EntryWrongName.Wrong"), parent.ware.getFullName(), entry.getName(), entity.getNormalizedName()); //$NON-NLS-1$
	}

	@Override
	public String getHTML()
	{
		return toHTML(String.format(StringEscapeUtils.escapeHtml4(Messages.getString("EntryWrongName.Wrong")), toBlue(parent.ware.getFullName()), toBold(entry.getName()), toBold(entity.getNormalizedName()))); //$NON-NLS-1$
	}

	@Override
	public String getDetail()
	{
		String msg="";
		msg += "== Expected == \n";
		msg += "Name : " + entity.getBaseName() + "\n";
		if (entity.getSize() >= 0)		msg += "Size : " + entity.getSize() + "\n";
		if (entity.getCrc() != null)	msg += "CRC : " + entity.getCrc() + "\n";
		if (entity.getMd5() != null)	msg += "MD5 : " + entity.getMd5() + "\n";
		if (entity.getSha1() != null)	msg += "SHA1 : " + entity.getSha1() + "\n";
		msg += "== Current == \n";
		msg += "Name : " + entry.getName() + "\n";
		if (entry.size >= 0)	msg += "Size : " + entry.size + "\n";
		if (entry.crc != null)	msg += "CRC : " + entry.crc + "\n";
		if (entry.md5 != null)	msg += "MD5 : " + entry.md5 + "\n";
		if (entry.sha1 != null)	msg += "SHA1 : " + entry.sha1 + "\n";
		return msg;
	}

	@Override
	public String getName()
	{
		return entity.getBaseName();
	}

	@Override
	public String getCrc()
	{
		return entity.getCrc();
	}

	@Override
	public String getSha1()
	{
		return entity.getSha1();
	}
}
