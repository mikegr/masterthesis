package at.tuwien.hoppla.migration.installation;

import java.util.Map;

public interface InstallationService {

	public boolean isInstalled(Map<String, String> options);
	public void install(Map<String, String> options) throws InstallationException;
	public void uninstall(Map<String, String> options) throws InstallationException;
	
}
