package grisu.grin.model.resources;

import java.util.Set;

import com.google.common.collect.Sets;

public class Version extends AbstractResource implements Comparable<Version> {

	private String version;

	public Version(String version) {
		setVersion(version);
	}

	public int compareTo(Version o) {
		return version.compareTo(o.getVersion());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Version other = (Version) obj;

		return getVersion().equals(other.getVersion());
	}

	@Override
	public Set<AbstractResource> getDirectConnections() {

		Set<AbstractResource> result = Sets.newHashSet();
		return result;
	}

	public String getVersion() {
		return version;
	}

	@Override
	public int hashCode() {
		return version.hashCode();
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return getVersion();
	}

}
