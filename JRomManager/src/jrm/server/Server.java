package jrm.server;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import fi.iki.elonen.NanoWSD.WebSocket;
import jrm.server.handlers.DataSourcesHandler;
import jrm.server.handlers.EnhStaticPageHandler;
import jrm.server.handlers.ResourceHandler;
import jrm.server.handlers.SessionHandler;
import jrm.server.handlers.UploadHandler;
import jrm.server.ws.WebSckt;

public class Server extends EnhRouterNanoHTTPD implements SessionStub
{
	private String clientPath;
	private static boolean debug = false;

	final static Map<String, WebSession> sessions = new HashMap<>();
	
		
	public Server(int port, String clientPath)
	{
		super(port);
		this.clientPath = clientPath;
		addMappings();
		ScheduledExecutorService cleanerService = Executors.newSingleThreadScheduledExecutor();
		cleanerService.scheduleAtFixedRate(new Runnable()
		{
			@Override
			public void run()
			{
				Iterator<Entry<String, WebSession>> iterator = sessions.entrySet().iterator();
				while(iterator.hasNext())
				{
					Entry<String, WebSession> entry = iterator.next();
					if((new Date().getTime() - entry.getValue().lastAction.getTime())>86400L*1000L)
					{
						System.out.println("Session "+entry.getKey()+" removed");
						iterator.remove();
					}
				}
			}
		}, 1, 1, TimeUnit.MINUTES);
	}

	/**
	 * Main entry point
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		Options options = new Options();
		options.addOption(new Option("c", "client", true, "Client Path"));
		options.addOption(new Option("p", "port", true, "Server Port"));
		options.addOption(new Option("w", "workpath", true, "Working Path"));
		options.addOption(new Option("d", "debug", false, "Debug"));
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd;

		String clientPath = null;
		int port = 8080;
		try
		{
			cmd = parser.parse(options, args);
			if (null == (clientPath = cmd.getOptionValue('c')))
			{
				try
				{
					clientPath = new File(new File(Server.class.getProtectionDomain().getCodeSource().getLocation().toURI()), "smartgwt").getPath();
				}
				catch (URISyntaxException e)
				{
					e.printStackTrace();
				}
			}
			try
			{
				if(cmd.hasOption('p'))
					port = Integer.parseInt(cmd.getOptionValue('p'));
			}
			catch (NumberFormatException e)
			{
			}
			if(cmd.hasOption('w'))
				System.setProperty("jrommanager.dir", cmd.getOptionValue('w').replace("%HOMEPATH%", System.getProperty("user.home")));
			if(cmd.hasOption('d'))
				debug = true;
		}
		catch (ParseException e)
		{
			System.out.println(e.getMessage());
			formatter.printHelp("Server", options);
			System.exit(1);
		}
		try
		{
			Locale.setDefault(Locale.US);
			System.setProperty("file.encoding", "UTF-8");
			for (Handler h : Logger.getGlobal().getHandlers())
				Logger.getGlobal().removeHandler(h);
			Logger.getGlobal().addHandler(new FileHandler(getLogPath() + "/Server.%g.log", 1024 * 1024, 5, false));
			Server server = new Server(port, clientPath);
			server.start(0);
			System.out.println("Start server");
			System.out.println("port: " + port);
			System.out.println("clientPath: " + clientPath);
			System.out.println("workPath: " + (System.getProperty("jrommanager.dir") != null ? System.getProperty("jrommanager.dir") : Paths.get(System.getProperty("user.dir"))));
			Runtime.getRuntime().addShutdownHook(new Thread(()->{
				WebSckt.saveAllSettings();
				server.stop();
				System.out.println("Server stopped.\n");
			}));
			if(debug)
			{
				System.in.read();
				System.exit(0);
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static Path getWorkPath()
	{
		String base = System.getProperty("jrommanager.dir");
		if (base == null)
			base = System.getProperty("user.dir");
		return Paths.get(base);
	}
	
	private static String getLogPath() throws IOException
	{
		Path path = getWorkPath().resolve("logs");
		Files.createDirectories(path);
		return path.toString();
	}

	@Override
	public void addMappings()
	{
		super.addMappings();
		addRoute("/", jrm.server.handlers.IndexHandler.class);
		addRoute("/index.html", jrm.server.handlers.IndexHandler.class);
		addRoute("/smartgwt/(.)+", EnhStaticPageHandler.class, new File(clientPath));
		addRoute("/images/(.)+", ResourceHandler.class, Server.class.getResource("/jrm/resources/"));
		addRoute("/datasources/:action/", DataSourcesHandler.class);
		addRoute("/session/", SessionHandler.class, this);
		addRoute("/upload/", UploadHandler.class, this);
	}
	
	@Override
	protected WebSocket openWebSocket(IHTTPSession handshake)
	{
		return new WebSckt(this, handshake);
	}

	public static WebSession getSession(String session)
	{
		WebSession s = sessions.get(session);
		if (s != null)
			s.lastAction = new Date();
		return s;
	};

	@Override
	public void setSession(WebSession session)
	{
		sessions.put(session.getSessionId(), session);
	}

	@Override
	public void unsetSession(WebSession session)
	{
		sessions.remove(session.getSessionId());
	}
}
