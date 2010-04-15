
public class ToolInstance {

	String version;
	ToolStatus status;
	Platform platform;
	Tool tool;
	
	public ToolInstance(Tool tool, String version, ToolStatus status, Platform platform) {
		super();
		this.tool = tool;
		this.version = version;
		this.status = status;
		this.platform = platform;
	}
	
	
	public Platform getPlatform() {
		return platform;
	}
	public void setPlatform(Platform platform) {
		this.platform = platform;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public ToolStatus getStatus() {
		return status;
	}
	public void setStatus(ToolStatus status) {
		this.status = status;
	}

	public Tool getTool() {
		return tool;
	}
}
