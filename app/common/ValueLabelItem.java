package common;

import com.google.common.base.MoreObjects;

/**
 * @author marco
 *
 */
public class ValueLabelItem {

	public Integer value;
	public String label;

	public ValueLabelItem(Integer value, String label) {
		this.value = value;
		this.label = label;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("value", value)
				.add("label", label).toString();
	}
}
