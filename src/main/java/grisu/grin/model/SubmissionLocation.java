package grisu.grin.model;

import grisu.grin.model.resources.Directory;
import grisu.grin.model.resources.Group;
import grisu.grin.model.resources.Queue;
import grisu.grin.model.resources.Site;

import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Sets;

public class SubmissionLocation implements Comparable<SubmissionLocation> {

	private final Set<Directory> stagingFileSystems;

	private final Queue queue;
	private final Group group;

	public SubmissionLocation(Queue queue, Group group) {
		this.queue = queue;
		this.group = group;
		this.stagingFileSystems = calculateStagingDirectories();
	}

	private Set<Directory> calculateStagingDirectories() {

		Set<Directory> temp = Sets.newHashSet();
		for (Directory d : queue.getDirectories()) {
			if (d.getGroups().contains(group)) {
				temp.add(d);
			}
		}

		return temp;

	}

	public int compareTo(SubmissionLocation o) {
		return ComparisonChain.start().compare(getSite(), o.getSite())
				.compare(getQueue(), o.getQueue()).result();
	}

	@Override
	public boolean equals(Object subloc) {
		if (subloc == null) {
			return false;
		}
		if (getClass() != subloc.getClass()) {
			return false;
		}
		final SubmissionLocation other = (SubmissionLocation) subloc;

		return Objects.equal(this.getQueue(), other.getQueue())
				&& Objects.equal(this.getGroup(), other.getGroup());

	}

	public Group getGroup() {
		return group;
	}

	public Queue getQueue() {
		return queue;
	}

	public Site getSite() {
		return getQueue().getSite();
	}

	public Set<Directory> getStagingFileSystems(Group group) {
		return stagingFileSystems;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getQueue(), getGroup());
	}

	public boolean isValid() {
		if (stagingFileSystems.size() > 0) {
			return true;
		} else {
			return false;
		}

	}
}
