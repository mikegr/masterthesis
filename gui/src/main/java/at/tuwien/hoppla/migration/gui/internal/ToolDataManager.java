package at.tuwien.hoppla.migration.gui.internal;


import java.util.List;
import java.util.Observer;

import at.tuwien.hoppla.migration.installation.InstallationService;


public interface ToolDataManager {

	public List<ToolInstance> getAvailableTools();
	public List<ToolInstance> getInstalledTools();
	public List<ToolInstance> getAllAvailableTools();
	
	public void install(ToolInstance tool);
	public void remove(ToolInstance tool);
	
	public boolean isInstalled(ToolInstance tool);
	 
	//public List<ToolInstance> getInstances(Tool tool);
	
	public void addObserver(Observer o);
}
