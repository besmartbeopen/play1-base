package common.issues;

import com.google.common.base.MoreObjects;

/**
 * @author marco
 *
 */
public class FeedbackData {
	
	public BrowserData browser;
	public String html;
	public byte[] img;
	public String note;
	public String url;
	
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("browser", browser)
				.add("note", note)
				.add("url", url)
				.toString();
	}
}