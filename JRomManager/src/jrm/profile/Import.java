package jrm.profile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;

import jrm.misc.Log;

public class Import
{
	public File org_file;
	public File file;
	public boolean is_mame = false;

	public Import(File file, boolean sl)
	{
		org_file = file;
		File workdir = Paths.get(".").toAbsolutePath().normalize().toFile(); //$NON-NLS-1$
		File xmldir = new File(workdir, "xmlfiles"); //$NON-NLS-1$
		xmldir.mkdir();

		String ext = FilenameUtils.getExtension(file.getName());
		if(ext.equalsIgnoreCase("exe")) //$NON-NLS-1$
		{
		//	Log.info("Get dat file from Mame...");
			try
			{
				File tmpfile = File.createTempFile("JRM", ".xml"); //$NON-NLS-1$ //$NON-NLS-2$
				tmpfile.deleteOnExit();
				Process process = new ProcessBuilder(file.getAbsolutePath(), sl?"-listsoftware":"-listxml").directory(file.getAbsoluteFile().getParentFile()).redirectOutput(tmpfile).start(); //$NON-NLS-1$
				process.waitFor();
				this.is_mame = true;
				this.file = tmpfile;
			}
			catch(IOException e)
			{
				Log.err("Caught IO Exception", e); //$NON-NLS-1$
			}
			catch(InterruptedException e)
			{
				Log.err("Caught Interrupted Exception", e); //$NON-NLS-1$
			}
		}
		else
			this.file = file;

	}
}