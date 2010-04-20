package at.tuwien.hoppla.migration.gui.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.tuwien.hoppla.migration.installation.InstallationException;
import at.tuwien.hoppla.migration.installation.InstallationService;


public class MockDataManager extends Observable implements ToolDataManager {

	
	private static Logger log = LoggerFactory.getLogger(MockDataManager.class);
	
	private Map<Tool, Map<Platform, ToolInstance>> instances = new HashMap<Tool, Map<Platform, ToolInstance>>();
	private Platform thisPlatform = SystemInfo.getPlatform();
	private BundleContext ctx;
	
	public MockDataManager(BundleContext ctx) {
		this.ctx = ctx;
		setup("OpenOffice.org", "openoffice.org", new String[]{"3.0", "3.1", "3.2"});
		setup("Mplayer", "mplayer", new String[] {"1.0"});
		setup("Calibre", "calibre", new String[] {"0.6"});
	}
	
	private void setup(String name, String packageName, String[] versions) {
		Tool tool = new Tool(name);
		Map<Platform, ToolInstance> map = new HashMap<Platform, ToolInstance>();
		for(String v:versions) {
			HashMap<String, String> options = new HashMap<String, String>();
			options.put("packageName", packageName);
			ToolInstance instance = new ToolInstance(tool, v, ToolStatus.NOT_INSTALLED, thisPlatform, "DEBIAN", options);
			try {
				if (getService(instance).isInstalled(instance.getInstallOptions())) {
					instance.setStatus(ToolStatus.INSTALLED);
				}
			} catch (Exception e) {
				log.warn("Setting installation status failed", e);
			}
			map.put(thisPlatform, instance);
		}
		instances.put(tool, map);
	}
	
	@Override
	public List<ToolInstance> getAllAvailableTools() {
		List<ToolInstance> result = new ArrayList<ToolInstance>();
		for(Map<Platform, ToolInstance> map:instances.values()) {
			result.addAll(map.values());
		}
		log.debug("AllAvailableTools: " + result.size());
		return result;
	}

	@Override
	public List<ToolInstance> getAvailableTools() {
		List<ToolInstance> result = new ArrayList<ToolInstance>();
		for(Map<Platform, ToolInstance> map:instances.values()) {
			result.add(map.get(thisPlatform));
		}
		log.debug("AvailableTools: " + result.size());
		return result;
	}

	@Override
	public List<ToolInstance> getInstalledTools() {
		List<ToolInstance> result = new ArrayList<ToolInstance>();
		for(Map<Platform, ToolInstance> map:instances.values()) {
			ToolInstance inst = map.get(thisPlatform);
			if (inst.getStatus() == ToolStatus.INSTALLED) {
				result.add(inst);
			}
		}
		log.debug("InstalledTools: " + result.size());
		return result;
	}

	public InstallationService getService(ToolInstance tool) throws InstallationException {
		InstallationService s = null;
		try {
			ServiceReference[] refs = ctx.getServiceReferences(InstallationService.class.getName(), "(hoppla.identifier=" + tool.getInstallId() + ")");
			if (refs != null && refs.length > 0 && refs[0] != null ) {
				s = (InstallationService) ctx.getService(refs[0]);
			}
			else {
				log.warn("Installation service is not available");
			}
		} catch (InvalidSyntaxException e) {
			log.warn("Getting service reference failed", e);
		}
		return s;
	}
	
	@Override
	public void install(ToolInstance tool) {
		log.info("Installing : " + tool.getInstallOptions().get("packageName"));
		try {
			getService(tool).install(tool.getInstallOptions());
			tool.setStatus(ToolStatus.INSTALLED);
		} catch (Exception e) {
			log.error("Installation failed", e);
		}
		setChanged();
		notifyObservers();
		log.debug("Observer notified");
	}
	

	@Override
	public void remove(ToolInstance tool) {
		tool.setStatus(ToolStatus.NOT_INSTALLED);
		try {
			getService(tool).install(tool.getInstallOptions());	
		} catch (Exception e) {
			log.error("Deinstallation failed", e);
		}
		setChanged();
		notifyObservers();
		log.debug("Observer notified");
	}

	@Override
	public boolean isInstalled(ToolInstance tool) {
		try {
			boolean result = getService(tool).isInstalled(tool.getInstallOptions());
			if (result) {
				tool.setStatus(ToolStatus.INSTALLED);
			}
			else {
				tool.setStatus(ToolStatus.NOT_INSTALLED);
			}
			return result; 	
		} catch (Exception e) {
			log.warn("isInstalled failed", e);
			return false;
		}
	}
	
	@Override
	public synchronized void addObserver(Observer o) {
		// TODO Auto-generated method stub
		super.addObserver(o);
		log.debug("Add observer:" + o);
	}
	

}
