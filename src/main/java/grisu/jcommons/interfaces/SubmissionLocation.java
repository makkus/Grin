package grisu.jcommons.interfaces;

import grisu.grin.model.resources.Directory;
import grisu.grin.model.resources.Group;
import grisu.grin.model.resources.Queue;
import grisu.grin.model.resources.Site;

import java.util.Collection;

public class SubmissionLocation {

	private final Queue queue;
	private final Package pkg;
	private final Group group;

	public SubmissionLocation(Group g, Package p, Queue q) {
		this.pkg = p;
		this.group = g;
		this.queue = q;
	}

	public Group getGroup() {
		return group;
	}

	public Package getPackage() {
		return pkg;
	}

	public Queue getQueue() {
		return queue;
	}

	public Site getSite() {
		return queue.getSite();
	}

	public Collection<Directory> getStagingFileSystems() {
		return queue.getDirectories(group);
	}

	@Override
	public String toString() {
		return getQueue().toString();
	}

}
