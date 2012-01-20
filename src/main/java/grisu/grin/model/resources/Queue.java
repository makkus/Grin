package grisu.grin.model.resources;

import grisu.grin.model.GroupRestrictions;

import java.util.Set;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Sets;

public class Queue extends AbstractResource implements Comparable<Queue>,
		GroupRestrictions {

	private Gateway gateway;

	private Set<Group> groups;

	private String name;

	private Set<Directory> directories = Sets.newHashSet();

	private Queue() {
	}

	public Queue(Gateway gw, String queueName, Set<Group> groups,
			Set<Directory> stagingFileSystems) {
		this.gateway = gw;
		this.name = queueName;
		this.groups = groups;
		this.directories = stagingFileSystems;
	}

	public int compareTo(Queue o) {
		return ComparisonChain.start().compare(getSite(), o.getSite())
				.compare(getName(), getName()).result();
	}

	@Override
	public Set<AbstractResource> getDirectConnections() {

		Set<AbstractResource> result = Sets.newHashSet();
		result.addAll(getGroups());
		result.addAll(getDirectories());
		result.add(getGateway());
		return result;
	}

	public Set<Directory> getDirectories() {
		return directories;
	}

	public Gateway getGateway() {
		return this.gateway;

	}

	public Set<Group> getGroups() {
		return groups;
	}

	public String getName() {
		return this.name;
	}

	public Site getSite() {
		return getGateway().getSite();
	}

	private void setDirectories(Set<Directory> d) {
		this.directories = d;
	}

	private void setGateway(Gateway gw) {
		this.gateway = gw;
	}

	private void setGroups(Set<Group> g) {
		this.groups = g;
	}

	private void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return getName();
	}

}
