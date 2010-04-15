import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MockDataManager extends Observable implements ToolDataManager {

	
	private static Logger log = LoggerFactory.getLogger(MockDataManager.class);
	
	private Map<Tool, Map<Platform, ToolInstance>> instances = new HashMap<Tool, Map<Platform, ToolInstance>>();
	private Platform thisPlatform = SystemInfo.getPlatform();
	
	public MockDataManager() {
		setup("OpenOffice.org", new String[]{"3.0", "3.1", "3.2"});
		setup("Mplayer", new String[] {"1.0"});
		setup("Calibre", new String[] {"0.6"});
	}
	
	private void setup(String name, String[] versions) {
		Tool tool = new Tool(name);
		Map<Platform, ToolInstance> map = new HashMap<Platform, ToolInstance>();
		for(String v:versions) {
			ToolInstance instance = new ToolInstance(tool, v, ToolStatus.NOT_INSTALLED, thisPlatform);
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

	@Override
	public void install(ToolInstance tool) {
		tool.setStatus(ToolStatus.INSTALLED);
		setChanged();
		notifyObservers();
		log.debug("Observer notified");
	}

	@Override
	public void remove(ToolInstance tool) {
		tool.setStatus(ToolStatus.NOT_INSTALLED);
		setChanged();
		notifyObservers();
		log.debug("Observer notified");
	}

	
	
	@Override
	public synchronized void addObserver(Observer o) {
		// TODO Auto-generated method stub
		super.addObserver(o);
		log.debug("Add observer:" + o);
	}
	

}
