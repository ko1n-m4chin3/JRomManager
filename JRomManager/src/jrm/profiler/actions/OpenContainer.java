package jrm.profiler.actions;

import java.io.File;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import jrm.compressors.Archive;
import jrm.compressors.SevenZipArchive;
import jrm.compressors.ZipArchive;
import jrm.misc.FindCmd;
import jrm.misc.Settings;
import jrm.profiler.data.Container;
import jrm.profiler.scan.FormatOptions;
import jrm.ui.ProgressHandler;

public class OpenContainer extends ContainerAction
{

	public OpenContainer(Container container, FormatOptions format)
	{
		super(container, format);
	}
	
	public static OpenContainer getInstance(OpenContainer action, Container container, FormatOptions format)
	{
		if(action == null)
			action = new OpenContainer(container, format);
		return action;
	}

	static File tzip_cmd = new File(Settings.getProperty("tzip_cmd", FindCmd.findTZip()));

	@Override
	public boolean doAction(ProgressHandler handler)
	{
		handler.setProgress("<html><nobr>Fixing <span color='blue'>"+container.file.getName()+"</span> <span color='purple'>["+container.m.description+"]</span></nobr></html>");
		if(container.getType()==Container.Type.ZIP)
		{
			if(format==FormatOptions.ZIP)
			{
				Map<String,Object> env = new HashMap<>();
				env.put("create", "false");
				env.put("useTempFile", Boolean.TRUE);
				try(FileSystem fs = FileSystems.newFileSystem(URI.create("jar:"+container.file.toURI()), env);)
				{
					for(EntryAction action : entry_actions)
						if(!action.doAction(fs, handler))
						{
							System.err.println("action to "+container.file.getName()+"@"+action.entry.file+" failed");
							return false;
						}
					fs.close();
					if(format==FormatOptions.TZIP && tzip_cmd.exists())
					{
						return new ProcessBuilder(tzip_cmd.getPath(), container.file.getAbsolutePath()).directory(tzip_cmd.getParentFile()).start().waitFor() == 0;
					}
					return true;
				}
				catch(Throwable e)
				{
					e.printStackTrace();
				}
			}
			else if(format==FormatOptions.ZIPE)
			{
				try(Archive archive = new ZipArchive(container.file))
				{
					for(EntryAction action : entry_actions)
						if(!action.doAction(archive, handler))
						{
							System.err.println("action to "+container.file.getName()+"@"+action.entry.file+" failed");
							return false;
						}
					return true;
				}
				catch(Throwable e)
				{
					e.printStackTrace();
				}
			}
		}
		else if(container.getType()==Container.Type.SEVENZIP)
		{
			try(Archive archive = new SevenZipArchive(container.file))
			{
				for(EntryAction action : entry_actions)
					if(!action.doAction(archive, handler))
					{
						System.err.println("action to "+container.file.getName()+"@"+action.entry.file+" failed");
						return false;
					}
				return true;
			}
			catch(Throwable e)
			{
				e.printStackTrace();
			}
		}
		else if(container.getType()==Container.Type.DIR)
		{
			Path target = container.file.toPath();
			for(EntryAction action : entry_actions)
				if(!action.doAction(target, handler))
				{
					System.err.println("action to "+container.file.getName()+"@"+action.entry.file+" failed");
					return false;
				}
			return true;
		}
		return false;
	}

	@Override
	public String toString()
	{
		String str = "Open "+container;
		for(EntryAction action : entry_actions)
			str += "\n\t"+action;
		return str;
	}
}