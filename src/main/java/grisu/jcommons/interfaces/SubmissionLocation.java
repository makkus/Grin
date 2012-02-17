package grisu.jcommons.interfaces;

import grisu.jcommons.model.info.Directory;
import grisu.jcommons.model.info.Group;
import grisu.jcommons.model.info.Queue;
import grisu.jcommons.model.info.Site;

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
