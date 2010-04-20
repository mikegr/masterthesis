package at.tuwien.hoppla.migration.gui.internal;

import java.util.Map;


public class ToolInstance {

	String version;
	ToolStatus status;
	Platform platform;
	Tool tool;

	String installId;
	Map<String, String> installOptions;
	
	public ToolInstance(Tool tool, String version, ToolStatus status, Platform platform, String installId, Map<String, String> installOptions) {
		super();
		this.tool = tool;
		this.version = version;
		this.status = status;
		this.platform = platform;
		this.installId = installId;
		this.installOptions = installOptions;
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
	public String getInstallId() {
		return installId;
	}


	public void setInstallId(String installId) {
		this.installId = installId;
	}


	public Map<String, String> getInstallOptions() {
		return installOptions;
	}


	public void setInstallOptions(Map<String, String> installOptions) {
		this.installOptions = installOptions;
	}

}
