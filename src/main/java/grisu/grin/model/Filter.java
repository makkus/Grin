package grisu.grin.model;

import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Sets;

public class Filter {

	private final static String DEFAULT_KEY = "default";
	public final static Filter defaultKey = new Filter();

	private final String key;
	private final Object[] keys;

	private Filter() {
		this((Object[]) null);
	}

	public Filter(Object... keys) {
		if (keys == null || keys.length == 0) {
			this.keys = new Object[] {};
			this.key = DEFAULT_KEY;
		} else {
			this.keys = keys;
			Set<String> keystrings = Sets.newTreeSet();
			for (Object o : keys) {
				String s = o.getClass().getSimpleName() + "_" + o.toString();
				keystrings.add(s);
			}

			key = StringUtils.join(keystrings, "+");
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Filter other = (Filter) obj;

		return key.equals(other.key);
	}

	public Object[] getKeys() {
		return keys;
	}

	@Override
	public int hashCode() {
		return key.hashCode();
	}

	@Override
	public String toString() {
		return key;
	}
}
