package jrm.server.shared.actions;

import java.io.IOException;
import java.util.Date;

import com.eclipsesource.json.JsonObject;

import jrm.aui.basic.ResultColUpdater;
import jrm.aui.basic.SrcDstResult;
import jrm.aui.basic.SrcDstResult.SDRList;
import jrm.batch.TorrentChecker;
import jrm.io.torrent.options.TrntChkMode;
import jrm.misc.BreakException;
import jrm.misc.Log;
import jrm.misc.SettingsEnum;
import jrm.server.shared.WebSession;
import jrm.server.shared.Worker;

public class TrntChkActions
{
	private final ActionsMgr ws;

	public TrntChkActions(ActionsMgr ws)
	{
		this.ws = ws;
	}

	public void start(JsonObject jso)
	{
		(ws.getSession().setWorker(new Worker(()->{
			WebSession session = ws.getSession();
			final TrntChkMode mode = TrntChkMode.valueOf(session.getUser().getSettings().getProperty(SettingsEnum.trntchk_mode, "FILENAME"));
			final boolean removeUnknownFiles = session.getUser().getSettings().getProperty(SettingsEnum.trntchk_remove_unknown_files, false);
			final boolean removeWrongSizedFiles = session.getUser().getSettings().getProperty(SettingsEnum.trntchk_remove_wrong_sized_files, false);
			final boolean detectArchivedFolders = session.getUser().getSettings().getProperty(SettingsEnum.trntchk_detect_archived_folders, true);

			session.getWorker().progress = new ProgressActions(ws);
			try
			{
				SDRList sdrl =  SrcDstResult.fromJSON(session.getUser().getSettings().getProperty(SettingsEnum.trntchk_sdr, "[]"));
				try
				{
					new TorrentChecker(session, session.getWorker().progress, sdrl, mode, new ResultColUpdater()
					{
						@Override
						public void updateResult(int row, String result)
						{
							sdrl.get(row).result = result;
							session.getUser().getSettings().setProperty(SettingsEnum.trntchk_sdr, SrcDstResult.toJSON(sdrl));
							session.getUser().getSettings().saveSettings();
							TrntChkActions.this.updateResult(row, result);
						}
						
						@Override
						public void clearResults()
						{
							sdrl.forEach(sdr -> sdr.result = "");
							session.getUser().getSettings().setProperty(SettingsEnum.trntchk_sdr, SrcDstResult.toJSON(sdrl));
							session.getUser().getSettings().saveSettings();
							TrntChkActions.this.clearResults();
						}
					}, removeUnknownFiles, removeWrongSizedFiles, detectArchivedFolders);
				}
				catch (IOException e)
				{
					Log.err(e.getMessage(),e);
				}
			}
			catch(BreakException e)
			{
				
			}
			finally
			{
				TrntChkActions.this.end();
				session.getWorker().progress.close();
				session.getWorker().progress = null;
				session.setLastAction(new Date());
			}
		}))).start();
	}
	
	@SuppressWarnings("serial")
	void updateResult(int row, String result)
	{
		try
		{
			if(ws.isOpen())
			{
				ws.send(new JsonObject() {{
					add("cmd", "TrntChk.updateResult");
					add("params", new JsonObject() {{
						add("row", row);
						add("result", result);
					}});
				}}.toString());
			}
		}
		catch (IOException e)
		{
			Log.err(e.getMessage(),e);
		}
	}

	@SuppressWarnings("serial")
	void clearResults()
	{
		try
		{
			if(ws.isOpen())
			{
				ws.send(new JsonObject() {{
					add("cmd", "TrntChk.clearResults");
				}}.toString());
			}
		}
		catch (IOException e)
		{
			Log.err(e.getMessage(),e);
		}
	}

	@SuppressWarnings("serial")
	void end()
	{
		try
		{
			if(ws.isOpen())
			{
				ws.send(new JsonObject() {{
					add("cmd", "TrntChk.end");
				}}.toString());
			}
		}
		catch (IOException e)
		{
			Log.err(e.getMessage(),e);
		}
	}

}
