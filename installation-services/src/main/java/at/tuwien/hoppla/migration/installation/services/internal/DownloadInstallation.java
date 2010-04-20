package at.tuwien.hoppla.migration.installation.services.internal;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import at.tuwien.hoppla.migration.installation.InstallationException;
import at.tuwien.hoppla.migration.installation.InstallationService;


/**
 * This installation downloads the software from the internet and executes a command
 * afterwards. This can be either unpacking an archive or start an installer.  
 *  
 * @author mike
 */
//TODO: How to determine the action after the download

public class DownloadInstallation implements InstallationService, Observer {

	private static final String URL = "urlText";
	private static final String DESTINATION = "destination";

	public DownloadInstallation() {
	}
	
	private boolean finished;
	
	
	@Override
	public void install(Map<String, String> options) throws InstallationException {
		String urlText = options.get(URL);
		File destination = new File(options.get(DESTINATION));
		try {
			URL url = new URL(urlText);
			Download download = new Download(url, destination);
			download.addObserver(this);
			synchronized (this) {
				System.out.println("Entered sync block");
				while (!finished) {
					wait();
				}
				System.out.println("Finished sync block");
			}
			
		} catch (MalformedURLException e) {
			throw new InstallationException(e);
		} catch (InterruptedException e) {
			throw new InstallationException(e);
		}
	}
	
	@Override
	public synchronized void update(Observable o, Object arg) {
		if (((Download)o).getStatus() != Download.DOWNLOADING) {
			finished = true;
		}
		System.out.println("Notify!");
		notify();
	}

	@Override
	public void uninstall(Map<String, String> options) throws InstallationException {
		// TODO Auto-generated method stub
	}
	@Override
	public boolean isInstalled(Map<String, String> options) {
		
		return false;
	}
	
}
