package grisu.grin.model.resources;

import grisu.grin.model.GroupRestrictions;

import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Sets;

public class Package extends AbstractResource implements Comparable<Package>,
		GroupRestrictions {

	private Application application;

	private Version version;

	private Set<Group> groups = Sets.newTreeSet();

	private Set<Queue> queues = Sets.newTreeSet();

	private Package() {
	}

	public Package(Application app, Version version, Set<Queue> q) {
		this.application = app;
		this.version = version;
		this.queues = q;
	}

	public int compareTo(Package o) {
		return ComparisonChain
				.start()
				.compare(getApplication().getName(),
						o.getApplication().getName())
				.compare(getVersion(), o.getVersion())
				.compare(getQueues().size(), o.getQueues().size()).result();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Package other = (Package) obj;

		return Objects.equal(getApplication(), other.getApplication())
				&& Objects.equal(this.getVersion(), other.getVersion())
				&& (Sets.symmetricDifference(getGroups(), other.getGroups())
						.size() == 0)
				&& (Sets.symmetricDifference(this.getQueues(),
						other.getQueues()).size() == 0);
	}

	public Application getApplication() {
		return application;
	}

	@Override
	public Set<AbstractResource> getDirectConnections() {

		Set<AbstractResource> result = Sets.newHashSet();
		result.add(getVersion());
		result.add(getApplication());
		result.addAll(getQueues());

		return result;

	}

	public Set<Gateway> getGateways() {
		Set<Gateway> gws = Sets.newTreeSet();
		for (Queue q : getQueues()) {
			gws.add(q.getGateway());
		}
		return gws;
	}

	public Set<Group> getGroups() {

		return groups;
	}

	public String getName() {
		return getApplication().getName() + "_" + getVersion();
	}

	public Set<Queue> getQueues() {
		return queues;
	}

	public Set<Site> getSites() {
		Set<Site> sites = Sets.newTreeSet();
		for (Queue q : getQueues()) {
			sites.add(q.getSite());
		}
		return sites;
	}

	public Version getVersion() {
		return version;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getApplication(), getVersion(), getGroups(),
				getQueues());
	}

	private void setApplication(Application a) {
		this.application = a;
	}

	private void setGroups(Set<Group> groups) {
		this.groups = groups;
	}

	private void setQueues(Set<Queue> queues) {
		this.queues = queues;
	}

	private void setVersion(Version version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return getApplication() + " / " + getVersion();
	}
}
