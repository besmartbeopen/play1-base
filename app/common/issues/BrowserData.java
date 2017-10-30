package common.issues;

import java.util.List;

import com.google.common.base.MoreObjects;

/**
 * @author marco
 *
 */
public class BrowserData {
	public String appCodeName;
	public String appName;
	public String appVersion;
	public boolean cookieEnabled; 
	public boolean onLine;
	public String platform;
	public String userAgent;
	public List<String> plugins;
	
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("userAgent", userAgent)
				.add("platform", platform)
				.add("plugins", plugins)
				.toString();
	}
}