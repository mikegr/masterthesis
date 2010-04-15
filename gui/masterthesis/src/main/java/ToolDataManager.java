import java.util.List;
import java.util.Observer;


public interface ToolDataManager {

	public List<ToolInstance> getAvailableTools();
	public List<ToolInstance> getInstalledTools();
	public List<ToolInstance> getAllAvailableTools();
	
	public void install(ToolInstance tool);
	public void remove(ToolInstance tool);
	
	 
	//public List<ToolInstance> getInstances(Tool tool);
	
	public void addObserver(Observer o);
}
