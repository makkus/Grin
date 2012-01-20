package grisu.grin.model.resources;

import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Sets;

public class Gateway extends AbstractResource implements Comparable<Gateway> {

	private Site site;
	private String host;

	private Gateway() {
	}

	public Gateway(Site site, String host) {
		setSite(site);
		setHost(host);
	}

	public int compareTo(Gateway o) {
		return ComparisonChain.start().compare(getSite(), o.getSite())
				.compare(getHost(), o.getHost()).result();
	}

	@Override
	public boolean equals(Object obj) {

		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Gateway other = (Gateway) obj;

		return com.google.common.base.Objects.equal(getSite(), other.getSite())
				&& com.google.common.base.Objects.equal(this.getHost(),
						other.getHost());

	}

	@Override
	public Set<AbstractResource> getDirectConnections() {

		Set<AbstractResource> result = Sets.newHashSet();
		result.add(getSite());
		return result;
	}

	public String getHost() {
		return host;
	}

	public Site getSite() {
		return site;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getSite(), getHost());
	}

	private void setHost(String host) {
		this.host = host;
	}

	private void setSite(Site site) {
		this.site = site;
	}

	@Override
	public String toString() {
		return host;
	}

}
