
/**
 * Attribute that are null matches all;
 * @author mike
 *
 */
public class Platform {

	public String os;
	public String variant;	
	public String version;
	public String language;
	public String processor;
	
	
	public Platform(String os, String variant, String version, String language,
			String processor) {
		super();
		this.os = os;
		this.variant = variant;
		this.version = version;
		this.language = language;
		this.processor = processor;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((os == null) ? 0 : os.hashCode());
		result = prime * result
				+ ((processor == null) ? 0 : processor.hashCode());
		result = prime * result + ((variant == null) ? 0 : variant.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Platform other = (Platform) obj;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (os == null) {
			if (other.os != null)
				return false;
		} else if (!os.equals(other.os))
			return false;
		if (processor == null) {
			if (other.processor != null)
				return false;
		} else if (!processor.equals(other.processor))
			return false;
		if (variant == null) {
			if (other.variant != null)
				return false;
		} else if (!variant.equals(other.variant))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}
	
	
	
	
	
}
